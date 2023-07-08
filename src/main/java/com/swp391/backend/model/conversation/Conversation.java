package com.swp391.backend.model.conversation;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.swp391.backend.model.ConversationChatter.ConversationChatter;
import com.swp391.backend.model.message.Message;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class Conversation {
    @Id
    @GeneratedValue
    private Integer id;

    @OneToMany(mappedBy = "conversation")
    @JsonManagedReference
    private List<Message> messages;

    @OneToMany(mappedBy = "conversation")
    @JsonManagedReference
    private List<ConversationChatter> conversationChatters;
}
