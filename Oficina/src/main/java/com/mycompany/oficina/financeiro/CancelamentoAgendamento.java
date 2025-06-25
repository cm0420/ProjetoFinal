package com.mycompany.oficina.financeiro;

import com.mycompany.oficina.entidades.Cliente;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CancelamentoAgendamento {
    private final Cliente cliente;
    private final double valor;
    private final LocalDate dataCobranca;
    private final String motivo;

    public CancelamentoAgendamento(Cliente cliente, double valor, String motivo) {
        this.cliente = cliente;
        this.valor = valor;
        this.motivo = motivo;
        this.dataCobranca = LocalDate.now();
    }

    public Cliente getCliente() {
        return cliente;
    }

    public double getValor() {
        return valor;
    }

    public LocalDate getDataCobranca() {
        return dataCobranca;
    }

    public String getMotivo() {
        return motivo;
    }

    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("Data: %s | Cliente: %s | Valor: R$ %.2f | Motivo: %s",
                dtf.format(dataCobranca),
                cliente.getNome(),
                valor,
                motivo
        );
    }
}