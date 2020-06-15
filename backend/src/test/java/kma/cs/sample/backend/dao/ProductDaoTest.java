package kma.cs.sample.backend.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

import kma.cs.sample.backend.exception.ProductNotFoundException;
import kma.cs.sample.domain.NewProduct;
import kma.cs.sample.domain.Product;

@DatabaseTearDown("/tearDown.xml")
class ProductDaoTest extends AbstractDaoTest {

    @Autowired
    private ProductDao productDao;

    @Test
    @ExpectedDatabase(value = "/ProductDaoTest/expectedAfterInsertNew.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    void shouldSaveNewProduct() {
        assertThat(productDao.save(NewProduct.of("name1", BigDecimal.valueOf(1.1))))
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
                new BigDecimal("0.000")
            );
    }

    @Test
    void shouldThrowException_whenNoProductById() {
        assertThatThrownBy(() -> productDao.getById(100000))
            .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DatabaseSetup("/ProductDaoTest/productBeforeUpdate.xml")
    @ExpectedDatabase(value = "/ProductDaoTest/productAfterUpdate.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    void shouldUpdateProductById() {
        productDao.update(Product.of(1, "name_update", BigDecimal.valueOf(1111), BigDecimal.valueOf(700)));
    }

}