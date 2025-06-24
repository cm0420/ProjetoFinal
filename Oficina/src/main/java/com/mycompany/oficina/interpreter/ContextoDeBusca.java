/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.interpreter;
import com.mycompany.oficina.agendamento.AgendaOficina;
/**
 *
 * @author Miguel
 */
public class ContextoDeBusca {

    private final AgendaOficina agenda;

    public ContextoDeBusca(AgendaOficina agenda) {
        this.agenda = agenda;
    }

    public AgendaOficina getAgenda() {
        return agenda;
    }
}
