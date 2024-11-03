package com.example.cabonerfbe.response;

import lombok.*;

import java.util.UUID;

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
