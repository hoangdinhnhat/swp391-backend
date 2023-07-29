package com.swp391.backend.model.counter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CounterService {
    private final CounterRepository counterRepository;

    public Counter save(Counter counter) {
        return counterRepository.save(counter);
    }

    public Counter getById(String id) {
        return counterRepository.findById(id).orElse(null);
    }

    public void init() {
        Counter counter = Counter.builder()
                .id("VISIT_PAGE")
                .value(0.0)
                .build();

        Counter counter2 = Counter.builder()
                .id("WALLET")
                .value(1000000.0)
                .build();

        save(counter);
        save(counter2);
    }
}
