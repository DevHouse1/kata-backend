package com.kata.demo.services;

import com.kata.demo.model.Basket;

public interface IBasketService {
        Basket getOrCreateBasket(Long clientId);
        Basket addProductToBasket(Long clientId, Long productId, int quantity);
        Basket removeProductFromBasket(Long clientId, Long productId);
}
