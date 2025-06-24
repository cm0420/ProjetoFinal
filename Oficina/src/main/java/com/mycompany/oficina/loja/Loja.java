/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.loja;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 *
 * @author Mariaa
 */
public class Loja {
        private final String nome;
    private final Estoque estoque;
    private static int contadorRecibos = 1;

    /**
     * Construtor da Loja.
     *
     * @param nome Nome da loja.
     * @param estoque O estoque inicial da loja.
     */
    public Loja(String nome, Estoque estoque) {
        this.nome = nome;
        this.estoque = estoque;
    }

    /**
     * Adiciona um item ao carrinho do cliente, validando a disponibilidade no
     * estoque.
     *
     * @param carrinho O carrinho de compras do cliente.
     * @param idProduto O ID do produto a ser adicionado.
     * @param quantidade A quantidade desejada.
     * @return true se o produto foi adicionado com sucesso, false caso contrário.
     */
    public boolean adicionarAoCarrinho(Carrinho carrinho, String idProduto, int quantidade) {
        Produto produtoEmEstoque = estoque.buscarProduto(idProduto);

        if (produtoEmEstoque == null) {
           /* System.err.println("Erro: Produto com ID " + idProduto + " não encontrado.");*/
            return false;
        }

        if (produtoEmEstoque.getQuantidade() < quantidade) {
            /*System.err.println("Erro: Estoque insuficiente para o produto '" + produtoEmEstoque.getNome() + "'.");
            System.err.println("Disponível: " + produtoEmEstoque.getQuantidade() + ", Solicitado: " + quantidade);*/
            return false;
        }

        carrinho.adicionarProduto(produtoEmEstoque, quantidade);
       /* System.out.println(quantidade + "x '" + produtoEmEstoque.getNome() + "' adicionado(s) ao carrinho.");*/
        return true;
    }

    /**
     * Finaliza a compra, atualiza as quantidades no estoque e limpa o carrinho.
     * A operação só é concluída se todos os produtos no carrinho tiverem estoque
     * suficiente.
     *
     * @param carrinho O carrinho com os itens da compra.
     * @return true se a compra foi finalizada com sucesso, false se houve falha
     * (ex: falta de estoque).
     */
    public boolean finalizarCompra(Carrinho carrinho) {
        // 1. Validar se todos os produtos no carrinho ainda têm estoque
        for (int i = 0; i < carrinho.getProdutos().size(); i++) {
            Produto produtoNoCarrinho = carrinho.getProdutos().get(i);
            int quantidadeDesejada = carrinho.getQuantidade().get(i);
            Produto produtoEmEstoque = estoque.buscarProduto(produtoNoCarrinho.getIdProduto());

            if (produtoEmEstoque == null || produtoEmEstoque.getQuantidade() < quantidadeDesejada) {
               /* System.err.println("Falha ao finalizar a compra: Estoque insuficiente para o produto '"
                        + produtoNoCarrinho.getNome() + "'.");*/
                return false; // Falha na transação
            }
        }

        // 2. Se a validação passou, atualizar o estoque
        for (int i = 0; i < carrinho.getProdutos().size(); i++) {
            Produto produtoNoCarrinho = carrinho.getProdutos().get(i);
            int quantidadeComprada = carrinho.getQuantidade().get(i);
            Produto produtoEmEstoque = estoque.buscarProduto(produtoNoCarrinho.getIdProduto());

            int novaQuantidade = produtoEmEstoque.getQuantidade() - quantidadeComprada;
            produtoEmEstoque.setQuantidade(novaQuantidade);
        }

       /* System.out.println("\nCompra finalizada com sucesso!");*/
        return true;
    }

    /**
     * Gera uma string formatada como um recibo de compra.
     *
     * @param carrinho O carrinho cuja compra foi finalizada.
     * @return Uma string representando o recibo.
     */
    public String gerarRecibo(Carrinho carrinho) {
      /*  StringBuilder recibo = new StringBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime agora = LocalDateTime.now();

        recibo.append("========================================\n");
        recibo.append("            RECIBO DE COMPRA\n");
        recibo.append("Loja: ").append(this.nome).append("\n");
        recibo.append("Recibo Nº: ").append(String.format("%04d", contadorRecibos++)).append("\n");
        recibo.append("Data: ").append(dtf.format(agora)).append("\n");
        recibo.append("----------------------------------------\n");
        recibo.append("Item        Qtd.    Preço Un.    Subtotal\n");
        recibo.append("----------------------------------------\n");

        for (int i = 0; i < carrinho.getProdutos().size(); i++) {
            Produto p = carrinho.getProdutos().get(i);
            int qtd = carrinho.getQuantidade().get(i);
            double subtotal = p.getPreco() * qtd;
            recibo.append(String.format("%-12.12s %-6d R$%-11.2f R$%-8.2f\n",
                    p.getNome(), qtd, p.getPreco(), subtotal));
        }

        recibo.append("----------------------------------------\n");
        recibo.append(String.format("TOTAL A PAGAR: R$ %.2f\n", carrinho.calcularTotal()));
        recibo.append("========================================\n");
        recibo.append("      Obrigado pela sua preferência!    \n");
        recibo.append("========================================\n");*/

        return carrinho.toString();
    }
    
    /**
     * Exibe todos os produtos disponíveis no estoque.
     */
    public void exibirEstoque() {
        System.out.println("\n--- ESTOQUE ATUAL DA LOJA: " + this.nome + " ---");
        ArrayList<Produto> produtos = estoque.listarProdutos();
        if (produtos.isEmpty()) {
            System.out.println("O estoque está vazio.");
        } else {
            for (Produto p : produtos) {
                System.out.printf("ID: %s | Produto: %-15s | Qtd: %-4d | Preço: R$ %.2f\n", 
                    p.getIdProduto(), p.getNome(), p.getQuantidade(), p.getPreco());
            }
        }
        System.out.println("----------------------------------------");
}
}
