/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.application;

import com.mycompany.oficina.Menus.MenuMecanico;
import com.mycompany.oficina.Menus.Navegador;
import com.mycompany.oficina.agendamento.*;
import com.mycompany.oficina.controlador.*;
import com.mycompany.oficina.entidades.*;
import com.mycompany.oficina.interpreter.*;
import com.mycompany.oficina.loja.Estoque;
import com.mycompany.oficina.ordemservico.GerenciadorOrdemDeServico;
import com.mycompany.oficina.segurança.ServicoAutenticacao;
import com.mycompany.oficina.segurança.Sessao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Miguel
 */
public class OficinaAplicattion {

    private static final GerenciadorFuncionario gerenciadorFuncionario = new GerenciadorFuncionario();
    private static final GerenciadorCliente gerenciadorCliente = new GerenciadorCliente();
    private static final GerenciadorCarros gerenciadorCarros = new GerenciadorCarros();
    private static final GerenciadorOrdemDeServico gerenciadorOS = new GerenciadorOrdemDeServico(); // AQUI
    private static final ServicoAutenticacao servicoAutenticacao = new ServicoAutenticacao(gerenciadorFuncionario);


    private static final AgendaOficina agenda = new AgendaOficina();
    private static final Estoque estoque = new Estoque();

    public static void main(String[] args) {
        popularDadosIniciais();
        Scanner scanner = new Scanner(System.in);
        System.out.println("====== BEM-VINDO AO SISTEMA DA OFICINA ======");
        while (true) {
            if (!Sessao.getInstance().IsLogado()) {
                // ... (lógica de login inalterada) ...
            } else {
                if (Navegador.getInstance().pilhaVazia()) {
                    Sessao.getInstance().logout();
                } else {
                    Navegador.getInstance().navegarPara(Menu menu);
                }
            }
        }
    }

    public static void redirecionarParaMenuDeCargo() {
        String cargo = Sessao.getInstance().getCargoUsuarioLogado();
        if (cargo == null) return;
        switch (cargo) {
            case "Gerente":
                System.out.println("Menu do Gerente ainda não implementado.");
                break;
            case "Mecanico":
                // Passa as dependências corretas para o menu
                Navegador.getInstance().navegarPara(new MenuMecanico(agenda, estoque, gerenciadorOS));
                break;
            case "Atendente":
                System.out.println("Menu do Atendente ainda não implementado.");
                break;
            default:
                System.out.println("Cargo não reconhecido.");
        }
    }

    private static void popularDadosIniciais() {
        // ... (criação de funcionários, clientes, carros, etc.)
        Funcionario meca1 = gerenciadorFuncionario.criarFuncionario("mec456", "Mecanico", "Carlos Souza", "222", "...", "...", "...");
        Cliente cli1 = gerenciadorCliente.addCliente("João", "123.456", "...", "...", "...");
        Carro carro1 = gerenciadorCarros.cadastrarCarro(cli1, "VW", "Gol", "ABC-1234", "chassi1");

        // Apenas cria o agendamento. A OS será criada pelo mecânico.
        agenda.agendar(new Agendamento(cli1, carro1, meca1, TipoServico.REPARO, null, LocalDateTime.now().withHour(10).withMinute(0)));


    }
}
