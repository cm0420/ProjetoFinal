package com.mycompany.oficina.menus;

import com.mycompany.oficina.agendamento.AgendaOficina;
import com.mycompany.oficina.controlador.GerenciadorCarros;
import com.mycompany.oficina.controlador.GerenciadorCliente;
import com.mycompany.oficina.controlador.GerenciadorFuncionario;
import com.mycompany.oficina.entidades.Funcionario;
import com.mycompany.oficina.financeiro.GerenciadorFinanceiro;
import com.mycompany.oficina.loja.Estoque;
import com.mycompany.oficina.loja.Produto;
import com.mycompany.oficina.ordemservico.GerenciadorOrdemDeServico;
import com.mycompany.oficina.persistencia.PersistenciaJson;
import com.mycompany.oficina.seguranca.Sessao;
import com.mycompany.oficina.sistemaponto.GerenciadorPonto;
import com.mycompany.oficina.strategy.CpfValidate;
import com.mycompany.oficina.strategy.EmailValidate;
import com.mycompany.oficina.strategy.TelefoneValidate;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Menu para o Gerente. Herda todas as funcionalidades do MenuAtendente
 * e adiciona menus exclusivos para gerenciamento de funcionários, finanças e estoque.
 */
public class MenuGerente extends MenuAtendente implements Menu {

    private final GerenciadorFuncionario gerenciadorFuncionario;
    private final GerenciadorFinanceiro gerenciadorFinanceiro;
    private final Estoque estoque;
    private final Scanner scanner;

