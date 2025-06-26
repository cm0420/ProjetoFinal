package com.mycompany.oficina.sistemaponto;

import com.google.gson.reflect.TypeToken;
import com.mycompany.oficina.entidades.Funcionario;
import com.mycompany.oficina.persistencia.PersistenciaJson;

import java.util.ArrayList;
import java.util.List;

/**
 * Gerencia o registro de ponto dos funcionários, com persistência de dados integrada.
 */
public class GerenciadorPonto {

    private final List<RegistroPonto> todosOsRegistros;
    private final PersistenciaJson persistencia;

    /**
     * Construtor do GerenciadorPonto.
     * @param persistencia A instância do gerenciador de persistência.
     */
    public GerenciadorPonto(PersistenciaJson persistencia) {
        this.persistencia = persistencia;
        // Carrega os registros de ponto do arquivo JSON na inicialização
        this.todosOsRegistros = this.persistencia.carregarLista("pontos", new TypeToken<ArrayList<RegistroPonto>>() {});
    }

    /**
     * Registra a entrada de um funcionário e salva o estado atual.
     * @param funcionario O funcionário que está a bater o ponto.
     * @return O novo RegistroPonto criado, ou null se ele já tiver um ponto em aberto.
     */
    public RegistroPonto baterPontoEntrada(Funcionario funcionario) {
        // Lógica com for loop para verificar se já existe um ponto em aberto
        boolean pontoEmAberto = false;
        for (RegistroPonto registro : todosOsRegistros) {
            if (registro.getFuncionario().equals(funcionario) && registro.isPontoAberto()) {
                pontoEmAberto = true;
                break;
            }
        }

        if (pontoEmAberto) {
            System.out.println("ERRO: " + funcionario.getNome() + " já possui um registro de ponto em aberto.");
            return null;
        }

        RegistroPonto novoRegistro = new RegistroPonto(funcionario);
        this.todosOsRegistros.add(novoRegistro);
        System.out.println("SUCESSO: Ponto de entrada registado para " + funcionario.getNome() + ".");

        // Salva a lista atualizada no arquivo JSON imediatamente
        persistencia.salvarLista("pontos", this.todosOsRegistros);

        return novoRegistro;
    }

    /**
     * Registra a saída de um funcionário, fecha o último ponto em aberto e salva o estado atual.
     * @param funcionario O funcionário que está a bater o ponto.
     * @return O RegistroPonto atualizado, ou null se não houver ponto em aberto.
     */
    public RegistroPonto baterPontoSaida(Funcionario funcionario) {
        // Lógica com for loop para encontrar o último registro aberto
        RegistroPonto registroAberto = null;
        for (RegistroPonto registro : todosOsRegistros) {
            if (registro.getFuncionario().equals(funcionario) && registro.isPontoAberto()) {
                registroAberto = registro;
                break;
            }
        }

        if (registroAberto != null) {
            registroAberto.setDataHoraSaida();


            // Salva a lista atualizada no arquivo JSON imediatamente
            persistencia.salvarLista("pontos", this.todosOsRegistros);

            return registroAberto;
        } else {
            return null;
        }
    }

    /**
     * Lista todos os registos de ponto de um funcionário específico.
     * @param funcionario O funcionário cujos registos devem ser listados.
     * @return Uma lista de RegistroPonto.
     */
    public List<RegistroPonto> getRegistrosPorFuncionario(Funcionario funcionario) {
        // Lógica com for loop para filtrar os registos
        List<RegistroPonto> registrosEncontrados = new ArrayList<>();
        for (RegistroPonto registro : this.todosOsRegistros) {
            if (registro.getFuncionario().equals(funcionario)) {
                registrosEncontrados.add(registro);
            }
        }
        return registrosEncontrados;
    }

    /**
     * Retorna uma cópia de segurança de todos os registos de ponto.
     * @return Uma nova lista contendo todos os registos.
     */
    public List<RegistroPonto> getTodosOsRegistros() {
        return new ArrayList<>(this.todosOsRegistros);
    }
}