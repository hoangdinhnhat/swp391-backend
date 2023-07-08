package com.swp391.backend.model.settings;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Setting {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private Integer value;
}
