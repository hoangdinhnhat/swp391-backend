/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.controllers.chat;

import com.swp391.backend.model.ConversationChatter.ConversationChatter;
import com.swp391.backend.model.ConversationChatter.ConversationChatterService;
import com.swp391.backend.model.conversation.Conversation;
import com.swp391.backend.model.conversation.ConversationService;
import com.swp391.backend.model.message.Message;
import com.swp391.backend.model.message.MessageService;
import com.swp391.backend.model.message.MessageType;
import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.shop.ShopService;
import com.swp391.backend.model.user.User;
import com.swp391.backend.model.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Lenovo
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {
    private final ConversationService conversationService;
    private final ConversationChatterService conversationChatterService;
    private final MessageService messageService;
    private final ShopService shopService;
    private final UserService userService;

    public void init() {
        User user = (User) userService.loadUserByUsername("vuducthien@gmail.com");

        Shop shop = shopService.getShopById(1);

        Conversation conversation = new Conversation();
        conversationService.save(conversation);

        ConversationChatter conversationChatterOne = ConversationChatter.builder()
                .conversation(conversation)
                .shop(shop)
                .build();

        ConversationChatter conversationChatterTwo = ConversationChatter.builder()
                .conversation(conversation)
                .user(user)
                .build();

        conversationChatterService.save(conversationChatterOne);
        conversationChatterService.save(conversationChatterTwo);

        Message messageOne = Message.builder()
                .senderImage(user.getImageurl())
                .senderType("USER")
                .senderId(user.getId())
                .conId(conversation.getId())
                .content("Hello shop. Con legion 5 còn không ạ?")
                .type(MessageType.TEXT)
                .sendTime(new Date())
                .conversation(conversation)
                .build();

        Message messageTwo = Message.builder()
                .senderImage(shop.getShopImage())
                .senderType("SHOP")
                .senderId(shop.getId())
                .conId(conversation.getId())
                .content("Chào bạn. Legion 5 hiện đang còn hàng bạn nhé!")
                .type(MessageType.TEXT)
                .sendTime(new Date())
                .conversation(conversation)
                .build();

        Message messageThree = Message.builder()
                .senderImage(shop.getShopImage())
                .senderType("SHOP")
                .senderId(shop.getId())
                .conId(conversation.getId())
                .content("Nếu bạn có nhu cầu thì hãy để lại thông tin để shop tư vấn cho bạn 1 cách sớm nhất nhé!")
                .type(MessageType.TEXT)
                .sendTime(new Date())
                .conversation(conversation)
                .build();

        messageService.save(messageOne);
        messageService.save(messageTwo);
        messageService.save(messageThree);

    }

    public Message sendMessage(MessageRequest request) {
        Message message = null;
        if (request.getChatterType().equals("USER")) {
            User user = (User) userService.getById(request.getFromId());
            Conversation conversation = conversationService.getById(request.getConversationId());

            message = Message.builder()
                    .senderImage(user.getImageurl())
                    .senderType("USER")
                    .senderId(user.getId())
                    .conId(conversation.getId())
                    .content(request.getContent())
                    .type(MessageType.TEXT)
                    .sendTime(request.getSendTime())
                    .conversation(conversation)
                    .build();

            messageService.save(message);
        } else if (request.getChatterType().equals("SHOP")) {
            Shop shop = shopService.getShopById(request.getFromId());
            Conversation conversation = conversationService.getById(request.getConversationId());

            message = Message.builder()
                    .senderImage(shop.getShopImage())
                    .senderType("SHOP")
                    .senderId(shop.getId())
                    .conId(conversation.getId())
                    .content(request.getContent())
                    .type(MessageType.TEXT)
                    .sendTime(request.getSendTime())
                    .conversation(conversation)
                    .build();

            messageService.save(message);
        }
        return message;
    }

    public Message sendMediaMessage(MessageRequest request, MessageType messageType) {
        Message message = null;
        if (request.getChatterType().equals("USER")) {
            User user = (User) userService.getById(request.getFromId());
            Conversation conversation = conversationService.getById(request.getConversationId());

            message = Message.builder()
                    .senderImage(user.getImageurl())
                    .senderType("USER")
                    .senderId(user.getId())
                    .conId(conversation.getId())
                    .type(messageType)
                    .sendTime(request.getSendTime())
                    .conversation(conversation)
                    .build();

            messageService.save(message);
        } else if (request.getChatterType().equals("SHOP")) {
            Shop shop = shopService.getShopById(request.getFromId());
            Conversation conversation = conversationService.getById(request.getConversationId());

            message = Message.builder()
                    .senderImage(shop.getShopImage())
                    .senderType("SHOP")
                    .senderId(shop.getId())
                    .conId(conversation.getId())
                    .type(messageType)
                    .sendTime(request.getSendTime())
                    .conversation(conversation)
                    .build();

            messageService.save(message);
        }
        return message;
    }
}
