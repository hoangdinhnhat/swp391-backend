package com.swp391.backend.controllers.shop;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class RealTimeData {
    private List<Integer> prev;
    private List<Integer> cur;
}
