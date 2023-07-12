package com.swp391.backend.model.conversation;

import com.swp391.backend.model.ConversationChatter.ConversationChatter;
import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ConversationRepository conversationRepository;

    public Conversation save(Conversation conversation) {
        return conversationRepository.save(conversation);
    }

    public Conversation getById(Integer id) {
        return conversationRepository.findById(id).orElse(null);
    }
}
