package com.kata.demo.controller;

import com.kata.demo.model.Basket;
import com.kata.demo.requestdto.AddToBasketRequest;
import com.kata.demo.services.IBasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/baskets")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000/")
public class BasketController {
    private final IBasketService basketService;

    @GetMapping("/{clientId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Basket getOrCreateBasket(@PathVariable Long clientId) {
        return basketService.getOrCreateBasket(clientId);
    }

    @PostMapping("/{clientId}/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Basket addProductToBasket(@PathVariable Long clientId, @RequestBody AddToBasketRequest requestBody) {
        System.out.println("requestBody.getProductId() : "+ requestBody.getProductId());
        return basketService.addProductToBasket(clientId, requestBody.getProductId(), requestBody.getQuantity());
    }

    @DeleteMapping("/{clientId}/remove")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Basket removeProductFromBasket(@PathVariable Long clientId, @RequestParam Long productId) {
        return basketService.removeProductFromBasket(clientId, productId);
    }
}

