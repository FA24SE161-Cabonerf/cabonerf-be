package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.InviteUserOrganizationDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InviteMemberResponse {
    private List<InviteUserOrganizationDto> newMembers;
    private List<UUID> newAccountIds;
}
