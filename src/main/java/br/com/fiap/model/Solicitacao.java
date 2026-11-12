package br.com.fiap.model;

import java.time.LocalDateTime;

/**
 * Problema contado pelo cidadao, ja classificado em uma area (tabela NRT_SOLICITACAO).
 */
public class Solicitacao {

    private int idSolicitacao;
    private int idCidadao;
    private int idArea;
    private String relato;
    private String canal;
    private LocalDateTime dataAbertura;
    private String status;
    private Double valorEstimado;

    public Solicitacao() {
    }

    public Solicitacao(int idSolicitacao, int idCidadao, int idArea, String relato, String canal, LocalDateTime dataAbertura, String status, Double valorEstimado) {
        this.idSolicitacao = idSolicitacao;
        this.idCidadao = idCidadao;
        this.idArea = idArea;
        this.relato = relato;
        this.canal = canal;
        this.dataAbertura = dataAbertura;
        this.status = status;
        this.valorEstimado = valorEstimado;
    }

    public int getIdSolicitacao() {
        return idSolicitacao;
    }

    public void setIdSolicitacao(int idSolicitacao) {
        this.idSolicitacao = idSolicitacao;
    }

    public int getIdCidadao() {
        return idCidadao;
    }

    public void setIdCidadao(int idCidadao) {
        this.idCidadao = idCidadao;
    }

    public int getIdArea() {
        return idArea;
    }

    public void setIdArea(int idArea) {
        this.idArea = idArea;
    }

    public String getRelato() {
        return relato;
    }

    public void setRelato(String relato) {
        this.relato = relato;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getValorEstimado() {
        return valorEstimado;
    }

    public void setValorEstimado(Double valorEstimado) {
        this.valorEstimado = valorEstimado;
    }

}
