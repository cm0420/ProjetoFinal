package com.mycompany.oficina.Menus;

import com.mycompany.oficina.agendamento.AgendaOficina;
import com.mycompany.oficina.agendamento.Agendamento;
import com.mycompany.oficina.loja.Estoque;
import com.mycompany.oficina.loja.Produto;
import com.mycompany.oficina.ordemservico.GerenciadorOrdemDeServico;
import com.mycompany.oficina.ordemservico.OrdemDeServico;
import com.mycompany.oficina.segurança.Sessao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Interface de menu para o mecânico, responsável por todo o fluxo de serviço.
 */
public class MenuMecanico implements Menu {

    // Dependências necessárias para o menu funcionar
    private final GerenciadorOrdemDeServico gerenciadorOS;
    private final AgendaOficina agenda;
    private final Estoque estoque;

    // Ferramentas do menu
    private final Scanner scanner;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Construtor que recebe as dependências necessárias.
     * @param agenda A agenda de agendamentos do sistema.
     * @param estoque O estoque de peças da loja.
     * @param gerenciadorOS O gerenciador das Ordens de Serviço.
     */
    public MenuMecanico(AgendaOficina agenda, Estoque estoque, GerenciadorOrdemDeServico gerenciadorOS) {
        this.agenda = agenda;
        this.estoque = estoque;
        this.gerenciadorOS = gerenciadorOS;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void exibirMenu() {
        // Ponto de verificação de segurança
        if (!"Mecanico".equals(Sessao.getInstance().getCargoUsuarioLogado())) {
            System.out.println("Acesso negado. Esta área é restrita aos mecânicos.");
            Navegador.getInstance().voltarPara(); // Volta para o menu anterior
            return;
        }

        boolean executando = true;
        while (executando) {
            System.out.println("\n--- (Acesso Mecânico) BEM-VINDO, " + Sessao.getInstance().getUsuarioLogado().getNome() + " ---");
            System.out.println("1. Ver Agendamentos e Iniciar Nova OS");
            System.out.println("2. Gerenciar OS Existente");
            System.out.println("0. Voltar ao Menu Principal (Logout)");
            System.out.print("Escolha uma opção: ");

            int opcao;
            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida. Por favor, digite um número.");
                continue;
            }

            switch(opcao) {
                case 1:
                    iniciarOSDeAgendamento();
                    break;
                case 2:
                    gerenciarOSExistente();
                    break;
                case 0:
                    executando = false; // Finaliza o loop e volta
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
        Navegador.getInstance().voltarPara();
    }

    /**
     * Exibe os agendamentos do dia e permite iniciar uma OS a partir de um deles.
     */
    private void iniciarOSDeAgendamento() {
        System.out.println("\n--- Agendamentos do Dia ---");
        LocalDate hoje = LocalDate.now();
        List<Agendamento> agendamentosDoDia = new ArrayList<>();

        // Coleta agendamentos não nulos do dia
        for(Agendamento ag : agenda.getHorariosDoDia(hoje)) {
            if (ag != null) agendamentosDoDia.add(ag);
        }

        if (agendamentosDoDia.isEmpty()) {
            System.out.println("Nenhum agendamento para hoje.");
            return;
        }

        // Exibe os agendamentos para seleção
        for (int i = 0; i < agendamentosDoDia.size(); i++) {
            Agendamento ag = agendamentosDoDia.get(i);
            System.out.printf("%d. %s - Cliente: %s - Veículo: %s (%s)\n",
                    i + 1,
                    ag.getDataHora().format(formatter),
                    ag.getCliente().getNome(),
                    ag.getCarro().getModelo(),
                    ag.getTipoServico()
            );
        }

        System.out.print("\nEscolha o número do agendamento para iniciar a OS (ou 0 para cancelar): ");
        int escolha = Integer.parseInt(scanner.nextLine());

        if (escolha > 0 && escolha <= agendamentosDoDia.size()) {
            Agendamento agSelecinado = agendamentosDoDia.get(escolha - 1);

            System.out.print("Digite o defeito relatado pelo cliente: ");
            String defeito = scanner.nextLine();

            // Chama a camada de lógica para criar a OS, associando o mecânico logado
            OrdemDeServico novaOS = gerenciadorOS.abrirOS(
                    agSelecinado.getCliente(),
                    agSelecinado.getCarro(),
                    Sessao.getInstance().getUsuarioLogado(),
                    defeito
            );

            if (novaOS != null) {
                System.out.println("Ordem de Serviço " + novaOS.getNumeroOS() + " aberta com sucesso!");
                exibirSubMenuDaOS(novaOS); // Navega para gerenciar a OS recém-criada
            } else {
                System.out.println("Falha ao abrir a Ordem de Serviço. Verifique os dados.");
            }
        } else if (escolha != 0) {
            System.out.println("Seleção inválida.");
        }
    }

    /**
     * Lista todas as OS existentes e permite selecionar uma para gerenciar.
     */
    private void gerenciarOSExistente() {
        System.out.println("\n--- Gerenciar OS Existente ---");
        List<OrdemDeServico> listaOS = gerenciadorOS.listarTodas();

        if (listaOS.isEmpty()) {
            System.out.println("Nenhuma Ordem de Serviço aberta.");
            return;
        }

        // Filtra para não mostrar OS já finalizadas ou canceladas neste menu
        List<OrdemDeServico> osAtivas = listaOS.stream()
                .filter(os -> !os.getStatusAtual().equals("Finalizada") && !os.getStatusAtual().equals("Cancelada"))
                .collect(Collectors.toList());

        if (osAtivas.isEmpty()) {
            System.out.println("Nenhuma Ordem de Serviço ativa para gerenciar.");
            return;
        }

        System.out.println("ID da OS\t\tCliente\t\tVeículo\t\tStatus");
        System.out.println("------------------------------------------------------------------");
        for (OrdemDeServico os : osAtivas) {
            System.out.printf("%s\t\t%s\t\t%s\t\t%s\n",
                    os.getNumeroOS(), os.getCliente().getNome(), os.getCarro().getModelo(), os.getStatusAtual());
        }
        System.out.println("------------------------------------------------------------------");

        System.out.print("Digite o ID da OS para gerenciar: ");
        String idOS = scanner.nextLine();
        OrdemDeServico osSelecionada = gerenciadorOS.buscarPorId(idOS);

        if (osSelecionada != null) {
            exibirSubMenuDaOS(osSelecionada);
        } else {
            System.out.println("ID de OS inválido.");
        }
    }

    /**
     * Exibe um submenu de ações para uma OS específica, baseado em seu estado atual.
     * @param os A Ordem de Serviço a ser gerenciada.
     */
    private void exibirSubMenuDaOS(OrdemDeServico os) {
        boolean gerenciando = true;
        while(gerenciando) {
            System.out.println("\n--- Gerenciando a " + os.getNumeroOS() + " | Status: " + os.getStatusAtual() + " ---");
            String status = os.getStatusAtual();

            switch (status) {
                case "Aguardando":
                    System.out.println("1. Iniciar Inspeção");
                    break;
                case "Em Inspeção":
                    System.out.println("1. Iniciar Serviço");
                    break;
                case "Em Serviço":
                    System.out.println("1. Adicionar Peça");
                    System.out.println("2. Finalizar Serviço");
                    break;
                case "Finalizada":
                case "Cancelada":
                    System.out.println("Esta OS não pode mais ser alterada.");
                    System.out.println(os.gerarExtrato()); // Apenas mostra o extrato final
                    return; // Sai do submenu
            }
            System.out.println("9. Ver Extrato Atual");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            int opcao = Integer.parseInt(scanner.nextLine());

            if (opcao == 0) {
                gerenciando = false;
                continue;
            }
            if (opcao == 9) {
                System.out.println(os.gerarExtrato());
                continue;
            }

            processarAcaoDaOS(os, status, opcao);
        }
    }

    /**
     * Processa a ação escolhida pelo mecânico para a OS, delegando a chamada ao objeto OS.
     * @param os A Ordem de Serviço.
     * @param status O status atual da OS.
     * @param opcao A opção escolhida pelo usuario.
     */
    private void processarAcaoDaOS(OrdemDeServico os, String status, int opcao) {
        switch (status) {
            case "Aguardando":
                if (opcao == 1) os.iniciarInspecao();
                break;
            case "Em Inspeção":
                if (opcao == 1) os.iniciarServico();
                break;
            case "Em Serviço":
                if (opcao == 1) { // Adicionar Peça
                    System.out.println("\n--- Peças em Estoque ---");
                    estoque.listarProdutos().forEach(p -> System.out.printf("ID: %s | %s | Qtd: %d\n", p.getIdProduto(), p.getNome(), p.getQuantidade()));
                    System.out.print("Digite o ID da peça: ");
                    String idProduto = scanner.nextLine();
                    Produto p = estoque.buscarProduto(idProduto);
                    if (p != null) {
                        System.out.print("Digite a quantidade: ");
                        int qtd = Integer.parseInt(scanner.nextLine());
                        os.adicionarPeca(p, qtd); // A lógica de estoque está na classe EstadoEmServico
                    } else {
                        System.out.println("Produto não encontrado.");
                    }
                } else if (opcao == 2) { // Finalizar Serviço
                    os.finalizarServico();
                    System.out.println("\n>>> Serviço Finalizado com Sucesso! <<<");
                    System.out.println("O valor fixo de mão de obra foi somado ao custo das peças.");
                    System.out.println(os.gerarExtrato());
                }
                break;
            default:
                System.out.println("Opção inválida para o estado atual.");
        }
    }
}