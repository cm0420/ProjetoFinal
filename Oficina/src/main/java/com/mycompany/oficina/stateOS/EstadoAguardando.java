/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.stateOS;

import com.mycompany.oficina.ordemservico.OrdemDeServico;

/**
 *
 * @author Miguel
 */
public class EstadoAguardando extends EstadoBaseOS{
    public EstadoAguardando(OrdemDeServico os) { super(os); }

    @Override
    public void iniciarInspecao() {
        getOs().setEstado(new EstadoEmInspecao(getOs()));
    }

    @Override
    public String getStatus() {
        return "Aguardando";
    }
}
