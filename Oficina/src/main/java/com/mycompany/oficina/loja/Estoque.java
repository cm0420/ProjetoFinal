/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.loja;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mariaa
 */
public class Estoque {
    private List<Produto> produtos;

    public Estoque(ArrayList<Produto> produtos) {
        this.produtos = new ArrayList<>();
    }

    public Estoque() {
    }

    /**
     * @return
     */
    public List<Produto> getProdutos() {
        return produtos;
    }

    public void adicionarProduto(Produto produto) {
        produtos.add(produto);
    }

    public void removerProduto(Produto produto) {
        produtos.remove(produto);
    }

    /**
     * Cadastra um novo produto no estoque, se o ID ainda não existir.
     *
     * @param produto Produto a ser cadastrado
     * @return true se o produto foi cadastrado com sucesso, false se já existir
     */
    public boolean cadastrarProduto(Produto produto) {
        if (buscarProduto(produto.getIdProduto()) != null) {
            return false; // Produto já existe
        }
        adicionarProduto(produto);
        return true;
    }

    /**
     * Busca um produto no estoque pelo ID.
     *
     * @param idProduto ID do produto a buscar
     * @return Produto encontrado, ou null se não existir
     */
    public Produto buscarProduto(String idProduto) {
        for (Produto p : produtos) {
            if (p.getIdProduto().equals(idProduto)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Lista todos os produtos atualmente no estoque.
     *
     * @return Lista de todos os produtos
     */
    public ArrayList<Produto> listarProdutos() {
        return new ArrayList<>(produtos); // Cópia para segurança
    }

    /**
     * Lista os produtos cujo nome contenha a peça informada (ignora maiúsculas/minúsculas).
     *
     * @param peca Parte do nome a buscar
     * @return Lista de produtos encontrados
     */
    public ArrayList<Produto> listarProdutosPorPeca(String peca) {
        ArrayList<Produto> resultados = new ArrayList<>();
        String pecaLower = peca.toLowerCase();
        for (Produto p : produtos) {
            if (p.getNome().toLowerCase().contains(pecaLower)) {
                resultados.add(p);
            }
        }
        return resultados;
    }

    /**
     * Edita os dados de um produto identificado pelo ID.
     *
     * @param idProduto      ID do produto a editar
     * @param novoNome       Novo nome do produto
     * @param novoPreco      Novo preço
     * @param novaQuantidade Nova quantidade
     * @param novoFornecedor Novo fornecedor
     * @return true se o produto foi encontrado e atualizado, false se não encontrado
     */
    public boolean editarProduto(String idProduto, String novoNome, double novoPreco, int novaQuantidade, String novoFornecedor) {
        Produto produto = buscarProduto(idProduto);
        if (produto != null) {
            produto.setNome(novoNome);
            produto.setPreco(novoPreco);
            produto.setQuantidade(novaQuantidade);
            produto.setFornecedor(novoFornecedor);
            return true;
        }
        return false;
    }

    /**
     * Remove um produto do estoque com base no ID.
     *
     * @param idProduto ID do produto a remover
     * @return true se o produto foi removido, false se não encontrado
     */
    public boolean removerProduto(String idProduto) {
        Produto produto = buscarProduto(idProduto);
        if (produto != null) {
            removerProduto(produto);
            return true;
        }
        return false;
    }

}
