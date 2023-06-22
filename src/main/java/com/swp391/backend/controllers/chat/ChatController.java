package com.swp391.backend.controllers.chat;

import com.swp391.backend.controllers.authentication.AuthenticatedManager;
import com.swp391.backend.model.ConversationChatter.ConversationChatter;
import com.swp391.backend.model.ConversationChatter.ConversationChatterService;
import com.swp391.backend.model.conversation.Conversation;
import com.swp391.backend.model.conversation.ConversationService;
import com.swp391.backend.model.message.Message;
import com.swp391.backend.model.message.MessageService;
import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.shop.ShopService;
import com.swp391.backend.model.user.User;
import com.swp391.backend.model.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private UserService userService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private ConversationChatterService conversationChatterService;

    @Autowired
    private MessageService messageService;

    @MessageMapping("/personal")
    @Transactional
    public List<Conversation> receiveMessage(@Payload MessageRequest request){

        List<Conversation> conversations = null;
        String url = "";
        if (request.getChatterType().equals("USER"))
        {
            User user = (User) userService.getById(request.getFromId());
            if(user == null) return null;
            conversations =  user.getConversationChatters().stream()
                    .map(it -> it.getConversation())
                    .collect(Collectors.toList());
            url = "USER-" + user.getId();
        }else if (request.getChatterType().equals("SHOP"))
        {
            Shop shop = shopService.getShopById(request.getFromId());
            if(shop == null) return null;
            conversations =  shop.getConversationChatters().stream()
                    .map(it -> it.getConversation())
                    .collect(Collectors.toList());
            url = "SHOP-" + shop.getId();
        }
        simpMessagingTemplate.convertAndSend("/personal/" + url, conversations);
        return conversations;
    }

    @MessageMapping("/private-message")
    public Message recMessage(@Payload MessageRequest request){
        Message message = chatService.sendMessage(request);

        String des = "/conversation/" + request.getConversationId();
        simpMessagingTemplate.convertAndSend(des, message);
        return message;
    }

    public boolean isConnect (User user, Shop shop)
    {
        if (user == null || shop == null)
        {
            return false;
        }

        List<Conversation> conversations = user.getConversationChatters().stream()
                .map(it -> it.getConversation())
                .collect(Collectors.toList());

        for(Conversation it : conversations)
        {
            var list = it.getConversationChatters().stream()
                    .filter(cc -> cc.getShop() != null && cc.getShop().getId() == shop.getId())
                    .collect(Collectors.toList());
            if (list.size() > 0) return true;
        }
        return false;
    }

    @MessageMapping("/conversation-request")
    public Conversation conversationCreation(@Payload ConversationRequest request){
        User user = null;
        Shop shop = null;
        Message message = null;

        if (request.getChatterType().equals("USER"))
        {
            user = (User) userService.getById(request.getFromId());
            shop = shopService.getShopById(request.getToId());

            if (isConnect(user, shop)) return null;

            message = Message.builder()
                    .senderImage(user.getImageurl())
                    .senderType("USER")
                    .senderId(user.getId())
                    .sendTime(new Date())
                    .build();

        }else if (request.getChatterType().equals("SHOP"))
        {
            shop = shopService.getShopById(request.getFromId());
            user = (User) userService.getById(request.getToId());

            if (isConnect(user, shop)) return null;

            message = Message.builder()
                    .senderImage(shop.getShopImage())
                    .senderType("SHOP")
                    .senderId(shop.getId())
                    .sendTime(new Date())
                    .build();
        }

        Conversation conversation = new Conversation();
        conversationService.save(conversation);

        ConversationChatter conversationChatterOne = ConversationChatter.builder()
                .conversation(conversation)
                .user(user)
                .build();

        ConversationChatter conversationChatterTwo = ConversationChatter.builder()
                .conversation(conversation)
                .shop(shop)
                .build();

        message.setConId(conversation.getId());
        message.setContent(request.getContent());
        message.setConversation(conversation);

        messageService.save(message);

        conversationChatterService.save(conversationChatterOne);
        conversationChatterService.save(conversationChatterTwo);

        Conversation returnConversation = conversationService.getById(conversation.getId());

        String des1 = "/conversation-request/" + user.getId();
        String des2 = "/conversation-request/" + shop.getId();
        simpMessagingTemplate.convertAndSend(des1, returnConversation);
        simpMessagingTemplate.convertAndSend(des2, returnConversation);

        return returnConversation;
    }
}
