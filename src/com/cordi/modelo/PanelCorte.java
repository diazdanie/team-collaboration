package com.cordi.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelCorte extends JPanel {

    public JLabel lblTotalHoy;
    public JLabel lblVentasHoy;
    public JLabel lblFondoCaja;
    public JButton btnCerrarDia;
    public JButton btnFondoCaja;
    
    public JTable tablaHistorial;
    public DefaultTableModel modeloTabla;

    public PanelCorte() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelArriba = new JPanel(new BorderLayout(0, 20));
        panelArriba.setBackground(new Color(245, 247, 250));

        JPanel tarjetaOscura = new JPanel(new BorderLayout(10, 10));
        tarjetaOscura.setBackground(new Color(17, 24, 39));
        tarjetaOscura.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JPanel panelTitulosTarjeta = new JPanel(new BorderLayout());
        panelTitulosTarjeta.setBackground(new Color(17, 24, 39));
        JLabel lblTituloOscuro = new JLabel("VENTAS DEL DÍA");
        lblTituloOscuro.setForeground(Color.LIGHT_GRAY);
        lblTituloOscuro.setFont(new Font("Arial", Font.BOLD, 12));
        
        btnFondoCaja = new JButton("💰 Ingresar Fondo de Caja");
        btnFondoCaja.setBackground(new Color(22, 163, 74));
        btnFondoCaja.setForeground(Color.WHITE);
        btnFondoCaja.setFocusPainted(false);
        
        panelTitulosTarjeta.add(lblTituloOscuro, BorderLayout.WEST);
        panelTitulosTarjeta.add(btnFondoCaja, BorderLayout.EAST);
        tarjetaOscura.add(panelTitulosTarjeta, BorderLayout.NORTH);

        lblTotalHoy = new JLabel("$0.00");
        lblTotalHoy.setForeground(Color.WHITE);
        lblTotalHoy.setFont(new Font("Arial", Font.BOLD, 60));
        tarjetaOscura.add(lblTotalHoy, BorderLayout.CENTER);

        JPanel panelInfoInferior = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        panelInfoInferior.setBackground(new Color(17, 24, 39));

        JPanel infoVentas = crearCuadritoInfo("NO. VENTAS");
        lblVentasHoy = new JLabel("0");
        lblVentasHoy.setForeground(Color.WHITE);
        lblVentasHoy.setFont(new Font("Arial", Font.BOLD, 20));
        infoVentas.add(lblVentasHoy, BorderLayout.SOUTH);

        JPanel infoFondo = crearCuadritoInfo("FONDO DE CAJA INICIAL");
        lblFondoCaja = new JLabel("$0.00");
        lblFondoCaja.setForeground(new Color(156, 163, 175));
        lblFondoCaja.setFont(new Font("Arial", Font.BOLD, 20));
        infoFondo.add(lblFondoCaja, BorderLayout.SOUTH);

        panelInfoInferior.add(infoVentas);
        panelInfoInferior.add(infoFondo);
        tarjetaOscura.add(panelInfoInferior, BorderLayout.SOUTH);

        panelArriba.add(tarjetaOscura, BorderLayout.CENTER);

        btnCerrarDia = new JButton("CERRAR DÍA Y LIMPIAR CAJA");
        btnCerrarDia.setBackground(new Color(220, 38, 38)); 
        btnCerrarDia.setForeground(Color.WHITE);
        btnCerrarDia.setFont(new Font("Arial", Font.BOLD, 18));
        btnCerrarDia.setPreferredSize(new Dimension(0, 60));
        btnCerrarDia.setFocusPainted(false);
        panelArriba.add(btnCerrarDia, BorderLayout.SOUTH);

        add(panelArriba, BorderLayout.NORTH);

        // abajo
        JPanel panelAbajo = new JPanel(new BorderLayout(0, 10));
        panelAbajo.setBackground(new Color(245, 247, 250));
        
        JLabel lblHistorial = new JLabel("Historial de Cortes");
        lblHistorial.setFont(new Font("Arial", Font.BOLD, 18));
        panelAbajo.add(lblHistorial, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new String[]{"Estado", "Fecha y Hora", "No. Ventas", "Ventas Acumuladas", "Fondo de Caja", "TOTAL EN CAJÓN"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaHistorial = new JTable(modeloTabla);
        tablaHistorial.setRowHeight(40); 
        tablaHistorial.setShowGrid(false);
        tablaHistorial.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JScrollPane scrollTabla = new JScrollPane(tablaHistorial);
        scrollTabla.getViewport().setBackground(Color.WHITE);
        add(scrollTabla, BorderLayout.CENTER);

        add(panelAbajo, BorderLayout.CENTER);
    }

    private JPanel crearCuadritoInfo(String titulo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(31, 41, 55)); 
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        JLabel lbl = new JLabel(titulo);
        lbl.setForeground(Color.GRAY);
        lbl.setFont(new Font("Arial", Font.BOLD, 10));
        panel.add(lbl, BorderLayout.NORTH);
        return panel;
    }
}