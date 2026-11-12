package br.com.fiap.model;

import java.time.LocalDate;

/**
 * Passo do plano de acao, com prazo, semaforo e lei citada (tabela NRT_ACAO_PLANO).
 */
public class AcaoPlano {

    private int idAcao;
    private int idPlano;
    private Integer idLei;
    private int ordem;
    private String descricao;
    private LocalDate prazo;
    private String semaforo;
    private boolean concluida;

    public AcaoPlano() {
    }

    public AcaoPlano(int idAcao, int idPlano, Integer idLei, int ordem, String descricao, LocalDate prazo, String semaforo, boolean concluida) {
        this.idAcao = idAcao;
        this.idPlano = idPlano;
        this.idLei = idLei;
        this.ordem = ordem;
        this.descricao = descricao;
        this.prazo = prazo;
        this.semaforo = semaforo;
        this.concluida = concluida;
    }

    public int getIdAcao() {
        return idAcao;
    }

    public void setIdAcao(int idAcao) {
        this.idAcao = idAcao;
    }

    public int getIdPlano() {
        return idPlano;
    }

    public void setIdPlano(int idPlano) {
        this.idPlano = idPlano;
    }

    public Integer getIdLei() {
        return idLei;
    }

    public void setIdLei(Integer idLei) {
        this.idLei = idLei;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getPrazo() {
        return prazo;
    }

    public void setPrazo(LocalDate prazo) {
        this.prazo = prazo;
    }

    public String getSemaforo() {
        return semaforo;
    }

    public void setSemaforo(String semaforo) {
        this.semaforo = semaforo;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }

}
