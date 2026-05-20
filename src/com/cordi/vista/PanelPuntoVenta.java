package com.cordi.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelPuntoVenta extends JPanel {

    public JTextField txtBuscador;
    public JPanel panelCuadriculaProductos; 
    public JTable tablaTicket;
    public DefaultTableModel modeloTabla;
    public JLabel lblTotal;
    public JLabel lblDescuento;
    public JButton btnCobrar, btnCancelar, btnDescuento;

    public PanelPuntoVenta() {
        setLayout(new BorderLayout(20, 20)); 
        setBackground(new Color(245, 247, 250)); 
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 

        // buscador
        JPanel panelArriba = new JPanel(new BorderLayout());
        panelArriba.setBackground(Color.WHITE);
        panelArriba.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel lblLupa = new JLabel("Buscar producto: ");
        lblLupa.setFont(new Font("Arial", Font.BOLD, 14));
        txtBuscador = new JTextField();
        txtBuscador.setFont(new Font("Arial", Font.PLAIN, 16));
        txtBuscador.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        
        panelArriba.add(lblLupa, BorderLayout.WEST);
        panelArriba.add(txtBuscador, BorderLayout.CENTER);
        add(panelArriba, BorderLayout.NORTH);

        // centro
        panelCuadriculaProductos = new JPanel(new GridLayout(0, 3, 15, 15));
        panelCuadriculaProductos.setBackground(new Color(245, 247, 250));

        JPanel contenedorTarjetas = new JPanel(new BorderLayout());
        contenedorTarjetas.setBackground(new Color(245, 247, 250));
        contenedorTarjetas.add(panelCuadriculaProductos, BorderLayout.NORTH);

        JScrollPane scrollProductos = new JScrollPane(contenedorTarjetas);
        scrollProductos.setBorder(null); 
        scrollProductos.getVerticalScrollBar().setUnitIncrement(16); 
        add(scrollProductos, BorderLayout.CENTER);

        // ticket
        JPanel panelTicket = new JPanel(new BorderLayout(10, 10));
        panelTicket.setPreferredSize(new Dimension(380, 0)); // Lo hice un poco más ancho
        panelTicket.setBackground(Color.WHITE);
        panelTicket.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTituloTicket = new JLabel("Ticket de Venta");
        lblTituloTicket.setFont(new Font("Arial", Font.BOLD, 16));
        panelTicket.add(lblTituloTicket, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new String[]{"Cant", "Producto", "Total"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaTicket = new JTable(modeloTabla);
        tablaTicket.setShowGrid(false); 
        tablaTicket.setRowHeight(30);
        tablaTicket.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JScrollPane scrollTicket = new JScrollPane(tablaTicket);
        scrollTicket.getViewport().setBackground(Color.WHITE);
        scrollTicket.setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240)));
        panelTicket.add(scrollTicket, BorderLayout.CENTER);

        // abajo
        JPanel panelCobro = new JPanel(new BorderLayout(5, 10));
        panelCobro.setBackground(Color.WHITE);
        
        JPanel pnlTotales = new JPanel(new GridLayout(2, 1));
        pnlTotales.setBackground(Color.WHITE);
        
        lblDescuento = new JLabel("Descuento: -$ 0.00", SwingConstants.RIGHT);
        lblDescuento.setFont(new Font("Arial", Font.BOLD, 14));
        lblDescuento.setForeground(new Color(220, 38, 38)); 
        
        lblTotal = new JLabel("TOTAL: $ 0.00", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Arial", Font.BOLD, 28));
        lblTotal.setForeground(new Color(37, 99, 235)); 
        
        pnlTotales.add(lblDescuento);
        pnlTotales.add(lblTotal);
        panelCobro.add(pnlTotales, BorderLayout.NORTH);

        JPanel panelBotonesAccion = new JPanel(new GridLayout(1, 3, 5, 0));
        panelBotonesAccion.setBackground(Color.WHITE);

        btnCancelar = new JButton("CANCELAR");
        btnCancelar.setBackground(new Color(220, 38, 38)); 
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);

        btnDescuento = new JButton("DESC %");
        btnDescuento.setBackground(new Color(245, 158, 11));
        btnDescuento.setForeground(Color.WHITE);
        btnDescuento.setFocusPainted(false);

        btnCobrar = new JButton("COBRAR");
        btnCobrar.setBackground(new Color(34, 197, 94)); 
        btnCobrar.setForeground(Color.WHITE);
        btnCobrar.setFocusPainted(false);
        btnCobrar.setPreferredSize(new Dimension(0, 50)); 

        panelBotonesAccion.add(btnCancelar);
        panelBotonesAccion.add(btnDescuento);
        panelBotonesAccion.add(btnCobrar);

        panelCobro.add(panelBotonesAccion, BorderLayout.SOUTH);
        panelTicket.add(panelCobro, BorderLayout.SOUTH);
        
        add(panelTicket, BorderLayout.EAST);
    }
}