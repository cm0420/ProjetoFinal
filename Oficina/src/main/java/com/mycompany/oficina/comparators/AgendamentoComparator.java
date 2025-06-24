/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.comparators;

import com.mycompany.oficina.agendamento.Agendamento;
import java.time.LocalDateTime;
import java.util.Comparator;

/**
 *
 * @author Miguel
 */
public class AgendamentoComparator implements Comparator<Agendamento> {

    @Override
    public int compare(Agendamento o1, Agendamento o2) {
        LocalDateTime data1 = o1.getDataHora();
        LocalDateTime data2 = o2.getDataHora();

        if (data1.isBefore(data2)) {
            return -1;
        }else if (data1.isAfter(data2)) {
            return 1;
        }else{
        return 0;
        }
    }
}
