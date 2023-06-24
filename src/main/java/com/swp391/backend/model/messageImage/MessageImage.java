package com.swp391.backend.model.messageImage;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.swp391.backend.model.message.Message;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MessageImage {

    @Id
    @GeneratedValue
    private Integer id;
    private String url;

    @ManyToOne
    @JoinColumn(name = "message_id")
    @JsonBackReference
    private Message message;

}
