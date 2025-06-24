/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.interpreter;

import com.mycompany.oficina.agendamento.Agendamento;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Miguel
 */
public class ExpressaoE implements Expressao {

    private final Expressao esquerda;
    private final Expressao direita;

    public ExpressaoE(Expressao esquerda, Expressao direita) {
        this.esquerda = esquerda;
        this.direita = direita;
    }

    @Override
    public List<Agendamento> interpreter(ContextoDeBusca contexto) {
        // Pega o resultado da busca do lado esquerdo
        List<Agendamento> resultadoEsquerda = esquerda.interpreter(contexto);
        // Pega o resultado da busca do lado direito
        List<Agendamento> resultadoDireita = direita.interpreter(contexto);

        List<Agendamento> resultadoFinal = new ArrayList<>();

        // Compara as duas listas e fica apenas com os itens que estão em ambas (interseção)
        for (Agendamento ag : resultadoEsquerda) {
            if (resultadoDireita.contains(ag)) {
                resultadoFinal.add(ag);
            }
        }

        return resultadoFinal;
    }
}
