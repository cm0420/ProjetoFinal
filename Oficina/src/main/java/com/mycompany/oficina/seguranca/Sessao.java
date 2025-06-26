package com.mycompany.oficina.seguranca;

import com.mycompany.oficina.entidades.Funcionario;

public class Sessao {
    private static Sessao instance;
    private Funcionario usuarioLogado;

    private Sessao() {
    }

    public static Sessao getInstance() {
        if (instance == null) {
            instance = new Sessao();
        }
        return instance;
    }

    public void login(Funcionario funcionario) {
        usuarioLogado = funcionario;
    }
    public void logout() {
        usuarioLogado = null;
    }
    public boolean IsLogado() {
        return usuarioLogado != null;
    }
    public String getCargoUsuarioLogado() {
        if (IsLogado()) {
            return usuarioLogado.getCargo();
        }
        return null;
    }

    public Funcionario getUsuarioLogado() {
        return usuarioLogado;
    }
}
