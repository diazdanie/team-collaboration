package com.cordi.controlador;

import com.cordi.bd.ConexionBD;
import com.cordi.modelo.Proveedor;
import com.cordi.vista.PanelProveedores;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ControladorProveedores implements ActionListener {

    private PanelProveedores panelProveedores;
    private List<Proveedor> listaProveedoresBD;

    public ControladorProveedores(PanelProveedores panelProveedores) {
        this.panelProveedores = panelProveedores;
        this.listaProveedoresBD = new ArrayList<>();
        
        cargarProveedoresDesdeBD();
        this.panelProveedores.btnAgregarProveedor.addActionListener(this);
    }

    // select mysql
    private void cargarProveedoresDesdeBD() {
        listaProveedoresBD.clear();
        String sql = "SELECT * FROM proveedores ORDER BY nombre_empresa ASC";
        
        try (Connection con = ConexionBD.obtenerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
             
            while (rs.next()) {
                Proveedor p = new Proveedor(
                    rs.getInt("id_proveedor"),
                    rs.getString("nombre_empresa"),
                    rs.getString("nombre_contacto"),
                    rs.getString("email"),
                    rs.getString("tipo_productos"),
                    rs.getString("horario")
                );
                listaProveedoresBD.add(p);
            }
            dibujarTarjetas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelProveedores, "ERROR al cargar proveedores: " + ex.getMessage());
        }
    }

    private void dibujarTarjetas() {
        panelProveedores.panelCuadricula.removeAll();

        for (Proveedor p : listaProveedoresBD) {
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
            
            // delete sql
            btnEliminar.addActionListener(e -> {
                if (JOptionPane.showConfirmDialog(panelProveedores, "¿Eliminar definitivamente a " + p.getNombreEmpresa() + " de la BD?") == JOptionPane.YES_OPTION) {
                    String sqlDelete = "DELETE FROM proveedores WHERE id_proveedor = " + p.getId();
                    if (ConexionBD.ejecutarInstruccion(sqlDelete)) {
                        cargarProveedoresDesdeBD();
                    } else {
                        JOptionPane.showMessageDialog(panelProveedores, "ERROR al eliminar proveedor.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
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
            
            // insert / update sql
            String sql;
            if (provAEditar == null) {
                sql = "INSERT INTO proveedores (nombre_empresa, nombre_contacto, email, tipo_productos, horario) " +
                      "VALUES ('" + txtEmpresa.getText() + "', '" + txtContacto.getText() + "', '" + txtEmail.getText() + "', '" + txtProductos.getText() + "', '" + txtHorario.getText() + "')";
            } else {
                sql = "UPDATE proveedores SET nombre_contacto = '" + txtContacto.getText() + "', email = '" + txtEmail.getText() + "', " +
                      "tipo_productos = '" + txtProductos.getText() + "', horario = '" + txtHorario.getText() + "' " +
                      "WHERE id_proveedor = " + provAEditar.getId();
            }
            
            if (ConexionBD.ejecutarInstruccion(sql)) {
                JOptionPane.showMessageDialog(panelProveedores, "Datos guardados en MySQL");
                cargarProveedoresDesdeBD();
            } else {
                JOptionPane.showMessageDialog(panelProveedores, "ERROR al guardar en BD", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == panelProveedores.btnAgregarProveedor) {
            mostrarFormulario(null); 
        }
    }
}