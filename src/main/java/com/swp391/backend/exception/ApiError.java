/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.exception;

import java.time.LocalDateTime;
import lombok.Builder;

/**
 *
 * @author Lenovo
 */
@Builder
public record ApiError(
        String path,
        String message,
        String exceptionMessage,
        int statusCode,
        LocalDateTime localDateTime
) {
}
