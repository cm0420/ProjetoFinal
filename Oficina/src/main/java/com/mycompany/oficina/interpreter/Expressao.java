/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.oficina.interpreter;

import com.mycompany.oficina.agendamento.Agendamento;
import java.util.List;

/**
 * Define a interface comum para todos os elementos de uma expressão de busca.
 * <p>
 * O método chave é o 'interpretar', que contém a lógica para avaliar
 * a expressão e retornar os resultados.
 */
public interface Expressao {
    
    List<Agendamento> interpreter(ContextoDeBusca contexto);
}
