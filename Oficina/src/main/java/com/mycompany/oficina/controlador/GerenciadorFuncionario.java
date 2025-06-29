/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.controlador;

import com.google.gson.reflect.TypeToken;
import com.mycompany.oficina.entidades.Funcionario;
import com.mycompany.oficina.persistencia.PersistenciaJson;

import java.util.ArrayList;

/**
 *
 * @author Miguel
 */
public class GerenciadorFuncionario extends GerenciadorGenerico<Funcionario> {
    public GerenciadorFuncionario(PersistenciaJson persistencia) {
        super(persistencia, "funcionarios", new TypeToken<ArrayList<Funcionario>>(){});
    }

    public Funcionario criarFuncionario(String senha, String cargo, String nome, String cpf, String telefone, String endereco, String email) {
        Funcionario novoFuncionario = new Funcionario(senha, cargo, nome, cpf, telefone, endereco, email);
        // O metodo 'adicionar' da classe pai já se encarrega de salvar
        super.adicionar(novoFuncionario);
        return novoFuncionario;
    }

    public boolean editarFuncionario(String cpf, String novaSenha, String novoCargo, String novoNome, String novoTelefone, String novoEndereco, String novoEmail) {
        Funcionario funcionarioParaEditar = this.buscarPorIdentificador(cpf);

        if (funcionarioParaEditar != null) {
            funcionarioParaEditar.setSenha(novaSenha);
            funcionarioParaEditar.setCargo(novoCargo);
            funcionarioParaEditar.setNome(novoNome);
            funcionarioParaEditar.setTelefone(novoTelefone);
            funcionarioParaEditar.setEndereco(novoEndereco);
            funcionarioParaEditar.setEmail(novoEmail);

            // Pede para a classe pai salvar os dados após a alteração
            super.salvarAlteracoes();

            return true;
        }

        return false;
    }
}
