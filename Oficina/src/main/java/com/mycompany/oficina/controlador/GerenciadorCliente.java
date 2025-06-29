/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.controlador;

import com.google.gson.reflect.TypeToken;
import com.mycompany.oficina.entidades.Cliente;
import com.mycompany.oficina.persistencia.PersistenciaJson;

import java.util.ArrayList;

/**
 *
 * @author Miguel
 */
public class GerenciadorCliente extends GerenciadorGenerico<Cliente> {
    public GerenciadorCliente(PersistenciaJson persistencia) {
        super(persistencia, "clientes", new TypeToken<ArrayList<Cliente>>() {});
    }

    public Cliente addCliente(String nome, String cpf, String telefone, String endereco, String email) {
        Cliente cliente = new Cliente(nome, cpf, telefone, endereco, email);
        super.adicionar(cliente);
        return cliente;
    }

    public boolean editarCliente(String novoNome, String cpf,  String novoTelefone, String novoEndereco, String novoEmail) {
        Cliente clienteEditar = this.buscarPorIdentificador(cpf);
        if (clienteEditar != null) {

            clienteEditar.setNome(novoNome);
            clienteEditar.setTelefone(novoTelefone);
            clienteEditar.setEndereco(novoEndereco);
            clienteEditar.setEmail(novoEmail);
            super.salvarAlteracoes();
            return true;
        }
        return false;
    }

}
