package com.kata.demo.controller;

import com.kata.demo.model.Command;
import com.kata.demo.services.ICommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000/")
public class CommandController {
    private final ICommandService commandService;

    @GetMapping("/{clientId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<Command> getAllCommand(@PathVariable Long clientId) {
        return commandService.getAllCommand(clientId);
    }
    @PostMapping("/{clientId}")
    public Command createCommand(@PathVariable Long clientId) {
        return commandService.createCommand(clientId);
    }

    @PutMapping("/{orderId}/cancel")
    public Command cancelCommand(@PathVariable Long orderId) {
        return commandService.cancelCommand(orderId);
    }

    @PutMapping("/{orderId}/validate")
    public Command validateCommand(@PathVariable Long orderId) {
        return commandService.validateCommand(orderId);
    }
}
