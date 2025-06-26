package com.mycompany.oficina.Menus;

import com.mycompany.oficina.agendamento.AgendaOficina;
import com.mycompany.oficina.controlador.GerenciadorCarros;
import com.mycompany.oficina.controlador.GerenciadorCliente;
import com.mycompany.oficina.controlador.GerenciadorFuncionario;
import com.mycompany.oficina.loja.Estoque;
import com.mycompany.oficina.ordemservico.GerenciadorOrdemDeServico;
import com.mycompany.oficina.seguranca.Sessao;
import com.mycompany.oficina.sistemaponto.GerenciadorPonto;

import java.util.Scanner;

public class MenuPrincipal implements Menu {

    private final AgendaOficina agenda;
    private final Estoque estoque;
    private final GerenciadorOrdemDeServico gerenciadorOS;
    private final GerenciadorCliente gerenciadorCliente;
    private  final GerenciadorCarros gerenciadorCarros;
    private final GerenciadorFuncionario gerenciadorFuncionario;
    private final GerenciadorPonto gerenciadorPonto;


    // 2. Construtor que recebe as dependências
    public MenuPrincipal(AgendaOficina agenda, Estoque estoque, GerenciadorOrdemDeServico gerenciadorOS,
                         GerenciadorCliente gerenciadorCliente, GerenciadorCarros gerenciadorCarros,
                         GerenciadorFuncionario gerenciadorFuncionario, GerenciadorPonto gerenciadorPonto) {
        this.agenda = agenda;
        this.estoque = estoque;
        this.gerenciadorOS = gerenciadorOS;
        this.gerenciadorCliente = gerenciadorCliente;
        this.gerenciadorCarros = gerenciadorCarros;
        this.gerenciadorFuncionario = gerenciadorFuncionario;
        this.gerenciadorPonto = gerenciadorPonto;
    }


    @Override
    public void exibirMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n ----- MENU PRINCIPAL -----");
        System.out.println("\n Bem vindo(a)" + Sessao.getInstance().getUsuarioLogado().getNome()
                + "(" + Sessao.getInstance().getCargoUsuarioLogado() + ")");
        System.out.println("1. Acessar Menu do meu Cargo");
        System.out.println("0. Sair (Logout)");
        System.out.print("Escolha uma opção: ");
        int opcao = scanner.nextInt();

        switch (opcao) {
            case 1:
                // Navega para o menu específico do cargo
                redirecionarPorCargo();
                break;
            case 0:
                Sessao.getInstance().logout();
                Navegador.getInstance().limparPilha(); // Limpa a navegação
                break;
            default:
                System.out.println("Opção inválida.");
                break;


        }
    }

    private void redirecionarPorCargo() {
        String cargo = Sessao.getInstance().getCargoUsuarioLogado();
        switch (cargo) {
            case "Atendente":
               Navegador.getInstance().navegarPara(new MenuAtendente(gerenciadorCliente, gerenciadorCarros, gerenciadorFuncionario, agenda, gerenciadorOS));
                break;
            case "Mecanico":
                Navegador.getInstance().navegarPara(new MenuMecanico(agenda, estoque, gerenciadorOS, gerenciadorPonto));
                break;
            case "Gerente":
               /* Navegador.getInstance().navegarPara(new MenuGerente());*/
                break;
            // Adicionar outros cargos aqui...
            default:
                System.out.println("Nenhum menu específico para o seu cargo.");
                break;
        }
    }

}
