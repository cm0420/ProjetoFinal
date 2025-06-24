/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.stateOS;

import com.mycompany.oficina.loja.Produto;
import com.mycompany.oficina.ordemservico.OrdemDeServico;
import com.mycompany.oficina.ordemservico.PecaUtilizada;

/**
 *
 * @author Miguel
 */
public class EstadoEmServico extends EstadoBaseOS {
      public EstadoEmServico(OrdemDeServico os) { super(os); }

    @Override
    public void adicionarPeca(Produto produtoDoEstoque, int quantidade) {
        System.out.println("...Tentando adicionar " + quantidade + "x " + produtoDoEstoque.getNome() + " à OS #" + getOs().getNumeroOS());
        
        int estoqueAtual = produtoDoEstoque.getQuantidade();
        if (estoqueAtual >= quantidade) {
            produtoDoEstoque.setQuantidade(estoqueAtual - quantidade);
            PecaUtilizada pecaParaOS = new PecaUtilizada(produtoDoEstoque, quantidade);
            getOs().getListaDePecasUtilizadas().add(pecaParaOS);
            System.out.println("...Peça registrada e estoque atualizado com sucesso!");
        } else {
            System.err.println("!!! FALHA: Estoque insuficiente para a peça '" + produtoDoEstoque.getNome() + "'.");
        }
    }

    @Override
    public void finalizarServico() {
        getOs().setEstado(new EstadoFinalizada(getOs())); // Agora transita direto para Finalizada
    }

    @Override
    public String getStatus() {
        return "Em Serviço";
    }
}
