/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.interpreter;

import com.mycompany.oficina.agendamento.Agendamento;

import com.mycompany.oficina.entidades.Cliente;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Miguel
 */
public class ExpressaoPorCliente implements Expressao {
    private final Cliente cliente;

    public ExpressaoPorCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public List<Agendamento> interpreter(ContextoDeBusca contexto) {
        List<Agendamento> resultados = new ArrayList<>();

        // Pega apenas os dias que de fato têm agendamentos, tornando a busca mais focada.
        for (LocalDate data : contexto.getAgenda().getDatasAgendadas()) {
            // Pega os horários daquele dia específico
            for (Agendamento ag : contexto.getAgenda().getHorariosDoDia(data)) {
                if (ag != null && ag.getCliente().equals(this.cliente)) {
                    resultados.add(ag);
                }
            }
        }
        return resultados;
    }
}
