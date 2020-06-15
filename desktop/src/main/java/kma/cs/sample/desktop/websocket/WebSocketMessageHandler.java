package kma.cs.sample.desktop.websocket;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import kma.cs.sample.desktop.GlobalContext;
import kma.cs.sample.domain.ByteMessage;
import kma.cs.sample.domain.packet.Command;
import kma.cs.sample.domain.packet.Decoder;
import kma.cs.sample.domain.packet.Encoder;
import kma.cs.sample.domain.packet.Packet;

public class WebSocketMessageHandler extends StompSessionHandlerAdapter {

    private final Map<Long, Consumer<Packet<?>>> responseConsumers = new ConcurrentHashMap<>();
    private final AtomicBoolean connectionEstablished = new AtomicBoolean(false);
    private StompSession stompSession;

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        session.subscribe("/topic/process-ok", this);

        this.stompSession = session;
        connectionEstablished.set(true);
    }

    public <T> void send(final Command<T> command, final T body, final Consumer<Packet<?>> responseConsumer) {
        while (!connectionEstablished.get()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (stompSession == null || !stompSession.isConnected()) {
            throw new IllegalStateException("Connection not established");
        }
        final StompHeaders sendHeaders = new StompHeaders();
        sendHeaders.setDestination("/app/execute");
        sendHeaders.add("Authorization", GlobalContext.AUTHENTICATED_USER.getAccessToken());

        final long packetId = GlobalContext.PACKET_ID.incrementAndGet();

        stompSession.send(
            sendHeaders,
            new ByteMessage(Encoder.encode(
                Packet.<T>builder()
                    .packetId(packetId)
                    .userId(1)
                    .body(body)
                    .command(command)
                    .build()
            ))
        );

        responseConsumers.put(packetId, responseConsumer);
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

        Optional.ofNullable(responseConsumers.get(packet.getPacketId()))
            .ifPresentOrElse(
                packetConsumer -> packetConsumer.accept(packet),
                () -> System.out.println("No response consumers for packet: " + packet)
            );
    }

}
