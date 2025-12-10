package dev.erick.json;

import java.time.Instant;

import dev.erick.domain.GeoLocation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TelemetryRequest {
    @NotBlank
    private String vehicleId;

    @NotNull
    private Instant timestamp;

    @NotNull
    @Valid
    private GeoLocation location;

    @NotNull
    private Double speed;

    private Double fuelLevel;

    private Double rpm;
}
