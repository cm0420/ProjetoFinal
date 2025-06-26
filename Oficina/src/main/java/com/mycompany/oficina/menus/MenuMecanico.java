package com.mycompany.oficina.menus;

import com.mycompany.oficina.agendamento.AgendaOficina;
import com.mycompany.oficina.agendamento.Agendamento;
import com.mycompany.oficina.entidades.Funcionario;
import com.mycompany.oficina.loja.Estoque;
import com.mycompany.oficina.loja.Produto;
import com.mycompany.oficina.ordemservico.GerenciadorOrdemDeServico;
import com.mycompany.oficina.ordemservico.OrdemDeServico;
import com.mycompany.oficina.seguranca.Sessao;
import com.mycompany.oficina.sistemaponto.GerenciadorPonto;
import com.mycompany.oficina.sistemaponto.RegistroPonto;

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
    // Dependências
    private final GerenciadorOrdemDeServico gerenciadorOS;
    private final AgendaOficina agenda;
    private final Estoque estoque;
    private final GerenciadorPonto gerenciadorPonto; // Dependência para o ponto

    // Ferramentas
    private final Scanner scanner;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Construtor atualizado para incluir o GerenciadorPonto.
     */
    public MenuMecanico(AgendaOficina agenda, Estoque estoque, GerenciadorOrdemDeServico gerenciadorOS, GerenciadorPonto gerenciadorPonto) {
        this.agenda = agenda;
        this.estoque = estoque;
        this.gerenciadorOS = gerenciadorOS;
        this.gerenciadorPonto = gerenciadorPonto;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void exibirMenu() {
        if (!"Mecanico".equals(Sessao.getInstance().getCargoUsuarioLogado())) {
            System.out.println("Acesso negado.");
            Navegador.getInstance().voltarPara();
            return;
        }

        boolean executando = true;
        while (executando) {
            System.out.println("\n--- (Acesso Mecânico) BEM-VINDO, " + Sessao.getInstance().getUsuarioLogado().getNome() + " ---");
            System.out.println("1. Ver Agendamentos e Iniciar Nova OS");
            System.out.println("2. Gerenciar OS Existente");
            System.out.println("3. Registrar Ponto"); // <-- NOVA OPÇÃO
            System.out.println("0. Voltar ao Menu Principal (Logout)");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine();

            switch(opcao) {
                case "1":
                    iniciarOSDeAgendamento();
                    break;
                case "2":
                    gerenciarOSExistente();
                    break;
                case "3": // <-- NOVO CASE
                    menuPonto();
                    break;
                case "0":
                    executando = false;
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
        Navegador.getInstance().voltarPara();
    }

    // --- NOVO METODO: MENU DE PONTO ---
    private void menuPonto() {
        Funcionario funcionarioLogado = Sessao.getInstance().getUsuarioLogado();

        boolean noMenuPonto = true;
        while(noMenuPonto) {
            System.out.println("\n--- Sistema de Registro de Ponto ---");
            System.out.println("1. Bater Ponto de ENTRADA");
            System.out.println("2. Bater Ponto de SAÍDA");
            System.out.println("3. Ver Meus Registros de Hoje");
            System.out.println("0. Voltar ao Menu do Mecânico");
            System.out.print("Escolha uma opção: ");

            switch(scanner.nextLine()) {
                case "1":
                    gerenciadorPonto.baterPontoEntrada(funcionarioLogado);
                    break;
                case "2":
                    gerenciadorPonto.baterPontoSaida(funcionarioLogado);
                    break;
                case "3":
                    System.out.println("\n--- Meus Registros de Ponto de Hoje ---");
                    List<RegistroPonto> meusRegistros = gerenciadorPonto.getRegistrosPorFuncionario(funcionarioLogado);
                    List<RegistroPonto> registrosDeHoje = meusRegistros.stream()
                            .filter(r -> r.getDataHoraEntrada().toLocalDate().isEqual(LocalDate.now()))
                            .collect(Collectors.toList());

                    if(registrosDeHoje.isEmpty()){
                        System.out.println("Nenhum registro encontrado para hoje.");
                    } else {
                        registrosDeHoje.forEach(System.out::println);
                    }
                    break;
                case "0":
                    noMenuPonto = false;
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    // --- MÉTODOS EXISTENTES (sem alterações) ---

    private void iniciarOSDeAgendamento() {
        System.out.println("\n--- Agendamentos do Dia ---");
        LocalDate hoje = LocalDate.now();
        List<Agendamento> agendamentosDoDia = new ArrayList<>();

        for(Agendamento ag : agenda.getHorariosDoDia(hoje)) {
            if (ag != null) agendamentosDoDia.add(ag);
        }

        if (agendamentosDoDia.isEmpty()) {
            System.out.println("Nenhum agendamento para hoje.");
            return;
        }

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

            OrdemDeServico novaOS = gerenciadorOS.abrirOS(
                    agSelecinado.getCliente(),
                    agSelecinado.getCarro(),
                    Sessao.getInstance().getUsuarioLogado(),
                    defeito
            );

            if (novaOS != null) {
                System.out.println("Ordem de Serviço " + novaOS.getNumeroOS() + " aberta com sucesso!");
                exibirSubMenuDaOS(novaOS);
            } else {
                System.out.println("Falha ao abrir a Ordem de Serviço.");
            }
        } else if (escolha != 0) {
            System.out.println("Seleção inválida.");
        }
    }

    private void gerenciarOSExistente() {
        System.out.println("\n--- Gerenciar OS Existente ---");
        List<OrdemDeServico> listaOS = gerenciadorOS.listarTodos();

        if (listaOS.isEmpty()) {
            System.out.println("Nenhuma Ordem de Serviço aberta.");
            return;
        }

        List<OrdemDeServico> osAtivas = listaOS.stream()
                .filter(os -> !os.getStatusAtual().equals("Finalizada") && !os.getStatusAtual().equals("Cancelada"))
                .toList();

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
        OrdemDeServico osSelecionada = gerenciadorOS.buscarPorIdentificador(idOS);

        if (osSelecionada != null) {
            exibirSubMenuDaOS(osSelecionada);
        } else {
            System.out.println("ID de OS inválido.");
        }
    }

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
                    System.out.println(os.gerarExtrato());
                    return;
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

    private void processarAcaoDaOS(OrdemDeServico os, String status, int opcao) {
        switch (status) {
            case "Aguardando":
                if (opcao == 1) os.iniciarInspecao();
                break;
            case "Em Inspeção":
                if (opcao == 1) os.iniciarServico();
                break;
            case "Em Serviço":
                if (opcao == 1) {
                    System.out.println("\n--- Peças em Estoque ---");
                    estoque.listarProdutos().forEach(p -> System.out.printf("ID: %s | %s | Qtd: %d\n", p.getIdProduto(), p.getNome(), p.getQuantidade()));
                    System.out.print("Digite o ID da peça: ");
                    String idProduto = scanner.nextLine();
                    Produto p = estoque.buscarProduto(idProduto);
                    if (p != null) {
                        System.out.print("Digite a quantidade: ");
                        int qtd = Integer.parseInt(scanner.nextLine());
                        os.adicionarPeca(p, qtd);
                    } else {
                        System.out.println("Produto não encontrado.");
                    }
                } else if (opcao == 2) {
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