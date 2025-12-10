package dev.erick.domain;

import java.time.Instant;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import dev.erick.json.TelemetryRequest;
import dev.erick.messaging.TelemetryEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryDocument {
    @BsonId
    private ObjectId id;
    private String vehicleId;
    private Instant timestamp;
    private GeoLocation location;
    private Double speed;
    private Double fuelLevel;
    private Double rpm;

    public static TelemetryDocument fromRequest(TelemetryRequest request) {
        TelemetryDocument document = new TelemetryDocument();
        document.setId(new ObjectId());
        document.setVehicleId(request.getVehicleId());
        document.setTimestamp(request.getTimestamp());
        document.setLocation(request.getLocation());
        document.setSpeed(request.getSpeed());
        document.setFuelLevel(request.getFuelLevel());
        document.setRpm(request.getRpm());
        return document;
    }

    public static TelemetryDocument fromEvent(TelemetryEvent event) {
        TelemetryDocument document = new TelemetryDocument();
        document.setId(event.getId() != null && ObjectId.isValid(event.getId())
                ? new ObjectId(event.getId())
                : new ObjectId());
        document.setVehicleId(event.getVehicleId());
        document.setTimestamp(event.getTimestamp());
        document.setLocation(event.getLocation());
        document.setSpeed(event.getSpeed());
        document.setFuelLevel(event.getFuelLevel());
        document.setRpm(event.getRpm());
        return document;
    }

    public TelemetryEvent toEvent() {
        TelemetryEvent event = new TelemetryEvent();
        event.setId(id != null ? id.toHexString() : null);
        event.setVehicleId(vehicleId);
        event.setTimestamp(timestamp);
        event.setLocation(location);
        event.setSpeed(speed);
        event.setFuelLevel(fuelLevel);
        event.setRpm(rpm);
        return event;
    }
}
