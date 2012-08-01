package com.autentia.tutorial.websockets;


import com.autentia.tutorial.websockets.messages.ConnectionInfoMessage;
import com.autentia.tutorial.websockets.messages.MessageInfoMessage;
import com.autentia.tutorial.websockets.messages.StatusInfoMessage;
import com.google.gson.Gson;
import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.*;

@WebServlet(urlPatterns = "/chat")
public class WebSocketCharServlet extends WebSocketServlet {

    private static final Logger log = LoggerFactory.getLogger(WebSocketCharServlet.class);

    private static final Map<String, ChatConnection> connections = new HashMap<String, ChatConnection>();

    @Override
    protected boolean verifyOrigin(String origin) {
        return true;
    }

    @Override
    protected StreamInbound createWebSocketInbound(String subProtocol, HttpServletRequest request) {
        final String connectionId = request.getSession().getId();
        final String userName = request.getParameter("userName");
        return new ChatConnection(connectionId, userName);
    }

    private static class ChatConnection extends MessageInbound {

        private final String connectionId;

        private final String userName;

        private final Gson jsonProcessor;

        private ChatConnection(String connectionId, String userName) {
            this.connectionId = connectionId;
            this.userName = userName;
            this.jsonProcessor = new Gson();
        }

        @Override
        protected void onOpen(WsOutbound outbound) {
            sendConnectionInfo(outbound);
            sendStatusInfoToOtherUsers(new StatusInfoMessage(userName, StatusInfoMessage.STATUS.CONNECTED));
            connections.put(connectionId, this);
        }

        @Override
        protected void onClose(int status) {
            sendStatusInfoToOtherUsers(new StatusInfoMessage(userName, StatusInfoMessage.STATUS.DISCONNECTED));
            connections.remove(connectionId);
        }

        @Override
        protected void onBinaryMessage(ByteBuffer byteBuffer) throws IOException {
            throw new UnsupportedOperationException("Binary messages not supported");
        }

        @Override
        protected void onTextMessage(CharBuffer charBuffer) throws IOException {
            final MessageInfoMessage message = jsonProcessor.fromJson(charBuffer.toString(), MessageInfoMessage.class);
            final ChatConnection destinationConnection = getDestinationUserConnection(message.getMessageInfo().getTo());
            if (destinationConnection != null) {
                final CharBuffer jsonMessage = CharBuffer.wrap(jsonProcessor.toJson(message));
                destinationConnection.getWsOutbound().writeTextMessage(jsonMessage);
            } else {
                log.warn("Se está intentando enviar un mensaje a un usuario no conectado");
            }
        }

        public String getUserName() {
            return userName;
        }

        private void sendConnectionInfo(WsOutbound outbound) {
            final List<String> activeUsers = getActiveUsers();
            final ConnectionInfoMessage connectionInfoMessage = new ConnectionInfoMessage(userName, activeUsers);
            try {
                outbound.writeTextMessage(CharBuffer.wrap(jsonProcessor.toJson(connectionInfoMessage)));
            } catch (IOException e) {
                log.error("No se pudo enviar el mensaje", e);
            }
        }

        private List<String> getActiveUsers() {
            final List<String> activeUsers = new ArrayList<String>();
            for (ChatConnection connection : connections.values()) {
                activeUsers.add(connection.getUserName());
            }
            return activeUsers;
        }

        private void sendStatusInfoToOtherUsers(StatusInfoMessage message) {
            final Collection<ChatConnection> otherUsersConnections = getAllChatConnectionsExceptThis();
            for (ChatConnection connection : otherUsersConnections) {
                try {
                    connection.getWsOutbound().writeTextMessage(CharBuffer.wrap(jsonProcessor.toJson(message)));
                } catch (IOException e) {
                    log.error("No se pudo enviar el mensaje", e);
                }
            }
        }

        private Collection<ChatConnection> getAllChatConnectionsExceptThis() {
            final Collection<ChatConnection> allConnections = connections.values();
            allConnections.remove(this);
            return allConnections;
        }

        private ChatConnection getDestinationUserConnection(String destinationUser) {
            for (ChatConnection connection : connections.values()) {
                if (destinationUser.equals(connection.getUserName())) {
                    return connection;
                }
            }
            return null;
        }

    }

}
