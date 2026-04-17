package br.com.alura.service.http;

import br.com.alura.domain.Agencia;
import br.com.alura.service.http.AgenciaHttp;
import br.com.alura.service.http.SituacaoCadastral;
import br.com.alura.exception.AgenciaNaoAtivaOuNaoEncontradaException;
import br.com.alura.repository.AgenciaRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class AgenciaService {

    private final AgenciaRepository agenciaRepository;
    private final MeterRegistry meterRegistry;

    AgenciaService(AgenciaRepository agenciaRepository, MeterRegistry meterRegistry) {
        this.agenciaRepository = agenciaRepository;
        this.meterRegistry = meterRegistry;
    }


    @RestClient
    SituacaoCadastralHttpService situacaoCadastralHttpService;

    public void cadastrar(Agencia agencia) {
        AgenciaHttp agenciaHttp = situacaoCadastralHttpService.buscarPorCnpj(agencia.getCnpj());
        if(agenciaHttp != null && agenciaHttp.getSituacaoCadastral().equals(SituacaoCadastral.ATIVO)) {
            this.meterRegistry.counter("agencia_adicionada_count").increment();
            Log.info("Agencia com o CNPJ " + agencia.getCnpj() + " foi cadastrada.");
            agenciaRepository.persist(agencia);
        } else {
            Log.info("Agencia com CNPJ " + agencia.getCnpj() + " não foi cadastrada.");
            this.meterRegistry.counter("agencia_nao_adicionada_count").increment();
            throw new AgenciaNaoAtivaOuNaoEncontradaException();
        }
    }

    public Agencia buscarPorId(Long id) {
        return agenciaRepository.findById(id);
    }

    public void deletar(Long id) {
        Log.info("A agência com o id " + id + " foi deletada.");
        agenciaRepository.deleteById(id);
    }

    public void alterar(Agencia agencia) {
        // Busca a entidade pelo ID
        Agencia entidadeExistente = agenciaRepository.findById(agencia.getId().longValue());

        if (entidadeExistente != null) {
            // Atualiza os atributos desejados
            entidadeExistente.setNome(agencia.getNome());
            entidadeExistente.setRazaoSocial(agencia.getRazaoSocial());
            entidadeExistente.setCnpj(agencia.getCnpj());

            Log.info("A agência com o CNPJ " + agencia.getCnpj() + " foi alterada.");

        } else {
            throw new IllegalStateException("Agência com ID " + agencia.getId() + " não encontrada");
        }
    }
}