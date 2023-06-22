package com.swp391.backend.model.message;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.swp391.backend.model.conversation.Conversation;
import lombok.*;

import jakarta.persistence.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@ToString
public class Message {
    @Id
    @GeneratedValue
    private Integer id;
    private String senderImage;
    private String senderType;
    private Integer senderId;
    private Integer conId;
    private String content;
    private Date sendTime;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    @JsonBackReference
    private Conversation conversation;
}
