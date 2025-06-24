package com.mycompany.oficina.agendamento;

import com.mycompany.oficina.entidades.Carro;
import com.mycompany.oficina.entidades.Cliente;
import com.mycompany.oficina.entidades.Elevador;
import com.mycompany.oficina.entidades.Funcionario;

import java.time.LocalDateTime;

/**
 * Representa um agendamento de serviço imutável na oficina.
 * <p>
 * Contém todas as informações pertinentes a um serviço, como cliente, veículo,
 * mecânico, tipo de serviço e a data e hora exatas.
 *
 * @author Miguel
 */
public final class Agendamento {

    private final Cliente cliente;
    private final Carro carro;
    private final Funcionario mecanico;
    private final TipoServico tipoServico;
    private final Elevador elevador;
    private final LocalDateTime dataHora;

    /**
     * Constrói uma nova instância de Agendamento.
     */
    public Agendamento(Cliente cliente, Carro carro, Funcionario mecanico, TipoServico tipoServico, Elevador elevador, LocalDateTime dataHora) {
        this.cliente = cliente;
        this.carro = carro;
        this.mecanico = mecanico;
        this.tipoServico = tipoServico;
        this.elevador = elevador;
        this.dataHora = dataHora;
    }

    // Getters para todos os atributos
    public Cliente getCliente() {
        return cliente;
    }

    public Carro getCarro() {
        return carro;
    }

    public Funcionario getMecanico() {
        return mecanico;
    }

    public TipoServico getTipoServico() {
        return tipoServico;
    }

    public Elevador getElevador() {
        return elevador;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }
}