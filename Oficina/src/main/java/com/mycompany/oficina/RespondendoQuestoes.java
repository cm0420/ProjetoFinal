package com.mycompany.oficina;

import com.mycompany.oficina.agendamento.AgendaOficina;
import com.mycompany.oficina.application.OficinaAplicattion;
import com.mycompany.oficina.controlador.GerenciadorCarros;
import com.mycompany.oficina.controlador.GerenciadorCliente;
import com.mycompany.oficina.controlador.GerenciadorFuncionario;
import com.mycompany.oficina.entidades.Cliente;
import com.mycompany.oficina.entidades.Funcionario;
import com.mycompany.oficina.loja.Estoque;
import com.mycompany.oficina.ordemservico.GerenciadorOrdemDeServico;
import com.mycompany.oficina.persistencia.PersistenciaJson;

public class RespondendoQuestoes {

        OficinaAplicattion app = OficinaAplicattion.getInstance();
        PersistenciaJson persistencia = new PersistenciaJson();
        GerenciadorCliente gerenciadorCliente = app.getGerenciadorCliente();
        GerenciadorCarros gerenciadorCarros = app.getGerenciadorCarros();
        GerenciadorFuncionario gerenciadorFuncionario = app.getGerenciadorFuncionario();
        GerenciadorOrdemDeServico gerenciadorOS = app.getGerenciadorOS();
        Estoque estoque = app.getEstoque();
        AgendaOficina agenda = app.getAgenda();


        //Questao - 6 Deve ser possível cadastrar os colaboradores no sistema, alterar ou editar seus atributos.
        //Criando Colaborador
        Funcionario novoFuncionario = gerenciadorFuncionario.criarFuncionario("1234", "atendente", "Marcos",
                "12345678910", "38994556856", "N/A", "blablabla@gmail.com");
        //Buscando Colaborador por CPF
        Funcionario funcionarioEncontrado = gerenciadorFuncionario.buscarPorIdentificador("12345678910");
        //Editando colaborador
        boolean edicao = gerenciadorFuncionario.editarFuncionario("1234578910", "atendente", "atendente",
                "Marcos", "38994556856", "N/A", "blablabla@gmail.com");
        //Removendo Colaborador
        boolean remocao = gerenciadorFuncionario.removerItemPorIdentificador("1234578910");

        //Questao 7 - Cadastrar, alterar ou excluir clientes;
        //Criando CLiente
        Cliente novoCLiente = gerenciadorCliente.addCliente("Maria", "12345678910", "3865456", "N/A", "blabala@gmail.com");
        //Editando Ciiente
        boolean edicaoCliente = gerenciadorCliente.editarCliente("Maria", "12345678910", "3865456", "N/A", "blabala@gmail.com");
        //Removendo CLiente
        boolean remocaoCliente = gerenciadorCliente.removerItemPorIdentificador("1234578910");

        //Questão 8 -

}
