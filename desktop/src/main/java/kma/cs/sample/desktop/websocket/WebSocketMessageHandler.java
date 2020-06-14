package kma.cs.sample.desktop.websocket;

import java.lang.reflect.Type;
import java.math.BigDecimal;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import kma.cs.sample.desktop.GlobalContext;
import kma.cs.sample.domain.ByteMessage;
import kma.cs.sample.domain.Product;
import kma.cs.sample.domain.packet.Command;
import kma.cs.sample.domain.packet.Decoder;
import kma.cs.sample.domain.packet.Encoder;
import kma.cs.sample.domain.packet.Packet;

public class WebSocketMessageHandler extends StompSessionHandlerAdapter {

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println("New session established : " + session.getSessionId());

        session.subscribe("/topic/process-ok", this);

        final StompHeaders sendHeaders = new StompHeaders();
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
    public void handleException(final StompSession session, final StompCommand command, final StompHeaders headers, final byte[] payload, final Throwable exception) {
        exception.printStackTrace();
    }

    @Override
    public Type getPayloadType(final StompHeaders headers) {
        return ByteMessage.class;
    }

    @Override
    public void handleTransportError(final StompSession session, final Throwable exception) {
        exception.printStackTrace();
    }

    @Override
    public void handleFrame(final StompHeaders headers, final Object payload) {
        final ByteMessage msg = (ByteMessage) payload;
        final Packet<?> packet = Decoder.decode(msg.getMessage());
        System.out.println("Received : " + packet);
    }

}
