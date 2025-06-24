/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.loja;

/**
 *
 * @author Mariaa
 */
public class Produto {
    private static int contadorId = 1; // Contador estático para gerar IDs únicos
    private String idProduto; // Identificador do produto no formato PR-001
    private String nome; // Nome do produto
    private double preco; // Preço do produto
    private int quantidade; // Quantidade disponível em estoque
    private String fornecedor; // Nome do fornecedor do produto
    
    /**
     * Construtor parametrizado para criar um novo produto.
     * 
     * @param nome Nome do produto
     * @param preco Preço do produto8
     * @param quantidade Quantidade disponível em estoque
     * @param fornecedor
     */
    public Produto(String nome, double preco, int quantidade, String fornecedor) {
        this.idProduto = "PR-" + String.format("%03d", contadorId++); // Gera o ID do produto
        this.nome = nome;
        this.preco = preco;
        this.fornecedor = fornecedor;
        this.quantidade = quantidade;
    }

    /**
     * Construtor padrão para criar um produto sem inicializar os atributos.
     */
    public Produto() { // construtor padrão 
    }
    
    /**
     * Retorna o contador de produtos.
     * 
     * @return O contador de produtos
     */
    public static int getContador() {
        return contadorId;
    }

    
    /**
     * Retorna o ID do produto.
     * 
     * @return O ID do produto
     */
    public String getIdProduto() {
        return idProduto;
    }

    /**
     * Retorna o nome do produto.
     * 
     * @return O nome do produto
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do produto.
     * 
     * @param nome O novo nome do produto
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna o preço do produto.
     * 
     * @return O preço do produto
     */
    public double getPreco() {
        return preco;
    }

    /**
     * Define o preço do produto.
     * 
     * @param preco O novo preço do produto
     */
    public void setPreco(double preco) {
        this.preco = preco;
    }

    /**
     * Retorna a quantidade disponível do produto.
     * 
     * @return A quantidade do produto
     */
    public int getQuantidade() {
        return quantidade;
    }

    /**
     * Define a quantidade disponível do produto.
     * 
     * @param quantidade A nova quantidade do produto
     */
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    /**
     * Retorna o nome do fornecedor do produto.
     * 
     * @return O nome do fornecedor
     */
    public String getFornecedor() {
        return fornecedor;
    }

    /**
     * Define o nome do fornecedor do produto.
     * 
     * @param fornecedor
     */
    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    /**
     * Retorna uma representação em String do produto.
     * 
     * @return Uma String com os detalhes do produto
     */
    @Override
    public String toString() {
        return "Produto{" + "id=" + idProduto + ", nome=" + nome + ", preco=" + preco + ", quantidade=" + quantidade + ", fornecador=" + fornecedor + '}';
    }
}

