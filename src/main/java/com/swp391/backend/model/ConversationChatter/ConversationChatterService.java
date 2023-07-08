package com.swp391.backend.model.ConversationChatter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConversationChatterService {

    private final ConversationChatterRepository conversationChatterRepository;

    public ConversationChatter save(ConversationChatter conversationChatter) {
        return conversationChatterRepository.save(conversationChatter);
    }

    public ConversationChatter getById(Integer id) {
        return conversationChatterRepository.findById(id).orElse(null);
    }
}
