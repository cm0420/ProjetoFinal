/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.ordemservico;

import com.mycompany.oficina.loja.Produto;

/**
 *
 * @author Miguel
 */
public class PecaUtilizada {
     private final Produto produtoOriginal; // Referência ao produto do estoque
    private final int quantidadeUtilizada;
    private final double precoNoMomentoDoUso;

    public PecaUtilizada(Produto produtoOriginal, int quantidadeUtilizada) {
        if (produtoOriginal == null) {
            throw new IllegalArgumentException("O produto original não pode ser nulo.");
        }
        this.produtoOriginal = produtoOriginal;
        this.quantidadeUtilizada = quantidadeUtilizada;
        this.precoNoMomentoDoUso = produtoOriginal.getPreco(); // "Congela" o preço
    }

    // Getters
    public Produto getProdutoOriginal() {
        return produtoOriginal;
    }

    public int getQuantidadeUtilizada() {
        return quantidadeUtilizada;
    }

    public double getPrecoNoMomentoDoUso() {
        return precoNoMomentoDoUso;
    }
    
    public double getSubtotal() {
        return this.quantidadeUtilizada * this.precoNoMomentoDoUso;
    }

    @Override
    public String toString() {
        return quantidadeUtilizada + "x " + produtoOriginal.getNome() + " (R$ " + String.format("%.2f", precoNoMomentoDoUso) + " cada)";
    }
}
