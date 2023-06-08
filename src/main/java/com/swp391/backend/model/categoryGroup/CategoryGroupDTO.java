package com.swp391.backend.model.categoryGroup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CategoryGroupDTO {
    private Integer id;
    private String name;
}
