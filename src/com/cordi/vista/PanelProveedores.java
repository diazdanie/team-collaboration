package com.cordi.vista;

import javax.swing.*;
import java.awt.*;

public class PanelProveedores extends JPanel {

    public JButton btnAgregarProveedor;
    public JPanel panelCuadricula; 

    public PanelProveedores() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250)); 
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(245, 247, 250));
        
        JPanel panelTitulos = new JPanel(new GridLayout(2, 1));
        panelTitulos.setBackground(new Color(245, 247, 250));
        JLabel lblTitulo = new JLabel("Directorio de Proveedores");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        JLabel lblSub = new JLabel("Gestionar relaciones comerciales, horarios y productos");
        lblSub.setForeground(Color.GRAY);
        panelTitulos.add(lblTitulo);
        panelTitulos.add(lblSub);
        
        panelHeader.add(panelTitulos, BorderLayout.WEST);

        btnAgregarProveedor = new JButton("+ AGREGAR PROVEEDOR");
        btnAgregarProveedor.setBackground(new Color(37, 99, 235));
        btnAgregarProveedor.setForeground(Color.WHITE);
        btnAgregarProveedor.setFont(new Font("Arial", Font.BOLD, 12));
        btnAgregarProveedor.setFocusPainted(false);
        btnAgregarProveedor.setPreferredSize(new Dimension(200, 40));
        
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBtn.setBackground(new Color(245, 247, 250));
        pnlBtn.add(btnAgregarProveedor);
        panelHeader.add(pnlBtn, BorderLayout.EAST);

        add(panelHeader, BorderLayout.NORTH);

        panelCuadricula = new JPanel(new GridLayout(0, 2, 20, 20));
        panelCuadricula.setBackground(new Color(245, 247, 250));

        JPanel contenedorArriba = new JPanel(new BorderLayout());
        contenedorArriba.setBackground(new Color(245, 247, 250));
        contenedorArriba.add(panelCuadricula, BorderLayout.NORTH);

        JScrollPane scrollTarjetas = new JScrollPane(contenedorArriba);
        scrollTarjetas.setBorder(null);
        scrollTarjetas.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollTarjetas, BorderLayout.CENTER);
    }
}