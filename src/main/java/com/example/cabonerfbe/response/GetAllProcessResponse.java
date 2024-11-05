package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.PageList;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetAllProcessResponse {
    private PageList<CreateElementaryResponse> processes;
}
