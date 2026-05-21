package com.cordi.vista;

import com.cordi.modelo.Usuario;
import javax.swing.*;
import java.awt.*;

public class FrmDashboard extends JFrame {

    public JButton btnVenta, btnInventario, btnTickets, btnProveedores, btnCorte, btnConfig, btnSalir;
    
    public Usuario usuarioActivo;
    
    public JPanel panelDerecho;
    public CardLayout layoutTarjetas;

    public FrmDashboard(Usuario usuarioActivo) {
    	
    	this.usuarioActivo = usuarioActivo;
    	
        setTitle("Abarrotes POS SYSTEM");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // menu lateral
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setPreferredSize(new Dimension(250, 700));
        panelIzquierdo.setBackground(Color.WHITE);
        panelIzquierdo.setLayout(null);
        
        panelIzquierdo.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 220, 220)));

        JLabel lblLogo = new JLabel("SITEMA POS GOD");
        lblLogo.setFont(new Font("Arial", Font.BOLD, 20));
        lblLogo.setForeground(new Color(37, 99, 235));
        lblLogo.setBounds(20, 20, 200, 30);
        panelIzquierdo.add(lblLogo);

        JLabel lblSesion = new JLabel("SESION: " + usuarioActivo.obtenerRol().toUpperCase());
        lblSesion.setFont(new Font("Arial", Font.BOLD, 10));
        lblSesion.setForeground(Color.GRAY);
        lblSesion.setBounds(20, 50, 200, 20);
        panelIzquierdo.add(lblSesion);

        btnVenta = crearBotonMenu("Venta", 90);
        btnInventario = crearBotonMenu("Inventario", 140);
        btnTickets = crearBotonMenu("Tickets", 190);
        btnProveedores = crearBotonMenu("Proveedores", 240);
        btnCorte = crearBotonMenu("Corte", 290);
        btnConfig = crearBotonMenu("Configuracion", 340);

        panelIzquierdo.add(btnVenta);
        panelIzquierdo.add(btnInventario);
        panelIzquierdo.add(btnTickets);
        panelIzquierdo.add(btnProveedores);
        panelIzquierdo.add(btnCorte);
        panelIzquierdo.add(btnConfig);

        btnSalir = new JButton("⬅ Salir");
        btnSalir.setBounds(20, 600, 100, 35);
        btnSalir.setForeground(new Color(220, 38, 38));
        btnSalir.setBackground(Color.WHITE);
        btnSalir.setContentAreaFilled(true); 
        btnSalir.setFocusPainted(false);
        btnSalir.setBorder(BorderFactory.createLineBorder(new Color(220, 38, 38), 1, true)); 
        panelIzquierdo.add(btnSalir);

        // herencia
        if (usuarioActivo.obtenerRol().equals("Vendedor")) {
            btnInventario.setEnabled(false);
            btnProveedores.setEnabled(false);
            btnConfig.setEnabled(false);
        }

        // panel derecho (el que cambia)
        layoutTarjetas = new CardLayout();
        panelDerecho = new JPanel(layoutTarjetas);
        panelDerecho.setBackground(new Color(245, 247, 250));

        add(panelIzquierdo, BorderLayout.WEST);
        add(panelDerecho, BorderLayout.CENTER);
    }

    private JButton crearBotonMenu(String texto, int posY) {
        JButton btn = new JButton(texto);
        btn.setBounds(20, posY, 210, 40);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.DARK_GRAY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        return btn;
    }
}