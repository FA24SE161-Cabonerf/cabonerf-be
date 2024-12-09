package com.example.cabonerfbe.request;

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
