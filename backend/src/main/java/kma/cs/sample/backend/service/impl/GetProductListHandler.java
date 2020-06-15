package kma.cs.sample.backend.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import kma.cs.sample.backend.dao.ProductDao;
import kma.cs.sample.backend.service.CommandHandler;
import kma.cs.sample.domain.Product;
import kma.cs.sample.domain.ProductFilter;
import kma.cs.sample.domain.ProductList;
import kma.cs.sample.domain.packet.Command;
import kma.cs.sample.domain.packet.Packet;

@Component
public class GetProductListHandler extends CommandHandler<ProductFilter> {

    private final ProductDao productDao;

    public GetProductListHandler(final ProductDao productDao) {
        super(Command.GET_PRODUCTS_LIST);
        this.productDao = productDao;
    }

    @Override
    public Packet<?> handle(final Packet<ProductFilter> packet) {
        final List<Product> products = productDao.getList(packet.getBody());
        final long totalProducts = productDao.count(packet.getBody());

        return Packet.<ProductList>builder()
            .command(Command.RESPONSE_PRODUCTS_LIST)
            .body(ProductList.of(products, totalProducts))
            .packetId(packet.getPacketId())
            .userId(0)
            .build();
    }
}
