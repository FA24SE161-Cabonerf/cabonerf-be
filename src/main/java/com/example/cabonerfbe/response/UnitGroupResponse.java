package com.example.cabonerfbe.response;

import lombok.*;

import java.util.UUID;

/**
 * The class Unit group response.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UnitGroupResponse {
    private UUID id;
    private String unitGroupName;
    private String unitGroupType;
}
