package kma.cs.sample.backend.service.impl;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import kma.cs.sample.backend.dao.ProductDao;
import kma.cs.sample.backend.service.CommandHandler;
import kma.cs.sample.domain.Product;
import kma.cs.sample.domain.packet.Command;
import kma.cs.sample.domain.packet.Packet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
class EditProductHandler extends CommandHandler<Product> {

    private final ProductDao productDao;

    public EditProductHandler(final ProductDao productDao) {
        super(Command.UPDATE_PRODUCT);
        this.productDao = productDao;
    }

    @Override
    @PreAuthorize("hasAuthority('user')")
    public Packet<Void> handle(final Packet<Product> packet) {
        log.info("Update product {}", packet.getBody());
        productDao.update(packet.getBody());

        return Packet.<Void>builder()
            .body(null)
            .packetId(packet.getPacketId())
            .userId(1)
            .command(Command.OK)
            .build();
    }
}
