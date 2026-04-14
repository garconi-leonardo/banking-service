package br.com.alura.service.http;

import br.com.alura.service.http.AgenciaHttp;
import br.com.alura.exception.AgenciaNaoAtivaOuNaoEncontraException;
import br.com.alura.service.http.SituacaoCadastral;
import br.com.alura.service.http.SituacaoCadastralHttpService;
import br.com.alura.domain.Agencia;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.ArrayList;
import java.util.List;

//garante que só vai existir uma instância dessa classe na aplicação inteira
@ApplicationScoped
public class AgenciaService {
/*
    @RestClient = injeta um cliente HTTP que o MicroProfile cria automaticamente
    a partir de uma interface, para consumir uma API externa
 */
    @RestClient
    SituacaoCadastralHttpService situacaoCadastralHttpService;

    private final List<Agencia> agencias = new ArrayList<>();

    public void cadastrar(Agencia agencia) {
        AgenciaHttp agenciaHttp = situacaoCadastralHttpService.buscarPorCnpj(agencia.getCnpj());
        if (agenciaHttp != null && agenciaHttp.getSituacaoCadastral() == SituacaoCadastral.ATIVO) {
            agencias.add(agencia);
        } else {
            throw new AgenciaNaoAtivaOuNaoEncontradaException();
        }
    }

    public Agencia buscarPorId(Integer id) {
        return agencias.stream().filter(agencia -> agencia.getId().equals(id)).toList().getFirst();
    }

    public void deletar(Integer id) {
        agencias.removeIf(agencia -> agencia.getId().equals(id));
    }

    public void alterar(Agencia agencia) {
        deletar(agencia.getId());
        agencias.add(agencia);
    }
}