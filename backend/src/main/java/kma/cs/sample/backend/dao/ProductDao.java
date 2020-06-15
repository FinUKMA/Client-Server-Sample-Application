package kma.cs.sample.backend.dao;

import static kma.cs.sample.backend.dao.DaoUtils.gte;
import static kma.cs.sample.backend.dao.DaoUtils.like;
import static kma.cs.sample.backend.dao.DaoUtils.lte;
import static kma.cs.sample.backend.dao.DaoUtils.toArray;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kma.cs.sample.backend.exception.ProductNotFoundException;
import kma.cs.sample.domain.NewProduct;
import kma.cs.sample.domain.Product;
import kma.cs.sample.domain.ProductFilter;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public Product save(final NewProduct product) {
        final GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            final PreparedStatement preparedStatement = connection.prepareStatement("insert into products(name, price, total) values (?, ?, 0)", new String[]{ "id" });
            preparedStatement.setString(1, product.getName());
            preparedStatement.setBigDecimal(2, product.getPrice());
            return preparedStatement;
        }, generatedKeyHolder);

        return getById(generatedKeyHolder.getKey().intValue());
    }

    @Transactional
    public void delete(final int id) {
        jdbcTemplate.execute("delete from products where id = " + id);
    }

    @Transactional
    public void update(final Product product) {
        jdbcTemplate.update(
            "update products set name = ?, price = ?, total = ? where id = ?",
            product.getName(), product.getPrice(), product.getTotal(), product.getId()
        );
    }

    public Product getById(final int id) {
        final List<Product> products = jdbcTemplate.query("select * from products where id = ?", toArray(id), ProductDao::productRowMapper);
        if (products.isEmpty()) {
            throw new ProductNotFoundException(id);
        }
        return products.get(0);
    }

    public List<Product> getList(final ProductFilter filter) {
        final String query = filterToWhereClause(filter);
        final String where = query.isEmpty() ? "" : " where " + query;
        final String sql = String.format("select * from products %s order by id limit %d offset %d", where, filter.getSize(), filter.getPage() * filter.getSize());

        return jdbcTemplate.query(sql, ProductDao::productRowMapper);
    }

    public long count(final ProductFilter filter) {
        final String query = filterToWhereClause(filter);
        final String where = query.isEmpty() ? "" : " where " + query;
        final String sql = String.format("select count(*) as total_products from products %s", where);

        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    private static String filterToWhereClause(final ProductFilter filter) {
        return Stream.of(
            like("name", filter.getName()),
            gte("price", filter.getPriceFrom()),
            lte("price", filter.getPriceTo()),
            gte("total", filter.getTotalFrom()),
            lte("total", filter.getTotalTo())
        )
            .filter(Objects::nonNull)
            .collect(Collectors.joining(" AND "));
    }

    private static Product productRowMapper(final ResultSet resultSet, int rowNumber) throws SQLException {
        return Product.of(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getBigDecimal("price"), resultSet.getBigDecimal("total"));
    }

}
