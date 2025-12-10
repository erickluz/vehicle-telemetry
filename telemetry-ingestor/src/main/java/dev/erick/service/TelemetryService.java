package dev.erick.service;

import java.time.Instant;

import dev.erick.domain.TelemetryDocument;
import dev.erick.json.TelemetryRequest;
import dev.erick.messaging.TelemetryEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;
import org.eclipse.microprofile.reactive.messaging.Channel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class TelemetryService {

    private final ReactiveMongoCollection<TelemetryDocument> collection;
    private final ObjectMapper objectMapper;
    private final MutinyEmitter<String> telemetryEmitter;

    @Inject
    public TelemetryService(ReactiveMongoClient mongoClient,
                            ObjectMapper objectMapper,
                            @Channel("vehicle-telemetry-out") MutinyEmitter<String> telemetryEmitter,
                            @ConfigProperty(name = "quarkus.mongodb.database") String databaseName) {
        this.collection = mongoClient.getDatabase(databaseName)
                .getCollection("telemetry_events", TelemetryDocument.class);
        this.objectMapper = objectMapper;
        this.telemetryEmitter = telemetryEmitter;
    }

    public Uni<TelemetryDocument> ingest(TelemetryRequest request) {
        TelemetryDocument document = TelemetryDocument.fromRequest(normalize(request));

        return persist(document)
                .onItem().call(this::emitToKafka);
    }

    private TelemetryRequest normalize(TelemetryRequest request) {
        if (request.getTimestamp() == null) {
            request.setTimestamp(Instant.now());
        }
        return request;
    }

    private TelemetryEvent normalize(TelemetryEvent event) {
        if (event.getTimestamp() == null) {
            event.setTimestamp(Instant.now());
        }
        return event;
    }

    public Uni<TelemetryDocument> ingestFromKafkaPayload(String payload) {
        return parseEvent(payload)
                .onItem().transform(this::normalize)
                .onItem().transform(TelemetryDocument::fromEvent)
                .onItem().transformToUni(this::persist);
    }

    private Uni<TelemetryEvent> parseEvent(String payload) {
        try {
            return Uni.createFrom().item(objectMapper.readValue(payload, TelemetryEvent.class));
        } catch (JsonProcessingException e) {
            return Uni.createFrom().failure(e);
        }
    }

    private Uni<TelemetryDocument> persist(TelemetryDocument document) {
        return collection.insertOne(document)
                .replaceWith(document);
    }

    private Uni<Void> emitToKafka(TelemetryDocument document) {
        TelemetryEvent event = document.toEvent();
        try {
            String payload = objectMapper.writeValueAsString(event);
            return telemetryEmitter.send(payload);
        } catch (JsonProcessingException e) {
            return Uni.createFrom().failure(e);
        }
    }
}
