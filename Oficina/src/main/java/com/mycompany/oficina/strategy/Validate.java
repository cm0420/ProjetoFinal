package com.mycompany.oficina.strategy;

public interface Validate {
    boolean validar(String dado);
    String getMensagemErro();
}
