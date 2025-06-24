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

/**
 *
 * @author Miguel
 */
public class Oficina {

    public static void main(String[] args) {

   /*     // 1. Seus gerenciadores
        GerenciadorCliente gc = new GerenciadorCliente();
        GerenciadorFuncionario gf = new GerenciadorFuncionario();
        GerenciadorCarros gca = new GerenciadorCarros();

        // 2. Adicionando clientes (isso está correto)
        gc.addCliente("Miguel", "1234", "3899924008", "Rua Joao Vicente Faria", "miguel@costa");
        gc.addCliente("Maria", "12345", "3891516161", "dfsdsfs", "fdsfsdfsdf");

        // 3. Adicionando funcionários (isso está correto)
        gf.criarFuncionario("123", "Mecanico", "Luiz", "3552626", "25161651", "dshbccdndcs ", "dsjfskd");
        gf.criarFuncionario("123", "Mecanico", "Claudio", "35526267", "25161651", "dshbccdndcs ", "dsjfskd");

        String cpfDoDonoMaria = "12345"; // CPF da Maria
        String cpfDoDonoMiguel = "1234"; // CPF da Miguel

        // 5. BUSQUE o objeto Cliente usando o CPF
        // (Isso assume que você tem o método 'buscarClientePorCpf' no seu GerenciadorCliente)
        Cliente donaMaria = gc.buscarPorIdentificador(cpfDoDonoMaria);
        Cliente donoMiguel = gc.buscarPorIdentificador(cpfDoDonoMiguel);

        // 6. VERIFIQUE se o cliente foi encontrado antes de prosseguir
        if (donaMaria != null) {
            // 7. Se encontrou, use o OBJETO 'donoDoCarro' para cadastrar o carro
            System.out.println("\nCliente '" + donaMaria.getNome() + "' encontrado. Cadastrando carro...");
            gca.cadastrarCarro(donaMaria, "fiat", "toro", "rav123", "dcdc54sdc");
        } else {
            // Caso contrário, mostre um erro
            System.out.println("\nERRO: Não foi possível cadastrar o carro. Cliente com CPF " + cpfDoDonoMaria + " não foi encontrado.");
        }
        if (donoMiguel != null) {
            // 7. Se encontrou, use o OBJETO 'donoDoCarro' para cadastrar o carro
            System.out.println("\nCliente '" + donoMiguel.getNome() + "' encontrado. Cadastrando carro...");
            gca.cadastrarCarro(donoMiguel, "fiat", "uno", "adv123", "sdas55");
        } else {
            // Caso contrário, mostre um erro
            System.out.println("\nERRO: Não foi possível cadastrar o carro. Cliente com CPF " + cpfDoDonoMiguel + " não foi encontrado.");
        }

        System.out.println("Lista final de veiculos");
        List<Carro> listaFinalDeCarros = gca.listarTodos();
        for (Carro carro : listaFinalDeCarros) {
            System.out.println(
                    "Veiculo" + carro.getFabricante() + " " + carro.getModelo()
                    + "Placa " + carro.getPlaca() + "Proprietario" + carro.getNomeDono());
        }

      // --- 2. CRIAÇÃO DOS AGENDAMENTOS ---
        System.out.println("\n>>> Criando agendamentos...");
        AgendaOficina agenda = new AgendaOficina();

        Carro carroDaMaria = gca.buscarPorIdentificador("dcdc54sdc");
        Carro carroDoMiguel = gca.buscarPorIdentificador("sdas55");
        Funcionario mecanicoLuiz = gf.buscarPorIdentificador("3552626");
        Funcionario mecanicoClaudio = gf.buscarPorIdentificador("35526267");
        
        // Datas para os agendamentos
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
        System.out.println(">>> Agendamentos criados com sucesso.");

         // --- PARTE 3: UTILIZANDO O INTERPRETER COM "E" ---
        System.out.println("\n>>> Realizando buscas com o padrao Interpreter...");
        ContextoDeBusca contexto = new ContextoDeBusca(agenda);

        System.out.println("\n--- BUSCA 1: Agendamentos para 'Miguel' E que sejam para a data de amanha ---");
        
        Expressao buscaComE = new ExpressaoE(
            new ExpressaoPorCliente(donoMiguel),
            new ExpressaoPorData(data1)
        );
        
        List<Agendamento> resultados1 = buscaComE.interpreter(contexto);
        imprimirResultados(resultados1);
        
        // --- PARTE 4: DEMONSTRANDO A FUNCIONALIDADE 'OU') ---
        System.out.println("\n--- BUSCA 2: Agendamentos da cliente 'Maria' OU que sejam para a data de depois de amanha ---");

        // Monta a expressão de busca usando o operador "OU"
        Expressao buscaComOu = new ExpressaoOu(
            new ExpressaoPorCliente(donaMaria),
            new ExpressaoPorData(data2)
        );
        
        // A chamada ao interpretador é idêntica
        List<Agendamento> resultados2 = buscaComOu.interpreter(contexto);
        imprimirResultados(resultados2);
    }
    
    /**
     * Metodo auxiliar para imprimir os resultados de uma busca de forma legível.
     *//*
    private static void imprimirResultados(List<Agendamento> resultados) {
        if (resultados.isEmpty()) {
            System.out.println(">> Nenhum agendamento encontrado para os critérios informados.");
        } else {
            System.out.println(">> " + resultados.size() + " agendamento(s) encontrado(s):");
            for (Agendamento ag : resultados) {
                System.out.printf(" -> Data: %s, Hora: %s, Cliente: %s, Servico: %s%n",
                        ag.getDataHora().toLocalDate(),
                        ag.getDataHora().toLocalTime(),
                        ag.getCliente().getNome(),
                        ag.getTipoServico());
            }
        }*/
     
          List<Agendamento> agendamentos = new ArrayList<>();
        agendamentos.add(new Agendamento(null, null, null, null, null, LocalDateTime.of(2025, 10, 25, 14, 0))); // Mais tarde
        agendamentos.add(new Agendamento(null, null, null, null, null, LocalDateTime.of(2025, 10, 25, 9, 0)));  // Meio
        agendamentos.add(new Agendamento(null, null, null, null, null, LocalDateTime.of(2025, 10, 24, 16, 0))); // Mais cedo

        System.out.println("Lista ANTES da ordenação:");
        for (Agendamento ag : agendamentos) {
            System.out.println(" -> " + ag.getDataHora());
        }
        
       // Questao COmparators//
       
        // O método .sort() da lista usa a nossa classe de comparação para
        // saber como organizar os elementos.
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
        agendamentos.sort(new AgendamentoComparator());

        System.out.println("\nLista DEPOIS da ordenação:");
        for (Agendamento ag : agendamentos) {
            System.out.println(" -> " + ag.getDataHora().format(formatador));
        }
     
     
    }
    
}
