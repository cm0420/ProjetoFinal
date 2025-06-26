package com.mycompany.oficina.controlador;

import com.google.gson.reflect.TypeToken;
import com.mycompany.oficina.entidades.Entidades;
import com.mycompany.oficina.persistencia.PersistenciaJson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe genérica para gerenciar entidades que implementam a interface 'Entidades'.
 * Agora com persistência de dados automática e integrada.
 * @param <T> O tipo da entidade a ser gerenciada (ex: Cliente, Funcionario).
 */
public abstract class GerenciadorGenerico<T extends Entidades> {

    // A lista agora é populada a partir do arquivo JSON
    private List<T> lista;
    private final PersistenciaJson persistencia;
    private final String entidadeChave; // Ex: "clientes", "funcionarios"
    private final TypeToken<ArrayList<T>> tipoToken;

    /**
     * Construtor do GerenciadorGenérico.
     * @param persistencia A instância do gerenciador de persistência.
     * @param entidadeChave A chave única para esta entidade (usada para nome do arquivo).
     * @param tipoToken O TypeToken que descreve o tipo da lista para o Gson.
     */
    public GerenciadorGenerico(PersistenciaJson persistencia, String entidadeChave, TypeToken<ArrayList<T>> tipoToken) {
        this.persistencia = persistencia;
        this.entidadeChave = entidadeChave;
        this.tipoToken = tipoToken;
        // Carrega os dados do arquivo JSON assim que o gerenciador é criado
        this.lista = carregarDados();
    }

    private List<T> carregarDados() {
        return persistencia.carregarLista(this.entidadeChave, this.tipoToken);
    }

    private void salvarDados() {
        persistencia.salvarLista(this.entidadeChave, this.lista);
    }

    public void adicionar(T item) {
        lista.add(item);
        salvarDados(); // Salva a lista após adicionar um item
    }

    public T buscarPorIdentificador(String identificador) {
        for (T item : lista) {
            if (item.getIdentificador().equals(identificador)) {
                return item;
            }
        }
        return null;
    }

    public boolean removerItemPorIdentificador(String identificador) {
        T itemParaRemover = buscarPorIdentificador(identificador);
        if (itemParaRemover != null) {
            boolean removido = lista.remove(itemParaRemover);
            if (removido) {
                salvarDados(); // Salva a lista se o item foi removido com sucesso
            }
            return removido;
        }
        return false;
    }

    public List<T> listarTodos() {
        // Retorna uma cópia não modificável para segurança
        return Collections.unmodifiableList(lista);
    }

    // Método para permitir que as classes filhas salvem após operações específicas (como edição)
    protected void salvarAlteracoes() {
        salvarDados();
    }
}