package br.com.fiap.model;

import java.time.LocalDate;

/**
 * Norma citada nos planos, com link da fonte oficial (tabela NRT_LEI).
 */
public class Lei {

    private int idLei;
    private String tipoNorma;
    private String numero;
    private LocalDate data;
    private String apelido;
    private String resumo;
    private String urlFonte;
    private boolean revogada;

    public Lei() {
    }

    public Lei(int idLei, String tipoNorma, String numero, LocalDate data, String apelido, String resumo, String urlFonte, boolean revogada) {
        this.idLei = idLei;
        this.tipoNorma = tipoNorma;
        this.numero = numero;
        this.data = data;
        this.apelido = apelido;
        this.resumo = resumo;
        this.urlFonte = urlFonte;
        this.revogada = revogada;
    }

    public int getIdLei() {
        return idLei;
    }

    public void setIdLei(int idLei) {
        this.idLei = idLei;
    }

    public String getTipoNorma() {
        return tipoNorma;
    }

    public void setTipoNorma(String tipoNorma) {
        this.tipoNorma = tipoNorma;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public String getUrlFonte() {
        return urlFonte;
    }

    public void setUrlFonte(String urlFonte) {
        this.urlFonte = urlFonte;
    }

    public boolean isRevogada() {
        return revogada;
    }

    public void setRevogada(boolean revogada) {
        this.revogada = revogada;
    }

}
