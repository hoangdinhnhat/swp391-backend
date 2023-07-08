package com.swp391.backend.controllers.authentication;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Response {
    public int code;
    public String message;
    public ResponseData data;
}
