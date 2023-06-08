package com.swp391.backend.model.categoryDetailInfo;

import com.swp391.backend.model.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryDetailInfoRepository extends JpaRepository<CategoryDetailInfo, Integer> {
    List<CategoryDetailInfo> findByCategory(Category category);
}
