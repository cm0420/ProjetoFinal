/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.entidades;

import com.mycompany.oficina.entidades.Pessoa;

/**
 * Representa um cliente da oficina, que é uma especialização da classe Pessoa.
 * Cada cliente possui um identificador único gerado automaticamente.
 * 
 * @author Miguel
 */
public class Cliente extends Pessoa implements Entidades{

    /**
     * Identificador único do cliente, no formato "CL-XXX".
     */
    private String idCliente;

    /**
     * Contador estático para gerar IDs sequenciais dos clientes.
     */
    private static int contadorIdCliente = 1;

    /**
     * Construtor para criar um novo cliente com os dados fornecidos.
     * O ID do cliente é gerado automaticamente no formato "CL-XXX", onde XXX é um número sequencial.
     * 
     * @param nome Nome completo do cliente.
     * @param cpf CPF do cliente.
     * @param telefone Telefone do cliente.
     * @param endereco Endereço do cliente.
     * @param email E-mail do cliente.
     */
    public Cliente(String nome, String cpf, String telefone, String endereco, String email) {
        super(nome, cpf, telefone, endereco, email);
        this.idCliente = "CL-" + String.format("%03d", contadorIdCliente++);
    }

    /**
     * Obtém o identificador único do cliente.
     * 
     * @return ID do cliente.
     */
    public String getIdCliente() {
        return idCliente;
    }

    /**
     * Define um novo identificador para o cliente.
     * Normalmente não é utilizado, pois o ID é gerado automaticamente.
     * 
     * @param idCliente Novo ID do cliente.
     */
    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    @Override
    public String toString() {
        return "Cliente{" + "idCliente=" + idCliente + '}';
    }

    /**
     * Retorna uma representação em String do cliente, contendo seu ID.
     * 
     * @return String representando o cliente.
     */
    
    @Override
    public String getIdentificador() {
        return this.getCpf();
    }
}
