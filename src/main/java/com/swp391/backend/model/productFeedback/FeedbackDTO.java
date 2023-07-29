package com.swp391.backend.model.productFeedback;

import com.swp391.backend.model.productFeedbackImage.ProductFeedbackImage;
import com.swp391.backend.model.productFeedbackReply.FeedbackReply;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class FeedbackDTO {
    private Integer id;
    private Integer userId;
    private String userName;
    private String userImageUrl;
    private String productImage;
    private String productName;
    private double productPrice;
    private Date time;
    private Integer rate;
    private String description;
    private String videoUrl;
    private List<ProductFeedbackImage> images;
    private List<FeedbackReply> feedbackReplies;
    private String type;
    private boolean processed;
    private String orderId;
}
