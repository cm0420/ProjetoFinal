package com.mycompany.oficina.ordemservico;

import com.mycompany.oficina.*;
import com.mycompany.oficina.agendamento.TipoServico;
import java.util.ArrayList;
import java.util.Date;
import com.mycompany.oficina.entidades.*;
import com.mycompany.oficina.loja.Produto;
import java.time.LocalDateTime;
import java.util.List;
import com.mycompany.oficina.ObserverOS.*;
import com.mycompany.oficina.stateOS.*;

public class OrdemDeServico implements Assunto {

    private String numeroOS;
    private static int contadorNumeroOS;
    private final Cliente cliente;
    private final Carro carro;
    private final Funcionario mecanicoResponsavel;
    private final String defeitoRelatado;
    private final LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;
    private final List<String> servicosRealizados;
    private final List<PecaUtilizada> pecasUtilizadas;

    private EstadoOS estadoAtual; // Padrão State
    private final List<Observador> observadores = new ArrayList<>();

    public OrdemDeServico(String numeroOS, Cliente cliente, Carro carro, Funcionario mecanicoResponsavel, String defeitoRelatado, LocalDateTime dataAbertura, LocalDateTime dataFechamento, List servicosRealizados, List pecasUtilizadas) {
        this.numeroOS = "Ordem-Serviço" + String.format("%03d", contadorNumeroOS++);
        this.cliente = cliente;
        this.carro = carro;
        this.mecanicoResponsavel = mecanicoResponsavel;
        this.defeitoRelatado = defeitoRelatado;
        this.dataAbertura = dataAbertura;
        this.dataFechamento = dataFechamento;
        this.servicosRealizados = servicosRealizados;
        this.pecasUtilizadas = pecasUtilizadas;
        setEstado(new EstadoAguardando(this));
    }
    // MÉTODOS QUE DELEGAM AÇÕES PARA O ESTADO ATUAL

    public void iniciarInspecao() {
        this.estadoAtual.iniciarInspecao();
    }

    public void iniciarServico() {
        this.estadoAtual.iniciarServico();
    }

    public void adicionarPeca(Produto produtoDoEstoque, int quantidade) {
        this.estadoAtual.adicionarPeca(produtoDoEstoque, quantidade);
    }

    public void finalizarServico() {
        this.estadoAtual.finalizarServico();
    }

    public void cancelar(String motivo) {
        this.estadoAtual.cancelar(motivo);
    }

    // Método de transição chamado pelos objetos Estado
    public final void setEstado(EstadoOS novoEstado) {
        this.estadoAtual = novoEstado;
        System.out.println("\n>>> MUDANÇA DE ESTADO DA OS #" + this.numeroOS + ": " + this.estadoAtual.getStatus());
        this.notificarObservadores();
    }
    // IMPLEMENTAÇÃO DO PADRÃO OBSERVER

    @Override
    public void adicionarObservador(Observador observador) {
        this.observadores.add(observador);
    }

    @Override
    public void removerObservador(Observador observador) {
        this.observadores.remove(observador);
    }

    @Override
    public void notificarObservadores() {
        observadores.forEach(obs -> obs.atualizar(this));
    }

    // GETTERS
    public List<PecaUtilizada> getListaDePecasUtilizadas() {
        return pecasUtilizadas;
    }

    public String getNumeroOS() {
        return numeroOS;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Carro getCarro() {
        return carro;
    }

    public String getStatusAtual() {
        return this.estadoAtual.getStatus();
    }

}
