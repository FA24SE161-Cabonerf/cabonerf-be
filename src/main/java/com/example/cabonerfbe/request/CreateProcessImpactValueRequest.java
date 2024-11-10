package com.example.cabonerfbe.request;

import com.example.cabonerfbe.models.Process;
import com.example.cabonerfbe.models.Project;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateProcessImpactValueRequest implements Serializable {
    UUID processId;
    UUID methodId;
}
