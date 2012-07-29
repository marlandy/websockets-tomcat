package com.autentia.tutorial.websockets.messages;

import com.google.gson.Gson;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MessageInfoMessageTest {

    private final Gson jsonProcessor = new Gson();

    @Test
    public void shouldReturnMessageInfoFromJSON() {
        final String jsonMessage = "{messageInfo : { from : 'Miguel Arlandy', to : 'Cristiano Ronaldo', message : 'Cristiano eres el mejor!' } }";
        final MessageInfoMessage message = jsonProcessor.fromJson(jsonMessage, MessageInfoMessage.class);
        assertNotNull(message);
        assertEquals("Miguel Arlandy", message.getMessageInfo().getFrom());
        assertEquals("Cristiano Ronaldo", message.getMessageInfo().getTo());
        assertEquals("Cristiano eres el mejor!", message.getMessageInfo().getMessage());
    }

    @Test
    public void shouldReturnJSONFromConnectionInfo() {
        final MessageInfoMessage message = new MessageInfoMessage("Miguel Arlandy", "Cristiano Ronaldo", "Hala Madrid!");
        final String jsonMessage = jsonProcessor.toJson(message);
        assertEquals("{\"messageInfo\":{\"from\":\"Miguel Arlandy\",\"to\":\"Cristiano Ronaldo\",\"message\":\"Hala Madrid!\"}}", jsonMessage);
    }

}
