package kma.cs.sample.domain;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductFilter {

    private String name;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;
    private BigDecimal totalFrom;
    private BigDecimal totalTo;

    private int page;
    private int size;

}
