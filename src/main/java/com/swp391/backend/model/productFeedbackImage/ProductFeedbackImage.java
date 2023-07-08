package com.swp391.backend.model.productFeedbackImage;

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
public class ProductFeedbackImage {
    @Id
    @GeneratedValue
    private Integer id;
    private String url;

    @ManyToOne
    @JoinColumn(name = "feedback_id")
    @JsonBackReference
    private Feedback feedback;
}
