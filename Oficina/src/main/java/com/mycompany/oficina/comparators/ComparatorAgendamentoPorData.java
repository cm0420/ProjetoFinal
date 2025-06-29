package com.mycompany.oficina.comparators;

import com.mycompany.oficina.agendamento.Agendamento;

import java.util.Comparator;

public class ComparatorAgendamentoPorData implements Comparator<Agendamento> {
    @Override
    public int compare(Agendamento o1, Agendamento o2) {
        if (o1.getDataHora().isBefore(o2.getDataHora())) {
            return -1;
        }
        if (o1.getDataHora().isAfter(o2.getDataHora())) {
            return 1;
        }
        return 0;
    }
}
