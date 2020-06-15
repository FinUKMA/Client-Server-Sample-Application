package kma.cs.sample.domain.packet;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;

import kma.cs.sample.domain.Product;

class DecoderTest {

    @Test
    void shouldDecodeToCreateProductPacket() throws DecoderException {
        final Packet<Product> productPacket = Decoder.decode(Hex.decodeHex(
            "1300000000000000000a00000048e48a000000020000000a65794a705a4349364d537769626d46745a534936496d3568625755784969776963484a70593255694f6a45754d53776964473930595777694f6a49754d6e303d0551"
        ));

        assertThat(productPacket)
            .extracting(Packet::getPacketId, Packet::getCommand, Packet::getBody)
            .containsExactly(10L, Command.GET_PRODUCT_BY_ID, Product.of(1, "name1", BigDecimal.valueOf(1.1), BigDecimal.valueOf(2.2)));
    }

}