/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.interpreter;
import com.mycompany.oficina.agendamento.Agendamento;
import com.mycompany.oficina.agendamento.DiaAgenda;
import com.mycompany.oficina.entidades.Cliente;
import java.util.ArrayList;
import java.util.List;
/**
 *
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
        // Itera sobre todos os dias e todos os horários
        for (DiaAgenda dia : contexto.getAgenda().getDias()) {
            for (Agendamento ag : dia.getHorarios()) {
                if (ag != null && ag.getCliente().equals(this.cliente)) {
                    resultados.add(ag);
                }
            }
        }
        return resultados;
    }  
}
