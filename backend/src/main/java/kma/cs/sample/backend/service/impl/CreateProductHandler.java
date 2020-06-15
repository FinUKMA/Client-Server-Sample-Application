package kma.cs.sample.backend.service.impl;

import org.springframework.stereotype.Component;

import kma.cs.sample.backend.dao.ProductDao;
import kma.cs.sample.backend.service.CommandHandler;
import kma.cs.sample.domain.NewProduct;
import kma.cs.sample.domain.Product;
import kma.cs.sample.domain.packet.Command;
import kma.cs.sample.domain.packet.Packet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
class CreateProductHandler extends CommandHandler<NewProduct> {

    private final ProductDao productDao;

    public CreateProductHandler(final ProductDao productDao) {
        super(Command.CREATE_PRODUCT);

        this.productDao = productDao;
    }

    @Override
    public Packet<Product> handle(final Packet<NewProduct> packet) {
        log.info("Create new product {}", packet.getBody());
        final Product product = productDao.save(packet.getBody());

        return Packet.<Product>builder()
            .body(product)
            .packetId(packet.getPacketId())
            .userId(1)
            .command(Command.GET_PRODUCT_BY_ID)
            .build();
    }
}
