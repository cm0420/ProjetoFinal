/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.controlador;

import com.mycompany.oficina.entidades.Cliente;

/**
 *
 * @author Miguel
 */
public class GerenciadorCliente extends GerenciadorGenerico<Cliente> {

    public Cliente addCliente(String nome, String cpf, String telefone, String endereco, String email) {
        Cliente cliente = new Cliente(nome, cpf, telefone, endereco, email);
        super.adicionar(cliente);
        return cliente;
    }

    public boolean editarCliente(String novoNome, String cpf, String novoCpf, String novoTelefone, String novoEndereco, String novoEmail) {
        Cliente clienteEditar = this.buscarPorIdentificador(cpf);
        if (clienteEditar != null) {

            clienteEditar.setNome(novoNome);
            clienteEditar.setCpf(novoCpf);
            clienteEditar.setTelefone(novoTelefone);
            clienteEditar.setEndereco(novoEndereco);
            clienteEditar.setEmail(novoEmail);
            return true;
        }
        return false;
    }

}
