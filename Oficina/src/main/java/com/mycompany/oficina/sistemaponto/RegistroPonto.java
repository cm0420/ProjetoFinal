package com.mycompany.oficina.sistemaponto;

import com.mycompany.oficina.entidades.Funcionario;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RegistroPonto {
    private final Funcionario funcionario;
    private final LocalDateTime dataHoraEntrada;
    private LocalDateTime dataHoraSaida;

    public RegistroPonto(Funcionario funcionario) {
        this.funcionario = funcionario;
        this.dataHoraEntrada = LocalDateTime.now(); // Marca a entrada no momento da criação
        this.dataHoraSaida = null; // A saída ainda não foi registrada
    }

    public LocalDateTime getDataHoraSaida() {
        return dataHoraSaida;
    }

    public LocalDateTime getDataHoraEntrada() {
        return dataHoraEntrada;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setDataHoraSaida() {
        this.dataHoraSaida = LocalDateTime.now();
    }
    public double getHorasTrabalhadas() {
        if (dataHoraSaida == null) {
            return 0;
        }
        // Calcula a duração entre a entrada e a saída
        Duration duracao = Duration.between(dataHoraEntrada, dataHoraSaida);
        // Converte a duração para horas                                                                                                                                     (ex: 8.    5 para 8 horas e 30 minutos)
        return duracao.toMinutes() / 60.0;
    }

    public boolean isPontoAberto() {
        return this.dataHoraSaida == null;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String entradaFormatada = formatter.format(dataHoraEntrada);
        String saidaFormatada = (dataHoraSaida != null) ? formatter.format(dataHoraSaida) : "PONTO EM ABERTO";

        return String.format("Funcionário: %s | Entrada: %s | Saída: %s | Horas: %.2f",
                funcionario.getNome(),
                entradaFormatada,
                saidaFormatada,
                getHorasTrabalhadas()
        );
    }
}
