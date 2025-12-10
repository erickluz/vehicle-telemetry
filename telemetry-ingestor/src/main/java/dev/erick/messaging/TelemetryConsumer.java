package dev.erick.messaging;

import dev.erick.service.TelemetryService;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class TelemetryConsumer {

    private final TelemetryService telemetryService;

    @Inject
    public TelemetryConsumer(TelemetryService telemetryService) {
        this.telemetryService = telemetryService;
    }

    @Incoming("vehicle-telemetry-in")
    public Uni<Void> consume(String payload) {
        return telemetryService.ingestFromKafkaPayload(payload)
                .replaceWithVoid();
    }
}
