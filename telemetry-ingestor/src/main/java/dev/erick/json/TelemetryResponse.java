package dev.erick.json;

import java.time.Instant;

import dev.erick.domain.GeoLocation;
import dev.erick.domain.TelemetryDocument;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TelemetryResponse {
    private String id;
    private String vehicleId;
    private Instant timestamp;
    private GeoLocation location;
    private Double speed;
    private Double fuelLevel;
    private Double rpm;

    public TelemetryResponse(TelemetryDocument document) {
        this.id = document.getId() != null ? document.getId().toHexString() : null;
        this.vehicleId = document.getVehicleId();
        this.timestamp = document.getTimestamp();
        this.location = document.getLocation();
        this.speed = document.getSpeed();
        this.fuelLevel = document.getFuelLevel();
        this.rpm = document.getRpm();
    }
}
