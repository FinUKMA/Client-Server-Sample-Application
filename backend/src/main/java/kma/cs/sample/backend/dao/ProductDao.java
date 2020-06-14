package kma.cs.sample.backend.dao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kma.cs.sample.backend.exception.ProductNotFoundException;
import kma.cs.sample.domain.Product;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public Product save(final Product product) {
        final GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            final PreparedStatement preparedStatement = connection.prepareStatement("insert into products(name, price, total) values (?, ?, ?)", new String[]{ "id" });
            preparedStatement.setString(1, product.getName());
            preparedStatement.setBigDecimal(2, product.getPrice());
            preparedStatement.setBigDecimal(3, product.getTotal());
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

    private static Product productRowMapper(final ResultSet resultSet) throws SQLException {
        return resultSet.next()
            ? Product.of(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getBigDecimal("price"), resultSet.getBigDecimal("total"))
            : null;
    }

}
