package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class MemberOrganizationDto {
    private UUID id;
    private List<InviteUserOrganizationDto> members;
}
