/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.stateOS;

import com.mycompany.oficina.loja.Produto;
import com.mycompany.oficina.ordemservico.OrdemDeServico;

/**
 *
 * @author Miguel
 */
public abstract class EstadoBaseOS implements EstadoOS {
    private final OrdemDeServico os;

    public EstadoBaseOS(OrdemDeServico os) {
        this.os = os;
    }
    
    // MUDANÇA: Criamos um método "getter" para que as classes filhas possam acessar o objeto OS de forma controlada.
    public OrdemDeServico getOs() {
        return this.os;
    }
    
    // O resto da classe permanece igual, delegando para o novo getter se necessário
    private void lancaErro() { throw new UnsupportedOperationException("Operação não permitida no estado atual: " + getStatus()); }
    
    @Override public void iniciarInspecao() { lancaErro(); }
    @Override public void iniciarServico() { lancaErro(); }
    @Override public void adicionarPeca(Produto p, int q) { lancaErro(); }
    @Override public void finalizarServico() { lancaErro(); }
    
    @Override 
    public void cancelar(String motivo) { 
        // Agora usamos o getter para acessar a OS
        getOs().setEstado(new EstadoCancelada(getOs(), motivo)); 
    }
}
