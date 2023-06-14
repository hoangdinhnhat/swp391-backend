package com.swp391.backend.model.order;

import com.swp391.backend.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.sql.Date;
import java.util.List;

public interface OrderRepository extends PagingAndSortingRepository<Order, Integer>, JpaRepository<Order, Integer> {
    @Query(
            value = "select count(o.created_time)\n" +
                    "from _order o\n" +
                    "order by o.created_time\n" +
                    "having o.created_time = ?1",
            nativeQuery = true)
    Integer getNumberOfOrderInDate(Date date);
}
