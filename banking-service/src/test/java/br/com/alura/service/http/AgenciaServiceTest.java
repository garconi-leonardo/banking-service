package br.com.alura.service.http;

import br.com.alura.domain.Agencia;
import br.com.alura.domain.Endereco;
import br.com.alura.exception.AgenciaNaoAtivaOuNaoEncontradaException;
import br.com.alura.repository.AgenciaRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@QuarkusTest // Ativa o suporte ao Quarkus para rodar os testes
public class AgenciaServiceTest {

    @InjectMock // Cria uma versão simulada (mock) do repositório
    private AgenciaRepository agenciaRepository;

    @InjectMock // Cria um mock do cliente HTTP
    @RestClient // Indica que este mock é de um cliente REST externo
    private SituacaoCadastralHttpService situacaoCadastralHttpService;

    @Inject // Injeta a classe real que será testada
    private AgenciaService agenciaService;

    @Test // Define que o método é um caso de teste unitário
    public void deveNaoCadastrarQuandoClientRetornarNull() {

        Agencia agencia = criarAgenciaVazia();

        // Configura o mock para retornar null quando o CNPJ for consultado
        Mockito.when(situacaoCadastralHttpService.buscarPorCnpj("123")).thenReturn(null);

        /*
         O Lambda () -> ... encapsula a chamada do metodo.
         A lógica permite que o assertThrows execute o código e capture a exceção
         esperada (AgenciaNaoAtivaOuNaoEncontradaException) para validar o erro.
        */
        Assertions.assertThrows(AgenciaNaoAtivaOuNaoEncontradaException.class, () -> agenciaService.cadastrar(agencia));

        // Verifica se o repositório NUNCA foi chamado para persistir, já que houve erro
        Mockito.verify(agenciaRepository, Mockito.never()).persist(agencia);
    }

    @Test // Define que o metodo e um caso de teste unitario
    public void deveCadastrarQuandoClientRetornarSituacaoCadastralAtiva() {
        Agencia agencia = criarAgencia();

        // Configura o mock para retornar um objeto de agência ativa
        Mockito.when(situacaoCadastralHttpService.buscarPorCnpj("123")).thenReturn(criarAgenciaHttp());

        agenciaService.cadastrar(agencia);

        // Verifica se o método persist foi chamado corretamente no repositório
        Mockito.verify(agenciaRepository).persist(agencia);
    }

    private Agencia criarAgenciaVazia() {
        Endereco endereco = new Endereco(1,"","","",1);
        return new Agencia(1, "", "", "", endereco);
    }

    private AgenciaHttp criarAgenciaHttp() {
        return new AgenciaHttp("Agencia Teste", "Razao Agencia Teste", "123", "ATIVO");
    }

    private Agencia criarAgencia() {
        Endereco endereco = new Endereco(1, "Quadra", "Teste", "Teste", 1);
        return new Agencia(1, "Agencia Teste", "Razao Agencia Teste", "123", endereco);
    }
}
