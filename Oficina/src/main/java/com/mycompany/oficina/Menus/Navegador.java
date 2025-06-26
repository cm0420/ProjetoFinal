package com.mycompany.oficina.Menus;

import java.util.ArrayDeque;
import java.util.Deque;

public class Navegador {
    private static Navegador instance;
    private final Deque<Menu> pilhaDeMenus = new ArrayDeque<>();


    public static Navegador getInstance() {
        if (instance == null) {
            instance = new Navegador();
        }
        return instance;
    }
    public void navegarPara(Menu menu) {
        pilhaDeMenus.push(menu);
    }
    public void voltarPara() {
        if (!pilhaDeMenus.isEmpty()) {
           Menu menu = pilhaDeMenus.pop();
        }
    }
    public void exibirMenuAtual() {
        if (!this.pilhaDeMenus.isEmpty()) {
            // Usa .peek() para olhar o item do topo sem remove lo
            this.pilhaDeMenus.peek().exibirMenu();
        }
    }
    public boolean pilhaVazia() {
        return pilhaDeMenus.isEmpty();
    }
    public void limparPilha() {
        pilhaDeMenus.clear();
    }
}
