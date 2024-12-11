package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.ProjectDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * The class Get all project response.
 *
 * @author SonPHH.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetAllProjectResponse {
    private int pageCurrent;
    private int pageSize;
    private int totalPage;
    private List<ProjectDto> projects;
}
