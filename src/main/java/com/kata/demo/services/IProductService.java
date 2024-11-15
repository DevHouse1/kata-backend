package com.kata.demo.services;

import com.kata.demo.model.Product;
import java.util.List;

public interface IProductService {
    public List<Product> getAllProducts();
    public Product getProductById(Long id);
}
