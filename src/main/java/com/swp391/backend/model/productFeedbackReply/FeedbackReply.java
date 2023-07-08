package com.swp391.backend.model.productFeedbackReply;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.swp391.backend.model.productFeedback.Feedback;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FeedbackReply {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "feedback_id")
    @JsonBackReference
    private Feedback feedback;
}
