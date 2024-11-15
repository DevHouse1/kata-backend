package com.kata.demo.repository;

import com.kata.demo.model.BasketRecords;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRecordsRepository extends JpaRepository<BasketRecords, Long> {
}
