/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.controlador;

import com.mycompany.oficina.entidades.Funcionario;

/**
 *
 * @author Miguel
 */
public class GerenciadorFuncionario extends GerenciadorGenerico<Funcionario> {
     public Funcionario criarFuncionario(String senha, String cargo, String nome, String cpf, String telefone, String endereco, String email) {

        Funcionario novoFuncionario = new Funcionario(senha, cargo, nome, cpf, telefone, endereco, email);
        super.adicionar(novoFuncionario);
        return novoFuncionario;
        
        
    }
     public boolean editarFuncionario(String cpf, String novoCpf, String novaSenha, String novoCargo, String novoNome, String novoTelefone, String novoEndereco, String novoEmail) {
        
        Funcionario funcionarioParaEditar = this.buscarPorIdentificador(cpf);

        if (funcionarioParaEditar != null) {
            funcionarioParaEditar.setSenha(novaSenha);
            funcionarioParaEditar.setCargo(novoCargo);
            funcionarioParaEditar.setNome(novoNome);
            funcionarioParaEditar.setCpf(novoCpf);
            funcionarioParaEditar.setTelefone(novoTelefone);
            funcionarioParaEditar.setEndereco(novoEndereco);
            funcionarioParaEditar.setEmail(novoEmail);
            return true;
        }
        
        return false;
    }
}
