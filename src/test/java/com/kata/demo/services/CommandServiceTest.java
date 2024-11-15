package com.kata.demo.services;

import com.kata.demo.model.*;
import com.kata.demo.repository.BasketRepository;
import com.kata.demo.repository.CommandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CommandServiceTest {

    @InjectMocks
    private CommandService  commandService ;

    @Mock
    private CommandRepository commandRepository;

    @Mock
    private BasketRepository basketRepository;

    private Command mockCommand;

    private Basket basket;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        CommandLines line1 = CommandLines.builder().id(1L)
                              .product(new Product(1L,"LapTOP", "some description", 100.00, 4, "/image/test.png" ))
                              .quantite(2).build();

        CommandLines line2  = CommandLines.builder().id(1L)
                .product(new Product(1L,"watch", "some description", 150.00, 4, "/image/test.png" ))
                .quantite(2).build();

        mockCommand =  Command.builder().id(1L).client(Client.builder().id(1L).build())
                .lignes(Arrays.asList(line1, line2))
                .statut(Command.Statut.EN_ATTENTE)
                .dateCreation(new Date())
                .dateValidation(null)
                .dateAnnulation(null)
                .build();

        BasketRecords basketRecords = BasketRecords.builder().id(1L)
                .product(new Product(1L,"watch", "some description", 150.00, 4, "/image/test.png" ))
                .quantite(1).build();

        basket = Basket.builder().id(1L).client(Client.builder().id(1L).build())
                .lignes(Collections.singletonList(basketRecords)).build();
    }

    @Test
    void testGetAllCommands() {
        when(commandRepository.findByClientId(1L)).thenReturn(Collections.singletonList(mockCommand));

        var commands = commandService.getAllCommand(1L);

        assertNotNull(commands);
        assertEquals(1, commands.size());
        assertEquals(Command.Statut.EN_ATTENTE, commands.getFirst().getStatut());
        verify(commandRepository, times(1)).findByClientId(1L);

    }

    @Test
    void createCommandNormalCase() {
        when(basketRepository.findByClientId(1L)).thenReturn(Optional.of(basket));


        Command command = commandService.createCommand(1L);

        assertNotNull(basket);
        // assert that the same product from basketrecords gets copied to  commadlignes
        assertEquals(basket.getLignes().getFirst().getProduct(), command.getLignes().getFirst().getProduct());
        assertEquals(Command.Statut.EN_ATTENTE, command.getStatut());
        verify(basketRepository, times(1)).findByClientId(1L);

        // any because i don't have access to the command creaeted inside
        verify(commandRepository, times(1)).save(ArgumentMatchers.any());
        verify(basketRepository, times(1)).delete(basket);

    }

}
