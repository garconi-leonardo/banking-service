package br.com.alura.service.http;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

// @RegisterRestClient registra essa interface como cliente REST do MicroProfile.
//   O configKey aponta para a URL base configurada no application.properties.
//   O framework gera a implementação automaticamente — só definimos o contrato.

@Path("/situacao-cadastral")
@RegisterRestClient(configKey = "situacao-cadastral-api")
interface SituacaoCadastralHttpService {

    @GET
    @Path("{cnpj}")
    AgenciaHttp buscarPorCNPJ(String cnpj);

}
