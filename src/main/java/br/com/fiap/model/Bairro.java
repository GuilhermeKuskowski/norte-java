package br.com.fiap.model;

/**
 * Bairro do territorio atendido (tabela NRT_BAIRRO).
 */
public class Bairro {

    private int idBairro;
    private String nome;
    private String distrito;
    private String subprefeitura;

    public Bairro() {
    }

    public Bairro(int idBairro, String nome, String distrito, String subprefeitura) {
        this.idBairro = idBairro;
        this.nome = nome;
        this.distrito = distrito;
        this.subprefeitura = subprefeitura;
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

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getSubprefeitura() {
        return subprefeitura;
    }

    public void setSubprefeitura(String subprefeitura) {
        this.subprefeitura = subprefeitura;
    }

}
