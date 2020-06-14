package kma.cs.sample.desktop.websocket;

import java.lang.reflect.Type;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import kma.cs.sample.domain.Message;

public class WebSocketMessageHandler extends StompSessionHandlerAdapter {

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println("New session established : " + session.getSessionId());
        session.subscribe("/topic/messages", this);


        StompHeaders sendHeaders = new StompHeaders();
        sendHeaders.setDestination("/app/chat");
        sendHeaders.setReceiptId("1");

        session.send(sendHeaders, new Message("Nicky", "Howdy"));
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        exception.printStackTrace();
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Message.class;
    }

    @Override
    public void handleTransportError(final StompSession session, final Throwable exception) {
        exception.printStackTrace();
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        Message msg = (Message) payload;
        System.out.println("Received : " + msg.getText() + " from : " + msg.getFrom());
    }

}
