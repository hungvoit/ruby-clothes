package com.javaguides.clothesbabies.dto;

import com.javaguides.clothesbabies.model.ProductImage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
@Data
public class ProductDto {
    @Id
    private String _id;
    private Long customerId;
    private Long parentCategoryId;
    private Double price;
    private Integer quantity;
    public String discount;
    private Date displayStartDate;
    private Date displayEndDate;
    private String description;
    private String productName;
    public List<ProductImage> images;
    public String status;
    public ProductShippingDto shipping;
    public List<Long> returnRules;

    public ProductDto() {
    }
}
