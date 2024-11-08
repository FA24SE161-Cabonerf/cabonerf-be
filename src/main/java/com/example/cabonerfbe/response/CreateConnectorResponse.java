package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.ConnectorDto;
import com.example.cabonerfbe.dto.ProcessDto;
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
public class CreateConnectorResponse {
    private ConnectorDto connector;
    private ProcessDto process;
}
