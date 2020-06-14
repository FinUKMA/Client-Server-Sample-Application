package kma.cs.sample.desktop.websocket;

import java.lang.reflect.Type;
import java.math.BigDecimal;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import kma.cs.sample.desktop.GlobalContext;
import kma.cs.sample.domain.ByteMessage;
import kma.cs.sample.domain.Message;
import kma.cs.sample.domain.Product;
import kma.cs.sample.domain.packet.Command;
import kma.cs.sample.domain.packet.Encoder;
import kma.cs.sample.domain.packet.Packet;

public class WebSocketMessageHandler extends StompSessionHandlerAdapter {

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println("New session established : " + session.getSessionId());

        StompHeaders sendHeaders = new StompHeaders();
        sendHeaders.setDestination("/app/execute");
        sendHeaders.add("Authorization", GlobalContext.AUTHENTICATED_USER.getAccessToken());

        session.send(
            sendHeaders,
            new ByteMessage(Encoder.encode(
                Packet.<Product>builder()
                    .packetId(1L)
                    .userId(1)
                    .body(Product.of("name1", BigDecimal.valueOf(1.1), BigDecimal.valueOf(2.2)))
                    .command(Command.CREATE_PRODUCT)
                    .build()
            ))
        );
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
