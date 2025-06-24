/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.loja;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mariaa
 */
public class Carrinho {
    private List<Produto> produtos;
    private List<Integer> quantidades;

    public Carrinho() {
    }

    public Carrinho(ArrayList<Produto> produtos, ArrayList<Integer> quantidade) {
        this.produtos = new ArrayList<>();
        this.quantidades = new ArrayList<>();
    }

    public void adicionarProduto(Produto produto, int quantidade) {
        if (produto == null || quantidade <= 0) {
            return;
        }

        int index = produtos.indexOf(produto);
        if (index >= 0) {
            quantidades.set(index, quantidades.get(index) + quantidade);
        } else {
            produtos.add(produto);
            quantidades.add(quantidade);

        }

    }

    public void removerProduto(Produto produto) {
        int index = produtos.indexOf(produto);
        if (index >= 0) {
            produtos.remove(index);
            quantidades.remove(index);

        }
    }

    public double calcularTotal() {
        double total = 0.0;
        for (int i = 0; i < produtos.size(); i++) {
            total += produtos.get(i).getPreco() * quantidades.get(i);
        }
        return total;
    }

    public void exibirCarrinho() {
        if (produtos.isEmpty()) {
            System.out.println("Carrinho vazio");
            return;

        }
        for (int i = 0; i < produtos.size(); i++) {
            Produto p = produtos.get(i);
            int quantidade = quantidades.get(i);
            System.out.println(p.getNome() + " - Quantidade " + quantidade + " Preco R$ " + p.getPreco());
        }
        System.out.println("Total: R$" + calcularTotal());
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public List<Integer> getQuantidade() {
        return quantidades;
    }

    public void limparCarrinho() {
        produtos.clear();
        quantidades.clear();
    }
    
}
