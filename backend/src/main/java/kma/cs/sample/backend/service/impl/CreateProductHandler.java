package kma.cs.sample.backend.service.impl;

import org.springframework.stereotype.Component;

import kma.cs.sample.backend.dao.ProductDao;
import kma.cs.sample.backend.service.CommandHandler;
import kma.cs.sample.domain.Product;
import kma.cs.sample.domain.packet.Command;
import kma.cs.sample.domain.packet.Packet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
class CreateProductHandler extends CommandHandler<Product> {

    private final ProductDao productDao;

    public CreateProductHandler(final ProductDao productDao) {
        super(Command.CREATE_PRODUCT);

        this.productDao = productDao;
    }

    @Override
    public Packet<Void> handle(final Packet<Product> packet) {
        log.info("Create new product {}", packet.getBody());
        productDao.save(packet.getBody());

        return Packet.<Void>builder()
            .body(null)
            .packetId(packet.getPacketId())
            .userId(1)
            .command(Command.OK)
            .build();
    }
}
