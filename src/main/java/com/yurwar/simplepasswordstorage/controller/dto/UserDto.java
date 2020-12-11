package com.yurwar.simplepasswordstorage.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String username;
    private String address;
}
