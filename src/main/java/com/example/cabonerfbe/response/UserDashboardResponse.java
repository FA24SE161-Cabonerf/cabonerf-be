package com.example.cabonerfbe.response;

import lombok.*;

/**
 * The class User dashboard response.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UserDashboardResponse {
    private String month;
    private int userCount;
}
