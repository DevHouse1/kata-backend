package com.kata.demo.repository;

import com.kata.demo.model.Command;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandRepository extends JpaRepository<Command, Long> {
    List<Command> findByClientId(Long clientId);
}
