package dev.erick.resource;

import java.net.URI;

import dev.erick.domain.Veiculo;
import dev.erick.json.VeiculoDTO;
import dev.erick.service.VeiculoServico;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/veiculos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Vehicles", description = "Vehicle management operations")
public class VeiculoResource {

    @Inject
    private VeiculoServico veiculoServico;

    @GET
    @Path("/{id}")
    @Operation(summary = "Search vehicle by ID", description = "Returns a specific vehicle by its ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Vehicle found",
                content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Veiculo.class))),
        @APIResponse(responseCode = "404", description = "Vehicle not found")
    })
    public Response getVeiculoPorId(
        @Parameter(description = "ID of the vehicle", required = true) @PathParam("id") Long id) {
        Veiculo v = veiculoServico.getVeiculoPorId(Long.toString(id));
        if (v == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(v).build();
    }

    @GET
    @Path("/placa/{placa}")
    @Operation(summary = "Search vehicle by license plate", description = "Returns a specific vehicle by its license plate")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Vehicle found",
                content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Veiculo.class))),
        @APIResponse(responseCode = "404", description = "Vehicle not found")
    })
    public Response getVeiculoPorPlaca(
        @Parameter(description = "License plate of the vehicle", required = true) @PathParam("placa") String placa) {
        Veiculo v = veiculoServico.getVeiculoPorPlaca(placa);
        if (v == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(v).build();
    }

    @PUT
    @Operation(summary = "Update vehicle", description = "Updates the data of an existing vehicle")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Vehicle updated successfully"),
        @APIResponse(responseCode = "400", description = "Invalid data")
    })
    public Response atualizarVeiculo(
        @Parameter(description = "Data of the vehicle to update", required = true) VeiculoDTO veiculoDTO) {
        veiculoServico.salvarVeiculo(veiculoDTO);
        return Response.noContent().build();
    }

    @POST
    @Operation(summary = "Create a new vehicle", description = "Creates a new vehicle and returns the Location of the created resource")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Vehicle created successfully"),
        @APIResponse(responseCode = "400", description = "Invalid data"),
        @APIResponse(responseCode = "500", description = "Error creating vehicle")
    })
    public Response cadastrarVeiculo(
        @Parameter(description = "Data of the new vehicle", required = true) VeiculoDTO veiculoDTO, 
        @Context UriInfo uriInfo) {
        Veiculo veiculo = veiculoServico.cadastrarVeiculo(veiculoDTO);
        
        if (veiculo == null || veiculo.getId() == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Error creating vehicle: id not generated").build();
        }

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(Long.toString(veiculo.getId()));
        URI location = builder.build();

        return Response.created(location).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Deactivate vehicle", description = "Deactivates an existing vehicle by its ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Vehicle deactivated successfully"),
        @APIResponse(responseCode = "404", description = "Vehicle not found")
    })
    public Response desativarVeiculo(
        @Parameter(description = "ID of the vehicle to deactivate", required = true) @PathParam("id") Long id) {
        veiculoServico.desativarVeiculo(Long.toString(id));
        return Response.noContent().build();
    }

}
