/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.oficina.ordemservico.ObserverOS;

/**
 *
 * @author Miguel
 */
public interface Assunto {
    void adicionarObservador(Observador observador);
    void removerObservador(Observador observador);
    void notificarObservadores();
}
