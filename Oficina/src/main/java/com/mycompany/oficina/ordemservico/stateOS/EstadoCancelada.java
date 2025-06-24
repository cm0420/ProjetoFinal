/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.ordemservico.stateOS;

import com.mycompany.oficina.ordemservico.OrdemDeServico;

/**
 *
 * @author Miguel
 */
public class EstadoCancelada extends EstadoBaseOS {

    public EstadoCancelada(OrdemDeServico os, String motivo) {
        super(os);
    }

    @Override
    public String getStatus() {
        return "Cancelada";
    }

    @Override
    public void cancelar(String motivo) {
        throw new UnsupportedOperationException("A OS já está cancelada.");
    }
}
