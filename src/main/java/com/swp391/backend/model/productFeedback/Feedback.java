package com.swp391.backend.model.productFeedback;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.swp391.backend.model.product.Product;
import com.swp391.backend.model.productFeedbackImage.ProductFeedbackImage;
import com.swp391.backend.model.productImage.ProductImage;
import com.swp391.backend.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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
    private String description;
    private String videoUrl;

    @OneToMany(mappedBy = "feedback")
    @JsonManagedReference
    private List<ProductFeedbackImage> images;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    public FeedbackDTO toDto() {
        return FeedbackDTO.builder()
                .userName(user.getFirstname() + " " + user.getLastname())
                .userImageUrl(user.getImageurl())
                .time(time)
                .rate(rate)
                .description(description)
                .videoUrl(videoUrl)
                .images(images)
                .build();
    }
}
