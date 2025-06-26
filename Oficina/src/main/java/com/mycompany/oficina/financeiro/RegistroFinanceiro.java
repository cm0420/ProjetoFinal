package com.mycompany.oficina.financeiro;

import java.time.LocalDateTime;

public class RegistroFinanceiro {
    private final String descricao;
    private final double valor;
    private final TipoRegistro tipo;
    private final LocalDateTime data;

    public RegistroFinanceiro(String descricao, double valor, TipoRegistro tipo, LocalDateTime data) {
        this.descricao = descricao;
        this.valor = valor;
        this.tipo = tipo;
        this.data = data;
    }
    public String getDescricao() { return descricao; }
    public double getValor() { return valor; }
    public TipoRegistro getTipo() { return tipo; }
    public LocalDateTime getData() { return data; }
}
