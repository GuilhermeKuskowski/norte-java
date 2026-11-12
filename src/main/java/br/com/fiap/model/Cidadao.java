package br.com.fiap.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Pessoa que usa o Norte (tabela NRT_CIDADAO). Nao guarda CPF: minimizacao de dados (LGPD).
 */
public class Cidadao {

    private int idCidadao;
    private int idBairro;
    private String nome;
    private LocalDate dataNascimento;
    private String email;
    private String celular;
    private boolean pcd;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAceiteTermos;

    public Cidadao() {
    }

    public Cidadao(int idCidadao, int idBairro, String nome, LocalDate dataNascimento, String email, String celular, boolean pcd, LocalDateTime dataCadastro, LocalDateTime dataAceiteTermos) {
        this.idCidadao = idCidadao;
        this.idBairro = idBairro;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.celular = celular;
        this.pcd = pcd;
        this.dataCadastro = dataCadastro;
        this.dataAceiteTermos = dataAceiteTermos;
    }

    public int getIdCidadao() {
        return idCidadao;
    }

    public void setIdCidadao(int idCidadao) {
        this.idCidadao = idCidadao;
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

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public boolean isPcd() {
        return pcd;
    }

    public void setPcd(boolean pcd) {
        this.pcd = pcd;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDateTime getDataAceiteTermos() {
        return dataAceiteTermos;
    }

    public void setDataAceiteTermos(LocalDateTime dataAceiteTermos) {
        this.dataAceiteTermos = dataAceiteTermos;
    }

}
