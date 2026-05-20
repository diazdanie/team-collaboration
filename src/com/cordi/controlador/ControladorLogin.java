package com.cordi.controlador;

import com.cordi.vista.FrmDashboard;
import com.cordi.vista.FrmLogin;
import com.cordi.modelo.Gestor;
import com.cordi.modelo.Usuario;
import com.cordi.modelo.Vendedor;
import com.cordi.utilidades.ExcepcionGeneral;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorLogin implements ActionListener {

    private FrmLogin vistaLogin;

    public ControladorLogin(FrmLogin vistaLogin) {
        this.vistaLogin = vistaLogin;
        
        this.vistaLogin.btnEntrar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vistaLogin.btnEntrar) {
            
            String usuarioEscrito = vistaLogin.txtUsuario.getText();
            String passwordEscrita = new String(vistaLogin.txtPassword.getPassword());

            try {
                // simulamos usuarios
                Usuario usuarioLogueado = null;

                if (usuarioEscrito.equals("admin") && passwordEscrita.equals("123")) {
                    usuarioLogueado = new Gestor(1, "Administrador", "123");
                } 
                else if (usuarioEscrito.equals("cajero") && passwordEscrita.equals("123")) {
                    usuarioLogueado = new Vendedor(2, "Juan Perez", "123");
                } 
                else {
                    throw new ExcepcionGeneral("Usuario o contraseña incorrectos. Intenta de nuevo.");
                }

                JOptionPane.showMessageDialog(vistaLogin, "¡Bienvenido " + usuarioLogueado.getNombre() + "!");
                
                vistaLogin.dispose();
                
                // abrimos dashboard
                FrmDashboard ventanaDashboard = new FrmDashboard(usuarioLogueado);
                new ControladorDashboard(ventanaDashboard);
                ventanaDashboard.setVisible(true);
                
            } catch (ExcepcionGeneral error) {
                JOptionPane.showMessageDialog(vistaLogin, error.getMessage(), "Error de Acceso", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}