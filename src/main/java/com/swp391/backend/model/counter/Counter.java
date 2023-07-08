package com.swp391.backend.model.counter;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class Counter {
    @Id
    private String id;
    private Double value;
}
