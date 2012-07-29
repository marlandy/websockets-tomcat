package com.autentia.tutorial.websockets.messages;

import com.google.gson.Gson;
import org.junit.Test;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class ConnectionInfoMessageTest {

    private final Gson jsonProcessor = new Gson();

    @Test
    public void shouldReturnConnectionInfoFromJSON() {
         final String jsonMessage = "{connectionInfo : { user : 'Miguel Arlandy', activeUsers : ['Cristiano Ronaldo', 'Fernando Alonso', 'Rafael Nadal'] } }";
         final ConnectionInfoMessage message = jsonProcessor.fromJson(jsonMessage, ConnectionInfoMessage.class);
        assertNotNull(message);
        assertEquals("Miguel Arlandy", message.getConnectionInfo().getUser());
        assertNotNull(message.getConnectionInfo().getActiveUsers());
        assertEquals(3, message.getConnectionInfo().getActiveUsers().size());
        assertEquals("Cristiano Ronaldo", message.getConnectionInfo().getActiveUsers().get(0));
        assertEquals("Fernando Alonso", message.getConnectionInfo().getActiveUsers().get(1));
        assertEquals("Rafael Nadal", message.getConnectionInfo().getActiveUsers().get(2));
    }

    @Test
    public void shouldReturnJSONFromConnectionInfo() {
        final ConnectionInfoMessage connectionInfoMessage = new ConnectionInfoMessage("Miguel Arlandy", Arrays.asList("Cristiano Ronaldo", "Fernando Alonso", "Rafael Nadal"));
        final String jsonMessage = jsonProcessor.toJson(connectionInfoMessage);
        assertEquals("{\"connectionInfo\":{\"user\":\"Miguel Arlandy\",\"activeUsers\":[\"Cristiano Ronaldo\",\"Fernando Alonso\",\"Rafael Nadal\"]}}", jsonMessage);
    }

}
