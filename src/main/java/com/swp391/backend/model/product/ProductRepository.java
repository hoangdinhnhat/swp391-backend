/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.model.product;

import java.util.List;

import com.swp391.backend.model.category.Category;
import com.swp391.backend.model.categoryGroup.CategoryGroup;
import com.swp391.backend.model.receiveinfo.ReceiveInfo;
import com.swp391.backend.model.shop.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author Lenovo
 */
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer>, JpaRepository<Product, Integer> {
    List<Product> findByCategoryGroup(CategoryGroup cg, Pageable pageable);

    List<Product> findByNameContainingIgnoreCase(String search, Pageable pageable);

    List<Product> findByNameContainingIgnoreCase(String search);

    List<Product> findAll(Sort sort);

    List<Product> findByShop(Shop shop, Pageable pageable);

    /*

    select
        r1_0.id,
        r1_0._default,
        r1_0.district_id,
        r1_0.fullname,
        r1_0.phone,
        r1_0.province_id,
        r1_0.specific_address,
        r1_0.user_id,
        r1_0.ward_id
    from
        receive_info r1_0
    where
        r1_0.user_id=?
    order by
        r1_0._default desc,
        r1_0.id asc offset ? rows fetch first ? rows only

     select p
     from
     Category c
     inner join
     CategoryGroup cg
     on c.id = cg.category
     inner join
     Product p
     on cg.id = p.categoryGroup
         order by
        r1_0._default desc,
        r1_0.id asc offset ? rows fetch first ? rows only

      select p
      from CategoryGroup cg
      where cg.category = ?1
      inner join Product p
      on cg.id = p.categoryGroup.id
               order by
        ?2 desc,
        r1_0.id asc offset ?3 rows fetch first ?4 rows only

        select p
        from Product p
        inner join CategoryGroup cg



    */
    List<Product> findByShop(Shop shop);

    @Query(
            value = "select p.id, \n" +
                    "       p.available, \n" +
                    "       p.description, \n" +
                    "       p.name, \n" +
                    "       p.price, \n" +
                    "       p.rating, \n" +
                    "       p.sold, \n" +
                    "       p.video, \n" +
                    "       p.category_group_id, \n" +
                    "       p.shop_id\n" +
                    "from product p \n" +
                    "    inner join category_group cg \n" +
                    "        on p.category_group_id = cg.id \n" +
                    "where cg.category_id = ?1",
            nativeQuery = true)
    List<Product> findByCategory(Integer categoryId, Pageable pageable);

    @Query(
            value = "select p.id, \n" +
                    "       p.available, \n" +
                    "       p.description, \n" +
                    "       p.name, \n" +
                    "       p.price, \n" +
                    "       p.rating, \n" +
                    "       p.sold, \n" +
                    "       p.video, \n" +
                    "       p.category_group_id, \n" +
                    "       p.shop_id\n" +
                    "from product p \n" +
                    "    inner join category_group cg \n" +
                    "        on p.category_group_id = cg.id \n" +
                    "where cg.category_id = ?1",
            nativeQuery = true)
    List<Product> findByCategory(Integer categoryId);
}