package br.com.fiap.model;

import java.time.LocalDateTime;

/**
 * Encaminhamento de uma solicitacao a um ponto de atendimento (tabela NRT_ENCAMINHAMENTO).
 */
public class Encaminhamento {

    private int idEncaminhamento;
    private int idSolicitacao;
    private int idPonto;
    private LocalDateTime dataEncaminhamento;
    private LocalDateTime dataAgendada;
    private String status;

    public Encaminhamento() {
    }

    public Encaminhamento(int idEncaminhamento, int idSolicitacao, int idPonto, LocalDateTime dataEncaminhamento, LocalDateTime dataAgendada, String status) {
        this.idEncaminhamento = idEncaminhamento;
        this.idSolicitacao = idSolicitacao;
        this.idPonto = idPonto;
        this.dataEncaminhamento = dataEncaminhamento;
        this.dataAgendada = dataAgendada;
        this.status = status;
    }

    public int getIdEncaminhamento() {
        return idEncaminhamento;
    }

    public void setIdEncaminhamento(int idEncaminhamento) {
        this.idEncaminhamento = idEncaminhamento;
    }

    public int getIdSolicitacao() {
        return idSolicitacao;
    }

    public void setIdSolicitacao(int idSolicitacao) {
        this.idSolicitacao = idSolicitacao;
    }

    public int getIdPonto() {
        return idPonto;
    }

    public void setIdPonto(int idPonto) {
        this.idPonto = idPonto;
    }

    public LocalDateTime getDataEncaminhamento() {
        return dataEncaminhamento;
    }

    public void setDataEncaminhamento(LocalDateTime dataEncaminhamento) {
        this.dataEncaminhamento = dataEncaminhamento;
    }

    public LocalDateTime getDataAgendada() {
        return dataAgendada;
    }

    public void setDataAgendada(LocalDateTime dataAgendada) {
        this.dataAgendada = dataAgendada;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
