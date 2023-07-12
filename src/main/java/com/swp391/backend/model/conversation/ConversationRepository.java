package com.swp391.backend.model.conversation;

import com.swp391.backend.model.ConversationChatter.ConversationChatter;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
    List<Conversation> findByConversationChatters(ConversationChatter chatter, Sort sort);
}
