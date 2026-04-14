package br.com.alura.service.http;

public class AgenciaHttp {

    private String nome;
    private String razaoSocial;
    private String CNPJ;
    private SituacaoCadastral situacaoCadastral;

    public String getNome() {
        return nome;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public String getCNPJ() {
        return CNPJ;
    }

    public SituacaoCadastral getSituacaoCadastral() {
        return situacaoCadastral;
    }
}
