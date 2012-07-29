package com.autentia.tutorial.websockets;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public class SimpleWebSocketServlet extends WebSocketServlet {

    @Override
    protected boolean verifyOrigin(String origin) {
        System.out.println("Origin: " + origin);
        return true;
    }

    @Override
    protected StreamInbound createWebSocketInbound(String subProtocol, HttpServletRequest request) {
        return new WebSocketConnection();
    }

    private static class WebSocketConnection extends MessageInbound {

        @Override
        protected void onOpen(WsOutbound outbound) {
            System.out.println("Conexión abierta");
        }

        @Override
        protected void onClose(int status) {
            System.out.println("Conexión cerrada");
        }

        @Override
        protected void onBinaryMessage(ByteBuffer byteBuffer) throws IOException {
            throw new UnsupportedOperationException("Binary messages not supported");
        }

        @Override
        protected void onTextMessage(CharBuffer charBuffer) throws IOException {
            final String user = charBuffer.toString();
            getWsOutbound().writeTextMessage(CharBuffer.wrap("Hola " + user + " desde WebSocket"));
        }
    }
}
