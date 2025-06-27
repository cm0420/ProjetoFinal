package com.mycompany.oficina.controller;

import com.mycompany.oficina.application.OficinaAplicattion;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MecanicoController {

    private final GerenciadorOrdemDeServico gerenciadorOS;
    private final AgendaOficina agenda;
    private final Estoque estoque;
    private final GerenciadorPonto gerenciadorPonto;

    public MecanicoController() {
        OficinaAplicattion app = OficinaAplicattion.getInstance();
        this.gerenciadorOS = app.getGerenciadorOS();
        this.agenda = app.getAgenda();
        this.estoque = app.getEstoque();
        this.gerenciadorPonto = app.getGerenciadorPonto();
    }

    // --- LÓGICA DE ORDEM DE SERVIÇO (OS) ---

    public List<Agendamento> listarAgendamentosDeHoje() {
        return Arrays.stream(agenda.getHorariosDoDia(LocalDate.now()))
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());
    }

    public OrdemDeServico abrirOS(Agendamento agendamento, String defeito) {
        if (agendamento == null || defeito == null || defeito.trim().isEmpty()) {
            return null;
        }
        return gerenciadorOS.abrirOS(
                agendamento.getCliente(),
                agendamento.getCarro(),
                Sessao.getInstance().getUsuarioLogado(),
                defeito
        );
    }

    public List<OrdemDeServico> listarOSAtivas() {
        return gerenciadorOS.listarTodos().stream()
                .filter(os -> !os.getStatusAtual().equals("Finalizada") && !os.getStatusAtual().equals("Cancelada"))
                .collect(Collectors.toList());
    }

    public OrdemDeServico buscarOS(String idOS) {
        return gerenciadorOS.buscarPorIdentificador(idOS);
    }

    public void iniciarInspecaoOS(OrdemDeServico os) {
        if (os != null) os.iniciarInspecao();
    }

    public void iniciarServicoOS(OrdemDeServico os) {
        if (os != null) os.iniciarServico();
    }

    public void finalizarServicoOS(OrdemDeServico os) {
        if (os != null) os.finalizarServico();
    }

    public void adicionarPecaOS(OrdemDeServico os, Produto produto, int quantidade) {
        if (os != null && produto != null && quantidade > 0) {
            os.adicionarPeca(produto, quantidade);
        }
    }

    public Produto buscarProdutoPorId(String id) {
        return estoque.buscarProduto(id);
    }

    public List<Produto> listarProdutosEstoque() {
        return estoque.listarProdutos();
    }
    public String gerarExtratoOS(OrdemDeServico os) {
        if (os == null) {
            return "Ordem de Serviço inválida ou não encontrada.";
        }
        return os.gerarExtrato();
    }
    // --- LÓGICA DE PONTO ---

    public RegistroPonto baterPontoEntrada() {
        return gerenciadorPonto.baterPontoEntrada(Sessao.getInstance().getUsuarioLogado());
    }

    public RegistroPonto baterPontoSaida() {
        return gerenciadorPonto.baterPontoSaida(Sessao.getInstance().getUsuarioLogado());
    }

    public List<RegistroPonto> verRegistrosDeHoje() {
        Funcionario usuarioLogado = Sessao.getInstance().getUsuarioLogado();
        if (usuarioLogado == null) return Collections.emptyList();

        List<RegistroPonto> meusRegistros = gerenciadorPonto.getRegistrosPorFuncionario(usuarioLogado);
        return meusRegistros.stream()
                .filter(r -> r.getDataHoraEntrada().toLocalDate().isEqual(LocalDate.now()))
                .collect(Collectors.toList());
    }
}