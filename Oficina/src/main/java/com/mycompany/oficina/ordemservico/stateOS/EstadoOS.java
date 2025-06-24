/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.oficina.ordemservico.stateOS;

import com.mycompany.oficina.loja.Produto;

/**
 *
 * @author Miguel
 */
public interface EstadoOS {
    void iniciarInspecao();
    void iniciarServico();
    void adicionarPeca(Produto produtoDoEstoque, int quantidade);
    void finalizarServico();
    void cancelar(String motivo);
    String getStatus();
}
