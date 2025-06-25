/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.oficina;

import com.mycompany.oficina.agendamento.*;
import com.mycompany.oficina.comparators.AgendamentoComparator;
import com.mycompany.oficina.controlador.GerenciadorCarros;
import com.mycompany.oficina.controlador.GerenciadorCliente;
import com.mycompany.oficina.controlador.GerenciadorFuncionario;
import com.mycompany.oficina.entidades.Carro;
import com.mycompany.oficina.entidades.Cliente;
import com.mycompany.oficina.entidades.Funcionario;
import com.mycompany.oficina.interpreter.*; // Importe o pacote do interpreter

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.oficina.Menus.MenuMecanico;
import com.mycompany.oficina.Menus.MenuPrincipal;
import com.mycompany.oficina.Menus.Navegador;
import com.mycompany.oficina.agendamento.*;
import com.mycompany.oficina.controlador.*;
import com.mycompany.oficina.entidades.*;
import com.mycompany.oficina.interpreter.*;
import com.mycompany.oficina.loja.Estoque;
import com.mycompany.oficina.loja.Produto;
import com.mycompany.oficina.ordemservico.GerenciadorOrdemDeServico;
import com.mycompany.oficina.segurança.ServicoAutenticacao;
import com.mycompany.oficina.segurança.Sessao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

/**
 * @author Miguel
 */
public class Oficina {
    private static final GerenciadorFuncionario gerenciadorFuncionario = new GerenciadorFuncionario();
    private static final GerenciadorCliente gerenciadorCliente = new GerenciadorCliente();
    private static final GerenciadorCarros gerenciadorCarros = new GerenciadorCarros();
    private static final GerenciadorOrdemDeServico gerenciadorOS = new GerenciadorOrdemDeServico();
    private static final ServicoAutenticacao servicoAutenticacao = new ServicoAutenticacao(gerenciadorFuncionario);

    // --- CAMADA DE DADOS EM MEMÓRIA ---
    private static final AgendaOficina agenda = new AgendaOficina();
    private static final Estoque estoque = new Estoque();

