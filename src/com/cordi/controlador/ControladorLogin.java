package com.cordi.controlador;

import com.cordi.bd.ConexionBD;
import com.cordi.modelo.Usuario;
import com.cordi.modelo.Gestor;
import com.cordi.modelo.Vendedor;
import com.cordi.vista.FrmLogin;
import com.cordi.vista.FrmDashboard;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ControladorLogin implements ActionListener {

    private FrmLogin vistaLogin;

    public ControladorLogin(FrmLogin vistaLogin) {
        this.vistaLogin = vistaLogin;
        this.vistaLogin.btnEntrar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vistaLogin.btnEntrar) {
            String txtUser = vistaLogin.txtUsuario.getText();
            String txtPass = new String(vistaLogin.txtPassword.getPassword());

            if (txtUser.isEmpty() || txtPass.isEmpty()) {
                JOptionPane.showMessageDialog(vistaLogin, "Por favor llena todos los campos.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // conexion a mysql
            // buscar si existe el usuario y contraseña en la base de datos
            String sql = "SELECT * FROM usuarios WHERE nombre = '" + txtUser + "' AND password = '" + txtPass + "'";
            
            try (Connection con = ConexionBD.obtenerConexion();
                 Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                if (rs.next()) {
                    // si existe, extraemos sus datos de la BD
                    int id = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    String rol = rs.getString("rol");

                    Usuario usuarioLogueado;
                    
                    // polimorfismo según el rol en bd
                    if (rol.equalsIgnoreCase("Gestor")) {
                        usuarioLogueado = new Gestor(id, nombre, txtPass);
                    } else {
                        usuarioLogueado = new Vendedor(id, nombre, txtPass);
                    }

                    JOptionPane.showMessageDialog(vistaLogin, "Bienvenido, " + nombre + " (" + rol + ")!");
                    vistaLogin.dispose();

                    // abri dashboard segun rol
                    FrmDashboard ventanaDashboard = new FrmDashboard(usuarioLogueado);
                    new ControladorDashboard(ventanaDashboard);
                    ventanaDashboard.setVisible(true);

                } else {
                    JOptionPane.showMessageDialog(vistaLogin, "Usuario o contraseña incorrectos", "Error Acceso", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vistaLogin, "ERROR de conexion al servidor local: " + ex.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}