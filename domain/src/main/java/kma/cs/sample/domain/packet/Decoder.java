package kma.cs.sample.domain.packet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Decoder {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> Packet<T> decode(final byte[] inputMsg) {
        if(inputMsg[0] != 0x13){
            throw new IllegalArgumentException("Invalid magic byte!");
        }
        final long packId = ByteBuffer.wrap(inputMsg, 2, 8)
            .order(ByteOrder.BIG_ENDIAN)
            .getLong();
        final int msgLenEnc = ByteBuffer.wrap(inputMsg, 10, 4)
            .order(ByteOrder.BIG_ENDIAN)
            .getInt();
        final short crc1 = ByteBuffer.wrap(inputMsg, 14, 2)
            .order(ByteOrder.BIG_ENDIAN)
            .getShort();
        final short crc1Evaluated = CRC16.evaluateCrc(inputMsg, 0, 14);
        if(crc1Evaluated != crc1){
            throw new IllegalArgumentException("CRC1 expected: " + crc1Evaluated + ", out was " + crc1);
        }
        byte[] enc_message = new byte[msgLenEnc];
        System.arraycopy(inputMsg, 16, enc_message, 0, msgLenEnc);
        final int msgLenDecr = enc_message.length;
        final int cType = ByteBuffer.wrap(enc_message, 0, 4)
            .order(ByteOrder.BIG_ENDIAN)
            .getInt();

        final Command<T> command = Command.getCommandById(cType);
        final int mUserId = ByteBuffer.wrap(enc_message, 4,4)
            .order(ByteOrder.BIG_ENDIAN)
            .getInt();
        byte[] jsonArray = new byte[msgLenDecr-8];
        System.arraycopy(enc_message, 8, jsonArray, 0, msgLenDecr-8);
        final short crc2 = ByteBuffer.wrap(inputMsg, 16 + msgLenEnc, 2)
            .order(ByteOrder.BIG_ENDIAN)
            .getShort();
        final short crc2Evaluated = CRC16.evaluateCrc(enc_message, 0, msgLenEnc);
        if(crc2Evaluated != crc2){
            throw new IllegalArgumentException("CRC2 expected: " + crc2Evaluated + ", out was " + crc2);
        }

        try {
            return Packet.<T>builder()
                .packetId(packId)
                .userId(mUserId)
                .command(command)
                .body(OBJECT_MAPPER.readValue(Base64.getDecoder().decode(jsonArray), command.getKlass()))
                .build();
        } catch (final IOException e) {
            throw new RuntimeException(String.format("Can't deserialize message %s to class %s", new String(jsonArray), command.getKlass()), e);
        }
    }

}