    public static void main(String[] args) {

        /*GerenciadorCliente gc = new GerenciadorCliente();
        GerenciadorFuncionario gf = new GerenciadorFuncionario();
        // Corrigido: Usando a classe GerenciadorCarros refatorada
        GerenciadorCarros gca = new GerenciadorCarros();

        gc.addCliente("Miguel", "1234", "3899924008", "Rua Joao Vicente Faria", "miguel@costa");
        gc.addCliente("Maria", "12345", "3891516161", "dfsdsfs", "fdsfsdfsdf");

        gf.criarFuncionario("senha123", "Mecanico", "Luiz", "3552626", "25161651", "dshbccdndcs", "dsjfskd");
        gf.criarFuncionario("senha456", "Mecanico", "Claudio", "35526267", "25161651", "dshbccdndcs", "dsjfskd");

        String cpfDoDonoMaria = "12345";
        String cpfDoDonoMiguel = "1234";

        Cliente donaMaria = gc.buscarPorIdentificador(cpfDoDonoMaria);
        Cliente donoMiguel = gc.buscarPorIdentificador(cpfDoDonoMiguel);

        if (donaMaria != null) {
            System.out.println("\nCliente '" + donaMaria.getNome() + "' encontrado. Cadastrando carro...");
            gca.cadastrarCarro(donaMaria, "fiat", "toro", "rav123", "dcdc54sdc");
        } else {
            System.out.println("\nERRO: Não foi possível cadastrar o carro. Cliente com CPF " + cpfDoDonoMaria + " não foi encontrado.");
        }
        if (donoMiguel != null) {
            System.out.println("\nCliente '" + donoMiguel.getNome() + "' encontrado. Cadastrando carro...");
            gca.cadastrarCarro(donoMiguel, "fiat", "uno", "adv123", "sdas55");
        } else {
            System.out.println("\nERRO: Não foi possível cadastrar o carro. Cliente com CPF " + cpfDoDonoMiguel + " não foi encontrado.");
        }

        System.out.println("\nLista de veiculos cadastrados:");
        List<Carro> listaFinalDeCarros = gca.listarTodos();
        for (Carro carro : listaFinalDeCarros) {
            System.out.printf(" -> Veiculo: %s %s, Placa: %s, Proprietario: %s%n",
                    carro.getFabricante(), carro.getModelo(), carro.getPlaca(), carro.getNomeDono());
        }

        // --- 2. CRIAÇÃO DOS AGENDAMENTOS NA NOVA AGENDA ---
        System.out.println("\n>>> Criando agendamentos...");
        AgendaOficina agenda = new AgendaOficina();

        // Corrigido: Usando o metodo de busca genérico (que busca pelo chassi no GerenciadorCarros)
        Carro carroDaMaria = gca.buscarPorIdentificador("dcdc54sdc");
        Carro carroDoMiguel = gca.buscarPorIdentificador("sdas55");
        Funcionario mecanicoLuiz = gf.buscarPorIdentificador("3552626");
        Funcionario mecanicoClaudio = gf.buscarPorIdentificador("35526267");

        LocalDate data1 = LocalDate.now().plusDays(1); // Amanhã
        LocalDate data2 = LocalDate.now().plusDays(2); // Depois de amanhã

        // Agendamento 1: Maria, para amanhã
        if (donaMaria != null && carroDaMaria != null && mecanicoLuiz != null) {
            agenda.agendar(new Agendamento(donaMaria, carroDaMaria, mecanicoLuiz, TipoServico.REPARO, null, data1.atTime(9, 0)));
        }

        // Agendamento 2: Miguel, para amanhã
        if (donoMiguel != null && carroDoMiguel != null && mecanicoClaudio != null) {
            agenda.agendar(new Agendamento(donoMiguel, carroDoMiguel, mecanicoClaudio, TipoServico.REPARO, null, data1.atTime(14, 0)));
        }

        // Agendamento 3: Miguel, para depois de amanhã
        if (donoMiguel != null && carroDoMiguel != null && mecanicoClaudio != null) {
            agenda.agendar(new Agendamento(donoMiguel, carroDoMiguel, mecanicoClaudio, TipoServico.INSPECAO, null, data2.atTime(10, 0)));
        }

        // --- 3. UTILIZANDO O INTERPRETER ATUALIZADO ---
        System.out.println("\n>>> Realizando buscas com o padrão Interpreter...");
        ContextoDeBusca contexto = new ContextoDeBusca(agenda);

        System.out.println("\n--- BUSCA 1: Agendamentos para 'Miguel' E que sejam para a data de amanhã ---");

        Expressao buscaComE = new ExpressaoE(
                new ExpressaoPorCliente(donoMiguel),
                new ExpressaoPorData(data1)
        );

        // Corrigido: Chamando o metodo 'interpretar'
        List<Agendamento> resultados1 = buscaComE.interpreter(contexto);
        imprimirResultados(resultados1);

        System.out.println("\n--- BUSCA 2: Agendamentos da cliente 'Maria' OU que sejam para a data de depois de amanhã ---");

        Expressao buscaComOu = new ExpressaoOu(
                new ExpressaoPorCliente(donaMaria),
                new ExpressaoPorData(data2)
        );

        // Corrigido: Chamando o metodo 'interpretar'
        List<Agendamento> resultados2 = buscaComOu.interpreter(contexto);
        imprimirResultados(resultados2);
    }

    private static void imprimirResultados(List<Agendamento> resultados) {
        if (resultados.isEmpty()) {
            System.out.println(">> Nenhum agendamento encontrado para os critérios informados.");
        } else {
            System.out.println(">> " + resultados.size() + " agendamento(s) encontrado(s):");
            for (Agendamento ag : resultados) {
                System.out.printf(" -> Data: %s, Hora: %s, Cliente: %s, Serviço: %s%n",
                        ag.getDataHora().toLocalDate(),
                        ag.getDataHora().toLocalTime(),
                        ag.getCliente().getNome(),
                        ag.getTipoServico());
            }*/


     /*
          List<Agendamento> agendamentos = new ArrayList<>();
        agendamentos.add(new Agendamento(null, null, null, null, null, LocalDateTime.of(2025, 10, 25, 14, 0))); // Mais tarde
        agendamentos.add(new Agendamento(null, null, null, null, null, LocalDateTime.of(2025, 10, 25, 9, 0)));  // Meio
        agendamentos.add(new Agendamento(null, null, null, null, null, LocalDateTime.of(2025, 10, 24, 16, 0))); // Mais cedo

        System.out.println("Lista ANTES da ordenação:");
        for (Agendamento ag : agendamentos) {
            System.out.println(" -> " + ag.getDataHora());
        }
        
       // Questao COmparators//
       
        // O metodo .sort() da lista usa a nossa classe de comparação para
        // saber como organizar os elementos.
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
        agendamentos.sort(new AgendamentoComparator());

        System.out.println("\nLista DEPOIS da ordenação:");
        for (Agendamento ag : agendamentos) {
            System.out.println(" -> " + ag.getDataHora().format(formatador));
        }
*/

        popularDadosIniciais();

        Scanner scanner = new Scanner(System.in);
        System.out.println("====== BEM-VINDO AO SISTEMA DA OFICINA ======");

        // 2. Loop principal da aplicação
        while (true) {
            // Se não há ninguém logado, mostra a tela de login
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
                    // Empilha o primeiro menu na pilha de navegação após o login
                    // Injeta as dependências necessárias no construtor do MenuPrincipal
                    Navegador.getInstance().navegarPara(new MenuPrincipal(agenda, estoque, gerenciadorOS,gerenciadorCliente, gerenciadorCarros, gerenciadorFuncionario));
                } else {
                    System.out.println(">>> ERRO: CPF ou senha inválidos. Tente novamente.");
                }
            }
            // Se há um usuário logado, processa a navegação de menus
            else {
                // Se a pilha de menus se esvaziar (logout do menu principal), desloga o usuário
                if (Navegador.getInstance().pilhaVazia()) {
                    Sessao.getInstance().logout();
                    System.out.println("Você saiu do sistema. Até logo!");
                    System.out.println("==============================================");
                } else {
                    // Exibe o menu que está no topo da pilha
                    Navegador.getInstance().exibirMenuAtual();
                }
            }
        }
    }

    /**
     * Cria dados de exemplo para que a aplicação seja testável desde o início.
     */
    private static void popularDadosIniciais() {
        System.out.println("[Sistema] Populando dados iniciais para demonstração...");

        // Funcionários
        Funcionario gerente = gerenciadorFuncionario.criarFuncionario("admin123", "Gerente", "Ana Silva", "111", "9999-0001", "Rua A", "ana@oficina.com");
        Funcionario meca1 = gerenciadorFuncionario.criarFuncionario("mec456", "Mecanico", "Carlos Souza", "222", "9999-0002", "Rua B", "carlos@oficina.com");

        // Clientes
        Cliente cli1 = gerenciadorCliente.addCliente("João Pereira", "123.456", "8888-0001", "Rua C", "joao@cliente.com");

        // Carros
        Carro carro1 = gerenciadorCarros.cadastrarCarro(cli1, "VW", "Gol", "ABC-1234", "chassi1");

        // Estoque
        estoque.cadastrarProduto(new Produto("Óleo Motor 1L", 45.00, 50, "Fornecedor A"));
        estoque.cadastrarProduto(new Produto("Filtro de Ar", 25.00, 30, "Fornecedor B"));

        // Agendamentos para hoje, para que o mecânico possa testar
        Agendamento ag1 = new Agendamento(cli1, carro1, meca1, TipoServico.REPARO, null, LocalDateTime.now().withHour(10).withMinute(0));
        agenda.agendar(ag1);

        System.out.println("[Sistema] Dados populados. Aplicação pronta.\n");

    }
}


