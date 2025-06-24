/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.interpreter;

import com.mycompany.oficina.agendamento.Agendamento;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Miguel
 */
public class ExpressaoOu implements Expressao {

    private final Expressao esquerda;
    private final Expressao direita;

    public ExpressaoOu(Expressao esquerda, Expressao direita) {
        this.esquerda = esquerda;
        this.direita = direita;
    }

    @Override
    public List<Agendamento> interpreter(ContextoDeBusca contexto) {
        // Pega os resultados de ambas as buscas
        List<Agendamento> resultadoEsquerda = esquerda.interpreter(contexto);
        List<Agendamento> resultadoDireita = direita.interpreter(contexto);

        // Usa um Set para juntar as duas listas, garantindo que não haverá duplicatas
        Set<Agendamento> uniao = new HashSet<>(resultadoEsquerda);
        uniao.addAll(resultadoDireita);

        return new ArrayList<>(uniao);
    }
}


