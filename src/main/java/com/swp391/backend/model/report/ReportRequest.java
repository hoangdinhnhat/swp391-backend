package com.swp391.backend.model.report;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReportRequest {
    private Integer reporterId;
    private Integer shopId;
    private Integer productId;
    private String orderId;
    private String reasonType;
    private String reasonSpecific;
}
