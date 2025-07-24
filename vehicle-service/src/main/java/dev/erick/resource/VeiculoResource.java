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
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

public class VeiculoResource {

    @Inject
    private VeiculoServico veiculoServico;

    @GET
    public Veiculo getVeiculoPorId(@QueryParam("id") String id) {
        return veiculoServico.getVeiculoPorId(id);
    }

    @GET
    public Veiculo getVeiculoPorPlaca(@QueryParam("placa") String placa) {
        return veiculoServico.getVeiculoPorPlaca(placa);
    }

    @PUT
    public Response atualizarVeiculo(VeiculoDTO veiculoDTO) {
        veiculoServico.salvarVeiculo(veiculoDTO);
        return Response.noContent().build();
    }

    @POST
    public Response cadastrarVeiculo(VeiculoDTO veiculoDTO, @Context UriInfo uriInfo) {
        Veiculo veiculo = veiculoServico.cadastrarVeiculo(veiculoDTO);
        
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(Long.toString(veiculo.getId()));
        URI location = builder.build();

        return Response.created(location).entity(veiculo).build();
    }

    @DELETE
    public Response desativarVeiculo(String id) {
        veiculoServico.desativarVeiculo(id);
        return Response.noContent().build();
    }

}
