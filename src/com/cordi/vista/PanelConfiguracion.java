package com.cordi.vista;

import javax.swing.*;
import java.awt.*;

public class PanelConfiguracion extends JPanel {

    public JTextField txtRazonSocial;
    public JToggleButton btnImpresora;
    public JButton btnGuardarConfig;

    public PanelConfiguracion() {
        setLayout(null);
        setBackground(new Color(245, 247, 250)); 

        JPanel panelCentro = new JPanel(null);
        panelCentro.setBounds(50, 50, 500, 300);
        panelCentro.setBackground(Color.WHITE);
        panelCentro.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitulo = new JLabel("Preferencias del Sistema");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setBounds(30, 20, 400, 30);
        panelCentro.add(lblTitulo);

        JLabel lblImpTitulo = new JLabel("Impresora de Tickets");
        lblImpTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblImpTitulo.setBounds(30, 80, 200, 20);
        panelCentro.add(lblImpTitulo);

        JLabel lblImpSub = new JLabel("Detectada: Nose");
        lblImpSub.setFont(new Font("Arial", Font.PLAIN, 12));
        lblImpSub.setForeground(Color.GRAY);
        lblImpSub.setBounds(30, 100, 200, 20);
        panelCentro.add(lblImpSub);

        btnImpresora = new JToggleButton("CONECTADA");
        btnImpresora.setBackground(new Color(34, 197, 94));
        btnImpresora.setForeground(Color.WHITE);
        btnImpresora.setFocusPainted(false);
        btnImpresora.setBounds(320, 85, 130, 30);
        panelCentro.add(btnImpresora);

        JLabel lblRazon = new JLabel("Razon Social (Aparecera en el ticket):");
        lblRazon.setFont(new Font("Arial", Font.BOLD, 12));
        lblRazon.setForeground(Color.GRAY);
        lblRazon.setBounds(30, 150, 300, 20);
        panelCentro.add(lblRazon);

        txtRazonSocial = new JTextField("La Bodega De Don Papu S.A. de C.V.");
        txtRazonSocial.setFont(new Font("Arial", Font.BOLD, 14));
        txtRazonSocial.setBackground(new Color(245, 247, 250));
        txtRazonSocial.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        txtRazonSocial.setBounds(30, 175, 420, 40);
        panelCentro.add(txtRazonSocial);

        btnGuardarConfig = new JButton("GUARDAR CAMBIOS");
        btnGuardarConfig.setBackground(new Color(37, 99, 235));
        btnGuardarConfig.setForeground(Color.WHITE);
        btnGuardarConfig.setFont(new Font("Arial", Font.BOLD, 14));
        btnGuardarConfig.setBounds(30, 240, 420, 40);
        panelCentro.add(btnGuardarConfig);

        add(panelCentro);
    }
}