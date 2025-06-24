/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.controlador;

import com.mycompany.oficina.entidades.Entidades;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Miguel
 * @param <T>
 */
public class GerenciadorGenerico<T extends Entidades> {

    private final List<T> lista;

    public GerenciadorGenerico() {
        this.lista = new ArrayList<>();
    }

    public void adicionar(T item) {
        lista.add(item);
    }

    public T buscarPorIdentificador(String Identificador) {
        for (T item : lista) {
            if (item.getIdentificador().equals(Identificador)) {
                return item;
            }
        }
        return null;
    }

    public boolean removerItemPorIdentificador(String identificador) {
        T itemParaRemover = buscarPorIdentificador(identificador);
        if (itemParaRemover != null) {
            return lista.remove(itemParaRemover);
        }
        return false;
    }
    public List<T> listarTodos(){
    return Collections.unmodifiableList(lista);
    
    }

}
