package com.swp391.backend.utils.zalopay_gateway;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ZaloPayResponse {
    private int return_code;
    private String return_message;
    private int sub_return_code;
    private String sub_return_message;
    private String zp_trans_token;
    private String order_url;
    private String order_token;
}