    public MenuGerente(GerenciadorCliente gCliente, GerenciadorCarros gCarros, GerenciadorFuncionario gFunc,
                       AgendaOficina ag, GerenciadorOrdemDeServico gOS, GerenciadorPonto gPonto,
                       GerenciadorFinanceiro gFinan, Estoque est) {
        // Chama o construtor da classe pai (MenuAtendente) para inicializar as funcionalidades herdadas
        super(gCliente, gCarros, gFunc, ag, gOS, gPonto, gFinan);

        // Inicializa os gerenciadores que são específicos ou mais utilizados pelo Gerente
        this.gerenciadorFuncionario = gFunc;
        this.gerenciadorFinanceiro = gFinan;
        this.estoque = est;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void exibirMenu() {
        // Camada de segurança para garantir que apenas o "Admin" veja este menu
        if (!"Admin".equals(Sessao.getInstance().getCargoUsuarioLogado())) {
            System.out.println("Acesso negado. Apenas gerentes podem acessar este menu.");
            // Impede que outros cargos avancem
            Navegador.getInstance().voltarPara();
            return;
        }

        boolean executando = true;
        while (executando) {
            System.out.println("\n--- (ACESSO GERENCIAL) BEM-VINDO, " + Sessao.getInstance().getUsuarioLogado().getNome() + " ---");
            System.out.println("1. Gerenciar Clientes (Função de Atendente)");
            System.out.println("2. Gerenciar Veículos (Função de Atendente)");
            System.out.println("3. Gerenciar Agendamentos (Função de Atendente)");
            System.out.println("4. Gerenciar Funcionários");
            System.out.println("5. Módulo Financeiro");
            System.out.println("6. Módulo de Estoque");
            System.out.println("0. Voltar ao Menu Principal (Logout)");
            System.out.print("Escolha uma opção: ");

            switch (scanner.nextLine()) {
                case "1":
                    super.getMenuClientes();
                    break;       // Chama o metodo herdado do MenuAtendente
                case "2":
                    super.getMenuVeiculos();
                    break;       // Chama o metodo herdado do MenuAtendente
                case "3":
                    super.getMenuAgendamentos();
                    break;   // Chama o metodo herdado do MenuAtendente
                case "4":
                    menuFuncionarios();
                    break;         // Chama o metodo local desta classe
                case "5":
                    menuFinanceiro();
                    break;           // Chama o metodo local desta classe
                case "6":
                    menuEstoque();
                    break;              // Chama o metodo local desta classe
                case "0":
                    executando = false;
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
        Navegador.getInstance().voltarPara();
    }

    // --- NOVOS MENUS EXCLUSIVOS DO GERENTE ---

    private void menuFuncionarios() {
        System.out.println("\n--- Gerenciar Funcionários ---");
        System.out.println("1. Cadastrar Novo Funcionário");
        System.out.println("2. Editar Funcionário");
        System.out.println("3. Remover Funcionário");
        System.out.println("4. Listar Funcionários");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");

        switch (scanner.nextLine()) {
            case "1":
                cadastrarFuncionario();
                break;
            case "2":
                editarFuncionario();
                break;
            case "3":
                removerFuncionario();
                break;
            case "4":
                listarFuncionarios();
            case "0":
                return;
            default:
                System.out.println("Opção inválida.");
        }
    }

    private void menuFinanceiro() {
        System.out.println("\n--- Módulo Financeiro ---");
        System.out.println("1. Emitir Balanço Diário");
        System.out.println("2. Emitir Balanço Mensal");
        System.out.println("3. Emitir Balanço Anual");
        System.out.println("4. Emitir Relatório Detalhado de Despesas do Mês");
        System.out.println("5. Pagar Salários do Mês");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");

        switch (scanner.nextLine()) {
            case "1":
                gerenciadorFinanceiro.emitirBalanco(LocalDate.now(), LocalDate.now());
                break;
            case "2":
                YearMonth mesCorrente = YearMonth.now();
                gerenciadorFinanceiro.emitirBalanco(mesCorrente.atDay(1), mesCorrente.atEndOfMonth());
                break;
            case "3":
                int anoCorrente = LocalDate.now().getYear();
                gerenciadorFinanceiro.emitirBalanco(LocalDate.of(anoCorrente, 1, 1), LocalDate.of(anoCorrente, 12, 31));
                break;
            case "4":
                YearMonth mes = YearMonth.now();
                gerenciadorFinanceiro.emitirRelatorioDespesasDetalhado(mes.atDay(1), mes.atEndOfMonth());
                break;
            case "5":
                gerenciadorFinanceiro.pagarSalarios(gerenciadorFuncionario.listarTodos());
                break;
            case "0":
                return;
            default:
                System.out.println("Opção inválida.");
        }
    }

    private void menuEstoque() {
        System.out.println("\n--- Módulo de Estoque ---");
        System.out.println("1. Cadastrar Nova Peça no Sistema");
        System.out.println("2. Repor Estoque de Peça Existente");
        System.out.println("3. Listar Estoque Atual");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");

        switch (scanner.nextLine()) {
            case "1":
                cadastrarNovaPeca();
                break;
            case "2":
                reporEstoque();
                break;
            case "3":
                System.out.println("\n--- Estoque Atual ---");
                List<Produto> produtos = estoque.listarProdutos();
                if (produtos.isEmpty()) {
                    System.out.println("O estoque está vazio.");
                } else {
                    produtos.forEach(p ->
                            System.out.printf("ID: %s | Nome: %s | Qtd: %d | Preço Venda: R$ %.2f\n",
                                    p.getIdProduto(), p.getNome(), p.getQuantidade(), p.getPreco()));
                }
                break;
            case "0":
                return;
            default:
                System.out.println("Opção inválida.");
        }
    }

    /**
     * NOVO METODO: Para cadastrar um item que não existe no sistema.
     */


    // --- LÓGICA DAS NOVAS FUNCIONALIDADES ---
    private void cadastrarFuncionario() {
        System.out.println("\n--- Cadastro de Novo Funcionário ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        String cpf = super.getValidadorDeDados("CPF (11 dígitos): ", new CpfValidate());
        String telefone = super.getValidadorDeDados("Telefone (11 dígitos): ", new TelefoneValidate());
        String email = super.getValidadorDeDados("E-mail: ", new EmailValidate());
        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();
        System.out.print("Cargo (Admin, Atendente, Mecanico): ");
        String cargo = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        gerenciadorFuncionario.criarFuncionario(senha, cargo, nome, cpf, telefone, endereco, email);
        System.out.println("Funcionário cadastrado com sucesso!");
    }

    private void editarFuncionario() {
        System.out.print("Digite o CPF do funcionário para editar: ");
        Funcionario f = gerenciadorFuncionario.buscarPorIdentificador(scanner.nextLine());
        if (f == null) {
            System.out.println("Funcionário não encontrado.");
            return;
        }
        System.out.println("Deixe em branco para não alterar.");
        System.out.print("Novo Nome (" + f.getNome() + "): ");
        String nome = scanner.nextLine();
        // A lógica de edição pode ser expandida aqui para outros campos
        gerenciadorFuncionario.editarFuncionario(f.getCpf(), f.getCpf(), f.getSenha(), f.getCargo(), nome.isEmpty() ? f.getNome() : nome, f.getTelefone(), f.getEndereco(), f.getEmail());
        System.out.println("Funcionário atualizado.");
    }

    private void removerFuncionario() {
        System.out.print("Digite o CPF do funcionário para remover: ");
        String cpf = scanner.nextLine();
        if (gerenciadorFuncionario.removerItemPorIdentificador(cpf)) {
            System.out.println("Funcionário removido com sucesso.");
        } else {
            System.out.println("Funcionário não encontrado ou não pode ser removido.");
        }
    }

    private void listarFuncionarios() {
        System.out.println("\n--- Lista de Funcionários Cadastrados ---");
        List<Funcionario> funcionarios = gerenciadorFuncionario.listarTodos();

        if (funcionarios.isEmpty()) {
            System.out.println("Nenhum funcionário cadastrado.");
            return;
        }

        System.out.println("------------------------------------------------------------");
        System.out.printf("| %-10s | %-25s | %-15s |\n", "ID Usuário", "Nome", "Cargo");
        System.out.println("------------------------------------------------------------");

        for (Funcionario f : funcionarios) {
            System.out.printf("| %-10s | %-25s | %-15s |\n",
                    f.getIdUsuario(),
                    f.getNome(),
                    f.getCargo());
        }
        System.out.println("------------------------------------------------------------");
    }

    private void cadastrarNovaPeca() {
        System.out.println("\n--- Cadastro de Nova Peça ---");
        try {
            System.out.print("Nome da nova peça: ");
            String nome = scanner.nextLine();

            System.out.print("Preço de VENDA final para o cliente: R$ ");
            double precoVenda = Double.parseDouble(scanner.nextLine());

            System.out.print("Quantidade inicial a ser comprada: ");
            int quantidade = Integer.parseInt(scanner.nextLine());

            System.out.print("Nome do Fornecedor: ");
            String fornecedor = scanner.nextLine();

            // Regra de negócio: preço de compra é 15 reais mais barato
            double precoCompraUnidade = precoVenda - 15.00;
            if (precoCompraUnidade < 0) precoCompraUnidade = 0; // Segurança para não ter preço negativo
            double custoTotal = precoCompraUnidade * quantidade;

            System.out.println("\n--- Resumo da Compra ---");
            System.out.printf("Peça: %s | Fornecedor: %s\n", nome, fornecedor);
            System.out.printf("Preço de Venda Definido: R$ %.2f\n", precoVenda);
            System.out.printf("Preço de Compra (por unidade): R$ %.2f\n", precoCompraUnidade);
            System.out.printf("Quantidade: %d\n", quantidade);
            System.out.println("---------------------------------");
            System.out.printf("CUSTO TOTAL DA COMPRA: R$ %.2f\n", custoTotal);
            System.out.println("---------------------------------");

            System.out.print("Confirmar cadastro e registrar despesa? (S/N): ");
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                Produto novoProduto = new Produto(nome, precoVenda, quantidade, fornecedor);
                estoque.cadastrarProduto(novoProduto); // O cadastrar já salva no JSON

                String nota = "Compra inicial de " + quantidade + "x " + nome;
                gerenciadorFinanceiro.registrarDespesaCompraPecas(nota, custoTotal);
                System.out.println("Peça cadastrada, estoque atualizado e despesa registrada com sucesso!");
            } else {
                System.out.println("Cadastro cancelado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: Preço ou quantidade inválida. Use apenas números.");
        }
    }

    /**
     * Método para adicionar mais unidades a uma peça que já existe.
     */
    private void reporEstoque() {
        System.out.println("\n--- Reposição de Estoque ---");
        List<Produto> pecasDisponiveis = estoque.listarProdutos();
        if (pecasDisponiveis.isEmpty()) {
            System.out.println("Nenhuma peça cadastrada no sistema. Use a opção 'Cadastrar Nova Peça' primeiro.");
            return;
        }

        try {
            System.out.println("Peças disponíveis para reposição:");
            for (int i = 0; i < pecasDisponiveis.size(); i++) {
                Produto p = pecasDisponiveis.get(i);
                System.out.printf("%d. %s (Estoque atual: %d)\n", i + 1, p.getNome(), p.getQuantidade());
            }

            System.out.print("Escolha a peça para repor (pelo número): ");
            int pecaIndex = Integer.parseInt(scanner.nextLine()) - 1;
            Produto pecaSelecionada = pecasDisponiveis.get(pecaIndex);

            System.out.print("Digite a quantidade a ser comprada: ");
            int quantidade = Integer.parseInt(scanner.nextLine());
            if (quantidade <= 0) {
                System.out.println("Quantidade deve ser positiva.");
                return;
            }

            double precoCompraUnidade = pecaSelecionada.getPreco() - 15.00;
            if (precoCompraUnidade < 0) precoCompraUnidade = 0;
            double custoTotal = precoCompraUnidade * quantidade;

            System.out.println("\n--- NOTA FISCAL DE REPOSIÇÃO ---");
            System.out.printf("Peça: %s\n", pecaSelecionada.getNome());
            System.out.printf("Preço de Compra (unidade): R$ %.2f\n", precoCompraUnidade);
            System.out.printf("Quantidade a Adicionar: %d\n", quantidade);
            System.out.println("---------------------------------");
            System.out.printf("CUSTO TOTAL DA REPOSIÇÃO: R$ %.2f\n", custoTotal);
            System.out.println("---------------------------------");

            System.out.print("Confirmar reposição e registrar despesa? (S/N): ");
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                pecaSelecionada.setQuantidade(pecaSelecionada.getQuantidade() + quantidade);
                estoque.salvarEstoque(); // Salva o estoque após a alteração

                String nota = "Reposição de " + quantidade + "x " + pecaSelecionada.getNome();
                gerenciadorFinanceiro.registrarDespesaCompraPecas(nota, custoTotal);
                System.out.println("Reposição realizada e despesa registrada com sucesso!");
            } else {
                System.out.println("Reposição cancelada.");
            }
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println("Erro: Escolha ou quantidade inválida.");
        }
    }

    private void comprarPecas() {
        System.out.println("\n--- Compra de Peças ---");
        List<Produto> pecasDisponiveis = estoque.listarProdutos();
        if (pecasDisponiveis.isEmpty()) {
            System.out.println("Nenhuma peça cadastrada no sistema. Cadastre uma peça primeiro.");
            return;
        }

        try {
            for (int i = 0; i < pecasDisponiveis.size(); i++) {
                Produto p = pecasDisponiveis.get(i);
                System.out.printf("%d. %s (Estoque: %d, Preço Venda: R$ %.2f)\n", i + 1, p.getNome(), p.getQuantidade(), p.getPreco());
            }

            System.out.print("Escolha a peça pelo número: ");
            int pecaIndex = Integer.parseInt(scanner.nextLine()) - 1;
            Produto pecaSelecionada = pecasDisponiveis.get(pecaIndex);

            System.out.print("Digite a quantidade a ser comprada: ");
            int quantidade = Integer.parseInt(scanner.nextLine());

            // Regra de negócio: preço de compra é 15 reais mais barato que o de venda
            double precoCompraUnidade = pecaSelecionada.getPreco() - 15.00;
            if (precoCompraUnidade < 0) precoCompraUnidade = 0; // Evita preço negativo
            double custoTotal = precoCompraUnidade * quantidade;

            // Exibição da "Nota Fiscal"
            System.out.println("\n--- NOTA FISCAL DE COMPRA ---");
            System.out.println("Peça: " + pecaSelecionada.getNome());
            System.out.println("Quantidade: " + quantidade);
            System.out.printf("Preço de Compra (por unidade): R$ %.2f\n", precoCompraUnidade);
            System.out.println("---------------------------------");
            System.out.printf("CUSTO TOTAL: R$ %.2f\n", custoTotal);
            System.out.println("---------------------------------");

            System.out.print("Confirmar compra e registrar despesa? (S/N): ");
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                // Atualiza a quantidade no estoque
                pecaSelecionada.setQuantidade(pecaSelecionada.getQuantidade() + quantidade);
                estoque.salvarEstoque(); // Supondo que você crie um método para salvar o estoque

                // Registra a despesa no financeiro
                String nota = "Compra de " + quantidade + "x " + pecaSelecionada.getNome();
                gerenciadorFinanceiro.registrarDespesaCompraPecas(nota, custoTotal);
                System.out.println("Compra realizada e despesa registrada com sucesso!");
            } else {
                System.out.println("Compra cancelada.");
            }

        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println("Erro: Escolha ou quantidade inválida.");
        }
    }
}