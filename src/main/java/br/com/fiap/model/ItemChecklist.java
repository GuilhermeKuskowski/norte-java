package br.com.fiap.model;

/**
 * Documento do checklist de um plano (tabelas NRT_PLANO_DOCUMENTO + NRT_DOCUMENTO).
 */
public class ItemChecklist {

    private int idDocumento;
    private String nomeDocumento;
    private String orientacao;
    private boolean possui;

    public ItemChecklist() {
    }

    public ItemChecklist(int idDocumento, String nomeDocumento, String orientacao, boolean possui) {
        this.idDocumento = idDocumento;
        this.nomeDocumento = nomeDocumento;
        this.orientacao = orientacao;
        this.possui = possui;
    }

    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getNomeDocumento() {
        return nomeDocumento;
    }

    public void setNomeDocumento(String nomeDocumento) {
        this.nomeDocumento = nomeDocumento;
    }

    public String getOrientacao() {
        return orientacao;
    }

    public void setOrientacao(String orientacao) {
        this.orientacao = orientacao;
    }

    public boolean isPossui() {
        return possui;
    }

    public void setPossui(boolean possui) {
        this.possui = possui;
    }

}
