package com.swp391.backend.controllers.authentication;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ShippingFeeCalculator {
    public int from_province_id;
    public int from_district_id;
    public String from_ward_code;
    public int service_id;
    public int service_type_id;
    public int to_province_id;
    public int to_district_id;
    public String to_ward_code;
    public int height;
    public int length;
    public int weight;
    public int width;
    public int insurance_value;
    public int cod_failed_amount;
}
