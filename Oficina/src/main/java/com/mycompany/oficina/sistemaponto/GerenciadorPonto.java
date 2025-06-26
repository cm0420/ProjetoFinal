package com.mycompany.oficina.sistemaponto;

import com.mycompany.oficina.entidades.Funcionario;

import java.util.ArrayList;
import java.util.List;

public class GerenciadorPonto {

    private final List<RegistroPonto> todosOsRegistros;

    public GerenciadorPonto() {
        this.todosOsRegistros = new ArrayList<>();
    }

    /**
     * Registra a entrada de um funcionário. Cria um novo registro de ponto aberto.
     *
     * @param funcionario O funcionário que está batendo o ponto.
     * @return O novo RegistroPonto criado, ou null se ele já tiver um ponto em aberto.
     */
    public RegistroPonto baterPontoEntrada(Funcionario funcionario) {
        // 1. Primeiro, percorremos TODA a lista para verificar se há um ponto em aberto.
        boolean pontoEmAberto = false;
        for (RegistroPonto registro : todosOsRegistros) {
            if (registro.getFuncionario().equals(funcionario) && registro.isPontoAberto()) {
                pontoEmAberto = true; // Se encontrarmos, marcamos como verdadeiro...
                break;                // ...e paramos a busca.
            }
        }

        // 2. AGORA, FORA do loop, tomamos a decisão com base no resultado.
        if (pontoEmAberto) {
            // Se, após verificar a lista inteira, encontrarmos um ponto aberto...
            System.out.println("ERRO: " + funcionario.getNome() + " já possui um registro de ponto em aberto.");
            return null; // ...então retornamos null e paramos a execução.
        }

        // 3. Se o código chegou até aqui, significa que NENHUM ponto em aberto foi encontrado.
        // Então, podemos registrar a nova entrada com segurança.
        RegistroPonto novoRegistro = new RegistroPonto(funcionario);
        this.todosOsRegistros.add(novoRegistro);
        System.out.println("SUCESSO: Ponto de entrada registrado para " + funcionario.getNome() + ".");
        return novoRegistro; // Retornamos o novo registro criado.
    }

    /**
     * Registra a saída de um funcionário, fechando o último ponto em aberto.
     *
     * @param funcionario O funcionário que está batendo o ponto.
     * @return O RegistroPonto atualizado com a hora de saída, ou null se não houver ponto em aberto.
     */
    public RegistroPonto baterPontoSaida(Funcionario funcionario) {
        // Encontra o último registro de ponto aberto para o funcionário especificado
        RegistroPonto registroAberto = null; // 1. Começamos com um objeto nulo
        for (RegistroPonto registro : todosOsRegistros) { // 2. Passamos por cada registro
            // 3. Verificamos as mesmas duas condições
            if (registro.getFuncionario().equals(funcionario) && registro.isPontoAberto()) {
                registroAberto = registro; // 4. Se encontrarmos, guardamos o objeto
                break; // 5. E paramos a busca (mesma lógica do findFirst)
            }
        }

        if (registroAberto != null) {
            // Não precisamos mais do .get(), usamos o objeto diretamente.
            registroAberto.setDataHoraSaida();
            System.out.println("SUCESSO: Ponto de saída registrado para " + funcionario.getNome() + ".");
            return registroAberto;
        } else {
            System.out.println("ERRO: Nenhum ponto de entrada em aberto encontrado para " + funcionario.getNome() + ".");
            return null;
        }
    }

    /**
     * Lista todos os registros de ponto de um funcionário específico.
     *
     * @param funcionario O funcionário cujos registros devem ser listados.
     * @return Uma lista de RegistroPonto.
     */
    public List<RegistroPonto> getRegistrosPorFuncionario(Funcionario funcionario) {
        // 1. Crie uma nova lista vazia para guardar os resultados.
        List<RegistroPonto> registrosEncontrados = new ArrayList<>();

        // 2. Percorra cada registro na lista principal de todos os registros.
        for (RegistroPonto registro : todosOsRegistros) {
            // 3. Verifique se o funcionário do registro atual é o que estamos procurando.
            if (registro.getFuncionario().equals(funcionario)) {
                // 4. Se for, adicione este registro à nossa lista de resultados.
                registrosEncontrados.add(registro);
            }
        }
        return registrosEncontrados;
    }

    public List<RegistroPonto> getTodosOsRegistros() {
        return new ArrayList<>(todosOsRegistros);
    }
}
