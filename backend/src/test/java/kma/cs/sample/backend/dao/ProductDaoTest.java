package kma.cs.sample.backend.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

import kma.cs.sample.backend.exception.ProductNotFoundException;
import kma.cs.sample.domain.Product;

@DatabaseTearDown("/tearDown.xml")
class ProductDaoTest extends AbstractDaoTest {

    @Autowired
    private ProductDao productDao;

    @Test
    @ExpectedDatabase(value = "/ProductDaoTest/expectedAfterInsertNew.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    void shouldSaveNewProduct() {
        assertThat(productDao.save(Product.of("name1", BigDecimal.valueOf(1.1), BigDecimal.valueOf(2.2))))
            .extracting(
                Product::getId,
                Product::getName,
                Product::getPrice,
                Product::getTotal
            )
            .containsExactly(
                1000,
                "name1",
                new BigDecimal("1.100"),
                new BigDecimal("2.200")
            );
    }

    @Test
    void shouldThrowException_whenNoProductById() {
        assertThatThrownBy(() -> productDao.getById(100000))
            .isInstanceOf(ProductNotFoundException.class);
    }

}