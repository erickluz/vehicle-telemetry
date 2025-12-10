package dev.erick.resource;

import dev.erick.json.TelemetryRequest;
import dev.erick.json.TelemetryResponse;
import dev.erick.service.TelemetryService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/telemetries")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Telemetry", description = "Telemetry ingestion operations")
public class TelemetryResource {

    @Inject
    TelemetryService telemetryService;

    @POST
    @Operation(summary = "Register telemetry event", description = "Validates, stores and forwards telemetry payloads")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Telemetry ingested",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = TelemetryResponse.class))),
            @APIResponse(responseCode = "400", description = "Invalid payload")
    })
    public Uni<Response> ingest(@NotNull @Valid TelemetryRequest request) {
        return telemetryService.ingest(request)
                .map(doc -> Response.status(Response.Status.CREATED).entity(new TelemetryResponse(doc)).build());
    }
}
