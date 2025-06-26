package com.mycompany.oficina.application;
import com.mycompany.oficina.financeiro.GerenciadorFinanceiro;
import com.mycompany.oficina.menus.MenuPrincipal;
import com.mycompany.oficina.menus.Navegador;
import com.mycompany.oficina.agendamento.AgendaOficina;
import com.mycompany.oficina.controlador.GerenciadorCarros;
import com.mycompany.oficina.controlador.GerenciadorCliente;
import com.mycompany.oficina.controlador.GerenciadorFuncionario;
import com.mycompany.oficina.entidades.Funcionario;
import com.mycompany.oficina.loja.Estoque;
import com.mycompany.oficina.ordemservico.GerenciadorOrdemDeServico;
import com.mycompany.oficina.persistencia.PersistenciaJson;
import com.mycompany.oficina.seguranca.Sessao;
import com.mycompany.oficina.seguranca.ServicoAutenticacao;
import com.mycompany.oficina.sistemaponto.GerenciadorPonto;

import java.util.Scanner;

/**
 * Classe principal que gerencia o ciclo de vida e os componentes da aplicação.
 * Implementada como um Singleton para garantir uma única instância em todo o sistema.
 */
public class OficinaAplicattion {

    // 1. Instância única (padrão Singleton)
    private static OficinaAplicattion instance;

    // 2. Gerenciadores e Serviços (agora são campos de instância)
    private final GerenciadorFuncionario gerenciadorFuncionario;
    private final GerenciadorCliente gerenciadorCliente;
    private final GerenciadorCarros gerenciadorCarros;
    private final GerenciadorOrdemDeServico gerenciadorOS;
    private final GerenciadorPonto gerenciadorPonto;
    private final AgendaOficina agenda;
    private final Estoque estoque;
    private final GerenciadorFinanceiro gerenciadorFinanceiro;
    private final ServicoAutenticacao servicoAutenticacao;

    private final PersistenciaJson persistencia;

    /**
     * Construtor privado para inicializar todos os componentes do sistema.
     * A ordem de instanciação é importante: PersistenciaDados vem primeiro.
     */
    private OficinaAplicattion() {
        // Primeiro, cria a instância de persistência
        this.persistencia = new PersistenciaJson();

        // Agora, injeta a persistência nos gerenciadores que precisam dela
        this.gerenciadorFuncionario = new GerenciadorFuncionario(persistencia);
        this.gerenciadorCliente = new GerenciadorCliente(persistencia);
        this.gerenciadorCarros = new GerenciadorCarros(persistencia);
        this.gerenciadorOS = new GerenciadorOrdemDeServico(persistencia);
        this.gerenciadorPonto = new GerenciadorPonto(persistencia);
        this.gerenciadorFinanceiro = GerenciadorFinanceiro.getInstance(persistencia);

        // Instancia os outros componentes que não dependem diretamente da persistência
        this.agenda = new AgendaOficina(); // (Se precisar de persistência, seguirá o mesmo padrão)
        this.estoque = new Estoque(persistencia);     // (Idem)
        this.servicoAutenticacao = new ServicoAutenticacao(gerenciadorFuncionario);
    }

    /**
     * Método público estático para obter a instância única da classe.
     * @return A instância de OficinaAplicattion.
     */
    public static OficinaAplicattion getInstance() {
        if (instance == null) {
            instance = new OficinaAplicattion();
        }
        return instance;
    }

    /**
     * Método que inicia e controla o fluxo principal da aplicação.
     */
    public void run() {
        // O carregamento agora é feito automaticamente pelos construtores dos gerenciadores.
        // Não precisamos mais de um método carregarTodosOsDados() aqui.
        verificarECriarAdminPadrao();
        Scanner scanner = new Scanner(System.in);
        System.out.println("====== BEM-VINDO AO SISTEMA DA OFICINA ======");

        while (true) {
            if (!Sessao.getInstance().IsLogado()) {
                System.out.println("\n--- TELA DE LOGIN ---");
                System.out.print("CPF: ");
                String cpf = scanner.nextLine();
                System.out.print("Senha: ");
                String senha = scanner.nextLine();

                Funcionario funcionario = servicoAutenticacao.autenticar(cpf, senha);

                if (funcionario != null) {
                    Sessao.getInstance().login(funcionario);
                    System.out.println("==============================================");
                    // Passa todos os gerenciadores para o MenuPrincipal
                    Navegador.getInstance().navegarPara(new MenuPrincipal(agenda, estoque, gerenciadorOS, gerenciadorCliente,
                            gerenciadorCarros, gerenciadorFuncionario, gerenciadorPonto, gerenciadorFinanceiro));
                } else {
                    System.out.println(">>> ERRO: CPF ou senha inválidos. Tente novamente.");
                }
            } else {
                if (Navegador.getInstance().pilhaVazia()) {
                    Sessao.getInstance().logout();
                    System.out.println("Você saiu do sistema. Até logo!");
                    System.out.println("==============================================");
                    // O salvamento agora é feito em tempo real. O backup final é opcional,
                    // mas podemos mantê-lo por segurança.
                    salvarBackupFinal();
                    break;
                } else {
                    Navegador.getInstance().exibirMenuAtual();
                }
            }
        }
        scanner.close();
    }
   private void verificarECriarAdminPadrao() {
        // Verifica se a lista de funcionários está vazia
        if (gerenciadorFuncionario.listarTodos().isEmpty()) {
            System.out.println("[Sistema] Nenhum funcionário encontrado. Criando usuário 'Admin' padrão...");

            // Usa o método do próprio gerenciador para criar e já persistir o funcionário
            gerenciadorFuncionario.criarFuncionario(
                    "admin",
                    "Admin",
                    "Administrador do Sistema",
                    "00000000000",
                    "00000000000",
                    "N/A",
                    "admin@oficina.com"
            );

            System.out.println("[Sistema] Usuário 'Admin' criado. Use CPF '00000000000' e senha 'admin' para o primeiro login.");
        }
    }

    /**
     * Salva um backup final de todos os dados ao encerrar a aplicação.
     * É uma camada extra de segurança, já que os dados são salvos em tempo real.
     */
    private void salvarBackupFinal() {
        System.out.println("[Sistema] Salvando backup final de todos os dados...");
        // A lógica de salvamento já está nos gerenciadores, mas podemos forçar um último save.
        persistencia.salvarLista("funcionarios", gerenciadorFuncionario.listarTodos());
        persistencia.salvarLista("clientes", gerenciadorCliente.listarTodos());
        persistencia.salvarLista("carros", gerenciadorCarros.listarTodos());
        persistencia.salvarLista("pontos", gerenciadorPonto.getTodosOsRegistros());
        persistencia.salvarLista("ordens_servico", gerenciadorOS.listarTodos());
        System.out.println("[Sistema] Backup final concluído.");
    }
}