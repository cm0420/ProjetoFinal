package com.mycompany.oficina.Menus;

import com.mycompany.oficina.segurança.Sessao;

import java.util.Scanner;

public class MenuPrincipal implements Menu {
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
                Navegador.getInstance().navegarPara(new MenuAtendente());
                break;
            case "Mecanico":
                Navegador.getInstance().navegarPara(new MenuMecanico());
                break;
            case "Gerente":
                Navegador.getInstance().navegarPara(new MenuGerente());
                break;
            // Adicionar outros cargos aqui...
            default:
                System.out.println("Nenhum menu específico para o seu cargo.");
                break;
        }
    }

}
