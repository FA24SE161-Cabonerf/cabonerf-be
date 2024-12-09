package com.example.cabonerfbe.response;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UserDashboardResponse {
    private String month;
    private int userCount;
}
