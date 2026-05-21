package com.cordi.vista;

import javax.swing.*;
import java.awt.*;

public class FrmLogin extends JFrame {

    // public para que el controlador las lea luego
    public JTextField txtUsuario;
    public JPasswordField txtPassword; 
    public JButton btnEntrar;

    public FrmLogin() {
        // conf ventana principal
        setTitle("Abarrotes BDDP - Iniciar Sesion");
        setSize(350, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(null);
        panelPrincipal.setBackground(Color.WHITE); 

        JLabel lblTitulo = new JLabel("Abarrotes BDDP", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setBounds(0, 30, 350, 40);
        panelPrincipal.add(lblTitulo);

        JLabel lblSub = new JLabel("Inicia sesion para comenzar", SwingConstants.CENTER);
        lblSub.setForeground(Color.GRAY);
        lblSub.setBounds(0, 70, 350, 20);
        panelPrincipal.add(lblSub);

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setBounds(40, 120, 250, 20);
        panelPrincipal.add(lblUsuario);

        txtUsuario = new JTextField();
        txtUsuario.setBounds(40, 140, 250, 35);
        panelPrincipal.add(txtUsuario);

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setBounds(40, 190, 250, 20);
        panelPrincipal.add(lblPassword);

        txtPassword = new JPasswordField(); 
        txtPassword.setBounds(40, 210, 250, 35);
        panelPrincipal.add(txtPassword);

        btnEntrar = new JButton("Entrar");
        btnEntrar.setBounds(40, 270, 250, 40);
        btnEntrar.setBackground(new Color(37, 99, 235));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnEntrar.setFocusPainted(false); 
        btnEntrar.setBorderPainted(false);
        panelPrincipal.add(btnEntrar);

        add(panelPrincipal);
    }
}