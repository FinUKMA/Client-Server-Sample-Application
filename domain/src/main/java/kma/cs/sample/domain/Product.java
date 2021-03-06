package kma.cs.sample.domain;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class Product {

    private int id;
    private String name;
    private BigDecimal price;
    private BigDecimal total;

}
