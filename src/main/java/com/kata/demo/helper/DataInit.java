package com.kata.demo.helper;


import com.kata.demo.model.Client;
import com.kata.demo.model.Product;
import com.kata.demo.repository.BasketRepository;
import com.kata.demo.repository.ClientRepository;
import com.kata.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInit implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;
    private final BasketRepository basketRepository;

    @Override
    public void run(String... args) throws Exception {

        // Initialize products
        productRepository.save(new Product(null, "Laptop", "High-performance laptop", 1200.00, 10, "/images/watch.png"));
        productRepository.save(new Product(null, "Smartphone", "Latest model smartphone", 800.00, 15, "/images/watch.png"));
        productRepository.save(new Product(null, "Headphones", "Wireless noise-cancelling headphones", 200.00, 20, "/images/watch.png"));
        productRepository.save(new Product(null, "Smartwatch", "Feature-packed smartwatch", 250.00, 30, "/images/watch.png"));
        productRepository.save(new Product(null, "Camera", "professional camera", 1500.00, 5, "/images/watch.png"));
        productRepository.save(new Product(null, "SmartWatch", "professional watch", 1500.00, 5, "/images/watch.png"));

        // initialize test client
        clientRepository.save(new Client(null, "houssam", "Hashed_Password"));

        // this is a temporary solution for the problem of having tow basket for the same client id
        // i am invistigating this problem
         basketRepository.deleteAll();


        System.out.println("Database init finished");
    }
}

