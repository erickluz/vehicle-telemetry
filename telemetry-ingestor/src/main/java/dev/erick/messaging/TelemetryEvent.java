package dev.erick.messaging;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;

import dev.erick.domain.GeoLocation;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TelemetryEvent {
    private String id;
    private String vehicleId;
    private Instant timestamp;
    private GeoLocation location;
    private Double speed;
    private Double fuelLevel;
    private Double rpm;
}
