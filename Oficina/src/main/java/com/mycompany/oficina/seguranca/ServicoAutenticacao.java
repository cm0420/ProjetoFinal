package com.mycompany.oficina.seguranca;

import com.mycompany.oficina.controlador.GerenciadorFuncionario;
import com.mycompany.oficina.entidades.Funcionario;

public class ServicoAutenticacao {
    private final GerenciadorFuncionario gerenciadorFuncionario;

    public ServicoAutenticacao(GerenciadorFuncionario gerenciadorFuncionario) {
        this.gerenciadorFuncionario = gerenciadorFuncionario;
    }
    public Funcionario autenticar(String cpf, String senha) {
        Funcionario funcionario = gerenciadorFuncionario.buscarPorIdentificador(cpf);

        if (funcionario != null && funcionario.getSenha().equals(senha)) {
            return funcionario;
        }

        return null;}
}
