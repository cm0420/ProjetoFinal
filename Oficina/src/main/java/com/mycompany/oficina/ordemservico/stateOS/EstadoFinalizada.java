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
public class EstadoFinalizada extends EstadoBaseOS {

    public EstadoFinalizada(OrdemDeServico os) {
        super(os);
    }

    @Override
    public String getStatus() {
        return "Finalizada";
    }

    @Override
    public void cancelar(String motivo) {
        throw new UnsupportedOperationException("Não é possível cancelar uma OS já finalizada.");

    }
}
