package com.kata.demo.requestdto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddToBasketRequest {
    private Long productId;
    private int quantity;

}
