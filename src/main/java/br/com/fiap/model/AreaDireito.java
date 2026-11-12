package br.com.fiap.model;

/**
 * Uma das 10 areas da triagem (tabela NRT_AREA_DIREITO).
 */
public class AreaDireito {

    private int idArea;
    private String nome;
    private String descricao;
    private boolean modoDiscreto;
    private boolean trilhaCalculo;

    public AreaDireito() {
    }

    public AreaDireito(int idArea, String nome, String descricao, boolean modoDiscreto, boolean trilhaCalculo) {
        this.idArea = idArea;
        this.nome = nome;
        this.descricao = descricao;
        this.modoDiscreto = modoDiscreto;
        this.trilhaCalculo = trilhaCalculo;
    }

    public int getIdArea() {
        return idArea;
    }

    public void setIdArea(int idArea) {
        this.idArea = idArea;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isModoDiscreto() {
        return modoDiscreto;
    }

    public void setModoDiscreto(boolean modoDiscreto) {
        this.modoDiscreto = modoDiscreto;
    }

    public boolean isTrilhaCalculo() {
        return trilhaCalculo;
    }

    public void setTrilhaCalculo(boolean trilhaCalculo) {
        this.trilhaCalculo = trilhaCalculo;
    }

}
