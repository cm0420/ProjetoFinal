package com.mycompany.oficina.controller;

import com.mycompany.oficina.application.OficinaAplicattion;
import com.mycompany.oficina.agendamento.AgendaOficina;
import com.mycompany.oficina.agendamento.Agendamento;
import com.mycompany.oficina.agendamento.TipoServico;
import com.mycompany.oficina.controlador.GerenciadorCarros;
import com.mycompany.oficina.controlador.GerenciadorCliente;
import com.mycompany.oficina.controlador.GerenciadorFuncionario;
import com.mycompany.oficina.entidades.Carro;
import com.mycompany.oficina.entidades.Cliente;
import com.mycompany.oficina.entidades.Funcionario;
import com.mycompany.oficina.financeiro.GerenciadorFinanceiro;
import com.mycompany.oficina.interpreter.ContextoDeBusca;
import com.mycompany.oficina.interpreter.Expressao;
import com.mycompany.oficina.interpreter.ExpressaoPorCliente;
import com.mycompany.oficina.interpreter.ExpressaoPorData;
import com.mycompany.oficina.seguranca.Sessao;
import com.mycompany.oficina.sistemaponto.GerenciadorPonto;
import com.mycompany.oficina.sistemaponto.RegistroPonto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AtendenteController {

    private final GerenciadorCliente gerenciadorCliente;
    private final GerenciadorCarros gerenciadorCarros;
    private final GerenciadorFuncionario gerenciadorFuncionario;
    private final GerenciadorPonto gerenciadorPonto;
    private final GerenciadorFinanceiro gerenciadorFinanceiro;
    private final AgendaOficina agenda;

    public AtendenteController() {
        OficinaAplicattion app = OficinaAplicattion.getInstance();
        this.gerenciadorCliente = app.getGerenciadorCliente();
        this.gerenciadorCarros = app.getGerenciadorCarros();
        this.gerenciadorFuncionario = app.getGerenciadorFuncionario();
        this.gerenciadorPonto = app.getGerenciadorPonto();
        this.gerenciadorFinanceiro = app.getGerenciadorFinanceiro();
        this.agenda = app.getAgenda();
    }

    // --- LÓGICA DE CLIENTES ---

    public List<Cliente> listarClientes() {
        return gerenciadorCliente.listarTodos();
    }

    public Cliente buscarCliente(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) return null;
        return gerenciadorCliente.buscarPorIdentificador(cpf.replaceAll("\\D", ""));
    }

    public boolean cadastrarCliente(String nome, String cpf, String telefone, String endereco, String email) {
        if (buscarCliente(cpf) != null) {
            return false;
        }
        Cliente novoCliente = gerenciadorCliente.addCliente(nome, cpf, telefone, endereco, email);
        return novoCliente != null;
    }

    public boolean editarCliente(String cpfAntigo, String novoNome, String novoCpf, String novoTelefone, String novoEndereco, String novoEmail) {
        Cliente cliente = buscarCliente(cpfAntigo);
        if (cliente == null) return false;

        String nomeFinal = (novoNome == null || novoNome.isEmpty()) ? cliente.getNome() : novoNome;
        String cpfFinal = (novoCpf == null || novoCpf.isEmpty()) ? cliente.getCpf() : novoCpf;
        String telefoneFinal = (novoTelefone == null || novoTelefone.isEmpty()) ? cliente.getTelefone() : novoTelefone;
        String enderecoFinal = (novoEndereco == null || novoEndereco.isEmpty()) ? cliente.getEndereco() : novoEndereco;
        String emailFinal = (novoEmail == null || novoEmail.isEmpty()) ? cliente.getEmail() : novoEmail;

        return gerenciadorCliente.editarCliente(nomeFinal, cpfAntigo, telefoneFinal, enderecoFinal, emailFinal);
    }

    public boolean removerCliente(String cpf) {
        Cliente cliente = buscarCliente(cpf);
        if (cliente == null) return false;

        boolean temCarros = gerenciadorCarros.listarTodos().stream()
                .anyMatch(c -> c.getCpfDono().equals(cliente.getCpf()));
        if (temCarros) {
            return false;
        }
        return gerenciadorCliente.removerItemPorIdentificador(cpf);
    }

    // --- LÓGICA DE VEÍCULOS ---

    public List<Carro> listarVeiculos() {
        return gerenciadorCarros.listarTodos();
    }

    public Carro buscarVeiculo(String chassi) {
        return gerenciadorCarros.buscarPorIdentificador(chassi);
    }

    public List<Carro> listarVeiculosDoCliente(String cpf) {
        return gerenciadorCarros.listarTodos().stream()
                .filter(c -> c.getCpfDono().equals(cpf))
                .collect(Collectors.toList());
    }

    public boolean cadastrarVeiculo(String cpfDono, String fabricante, String modelo, String placa, String chassi) {
        Cliente dono = buscarCliente(cpfDono);
        if (dono == null || buscarVeiculo(chassi) != null) {
            return false;
        }
        Carro novoCarro = gerenciadorCarros.cadastrarCarro(dono, fabricante, modelo, placa, chassi);
        return novoCarro != null;
    }

    public boolean editarVeiculo(String chassi, String novoFabricante, String novoModelo, String novaPlaca) {
        Carro carro = buscarVeiculo(chassi);
        if (carro == null) return false;

        String fabFinal = (novoFabricante == null || novoFabricante.isEmpty()) ? carro.getFabricante() : novoFabricante;
        String modFinal = (novoModelo == null || novoModelo.isEmpty()) ? carro.getModelo() : novoModelo;
        String placaFinal = (novaPlaca == null || novaPlaca.isEmpty()) ? carro.getPlaca() : novaPlaca;

        return gerenciadorCarros.editarCarro(chassi, fabFinal, modFinal, placaFinal);
    }

    // --- LÓGICA DE AGENDAMENTOS ---

    public List<Agendamento> buscarAgendamentos(String termoBusca) {
        Expressao expressaoDeBusca;
        try {
            LocalDate data = LocalDate.parse(termoBusca, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            expressaoDeBusca = new ExpressaoPorData(data);
        } catch (java.time.format.DateTimeParseException e) {
            Cliente cliente = buscarCliente(termoBusca);
            if (cliente == null) return Collections.emptyList();
            expressaoDeBusca = new ExpressaoPorCliente(cliente);
        }
        return expressaoDeBusca.interpreter(new ContextoDeBusca(agenda));
    }

    public List<Funcionario> listarMecanicosDisponiveis() {
        return gerenciadorFuncionario.listarTodos().stream()
                .filter(f -> "Mecanico".equals(f.getCargo()))
                .collect(Collectors.toList());
    }

    public boolean criarAgendamento(Cliente cliente, Carro carro, Funcionario mecanico, TipoServico tipo, LocalDateTime dataHora) {
        if (cliente == null || carro == null || mecanico == null || tipo == null || dataHora == null) return false;
        Agendamento novoAgendamento = new Agendamento(cliente, carro, mecanico, tipo, null, dataHora);
        return agenda.agendar(novoAgendamento);
    }

    public boolean cancelarAgendamento(Agendamento agendamento) {
        if (agendamento.getDataHora().toLocalDate().isEqual(LocalDate.now())) {
            double taxa = 150.0 * 0.20;
            String motivo = "Cancelamento no dia do serviço (" + LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ")";
            this.gerenciadorFinanceiro.registrarReceitaCancelamento(agendamento.getCliente().getNome(), taxa, motivo);
        }
        return agenda.cancelarAgendamento(agendamento);
    }
    public List<Agendamento> listarTodosAgendamentos() {
        return agenda.listarTodosAgendamentos();
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