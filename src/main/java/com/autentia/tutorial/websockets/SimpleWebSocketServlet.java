package com.autentia.tutorial.websockets;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public class SimpleWebSocketServlet extends WebSocketServlet {

    private static final Logger log = LoggerFactory.getLogger(SimpleWebSocketServlet.class);

    @Override
    protected boolean verifyOrigin(String origin) {
        log.trace("Origin: {}", origin);
        return true;
    }

    @Override
    protected StreamInbound createWebSocketInbound(String subProtocol, HttpServletRequest request) {
        return new WebSocketConnection();
    }

    private static class WebSocketConnection extends MessageInbound {

        @Override
        protected void onOpen(WsOutbound outbound) {
            log.info("Conexión abierta");
        }

        @Override
        protected void onClose(int status) {
            log.info("Conexión cerrada");
        }

        @Override
        protected void onBinaryMessage(ByteBuffer byteBuffer) throws IOException {
            log.warn("No se soportan mensajes binarios");
            throw new UnsupportedOperationException("No se soportan mensajes binarios");
        }

        @Override
        protected void onTextMessage(CharBuffer charBuffer) throws IOException {
            final String user = charBuffer.toString();
            log.debug("Mensaje recibido: {}", user);
            getWsOutbound().writeTextMessage(CharBuffer.wrap("Hola " + user + " desde WebSocket"));
        }
    }
}
