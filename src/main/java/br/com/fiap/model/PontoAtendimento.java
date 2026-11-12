package br.com.fiap.model;

/**
 * Local de atendimento gratuito (tabela NRT_PONTO_ATENDIMENTO).
 */
public class PontoAtendimento {

    private int idPonto;
    private int idBairro;
    private String nome;
    private String tipo;
    private String endereco;
    private String telefone;
    private boolean acessivel;
    private boolean gratuito;

    public PontoAtendimento() {
    }

    public PontoAtendimento(int idPonto, int idBairro, String nome, String tipo, String endereco, String telefone, boolean acessivel, boolean gratuito) {
        this.idPonto = idPonto;
        this.idBairro = idBairro;
        this.nome = nome;
        this.tipo = tipo;
        this.endereco = endereco;
        this.telefone = telefone;
        this.acessivel = acessivel;
        this.gratuito = gratuito;
    }

    public int getIdPonto() {
        return idPonto;
    }

    public void setIdPonto(int idPonto) {
        this.idPonto = idPonto;
    }

    public int getIdBairro() {
        return idBairro;
    }

    public void setIdBairro(int idBairro) {
        this.idBairro = idBairro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public boolean isAcessivel() {
        return acessivel;
    }

    public void setAcessivel(boolean acessivel) {
        this.acessivel = acessivel;
    }

    public boolean isGratuito() {
        return gratuito;
    }

    public void setGratuito(boolean gratuito) {
        this.gratuito = gratuito;
    }

}
