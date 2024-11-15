package com.kata.demo.services;

import com.kata.demo.model.Basket;
import com.kata.demo.model.Command;
import com.kata.demo.model.CommandLines;
import com.kata.demo.model.Product;
import com.kata.demo.repository.BasketRepository;
import com.kata.demo.repository.CommandRepository;
import com.kata.demo.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface ICommandService {

    public List<Command> getAllCommand(Long clientId);
    public Command createCommand(Long clientId);
    public Command cancelCommand(Long commandId);
    public Command validateCommand(Long commandId);
}

