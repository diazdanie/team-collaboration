package com.cordi.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelTickets extends JPanel {

    public JTable tablaTickets;
    public DefaultTableModel modeloTabla;
    public JButton btnReimprimir, btnExcel, btnFiltrar, btnDevolucion;
    public JTextField txtFechaInicio, txtFechaFin;

    public PanelTickets() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(245, 247, 250));
        
        JLabel lblTitulo = new JLabel("Historial y Devoluciones");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        panelHeader.add(lblTitulo, BorderLayout.WEST);

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelFiltros.setBackground(new Color(245, 247, 250));
        panelFiltros.add(new JLabel("Desde (DD/MM/AAAA):"));
        txtFechaInicio = new JTextField(8);
        panelFiltros.add(txtFechaInicio);
        panelFiltros.add(new JLabel("Hasta:"));
        txtFechaFin = new JTextField(8);
        panelFiltros.add(txtFechaFin);
        
        btnFiltrar = new JButton("Filtrar Fechas");
        btnFiltrar.setBackground(Color.DARK_GRAY);
        btnFiltrar.setForeground(Color.WHITE);
        btnFiltrar.setFocusPainted(false);
        panelFiltros.add(btnFiltrar);
        
        panelHeader.add(panelFiltros, BorderLayout.EAST);
        add(panelHeader, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new String[]{"ID Ticket", "Fecha", "Hora", "Total Venta", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } 
        };
        tablaTickets = new JTable(modeloTabla);
        tablaTickets.setRowHeight(30);
        tablaTickets.setShowGrid(false);
        tablaTickets.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JScrollPane scrollTabla = new JScrollPane(tablaTickets);
        scrollTabla.getViewport().setBackground(Color.WHITE);
        add(scrollTabla, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelBotones.setBackground(new Color(245, 247, 250));

        btnDevolucion = new JButton("🔄 Procesar Devolución");
        btnDevolucion.setBackground(new Color(220, 38, 38));
        btnDevolucion.setForeground(Color.WHITE);
        btnDevolucion.setPreferredSize(new Dimension(200, 40));
        btnDevolucion.setFocusPainted(false);

        btnReimprimir = new JButton("Re-imprimir Ticket");
        btnReimprimir.setBackground(new Color(37, 99, 235)); 
        btnReimprimir.setForeground(Color.WHITE);
        btnReimprimir.setPreferredSize(new Dimension(180, 40));
        btnReimprimir.setFocusPainted(false);

        btnExcel = new JButton("Exportar a Excel");
        btnExcel.setBackground(new Color(34, 197, 94)); 
        btnExcel.setForeground(Color.WHITE);
        btnExcel.setPreferredSize(new Dimension(180, 40));
        btnExcel.setFocusPainted(false);

        panelBotones.add(btnDevolucion);
        panelBotones.add(btnReimprimir);
        panelBotones.add(btnExcel);
        add(panelBotones, BorderLayout.SOUTH);
    }
}