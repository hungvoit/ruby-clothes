package com.javaguides.clothesbabies.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
@Data
public class ProductShippingDto {
    private Boolean isFreeShipping;
    private Integer timeFreeShipping;
    private Boolean isFlatRateShipping;
    private Integer timeFlatRateShipping;
    private Double timeFlatRatePrice;
    private String shippingService;
    private String shippingProvider;
}
