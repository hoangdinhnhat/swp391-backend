package com.swp391.backend.model.categoryGroup;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryGroupSold {
    private CategoryGroupDTO categoryGroup;
    private Integer sold;
}
