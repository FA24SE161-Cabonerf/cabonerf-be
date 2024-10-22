package com.example.cabonerfbe.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UnitGroupResponse {
    private Long id;
    private String unitGroupName;
    private String unitGroupType;
}
