package com.swp391.backend.model.counter;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CounterRepository extends JpaRepository<Counter, String> {
}
