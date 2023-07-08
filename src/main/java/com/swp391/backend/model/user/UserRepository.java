/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.model.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author Lenovo
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    @Query(
            value = "SELECT COUNT(u.id) " +
                    "FROM _user u " +
                    "WHERE EXTRACT(MONTH FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(MONTH FROM u.join_at)",
            nativeQuery = true
    )
    Integer getNewUserInMonth();

}
