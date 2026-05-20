package com.cordi.controlador;

import com.cordi.vista.PanelConfiguracion;
import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorConfiguracion implements ActionListener {

    private PanelConfiguracion panelConfig;

    public ControladorConfiguracion(PanelConfiguracion panelConfig) {
        this.panelConfig = panelConfig;
        
        this.panelConfig.btnGuardarConfig.addActionListener(this);
        this.panelConfig.btnImpresora.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == panelConfig.btnImpresora) {
            // mientras es simulado esto
            if (panelConfig.btnImpresora.isSelected()) {
                panelConfig.btnImpresora.setText("DESCONECTADA");
                panelConfig.btnImpresora.setBackground(new Color(220, 38, 38));
            } else {
                panelConfig.btnImpresora.setText("CONECTADA");
                panelConfig.btnImpresora.setBackground(new Color(34, 197, 94));
            }
        }
        else if (e.getSource() == panelConfig.btnGuardarConfig) {
            if (panelConfig.txtRazonSocial.getText().isEmpty()) {
                JOptionPane.showMessageDialog(panelConfig, "La razón social no puede estar vacía.");
                return;
            }
            
            JOptionPane.showMessageDialog(panelConfig, "Ajustes guardados correctamente.\nLos nuevos tickets saldrán a nombre de: " + panelConfig.txtRazonSocial.getText());
        }
    }
}