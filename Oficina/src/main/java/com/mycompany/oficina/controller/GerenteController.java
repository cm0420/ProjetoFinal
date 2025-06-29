package com.mycompany.oficina.controller;

import com.mycompany.oficina.application.OficinaAplicattion;
import com.mycompany.oficina.controlador.GerenciadorFuncionario;
import com.mycompany.oficina.entidades.Funcionario;
import com.mycompany.oficina.financeiro.GerenciadorFinanceiro;
import com.mycompany.oficina.loja.Estoque;
import com.mycompany.oficina.loja.Produto;
import java.time.LocalDate;
import java.util.List;

public class GerenteController extends AtendenteController {

    private final GerenciadorFuncionario gerenciadorFuncionario;
    private final GerenciadorFinanceiro gerenciadorFinanceiro;
    private final Estoque estoque;

    public GerenteController() {
        super();
        OficinaAplicattion app = OficinaAplicattion.getInstance();
        this.gerenciadorFuncionario = app.getGerenciadorFuncionario();
        this.gerenciadorFinanceiro = app.getGerenciadorFinanceiro();
        this.estoque = app.getEstoque();
    }

    // --- LÓGICA DE FUNCIONÁRIOS ---

    public List<Funcionario> listarFuncionarios() {
        return gerenciadorFuncionario.listarTodos();
    }

    public Funcionario buscarFuncionario(String cpf) {
        return gerenciadorFuncionario.buscarPorIdentificador(cpf);
    }

    public boolean cadastrarFuncionario(String senha, String cargo, String nome, String cpf, String telefone, String endereco, String email) {
        if (gerenciadorFuncionario.buscarPorIdentificador(cpf) != null) return false;
        Funcionario f = gerenciadorFuncionario.criarFuncionario(senha, cargo, nome, cpf, telefone, endereco, email);
        return f != null;
    }

    public boolean editarFuncionario(String cpf, String novoCpf, String novaSenha, String novoCargo, String novoNome, String novoTelefone, String novoEndereco, String novoEmail) {
        Funcionario f = buscarFuncionario(cpf);
        if (f == null) return false;

        String senhaFinal = (novaSenha == null || novaSenha.isEmpty()) ? f.getSenha() : novaSenha;
        String cargoFinal = (novoCargo == null || novoCargo.isEmpty()) ? f.getCargo() : novoCargo;
        String nomeFinal = (novoNome == null || novoNome.isEmpty()) ? f.getNome() : novoNome;
        String cpfFinal = (novoCpf == null || novoCpf.isEmpty()) ? f.getCpf() : novoCpf;
        String telFinal = (novoTelefone == null || novoTelefone.isEmpty()) ? f.getTelefone() : novoTelefone;
        String endFinal = (novoEndereco == null || novoEndereco.isEmpty()) ? f.getEndereco() : novoEndereco;
        String emailFinal = (novoEmail == null || novoEmail.isEmpty()) ? f.getEmail() : novoEmail;

        return gerenciadorFuncionario.editarFuncionario(cpf, senhaFinal, cargoFinal, nomeFinal, telFinal, endFinal, emailFinal);
    }

    public boolean removerFuncionario(String cpf) {
        return gerenciadorFuncionario.removerItemPorIdentificador(cpf);
    }

    // --- LÓGICA FINANCEIRA ---

    public void emitirBalanco(LocalDate inicio, LocalDate fim) {
        System.out.println("\n--- Balanço Financeiro de " + inicio + " a " + fim + " ---");
        gerenciadorFinanceiro.emitirBalanco(inicio, fim);
    }

    public void emitirRelatorioDespesas(LocalDate inicio, LocalDate fim) {
        System.out.println("\n--- Relatório de Despesas de " + inicio + " a " + fim + " ---");
        gerenciadorFinanceiro.emitirRelatorioDespesasDetalhado(inicio, fim);
    }

    public void pagarSalarios() {
        gerenciadorFinanceiro.pagarSalarios(gerenciadorFuncionario.listarTodos());
    }

    // --- LÓGICA DE ESTOQUE ---

    public List<Produto> listarProdutos() {
        return estoque.listarProdutos();
    }

    public Produto buscarProduto(String id) {
        return estoque.buscarProduto(id);
    }

    public boolean cadastrarNovaPeca(String nome, double precoVenda, int quantidade, String fornecedor) {
        Produto novoProduto = new Produto(nome, precoVenda, quantidade, fornecedor);
        double precoCompraUnidade = Math.max(0, precoVenda - 15.00);
        double custoTotal = precoCompraUnidade * quantidade;
        gerenciadorFinanceiro.registrarDespesaCompraPecas("Compra inicial de " + quantidade + "x " + nome, custoTotal);

        return estoque.cadastrarProduto(novoProduto);
    }

    public boolean editarPeca(String id, String novoNome, double novoPreco, int novaQtd, String novoForn) {
        return estoque.editarProduto(id, novoNome, novoPreco, novaQtd, novoForn);
    }

    public boolean reporEstoque(Produto peca, int quantidade) {
        if (peca == null || quantidade <= 0) return false;

        peca.setQuantidade(peca.getQuantidade() + quantidade);
        double precoCompraUnidade = Math.max(0, peca.getPreco() - 15.00);
        double custoTotal = precoCompraUnidade * quantidade;
        gerenciadorFinanceiro.registrarDespesaCompraPecas("Reposição de " + quantidade + "x " + peca.getNome(), custoTotal);

        estoque.salvarEstoque();
        return true;
    }
}