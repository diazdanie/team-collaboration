package com.cordi.main;

import com.cordi.controlador.ControladorLogin;
import com.cordi.vista.FrmLogin;

public class Main {

    public static void main(String[] args) {
        
        // login
        FrmLogin ventanaLogin = new FrmLogin();
        
        // logica login
        ControladorLogin controlador = new ControladorLogin(ventanaLogin);
        
        // visible
        ventanaLogin.setVisible(true);
        
    }
}