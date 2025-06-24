/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.entidades;

/**
 * Classe abstrata que representa uma pessoa genérica.
 * É a superclasse para entidades como {@code Cliente} e {@code Funcionario}.
 * Contém informações básicas como nome, CPF, telefone, endereço e e-mail.
 * 
 * Esta classe deve ser estendida e não instanciada diretamente.
 * 
 * @author Miguel
 */
public abstract class Pessoa {

    private String nome;
    private String cpf;
    private String telefone;
    private String endereco;
    private String email;

    /**
     * Construtor completo da classe Pessoa.
     *
     * @param nome     Nome completo da pessoa.
     * @param cpf      CPF da pessoa.
     * @param telefone Telefone para contato.
     * @param endereco Endereço da residência.
     * @param email    Endereço de e-mail.
     */
    public Pessoa(String nome, String cpf, String telefone, String endereco, String email) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.endereco = endereco;
        this.email = email;
    }

    /**
     * Construtor vazio (padrão).
     */
    public Pessoa() {
    }

    /**
     * Retorna o nome da pessoa.
     *
     * @return Nome completo.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome da pessoa.
     *
     * @param nome Nome completo.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna o CPF da pessoa.
     *
     * @return CPF no formato de string.
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * Define o CPF da pessoa.
     *
     * @param cpf CPF no formato de string.
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * Retorna o telefone da pessoa.
     *
     * @return Número de telefone.
     */
    public String getTelefone() {
        return telefone;
    }

    /**
     * Define o telefone da pessoa.
     *
     * @param telefone Número de telefone.
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     * Retorna o endereço da pessoa.
     *
     * @return Endereço completo.
     */
    public String getEndereco() {
        return endereco;
    }

    /**
     * Define o endereço da pessoa.
     *
     * @param endereco Endereço completo.
     */
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    /**
     * Retorna o e-mail da pessoa.
     *
     * @return Endereço de e-mail.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define o e-mail da pessoa.
     *
     * @param email Endereço de e-mail.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retorna uma representação textual da pessoa.
     *
     * @return Informações principais em formato de string.
     */
    @Override
    public String toString() {
        return "Pessoa{" +
                "nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", telefone='" + telefone + '\'' +
                ", endereco='" + endereco + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
