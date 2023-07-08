package com.swp391.backend.model.message;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.swp391.backend.model.conversation.Conversation;
import com.swp391.backend.model.messageImage.MessageImage;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    private MessageType type;

    @OneToMany(mappedBy = "message")
    @JsonManagedReference
    private List<MessageImage> images;

    private String video;

    private Date sendTime;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    @JsonBackReference
    private Conversation conversation;
}
