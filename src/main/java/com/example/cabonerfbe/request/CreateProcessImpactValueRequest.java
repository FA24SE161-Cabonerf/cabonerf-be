package com.example.cabonerfbe.request;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

/**
 * The class Create process impact value request.
 *
 * @author SonPHH.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateProcessImpactValueRequest implements Serializable {
    /**
     * The Process id.
     */
    UUID processId;
    /**
     * The Method id.
     */
    UUID methodId;
}
