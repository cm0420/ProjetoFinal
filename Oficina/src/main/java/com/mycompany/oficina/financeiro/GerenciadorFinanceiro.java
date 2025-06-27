package com.mycompany.oficina.financeiro;

import com.google.gson.reflect.TypeToken;
import com.mycompany.oficina.entidades.Funcionario;
import com.mycompany.oficina.ordemservico.OrdemDeServico;
import com.mycompany.oficina.persistencia.PersistenciaJson;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GerenciadorFinanceiro {

    private static GerenciadorFinanceiro instance;
    private final List<RegistroFinanceiro> registros;
    private final PersistenciaJson persistencia;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Construtor privado que recebe a persistência e carrega os dados
    private GerenciadorFinanceiro(PersistenciaJson persistencia) {
        this.persistencia = persistencia;
        this.registros = this.persistencia.carregarLista("financeiro", new TypeToken<ArrayList<RegistroFinanceiro>>() {});
    }

    // Método estático para obter a instância, garantindo a injeção da persistência
    public static GerenciadorFinanceiro getInstance(PersistenciaJson persistencia) {
        if (instance == null) {
            instance = new GerenciadorFinanceiro(persistencia);
        }
        return instance;
    }

    // Método privado para salvar o estado atual no arquivo JSON
    private void salvar() {
        persistencia.salvarLista("financeiro", this.registros);
    }

    // --- MÉTODOS DE REGISTRO DE TRANSAÇÕES ---

    /**
     * Registra a receita de uma taxa de cancelamento de agendamento.
     */
    public void registrarReceitaCancelamento(String clienteNome, double valor, String motivo) {
        String descricao = "Taxa de cancelamento para cliente " + clienteNome + ". Motivo: " + motivo;
        registros.add(new RegistroFinanceiro(descricao, valor, TipoRegistro.RECEITA_CANCELAMENTO, LocalDateTime.now()));
        salvar();
    }

    /**
     * Registra a receita de uma OS finalizada e a despesa da comissão do mecânico.
     */
    public void registrarFaturamentoOS(OrdemDeServico os) {
        // Registra a receita total do serviço
        String descReceita = "Receita da OS #" + os.getNumeroOS() + " para cliente " + os.getCliente().getNome();
        registros.add(new RegistroFinanceiro(descReceita, os.calcularValorTotal(), TipoRegistro.RECEITA_SERVICO, os.getDataAbertura()));

        // Registra a despesa com a comissão de 5%
        double comissao = os.calcularValorTotal() * 0.05;
        String descComissao = "Comissão (5%) da OS #" + os.getNumeroOS() + " para mecânico " + os.getMecanicoResponsavel().getNome();
        registros.add(new RegistroFinanceiro(descComissao, comissao, TipoRegistro.DESPESA_COMISSAO, os.getDataAbertura()));

        salvar(); // Salva as duas transações
    }

    /**
     * Registra a despesa com a compra de peças para repor o estoque.
     */
    public void registrarDespesaCompraPecas(String notaFiscal, double valorTotal) {
        registros.add(new RegistroFinanceiro(notaFiscal, valorTotal, TipoRegistro.DESPESA_PECAS, LocalDateTime.now()));
        salvar();
    }

    /**
     * Simula o pagamento de salários fixos para atendentes e mecânicos.
     */
    public void pagarSalarios(List<Funcionario> todosFuncionarios) {
        for (Funcionario f : todosFuncionarios) {
            double salario = 0;
            if ("Atendente".equals(f.getCargo())) salario = 1000;
            if ("Mecanico".equals(f.getCargo())) salario = 1500;

            if (salario > 0) {
                registros.add(new RegistroFinanceiro("Salário de " + f.getNome(), salario, TipoRegistro.DESPESA_SALARIO, LocalDateTime.now()));
            }
        }
        System.out.println("Folha de pagamento registrada.");
        salvar();
    }

    // --- MÉTODOS DE RELATÓRIOS E BALANÇOS ---

    public void emitirRelatorioServicos(LocalDate inicio, LocalDate fim) {
        // Este metodo já estava correto, focado apenas em serviços.
        System.out.println("\n--- Relatório de Serviços de " + inicio.format(dtf) + " a " + fim.format(dtf) + " ---");
        List<RegistroFinanceiro> servicos = registros.stream()
                .filter(r -> r.getTipo() == TipoRegistro.RECEITA_SERVICO)
                .filter(r -> !r.getData().toLocalDate().isBefore(inicio) && !r.getData().toLocalDate().isAfter(fim))
                .toList();

        if (servicos.isEmpty()) {
            System.out.println("Nenhum serviço encontrado no período.");
            return;
        }
        servicos.forEach(s -> System.out.printf("Data: %s | Descrição: %s | Valor: R$ %.2f\n", s.getData().format(dtf), s.getDescricao(), s.getValor()));
    }

    public void emitirBalanco(LocalDate inicio, LocalDate fim) {
        List<RegistroFinanceiro> registrosPeriodo = registros.stream()
                .filter(r -> !r.getData().toLocalDate().isBefore(inicio) && !r.getData().toLocalDate().isAfter(fim))
                .toList();

        double receitas = registrosPeriodo.stream()
                .filter(r -> r.getTipo() == TipoRegistro.RECEITA_SERVICO || r.getTipo() == TipoRegistro.RECEITA_CANCELAMENTO)
                .mapToDouble(RegistroFinanceiro::getValor)
                .sum();


        double despesas = registrosPeriodo.stream()
                .filter(r -> r.getTipo() != TipoRegistro.RECEITA_SERVICO && r.getTipo() != TipoRegistro.RECEITA_CANCELAMENTO)
                .mapToDouble(RegistroFinanceiro::getValor)
                .sum();

        System.out.println("\n--- Balanço Financeiro de " + inicio.format(dtf) + " a " + fim.format(dtf) + " ---");
        System.out.printf("Total de Receitas: R$ %.2f\n", receitas);
        System.out.printf("Total de Despesas: R$ %.2f\n", despesas);
        System.out.println("----------------------------------------");
        System.out.printf("Lucro (Salário do Gerente): R$ %.2f\n", (receitas - despesas));
        System.out.println("----------------------------------------");
    }

    public void emitirRelatorioDespesasDetalhado(LocalDate inicio, LocalDate fim) {
        System.out.println("\n--- Relatório Detalhado de Despesas de " + inicio.format(dtf) + " a " + fim.format(dtf) + " ---");

        // --- CORREÇÃO APLICADA AQUI TAMBÉM ---
        // O filtro agora garante que todas as despesas, incluindo as de peças, sejam listadas.
        List<RegistroFinanceiro> despesasPeriodo = registros.stream()
                .filter(r -> r.getTipo() != TipoRegistro.RECEITA_SERVICO && r.getTipo() != TipoRegistro.RECEITA_CANCELAMENTO)
                .filter(r -> !r.getData().toLocalDate().isBefore(inicio) && !r.getData().toLocalDate().isAfter(fim))
                .toList();

        if (despesasPeriodo.isEmpty()) {
            System.out.println("Nenhuma despesa encontrada no período.");
            return;
        }

        for (RegistroFinanceiro despesa : despesasPeriodo) {
            System.out.printf("Data: %s | Tipo: %-20s | Valor: R$ %-10.2f | Descrição: %s\n",
                    despesa.getData().format(dtf),
                    despesa.getTipo().name(),
                    despesa.getValor(),
                    despesa.getDescricao()
            );
        }

        double totalDespesas = despesasPeriodo.stream().mapToDouble(RegistroFinanceiro::getValor).sum();
        System.out.println("----------------------------------------------------------------------------------");
        System.out.printf("Total de Despesas no Período: R$ %.2f\n", totalDespesas);
        System.out.println("----------------------------------------------------------------------------------");
    }
}

