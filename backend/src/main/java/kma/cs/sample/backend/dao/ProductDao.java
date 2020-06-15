package kma.cs.sample.backend.dao;

import static kma.cs.sample.backend.dao.DaoUtils.gte;
import static kma.cs.sample.backend.dao.DaoUtils.like;
import static kma.cs.sample.backend.dao.DaoUtils.lte;

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

    public Product getById(final int id) {
        final Product product = jdbcTemplate.query("select * from products where id = ?", new Object[] { id }, ProductDao::productRowMapper);
        if (product == null) {
            throw new ProductNotFoundException(id);
        }
        return product;
    }

    public List<Product> getList(final ProductFilter filter) {
        final String query = filterToWhereClause(filter);
        final String where = query.isEmpty() ? "" : " where " + query;
        final String sql = String.format("select * from products %s limit %d offset %d", where, filter.getSize(), filter.getPage() * filter.getSize());

        return jdbcTemplate.query(sql, (resultSet, rowNumber) -> productRowMapper(resultSet));
    }

    public long count(final ProductFilter filter) {
        final String query = filterToWhereClause(filter);
        final String where = query.isEmpty() ? "" : " where " + query;
        final String sql = String.format("select count(*) as total_products from products %s", where);

        return jdbcTemplate.query(sql, rs -> {
            rs.next();
            return rs.getLong("total_products");
        });
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

    private static Product productRowMapper(final ResultSet resultSet) throws SQLException {
        return resultSet.next()
            ? Product.of(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getBigDecimal("price"), resultSet.getBigDecimal("total"))
            : null;
    }

}
