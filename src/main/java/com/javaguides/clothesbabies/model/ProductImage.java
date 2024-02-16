package com.javaguides.clothesbabies.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Data
@Document(collection = "product_image")
public class ProductImage {
    @Id
    private String _id;

    private String name;

    private int image_default;

    private int crop;

    private String type;

    private String image_url;

    private String image_url_small;

    public ProductImage() {
        super();
    }
}
