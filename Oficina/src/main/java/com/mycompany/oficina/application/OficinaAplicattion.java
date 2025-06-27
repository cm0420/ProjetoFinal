package com.mycompany.oficina.application;

import com.mycompany.oficina.financeiro.GerenciadorFinanceiro;
import com.mycompany.oficina.agendamento.AgendaOficina;
import com.mycompany.oficina.controlador.GerenciadorCarros;
import com.mycompany.oficina.controlador.GerenciadorCliente;
import com.mycompany.oficina.controlador.GerenciadorFuncionario;
import com.mycompany.oficina.loja.Estoque;
import com.mycompany.oficina.ordemservico.GerenciadorOrdemDeServico;
import com.mycompany.oficina.persistencia.PersistenciaJson;
import com.mycompany.oficina.seguranca.ServicoAutenticacao;
import com.mycompany.oficina.sistemaponto.GerenciadorPonto;

public class OficinaAplicattion {

    private static OficinaAplicattion instance;
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

    private OficinaAplicattion() {
        this.persistencia = new PersistenciaJson();
        this.gerenciadorFuncionario = new GerenciadorFuncionario(persistencia);
        this.gerenciadorCliente = new GerenciadorCliente(persistencia);
        this.gerenciadorCarros = new GerenciadorCarros(persistencia);
        this.gerenciadorOS = new GerenciadorOrdemDeServico(persistencia);
        this.gerenciadorPonto = new GerenciadorPonto(persistencia);
        this.gerenciadorFinanceiro = GerenciadorFinanceiro.getInstance(persistencia);
        this.agenda = new AgendaOficina();
        this.estoque = new Estoque(persistencia);
        this.servicoAutenticacao = new ServicoAutenticacao(gerenciadorFuncionario);
    }

    public static synchronized OficinaAplicattion getInstance() {
        if (instance == null) {
            instance = new OficinaAplicattion();
        }
        return instance;
    }

    // Métodos para acessar os gerenciadores
    public ServicoAutenticacao getServicoAutenticacao() { return servicoAutenticacao; }
    public GerenciadorCliente getGerenciadorCliente() { return gerenciadorCliente; }
    public GerenciadorCarros getGerenciadorCarros() { return gerenciadorCarros; }
    public GerenciadorFuncionario getGerenciadorFuncionario() { return gerenciadorFuncionario; }
    public AgendaOficina getAgenda() { return agenda; }
    public GerenciadorOrdemDeServico getGerenciadorOS() { return gerenciadorOS; }
    public GerenciadorPonto getGerenciadorPonto() { return gerenciadorPonto; }
    public GerenciadorFinanceiro getGerenciadorFinanceiro() { return gerenciadorFinanceiro; }
    public Estoque getEstoque() { return estoque; }


    public void verificarECriarAdminPadrao() {
        if (gerenciadorFuncionario.listarTodos().isEmpty()) {
            System.out.println("[Sistema] Nenhum funcionário encontrado. Criando usuário 'Admin' padrão...");
            gerenciadorFuncionario.criarFuncionario("admin", "Admin", "Administrador do Sistema", "00000000000", "00000000000", "N/A", "admin@oficina.com");
            System.out.println("[Sistema] Usuário 'Admin' criado. Use CPF '00000000000' e senha 'admin' para o primeiro login.");
        }
    }
}