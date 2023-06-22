package com.swp391.backend.model.conversation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ConversationRepository conversationRepository;

    public Conversation save(Conversation conversation)
    {
        return conversationRepository.save(conversation);
    }

    public Conversation getById(Integer id)
    {
        return conversationRepository.findById(id).orElse(null);
    }
}
