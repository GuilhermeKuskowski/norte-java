package br.com.fiap.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Plano de acao gerado para uma solicitacao (tabela NRT_PLANO_ACAO), com seus passos e o checklist de documentos.
 */
public class PlanoAcao {

    private int idPlano;
    private int idSolicitacao;
    private LocalDateTime dataGeracao;
    private boolean precisaAdvogado;
    private String resumo;
    private List<AcaoPlano> acoes = new ArrayList<>();
    private List<ItemChecklist> checklist = new ArrayList<>();

    public PlanoAcao() {
    }

    public PlanoAcao(int idPlano, int idSolicitacao, LocalDateTime dataGeracao, boolean precisaAdvogado, String resumo) {
        this.idPlano = idPlano;
        this.idSolicitacao = idSolicitacao;
        this.dataGeracao = dataGeracao;
        this.precisaAdvogado = precisaAdvogado;
        this.resumo = resumo;
    }

    public int getIdPlano() {
        return idPlano;
    }

    public void setIdPlano(int idPlano) {
        this.idPlano = idPlano;
    }

    public int getIdSolicitacao() {
        return idSolicitacao;
    }

    public void setIdSolicitacao(int idSolicitacao) {
        this.idSolicitacao = idSolicitacao;
    }

    public LocalDateTime getDataGeracao() {
        return dataGeracao;
    }

    public void setDataGeracao(LocalDateTime dataGeracao) {
        this.dataGeracao = dataGeracao;
    }

    public boolean isPrecisaAdvogado() {
        return precisaAdvogado;
    }

    public void setPrecisaAdvogado(boolean precisaAdvogado) {
        this.precisaAdvogado = precisaAdvogado;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public List<AcaoPlano> getAcoes() {
        return acoes;
    }

    public void setAcoes(List<AcaoPlano> acoes) {
        this.acoes = acoes;
    }

    public List<ItemChecklist> getChecklist() {
        return checklist;
    }

    public void setChecklist(List<ItemChecklist> checklist) {
        this.checklist = checklist;
    }

}
