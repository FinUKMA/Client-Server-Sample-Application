package kma.cs.sample.domain.packet;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;

import kma.cs.sample.domain.Product;

class EncoderTest {

    @Test
    void shouldBuildMessage() {
        final Packet<Product> productPacket = Packet.<Product>builder()
            .command(Command.CREATE_PRODUCT)
            .packetId(10L)
            .userId(10)
            .body(Product.of("name1", BigDecimal.valueOf(1.1), BigDecimal.valueOf(2.2)))
            .build();

        final byte[] bytes = Encoder.encode(productPacket);

        assertThat(new String(Hex.encodeHex(bytes)))
            .isEqualTo("1300000000000000000a0000004c278b000000010000000a65794a705a434936626e567362437769626d46745a534936496d3568625755784969776963484a70593255694f6a45754d53776964473930595777694f6a49754d6e303d6a17");

    }

}