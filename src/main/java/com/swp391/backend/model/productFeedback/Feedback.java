package com.swp391.backend.model.productFeedback;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.swp391.backend.model.product.Product;
import com.swp391.backend.model.productFeedbackImage.ProductFeedbackImage;
import com.swp391.backend.model.productFeedbackReply.FeedbackReply;
import com.swp391.backend.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Feedback {
    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
    private Date time;
    private Integer rate;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String videoUrl;

    @OneToMany(mappedBy = "feedback")
    @JsonManagedReference
    private List<ProductFeedbackImage> images;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    @OneToMany(mappedBy = "feedback")
    @JsonManagedReference
    private List<FeedbackReply> feedbackReplies;

    private String type;
    private String reason;
    private boolean processed;
    private String orderId;

    public FeedbackDTO toDto() {
        return FeedbackDTO.builder()
                .id(id)
                .userId(user.getId())
                .userName(user.getFirstname() + " " + user.getLastname())
                .userImageUrl(user.getImageurl())
                .productImage(product.getImages().get(0).getUrl())
                .productName(product.getName())
                .productPrice(product.getPrice())
                .time(time)
                .rate(rate)
                .description(description)
                .videoUrl(videoUrl)
                .images(images)
                .feedbackReplies(feedbackReplies)
                .processed(processed)
                .orderId(orderId)
                .type(type)
                .build();
    }
}
