/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.swp391.backend.utils.mail;

/**
 * @author Lenovo
 */
public interface EmailSender {
    public void send(String subject, String message, String targetEmail) throws Exception;
}
