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

@Path("/veiculos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VeiculoResource {

    @Inject
    private VeiculoServico veiculoServico;

    @GET
    @Path("/{id}")
    public Response getVeiculoPorId(@PathParam("id") Long id) {
        Veiculo v = veiculoServico.getVeiculoPorId(Long.toString(id));
        if (v == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(v).build();
    }

    @GET
    @Path("/placa/{placa}")
    public Response getVeiculoPorPlaca(@PathParam("placa") String placa) {
        Veiculo v = veiculoServico.getVeiculoPorPlaca(placa);
        if (v == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(v).build();
    }

    @PUT
    public Response atualizarVeiculo(VeiculoDTO veiculoDTO) {
        veiculoServico.salvarVeiculo(veiculoDTO);
        return Response.noContent().build();
    }

    @POST
    public Response cadastrarVeiculo(VeiculoDTO veiculoDTO, @Context UriInfo uriInfo) {
        Veiculo veiculo = veiculoServico.cadastrarVeiculo(veiculoDTO);
        
        // garantir que o ID foi gerado no serviço (use persistAndFlush)
        if (veiculo == null || veiculo.getId() == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Erro ao criar veículo: id não gerado").build();
        }

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(Long.toString(veiculo.getId()));
        URI location = builder.build();

        return Response.created(location).build();
    }

    @DELETE
    @Path("/{id}")
    public Response desativarVeiculo(@PathParam("id") Long id) {
        veiculoServico.desativarVeiculo(Long.toString(id));
        return Response.noContent().build();
    }

}
