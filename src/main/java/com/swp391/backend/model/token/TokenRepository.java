/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.swp391.backend.model.token;

import com.swp391.backend.model.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Lenovo
 */
public interface TokenRepository extends JpaRepository<Token, Integer>{
    Optional<Token> findByUserAndType(User user, String type);

    Optional<Token> findByValue(String value);
}
