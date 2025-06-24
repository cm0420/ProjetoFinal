/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.interpreter;
import com.mycompany.oficina.agendamento.AgendaOficina;
import com.mycompany.oficina.agendamento.Agendamento;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
       Agendamento[] horariosDoDia = agenda.getHorariosDoDia(data);
       return  Arrays.stream(horariosDoDia)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());


    }
}
