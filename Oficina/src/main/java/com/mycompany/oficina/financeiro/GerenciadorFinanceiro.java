package com.mycompany.oficina.financeiro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GerenciadorFinanceiro {
    private static GerenciadorFinanceiro instance;
    private final List<CancelamentoAgendamento> cobrancasPendentes;

    // Construtor privado impede a criação de instâncias fora da classe
    private GerenciadorFinanceiro() {
        this.cobrancasPendentes = new ArrayList<>();
    }

    /**
     * Retorna a instância única (Singleton) do gerenciador financeiro.
     * @return A instância de GerenciadorFinanceiro.
     */
    public static GerenciadorFinanceiro getInstance() {
        if (instance == null) {
            instance = new GerenciadorFinanceiro();
        }
        return instance;
    }

    /**
     * Registra uma nova cobrança de cancelamento.
     * @param cobranca A cobrança a ser arquivada.
     */
    public void registrarCobrancaCancelamento(CancelamentoAgendamento cobranca) {
        if (cobranca != null) {
            this.cobrancasPendentes.add(cobranca);
            System.out.println("[LOG Financeiro]: Nova cobrança de cancelamento registrada. " + cobranca);
        }
    }

    /**
     * Retorna uma lista de todas as cobranças pendentes.
     * A lista não pode ser modificada externamente.
     * @return Uma lista de CobrançaCancelamento.
     */
    public List<CancelamentoAgendamento> getCobrancasPendentes() {
        return Collections.unmodifiableList(cobrancasPendentes);
    }
}

