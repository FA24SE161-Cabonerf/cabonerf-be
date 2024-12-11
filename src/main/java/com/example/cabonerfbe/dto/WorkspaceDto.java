package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

/**
 * The class Workspace dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class WorkspaceDto {
    private UUID id;
    private String name;
    private boolean isDefault;
}
