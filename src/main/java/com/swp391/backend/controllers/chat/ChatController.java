package com.swp391.backend.controllers.chat;

import com.google.gson.Gson;
import com.swp391.backend.model.ConversationChatter.ConversationChatter;
import com.swp391.backend.model.ConversationChatter.ConversationChatterService;
import com.swp391.backend.model.conversation.Conversation;
import com.swp391.backend.model.conversation.ConversationService;
import com.swp391.backend.model.message.Message;
import com.swp391.backend.model.message.MessageService;
import com.swp391.backend.model.message.MessageType;
import com.swp391.backend.model.messageImage.MessageImage;
import com.swp391.backend.model.messageImage.MessageImageService;
import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.shop.ShopService;
import com.swp391.backend.model.user.User;
import com.swp391.backend.model.user.UserService;
import com.swp391.backend.utils.storage.MessageImageStorageService;
import com.swp391.backend.utils.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    @Qualifier(value = "productVideo")
    private StorageService productVideoStorageService;

    @Autowired
    private Gson gsonUtils;

    @Autowired
    private MessageImageService messageImageService;

    @Autowired
    @Qualifier(value = "messageImage")
    private StorageService messageImageStorageService;

    @Autowired
    @Qualifier(value = "messageVideo")
    private StorageService messageVideoStorageService;

    @MessageMapping("/personal")
    @Transactional
    public List<Conversation> receiveMessage(@Payload MessageRequest request) {

        List<Conversation> conversations = null;
        String url = "";
        if (request.getChatterType().equals("USER")) {
            User user = (User) userService.getById(request.getFromId());
            if (user == null) return null;
            conversations = user.getConversationChatters().stream()
                    .map(it -> it.getConversation())
                    .collect(Collectors.toList());
            url = "USER-" + user.getId();
        } else if (request.getChatterType().equals("SHOP")) {
            Shop shop = shopService.getShopById(request.getFromId());
            if (shop == null) return null;
            conversations = shop.getConversationChatters().stream()
                    .map(it -> it.getConversation())
                    .collect(Collectors.toList());
            url = "SHOP-" + shop.getId();
        }
        simpMessagingTemplate.convertAndSend("/personal/" + url, conversations);
        return conversations;
    }

    @MessageMapping("/private-message")
    public Message recMessage(@Payload MessageRequest request) {
        Message message = chatService.sendMessage(request);

        String des = "/conversation/" + request.getConversationId();
        simpMessagingTemplate.convertAndSend(des, message);
        return message;
    }

    @PostMapping("app/media-message")
    public ResponseEntity<String> mediaMessage(
            @RequestParam("message") String jsonRequest,
            @RequestParam("images") Optional<MultipartFile[]> imgs,
            @RequestParam("videos") Optional<MultipartFile[]> vds
    ) {
        var request = gsonUtils.fromJson(jsonRequest, MessageRequest.class);
        if (request.getContent().length() > 0) {
            Message message = chatService.sendMessage(request);
            String des = "/conversation/" + request.getConversationId();
            simpMessagingTemplate.convertAndSend(des, message);
        }

        MultipartFile[] images = imgs.orElse(null);
        if (images != null) {
            Message message = chatService.sendMediaMessage(request, MessageType.IMAGES);

            int count = 0;
            var services = (MessageImageStorageService) messageImageStorageService;
            List<MessageImage> messageImages = new ArrayList<>();
            for (MultipartFile image : images) {
                count++;
                services.store(image, message.getId().toString(), count + ".jpg");
                var mI = MessageImage.builder()
                        .message(message)
                        .url("/api/v1/publics/message/image/" + message.getId() + "?imgId=" + count)
                        .build();
                messageImages.add(mI);
                messageImageService.save(mI);
            }

            message.setImages(messageImages);
            String des = "/conversation/" + request.getConversationId();
            System.out.println(message);
            simpMessagingTemplate.convertAndSend(des, message);
        }

        MultipartFile[] videos = vds.orElse(null);
        if (videos != null) {
            var services = messageVideoStorageService;
            for (MultipartFile video : videos) {
                Message message = chatService.sendMediaMessage(request, MessageType.VIDEO);
                services.store(video, message.getId() + ".mp4");
                message.setVideo("/api/v1/publics/message/video/" + message.getId());

                message = messageService.save(message);
                String des = "/conversation/" + request.getConversationId();
                simpMessagingTemplate.convertAndSend(des, message);
            }
        }

        return ResponseEntity.ok().build();
    }

    public boolean isConnect(User user, Shop shop) {
        if (user == null || shop == null) {
            return false;
        }

        List<Conversation> conversations = user.getConversationChatters().stream()
                .map(it -> it.getConversation())
                .collect(Collectors.toList());

        for (Conversation it : conversations) {
            var list = it.getConversationChatters().stream()
                    .filter(cc -> cc.getShop() != null && cc.getShop().getId() == shop.getId())
                    .collect(Collectors.toList());
            if (list.size() > 0) return true;
        }
        return false;
    }

    @MessageMapping("/conversation-request")
    public Conversation conversationCreation(@Payload ConversationRequest request) {
        User user = null;
        Shop shop = null;
        Message message = null;

        if (request.getChatterType().equals("USER")) {
            user = (User) userService.getById(request.getFromId());
            shop = shopService.getShopById(request.getToId());

            if (isConnect(user, shop)) return null;

            message = Message.builder()
                    .senderImage(user.getImageurl())
                    .senderType("USER")
                    .type(MessageType.TEXT)
                    .senderId(user.getId())
                    .sendTime(new Date())
                    .build();

        } else if (request.getChatterType().equals("SHOP")) {
            shop = shopService.getShopById(request.getFromId());
            user = (User) userService.getById(request.getToId());

            if (isConnect(user, shop)) return null;

            message = Message.builder()
                    .senderImage(shop.getShopImage())
                    .senderType("SHOP")
                    .type(MessageType.TEXT)
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
