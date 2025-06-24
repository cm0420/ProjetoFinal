/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.interpreter;
import com.mycompany.oficina.agendamento.AgendaOficina;
import com.mycompany.oficina.agendamento.Agendamento;
import com.mycompany.oficina.agendamento.DiaAgenda;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Miguel
 */
public class ExpressaoPorData implements Expressao {
      private final LocalDate data;

    public ExpressaoPorData(LocalDate data) {
        this.data = data;
    }

    @Override
    public List<Agendamento> interpreter(ContextoDeBusca contexto) {
        AgendaOficina agenda = contexto.getAgenda();
        List<Agendamento> resultados = new ArrayList<>();

        for (DiaAgenda dia : agenda.getDias()) {
            if (dia.getData().isEqual(this.data)) {
                for (Agendamento ag : dia.getHorarios()) {
                    if (ag != null) {
                        resultados.add(ag);
                    }
                }
                break;
            }
        }
        return resultados;
    }
}
