package com.autentia.tutorial.websockets.messages;

import com.google.gson.Gson;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StatusInfoMessageTest {

    private final Gson jsonProcessor = new Gson();

    @Test
    public void shouldReturnStatusInfoFromJSON() {
        final String jsonMessage = "{statusInfo : { user : 'Miguel Arlandy', status : 'CONNECTED' } }";
        final StatusInfoMessage message = jsonProcessor.fromJson(jsonMessage, StatusInfoMessage.class);
        assertNotNull(message);
        assertEquals("Miguel Arlandy", message.getStatusInfo().getUser());
        assertEquals(StatusInfoMessage.STATUS.CONNECTED, message.getStatusInfo().getStatus());
    }

    @Test
    public void shouldReturnJSONFromConnectionInfo() {
        final StatusInfoMessage message = new StatusInfoMessage("Miguel Arlandy", StatusInfoMessage.STATUS.DISCONNECTED);
        final String jsonMessage = jsonProcessor.toJson(message);
        assertEquals("{\"statusInfo\":{\"user\":\"Miguel Arlandy\",\"status\":\"DISCONNECTED\"}}", jsonMessage);
    }

}
