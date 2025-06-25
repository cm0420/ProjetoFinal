package com.mycompany.oficina.strategy;

import javax.xml.validation.Validator;

public class EmailValidate implements Validate {
    @Override
    public boolean validar(String dado) {
        return dado != null && dado.contains("@") && dado.contains(".");
    }

    @Override
    public String getMensagemErro() {
        return "E-mail inválido. O formato parece incorreto.";
    }
}
