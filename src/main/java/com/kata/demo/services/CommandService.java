package com.kata.demo.services;

import com.kata.demo.exception.CommandAnnulationException;
import com.kata.demo.model.*;
import com.kata.demo.repository.BasketRepository;
import com.kata.demo.repository.CommandRepository;
import com.kata.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommandService implements ICommandService {

    private final CommandRepository commandRepository;
    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;

    @Override
    public List<Command> getAllCommand(Long clientId) {
        return commandRepository.findByClientId(clientId);
    }

    @Override
    @Transactional
    public Command createCommand(Long clientId) {

        Basket basket = basketRepository.findByClientId(clientId)
                .orElseThrow(() -> new RuntimeException("basket not Found"));

        if (basket.getLignes().isEmpty()) {
            throw new RuntimeException("Basket is Empty");
        }

        Command command = Command.builder()
                            .client(basket.getClient())
                            .dateCreation(new Date())
                            .lignes(new ArrayList<>())
                            .statut(Command.Statut.EN_ATTENTE).build();

        basket.getLignes().forEach(br -> {
            CommandLines commandLines = CommandLines.builder()
                                          .command(command)
                                          .product(br.getProduct())
                                          .quantite(br.getQuantite())
                                          .build();


            command.getLignes().add(commandLines);
        });

        commandRepository.save(command);
        basketRepository.delete(basket);

        return command;
    }

    @Override
    public Command cancelCommand(Long commandId) {
        Command command = commandRepository.findById(commandId)
                .orElseThrow(() -> new RuntimeException("order Not Found"));

        long diff = new Date().getTime() - command.getDateCreation().getTime();
        long hoursDiff = TimeUnit.MILLISECONDS.toHours(diff);
        if(hoursDiff > 1){
            throw  new CommandAnnulationException("you can't cancel commande after one hour");
        }

        if (command.getStatut() == Command.Statut.ANNULEE) {
            throw new RuntimeException("Order Cancelled.");
        }

        command.setStatut(Command.Statut.ANNULEE);
        command.setDateAnnulation(new Date());
        return commandRepository.save(command);
    }

    @Override
    @Transactional
    public Command validateCommand(Long commandId) {
        Command command = commandRepository.findById(commandId)
                .orElseThrow(() -> new RuntimeException("order not Found"));

        if (command.getStatut() == Command.Statut.ANNULEE) {
            throw new RuntimeException("order cancelled.");
        }

        if (command.getStatut() != Command.Statut.EN_ATTENTE) {
            throw new RuntimeException("order already validated.");
        }
        // check if there are products out of stock and raise an exception
        List<CommandLines> outOfStockProducts = command.getLignes().stream().filter(l ->
            l.getProduct().getQuantiteStock() - l.getQuantite() < 0
        ).toList();

        if (!outOfStockProducts.isEmpty()){
            throw new RuntimeException("Products out of stock "+ outOfStockProducts);
        }

        command.setStatut(Command.Statut.VALIDEE);
        command.setDateValidation(new Date());

        List<Product> productToUpdate = command.getLignes().stream().map(l -> {
            //update quantite en stock
            l.getProduct().setQuantiteStock(l.getProduct().getQuantiteStock() - l.getQuantite());
            return l.getProduct();
        }).toList();

        //save updated product to database
        productRepository.saveAll(productToUpdate);

        return commandRepository.save(command);
    }
}
