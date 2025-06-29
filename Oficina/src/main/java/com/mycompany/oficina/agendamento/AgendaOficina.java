package com.mycompany.oficina.agendamento;

import com.google.gson.reflect.TypeToken; // IMPORTAR ISTO
import com.mycompany.oficina.persistencia.PersistenciaJson; // IMPORTAR ISTO
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class AgendaOficina {

    private final int horaInicioManha = 8;
    private final int horaFimManha = 12;
    private final int horaInicioTarde = 14;
    private final int horaFimTarde = 18;
    private final int totalDeHorarios;

    // A estrutura de dados principal agora é um Mapa.
    private Map<LocalDate, Agendamento[]> agenda; // Removido o 'final' para poder carregar do arquivo

    // --- NOVOS ATRIBUTOS PARA PERSISTÊNCIA ---
    private final PersistenciaJson persistencia;
    private final String CHAVE_ARQUIVO = "agenda";

    /**
     * Construtor que inicializa a agenda e integra a persistência.
     */
    public AgendaOficina(PersistenciaJson persistencia) {
        // Calcula o total de slots disponíveis
        int slotsManha = horaFimManha - horaInicioManha;
        int slotsTarde = horaFimTarde - horaInicioTarde;
        this.totalDeHorarios = slotsManha + slotsTarde;

        // --- LÓGICA DE PERSISTÊNCIA ---
        this.persistencia = persistencia;
        // Carrega o mapa do arquivo JSON. Se não existir, cria um novo.
        this.agenda = this.persistencia.carregarMapa(CHAVE_ARQUIVO, new TypeToken<HashMap<LocalDate, Agendamento[]>>() {});
        if (this.agenda == null) {
            this.agenda = new HashMap<>();
        }
    }

    // --- NOVO MÉTODO PARA SALVAR ---
    private void salvarAgenda() {
        this.persistencia.salvarMapa(CHAVE_ARQUIVO, this.agenda);
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
        // getOrDefault garante que um novo array seja criado se for o primeiro agendamento do dia
        Agendamento[] horariosDoDia = agenda.getOrDefault(data, new Agendamento[this.totalDeHorarios]);

        if (horariosDoDia[indice] != null) {
            return false;
        }

        horariosDoDia[indice] = agendamento;
        agenda.put(data, horariosDoDia); // Garante que o mapa seja atualizado

        salvarAgenda(); // <--- PONTO CRÍTICO: SALVA AS ALTERAÇÕES
        System.out.println("SUCESSO: Agendamento realizado e salvo para " + data);
        return true;
    }

    public boolean cancelarAgendamento(Agendamento agendamento) {
        if (agendamento == null) {
            return false;
        }

        LocalDateTime dataHora = agendamento.getDataHora();
        LocalDate data = dataHora.toLocalDate();
        Agendamento[] horariosDoDia = agenda.get(data);

        if (horariosDoDia == null) {
            return false;
        }

        int indice = converterHoraParaIndice(dataHora);
        if (indice == -1 || horariosDoDia[indice] == null) {
            return false;
        }

        if (horariosDoDia[indice].equals(agendamento)) {
            horariosDoDia[indice] = null;
            salvarAgenda(); // <--- PONTO CRÍTICO: SALVA AS ALTERAÇÕES
            return true;
        }

        return false;
    }

    // O restante da classe (getHorariosDoDia, getDatasAgendadas, etc.) permanece igual...

    public Agendamento[] getHorariosDoDia(LocalDate data) {
        Agendamento[] horariosOriginais = agenda.get(data);
        if (horariosOriginais == null) {
            return new Agendamento[this.totalDeHorarios];
        }
        return Arrays.copyOf(horariosOriginais, horariosOriginais.length);
    }

    public Set<LocalDate> getDatasAgendadas() {
        return Collections.unmodifiableSet(agenda.keySet());
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

        return -1;
    }

    public List<Agendamento> listarTodosAgendamentos() {
        List<Agendamento> todos = new ArrayList<>();
        for (Agendamento[] horariosDoDia : agenda.values()) {
            for (Agendamento agendamento : horariosDoDia) {
                if (agendamento != null) {
                    todos.add(agendamento);
                }
            }
        }
        return todos;
    }
}