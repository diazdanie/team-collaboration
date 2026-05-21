package com.cordi.controlador;

import com.cordi.bd.ConexionBD;
import com.cordi.vista.PanelConfiguracion;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ControladorConfiguracion implements ActionListener {

    private PanelConfiguracion panelConfig;

    public ControladorConfiguracion(PanelConfiguracion panelConfig) {
        this.panelConfig = panelConfig;
        
        cargarDatosDesdeBD();
        
        // conectamos btn de guardar y el de la impresora
        this.panelConfig.btnGuardarConfig.addActionListener(this); 
        this.panelConfig.btnImpresora.addActionListener(this);
    }

    // select
    private void cargarDatosDesdeBD() {
        // solo sacamos el nombre para ponerlo en txtRazonSocial
        String sql = "SELECT nombre FROM configuracion WHERE id = 1";
        
        try (Connection con = ConexionBD.obtenerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
             
            if (rs.next()) {
                panelConfig.txtRazonSocial.setText(rs.getString("nombre"));
            }
        } catch (Exception ex) {
            System.out.println("ERROR al cargar configuracion: " + ex.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        // guardar en mysql
        if (e.getSource() == panelConfig.btnGuardarConfig) {
            String razonSocial = panelConfig.txtRazonSocial.getText();

            // validamos que no este en blanco
            if (razonSocial.isEmpty()) {
                JOptionPane.showMessageDialog(panelConfig, "La Razón Social no puede estar vacía.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // actualizar columna nombre en mysql
            String sql = "UPDATE configuracion SET nombre = '" + razonSocial + "' WHERE id = 1";

            if (ConexionBD.ejecutarInstruccion(sql)) {
                JOptionPane.showMessageDialog(panelConfig, "Razon Social actualizada", "Configuracion Guardada", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosDesdeBD(); 
            } else {
                JOptionPane.showMessageDialog(panelConfig, "ERROR al guardar en la BD", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        // boton impresora (visual solamente)
        else if (e.getSource() == panelConfig.btnImpresora) {
            if (panelConfig.btnImpresora.isSelected()) {
                panelConfig.btnImpresora.setText("DESCONECTADA");
                panelConfig.btnImpresora.setBackground(new Color(220, 38, 38));
            } else {
                panelConfig.btnImpresora.setText("CONECTADA");
                panelConfig.btnImpresora.setBackground(new Color(34, 197, 94));
            }
        }
    }
}