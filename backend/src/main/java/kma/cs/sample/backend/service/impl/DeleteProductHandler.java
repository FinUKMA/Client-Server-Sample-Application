package kma.cs.sample.backend.service.impl;

import org.springframework.stereotype.Component;

import kma.cs.sample.backend.dao.ProductDao;
import kma.cs.sample.backend.service.CommandHandler;
import kma.cs.sample.domain.packet.Command;
import kma.cs.sample.domain.packet.Packet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
class DeleteProductHandler extends CommandHandler<Integer> {

    private final ProductDao productDao;

    public DeleteProductHandler(final ProductDao productDao) {
        super(Command.DELETE_PRODUCT);
        this.productDao = productDao;
    }

    @Override
    public Packet<Void> handle(final Packet<Integer> packet) {
        final int productId = packet.getBody();
        log.info("Delete product {}", productDao.getById(productId));
        productDao.delete(productId);

        return Packet.<Void>builder()
            .body(null)
            .packetId(packet.getPacketId())
            .userId(1)
            .command(Command.OK)
            .build();
    }
}
