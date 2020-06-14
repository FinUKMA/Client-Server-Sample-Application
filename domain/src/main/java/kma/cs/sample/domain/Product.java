package kma.cs.sample.domain;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class Product {

    private Integer id;
    private String name;
    private BigDecimal price;
    private BigDecimal total;

    public static Product of(final String name, final BigDecimal price, final BigDecimal total) {
        return of(null, name, price, total);
    }

}
