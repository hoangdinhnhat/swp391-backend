package com.swp391.backend.model.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public Message save(Message message) {
        return messageRepository.save(message);
    }

    public Message getById(Integer id) {
        return messageRepository.findById(id).orElse(null);
    }
}
