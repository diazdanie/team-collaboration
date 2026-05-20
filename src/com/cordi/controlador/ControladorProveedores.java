package com.cordi.controlador;

import com.cordi.modelo.Proveedor;
import com.cordi.vista.PanelProveedores;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ControladorProveedores implements ActionListener {

    private PanelProveedores panelProveedores;
    private List<Proveedor> listaProveedores;

    public ControladorProveedores(PanelProveedores panelProveedores) {
        this.panelProveedores = panelProveedores;
        cargarDatosDePrueba();
        dibujarTarjetas();
        this.panelProveedores.btnAgregarProveedor.addActionListener(this);
    }

    private void cargarDatosDePrueba() {
        listaProveedores = new ArrayList<>();
        listaProveedores.add(new Proveedor(1, "Coca-Cola FEMSA", "Carlos Ruiz", "contacto@femsa.com", "Refrescos y Agua", "Lunes y Jueves - 10:00 AM"));
        listaProveedores.add(new Proveedor(2, "Bimbo", "Luis Méndez", "ventas@bimbo.com", "Pan dulce y salado", "Martes - 08:00 AM"));
    }

    private void dibujarTarjetas() {
        panelProveedores.panelCuadricula.removeAll();

        for (Proveedor p : listaProveedores) {
            JPanel tarjeta = new JPanel(new BorderLayout(15, 10));
            tarjeta.setBackground(Color.WHITE);
            tarjeta.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));

            JPanel panelDatos = new JPanel(new GridLayout(5, 1, 0, 5));
            panelDatos.setBackground(Color.WHITE);
            
            JLabel lblEmpresa = new JLabel(p.getNombreEmpresa());
            lblEmpresa.setFont(new Font("Arial", Font.BOLD, 18));
            lblEmpresa.setForeground(new Color(37, 99, 235));
            
            panelDatos.add(lblEmpresa);
            panelDatos.add(new JLabel("👤 Contacto: " + p.getNombreContacto() + " | ✉️ " + p.getEmail()));
            panelDatos.add(new JLabel("📦 Surtido: " + p.getTipoProductos()));
            panelDatos.add(new JLabel("⏰ Horario Visita: " + p.getHorario()));

            JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            panelBotones.setBackground(Color.WHITE);
            
            JButton btnEditar = new JButton("✏️ Modificar");
            btnEditar.setBackground(new Color(243, 244, 246));
            btnEditar.setFocusPainted(false);
            
            JButton btnEliminar = new JButton("🗑 Eliminar");
            btnEliminar.setBackground(new Color(254, 226, 226));
            btnEliminar.setForeground(Color.RED);
            btnEliminar.setFocusPainted(false);
            
            btnEditar.addActionListener(e -> mostrarFormulario(p));
            
            btnEliminar.addActionListener(e -> {
                if (JOptionPane.showConfirmDialog(panelProveedores, "¿Eliminar a " + p.getNombreEmpresa() + "?") == JOptionPane.YES_OPTION) {
                    listaProveedores.remove(p);
                    dibujarTarjetas();
                }
            });

            panelBotones.add(btnEditar);
            panelBotones.add(btnEliminar);

            tarjeta.add(panelDatos, BorderLayout.CENTER);
            tarjeta.add(panelBotones, BorderLayout.SOUTH);

            panelProveedores.panelCuadricula.add(tarjeta);
        }

        panelProveedores.panelCuadricula.revalidate();
        panelProveedores.panelCuadricula.repaint();
    }

    private void mostrarFormulario(Proveedor provAEditar) {
        JTextField txtEmpresa = new JTextField(provAEditar != null ? provAEditar.getNombreEmpresa() : "");
        JTextField txtContacto = new JTextField(provAEditar != null ? provAEditar.getNombreContacto() : "");
        JTextField txtEmail = new JTextField(provAEditar != null ? provAEditar.getEmail() : "");
        JTextField txtProductos = new JTextField(provAEditar != null ? provAEditar.getTipoProductos() : "");
        JTextField txtHorario = new JTextField(provAEditar != null ? provAEditar.getHorario() : "");
        
        if(provAEditar != null) txtEmpresa.setEditable(false);
        
        Object[] campos = {
            "Empresa Proveedora:", txtEmpresa,
            "Nombre del Contacto:", txtContacto,
            "Correo de Atención:", txtEmail,
            "Productos que surten:", txtProductos,
            "Horario/Día de visita:", txtHorario
        };
        
        int opcion = JOptionPane.showConfirmDialog(panelProveedores, campos, provAEditar == null ? "Nuevo Proveedor" : "Editar Proveedor", JOptionPane.OK_CANCEL_OPTION);
        
        if (opcion == JOptionPane.OK_OPTION && !txtEmpresa.getText().isEmpty()) {
            if (provAEditar == null) {
                listaProveedores.add(new Proveedor(listaProveedores.size() + 1, txtEmpresa.getText(), txtContacto.getText(), txtEmail.getText(), txtProductos.getText(), txtHorario.getText()));
            } else {
                provAEditar.setNombreContacto(txtContacto.getText());
                provAEditar.setEmail(txtEmail.getText());
                provAEditar.setTipoProductos(txtProductos.getText());
                provAEditar.setHorario(txtHorario.getText());
            }
            dibujarTarjetas();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == panelProveedores.btnAgregarProveedor) {
            mostrarFormulario(null);
        }
    }
}