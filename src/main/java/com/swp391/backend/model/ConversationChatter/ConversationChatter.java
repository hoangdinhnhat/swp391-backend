package com.swp391.backend.model.ConversationChatter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.swp391.backend.model.conversation.Conversation;
import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.user.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class ConversationChatter {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;
}
