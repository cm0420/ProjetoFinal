/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.loja;

import com.google.gson.reflect.TypeToken;
import com.mycompany.oficina.persistencia.PersistenciaJson; // <-- IMPORTAR

import java.util.ArrayList;
import java.util.List;

public class Estoque {
    private List<Produto> produtos;
    private PersistenciaJson persistencia;

    // Construtor que recebe a persistência e carrega os dados
    public Estoque(PersistenciaJson persistencia) {
        this.persistencia = persistencia;
        this.produtos = this.persistencia.carregarLista("estoque", new TypeToken<ArrayList<Produto>>() {});
    }

    // Método para salvar o estado atual do estoque no arquivo JSON
    public void salvarEstoque() {
        this.persistencia.salvarLista("estoque", this.produtos);
    }

    // O construtor vazio não é mais necessário se a persistência for obrigatória
    // public Estoque() { this.produtos = new ArrayList<>(); }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void adicionarProduto(Produto produto) {
        produtos.add(produto);
        salvarEstoque(); // Salva imediatamente
    }

    public boolean cadastrarProduto(Produto produto) {
        if (buscarProduto(produto.getIdProduto()) != null) {
            return false;
        }
        adicionarProduto(produto); // O adicionarProduto já salva
        return true;
    }

    public boolean editarProduto(String idProduto, String novoNome, double novoPreco, int novaQuantidade, String novoFornecedor) {
        Produto produto = buscarProduto(idProduto);
        if (produto != null) {
            produto.setNome(novoNome);
            produto.setPreco(novoPreco);
            produto.setQuantidade(novaQuantidade);
            produto.setFornecedor(novoFornecedor);
            salvarEstoque(); // Salva imediatamente
            return true;
        }
        return false;
    }

    public boolean removerProduto(String idProduto) {
        Produto produto = buscarProduto(idProduto);
        if (produto != null) {
            produtos.remove(produto);
            salvarEstoque(); // Salva imediatamente
            return true;
        }
        return false;
    }

    // --- MÉTODOS DE BUSCA E LISTAGEM (SEM ALTERAÇÃO) ---

    public Produto buscarProduto(String idProduto) {
        for (Produto p : produtos) {
            if (p.getIdProduto().equals(idProduto)) {
                return p;
            }
        }
        return null;
    }

    public List<Produto> listarProdutos() {
        // Retorna uma cópia para evitar modificação externa direta da lista principal
        return new ArrayList<>(produtos);
    }
}
