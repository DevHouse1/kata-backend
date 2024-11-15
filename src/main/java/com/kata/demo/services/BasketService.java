package com.kata.demo.services;

import com.kata.demo.exception.GlobalExceptionHandler;
import com.kata.demo.exception.StockInsuffisantException;
import com.kata.demo.model.Basket;
import com.kata.demo.model.BasketRecords;
import com.kata.demo.model.Client;
import com.kata.demo.model.Product;
import com.kata.demo.repository.BasketRecordsRepository;
import com.kata.demo.repository.BasketRepository;
import com.kata.demo.repository.ProductRepository;
import com.sun.tools.jconsole.JConsoleContext;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasketService implements IBasketService {
    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;
    private final BasketRecordsRepository basketRecordsRepository;

    @Override
    public Basket getOrCreateBasket(Long clientId) {
        Optional<Basket> basket = basketRepository.findByClientId(clientId);
        return basket.orElseGet(() -> {
            Basket newBasket = new Basket();
            newBasket.setClient(Client.builder().id(clientId).build());
            return basketRepository.save(newBasket);
        });
    }

    @Override
    @Transactional
    public Basket addProductToBasket(Long clientId, Long productId, int quantity) {
        Basket basket = this.getOrCreateBasket(clientId);
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product Not Found"));


        Optional<BasketRecords> existingLigne = basket.getLignes().stream()
                .filter(br -> br.getProduct().getId().equals(productId))
                .findFirst();

        if (existingLigne.isPresent()) {
            BasketRecords basketRecords = existingLigne.get();

            if(product.getQuantiteStock() < basketRecords.getQuantite() + quantity){
                throw new StockInsuffisantException("only "+product.getQuantiteStock()+" Left of this Product");
            }

            basketRecords.setQuantite(basketRecords.getQuantite() + quantity);

        } else {
            if(product.getQuantiteStock() <  quantity){
                throw new StockInsuffisantException("only "+product.getQuantiteStock()+" Left of this Product");
            }
            BasketRecords basketRecords = BasketRecords.builder()
                    .basket(basket)
                    .product(product)
                    .quantite(quantity).build();
            basket.getLignes().add(basketRecords);
        }

        return basketRepository.save(basket);
    }
    @Override
    @Transactional
    public Basket removeProductFromBasket(Long clientId, Long productId) {
        Basket basket = getOrCreateBasket(clientId);
        Optional<BasketRecords> basketRecord = basket.getLignes().stream()
                                                     .filter(br -> br.getProduct().getId().equals(productId))
                                                     .findFirst();
        basketRecord.ifPresent(basketRecordsRepository::delete);
        basket.getLignes().removeIf(br -> br.getProduct().getId().equals(productId));
        return basket;

    }

}

