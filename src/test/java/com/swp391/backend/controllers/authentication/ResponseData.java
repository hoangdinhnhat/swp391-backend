package com.swp391.backend.controllers.authentication;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResponseData {
    private int total;
    private int service_fee;
    private int insurance_fee;
    private int pick_station_fee;
    private int coupon_value;
    private int r2s_fee;
    private int return_again;
    private int document_return;
    private int double_check;
    private int cod_fee;
    private int pick_remote_areas_fee;
    private int deliver_remote_areas_fee;
    private int cod_failed_fee;
}
