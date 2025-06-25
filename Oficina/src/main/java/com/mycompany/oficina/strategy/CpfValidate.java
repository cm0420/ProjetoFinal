package com.mycompany.oficina.strategy;

public class CpfValidate implements Validate {
    @Override
    public boolean validar(String dado) {
        if (dado == null) return false;
        return dado.replaceAll("\\D", "").length() == 11;
    }

    @Override
    public String getMensagemErro() {
        return "CPF inválido. Por favor, insira 11 dígitos numéricos.";
    }
}
