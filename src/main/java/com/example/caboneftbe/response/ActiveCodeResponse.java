package com.example.caboneftbe.response;

import com.example.caboneftbe.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ActiveCodeResponse {
    private UserDto user;
}
