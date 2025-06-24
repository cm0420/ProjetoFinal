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
public class EstadoEmInspecao extends EstadoBaseOS {
       public EstadoEmInspecao(OrdemDeServico os) {
        super(os);
    }

    @Override
    public void iniciarServico() {
        // MUDANÇA: Em vez de "os.setEstado(...)", usamos "getOs().setEstado(...)"
        getOs().setEstado(new EstadoEmServico(getOs()));
    }

    @Override
    public String getStatus() {
        return "Em Inspeção";
    }
}
