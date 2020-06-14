package kma.cs.sample.domain.packet;

import java.nio.ByteBuffer;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Encoder {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> byte[] encode(final Packet<T> packet) {
        try {
            final byte[] message = Base64.getEncoder().encode(OBJECT_MAPPER.writeValueAsBytes(packet.getBody()));

            final byte[] header = ByteBuffer.allocate(1 + 1 + 8 + 4)
                .put((byte) 0x13)
                .put((byte) 0)
                .putLong(packet.getPacketId())
                .putInt(message.length + 8)
                .array();

            final byte[] fullMessage = ByteBuffer.allocate(4 + 4 + message.length)
                .putInt(packet.getCommand().getCommand())
                .putInt(packet.getUserId())
                .put(message)
                .array();

            return ByteBuffer.allocate(14 + 2 + 4 + 4 + message.length + 2)
                .put(header)
                .putShort(CRC16.evaluateCrc(header, 0, header.length))
                .put(fullMessage)
                .putShort(CRC16.evaluateCrc(fullMessage, 0, fullMessage.length))
                .array();
        } catch (final JsonProcessingException e) {
            throw new RuntimeException("Can't process object to JSON", e);
        }
    }

}
