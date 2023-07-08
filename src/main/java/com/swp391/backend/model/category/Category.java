package com.swp391.backend.model.category;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.swp391.backend.model.categoryDetailInfo.CategoryDetailInfo;
import com.swp391.backend.model.categoryGroup.CategoryGroup;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;

    @OneToMany(mappedBy = "category")
    @JsonManagedReference
    private List<CategoryGroup> categoryGroups;

    @OneToMany(mappedBy = "category")
    @JsonManagedReference
    private List<CategoryDetailInfo> specificDetails;

    public CategoryDTO toDto() {
        return CategoryDTO.builder()
                .id(id)
                .name(name)
                .build();
    }
}
