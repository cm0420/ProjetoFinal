/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.oficina;


import com.mycompany.oficina.application.OficinaAplicattion;




import com.mycompany.oficina.gui.LoginGUI;

import javafx.application.Application;


/**
 * @author Miguel
 */
public class Oficina {
    public static void main(String[] args) {
        OficinaAplicattion.getInstance().verificarECriarAdminPadrao();
        Application.launch(LoginGUI.class, args);
    }
}


