package com.swp391.backend.model.productFeedback;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.swp391.backend.model.product.Product;
import com.swp391.backend.model.productFeedbackImage.ProductFeedbackImage;
import com.swp391.backend.model.productFeedbackReply.FeedbackReply;
import com.swp391.backend.model.user.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class FeedbackDTO {
    private String userName;
    private String userImageUrl;
    private Date time;
    private Integer rate;
    private String description;
    private String videoUrl;
    private List<ProductFeedbackImage> images;
    private  List<FeedbackReply> feedbackReplies;
}
