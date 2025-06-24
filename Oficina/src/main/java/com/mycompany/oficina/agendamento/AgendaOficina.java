package com.mycompany.oficina.agendamento;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Gerencia a coleção completa de agendamentos da oficina de forma simples e direta.
 * <p>
 * Esta versão utiliza um Mapa para associar uma data a um vetor de horários,
 * tornando a lógica mais clara, eficiente e eliminando a necessidade de uma
 * classe DiaAgenda intermediária.
 */
public final class AgendaOficina {

    private final int horaInicioManha = 8;
    private final int horaFimManha = 12; // Os horários vão até 11:59
    private final int horaInicioTarde = 14;
    private final int horaFimTarde = 18; // Os horários vão até 17:59

    private final int totalDeHorarios;

    // A estrutura de dados principal agora é um Mapa.
    // Chave: LocalDate (a data da "página" da agenda)
    // Valor: Agendamento[] (o vetor de horários daquele dia)
    private final Map<LocalDate, Agendamento[]> agenda;

    /**
     * Construtor que inicializa a agenda.
     */
    public AgendaOficina() {
        // Calcula o total de slots disponíveis
        int slotsManha = horaFimManha - horaInicioManha; // 12 - 8 = 4 slots
        int slotsTarde = horaFimTarde - horaInicioTarde; // 18 - 14 = 4 slots
        this.totalDeHorarios = slotsManha + slotsTarde;
        this.agenda = new HashMap<>();
    }

    /**
     * Agenda um novo serviço, com validações de horário.
     * @param agendamento O objeto Agendamento a ser adicionado.
     * @return true se o agendamento foi bem-sucedido, false caso contrário.
     */
    public boolean agendar(Agendamento agendamento) {
        LocalDateTime dataHora = agendamento.getDataHora();
        int indice = converterHoraParaIndice(dataHora);

        if (indice == -1) {
            return false;
        }

        LocalDate data = dataHora.toLocalDate();
        Agendamento[] horariosDoDia = agenda.get(data);
        if (horariosDoDia == null) {
            horariosDoDia = new Agendamento[this.totalDeHorarios];
            agenda.put(data, horariosDoDia);
        }
        if (horariosDoDia[indice] != null) {
            return false;
        }

        horariosDoDia[indice] = agendamento;
        System.out.println("SUCESSO: Agendamento realizado para " + data);
        return true;
    }

    /**
     * Busca um agendamento específico em uma data e hora.
     * @return O agendamento encontrado, ou null se o horário estiver vago.
     */
    public Agendamento buscarAgendamento(LocalDateTime dataHora){
        Agendamento[] horariosDoDia = agenda.get(dataHora.toLocalDate());
        if (horariosDoDia == null) {
            return null;
        }
        int indice = converterHoraParaIndice(dataHora);
        if (indice == -1) {
            return null;
        }
        return horariosDoDia[indice];
    }

    /**
     * Retorna os horários de um dia específico.
     * @param data A data a ser consultada.
     * @return Uma cópia do vetor de agendamentos para o dia.
     */
    public Agendamento[] getHorariosDoDia(LocalDate data) {
        Agendamento[] horariosOriginais = agenda.get(data);
        if (horariosOriginais == null) {
            return new Agendamento[this.totalDeHorarios];
        }
        return Arrays.copyOf(horariosOriginais, horariosOriginais.length);
    }

    private int converterHoraParaIndice(LocalDateTime dataHora) {
        int hora = dataHora.getHour();
        int slotsManha = horaFimManha - horaInicioManha;

        if (hora >= horaInicioManha && hora < horaFimManha) {
            return hora - horaInicioManha;
        }

        if (hora >= horaInicioTarde && hora < horaFimTarde) {
            return (hora - horaInicioTarde) + slotsManha;
        }

        return -1; // Horário inválido
    }
}