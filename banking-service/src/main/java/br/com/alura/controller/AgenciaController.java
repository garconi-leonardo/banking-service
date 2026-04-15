package br.com.alura.controller;

import br.com.alura.domain.Agencia;
import br.com.alura.service.http.AgenciaService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestResponse;

//define a rota base desse controller. Todos os endpoints começam com /agencias
@Path("/agencias")
public class AgenciaController {

    private final AgenciaService agenciaService;

    AgenciaController(AgenciaService agenciaService) {
        this.agenciaService = agenciaService;
    }

    // @POST = responde requisições de criação
    // @Context UriInfo = dá acesso à URL da requisição, usado aqui para montar o header Location
    @POST
    public RestResponse<Void> cadastrar(Agencia agencia, @Context UriInfo uriInfo) {
        this.agenciaService.cadastrar(agencia);
        return RestResponse.created(uriInfo.getAbsolutePathBuilder().build());
    }

    // @GET + @Path("{id}") = responde GET /agencias/{id}, onde {id} vira o parâmetro do método
    @GET
    @Path("{id}")
    public RestResponse<Agencia> buscarPorId(Integer id) {
        Agencia agencia = this.agenciaService.buscarPorId(id);
        return RestResponse.ok(agencia);
    }

    @DELETE
    @Path("{id}")
    public RestResponse<Void> deletar(Integer id) {
        this.agenciaService.deletar(id);
        return RestResponse.ok();
    }

    @PUT
    public RestResponse<Void> alterar(Agencia agencia) {
        this.agenciaService.alterar(agencia);
        return RestResponse.ok();
    }
}
