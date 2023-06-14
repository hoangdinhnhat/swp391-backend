package com.swp391.backend.controllers.publics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@ToString
public class FeedbackRequest {
    private String userId;
    private Date time;
    private Integer rate;
    private String description;
    private Integer productId;
}
