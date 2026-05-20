package com.cordi.controlador;

import com.cordi.modelo.Producto;
import com.cordi.vista.PanelInventario;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ControladorInventario implements ActionListener {

    private PanelInventario panelInventario;
    private List<Producto> inventarioTemporal; 

    public ControladorInventario(PanelInventario panelInventario) {
        this.panelInventario = panelInventario;
        cargarProductosDePrueba();
        actualizarTabla();

        this.panelInventario.btnGuardarNuevo.addActionListener(this);
        this.panelInventario.btnActualizar.addActionListener(this);
        this.panelInventario.btnEliminar.addActionListener(this);
        this.panelInventario.btnLimpiar.addActionListener(this);
        
        this.panelInventario.btnBajoStock.addActionListener(this);
        this.panelInventario.btnVerTodos.addActionListener(this);

        this.panelInventario.tablaProductos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = panelInventario.tablaProductos.getSelectedRow();
                if (fila >= 0) {
                    panelInventario.txtCodigo.setText(panelInventario.modeloTabla.getValueAt(fila, 0).toString());
                    panelInventario.cbCategoria.setSelectedItem(panelInventario.modeloTabla.getValueAt(fila, 1).toString());
                    panelInventario.txtNombre.setText(panelInventario.modeloTabla.getValueAt(fila, 2).toString());
                    panelInventario.txtPrecio.setText(panelInventario.modeloTabla.getValueAt(fila, 3).toString().replace("$", ""));
                    panelInventario.txtStock.setText(panelInventario.modeloTabla.getValueAt(fila, 4).toString());
                    panelInventario.txtStockMin.setText(panelInventario.modeloTabla.getValueAt(fila, 5).toString());
                    panelInventario.txtCodigo.setEditable(false);
                }
            }
        });
    }

    private void cargarProductosDePrueba() {
        inventarioTemporal = new ArrayList<>();
        inventarioTemporal.add(new Producto(1, "1001", "Lácteos", "Leche Entera 1L", 25.50, 50, 5, "Sin Imagen"));
        inventarioTemporal.add(new Producto(2, "1002", "Lácteos", "Huevo Blanco 12pz", 32.00, 40, 5, "Sin Imagen"));
        inventarioTemporal.add(new Producto(3, "2001", "Abarrotes", "Aceite Vegetal 900ml", 38.50, 8, 10, "Sin Imagen")); 
    }

    private void actualizarTabla() {
        panelInventario.modeloTabla.setRowCount(0);
        for (Producto p : inventarioTemporal) {
            agregarFila(p);
        }
    }

    private void agregarFila(Producto p) {
        panelInventario.modeloTabla.addRow(new Object[]{p.getCodigo(), p.getCategoria(), p.getNombre(), "$" + p.getPrecio(), p.getStock(), p.getStockMinimo()});
    }

    private void limpiarCajas() {
        panelInventario.txtCodigo.setText("");
        panelInventario.txtNombre.setText("");
        panelInventario.txtPrecio.setText("");
        panelInventario.txtStock.setText("");
        panelInventario.txtStockMin.setText("");
        panelInventario.cbCategoria.setSelectedIndex(0);
        panelInventario.txtCodigo.setEditable(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        // stock bajo rep
        if (e.getSource() == panelInventario.btnBajoStock) {
            panelInventario.modeloTabla.setRowCount(0);
            boolean hayAlertas = false;
            
            for (Producto p : inventarioTemporal) {
                if (p.getStock() <= p.getStockMinimo()) {
                    agregarFila(p);
                    hayAlertas = true;
                }
            }
            
            if (!hayAlertas) {
                JOptionPane.showMessageDialog(panelInventario, "El inventario está sano. Ningún producto tiene bajo stock.", "Reporte Limpio", JOptionPane.INFORMATION_MESSAGE);
                actualizarTabla();
            }
        }
        
        // para ver todos los prod
        else if (e.getSource() == panelInventario.btnVerTodos) {
            actualizarTabla();
        }

        else if (e.getSource() == panelInventario.btnLimpiar) {
            limpiarCajas();
        }
        
        else if (e.getSource() == panelInventario.btnGuardarNuevo) {
            try {
                String codigo = panelInventario.txtCodigo.getText();
                String categoria = panelInventario.cbCategoria.getSelectedItem().toString();
                String nombre = panelInventario.txtNombre.getText();
                double precio = Double.parseDouble(panelInventario.txtPrecio.getText());
                int stock = Integer.parseInt(panelInventario.txtStock.getText());
                int min = Integer.parseInt(panelInventario.txtStockMin.getText());

                inventarioTemporal.add(new Producto(inventarioTemporal.size() + 1, codigo, categoria, nombre, precio, stock, min, "Sin Imagen"));
                actualizarTabla();
                limpiarCajas();
                JOptionPane.showMessageDialog(panelInventario, "Producto agregado correctamente.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panelInventario, "Revisa que los campos de precio y stock sean números.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        else if (e.getSource() == panelInventario.btnEliminar) {
            int fila = panelInventario.tablaProductos.getSelectedRow();
            if (fila >= 0) {
                if (JOptionPane.showConfirmDialog(panelInventario, "¿Seguro que deseas eliminar este producto?") == JOptionPane.YES_OPTION) {
                    inventarioTemporal.remove(fila);
                    actualizarTabla();
                    limpiarCajas();
                }
            } else {
                JOptionPane.showMessageDialog(panelInventario, "Selecciona un producto de la tabla primero.");
            }
        }
        
        else if (e.getSource() == panelInventario.btnActualizar) {
            int fila = panelInventario.tablaProductos.getSelectedRow();
            if (fila >= 0) {
                try {
                    Producto p = inventarioTemporal.get(fila);
                    p.setCategoria(panelInventario.cbCategoria.getSelectedItem().toString());
                    p.setNombre(panelInventario.txtNombre.getText());
                    p.setPrecio(Double.parseDouble(panelInventario.txtPrecio.getText()));
                    p.setStock(Integer.parseInt(panelInventario.txtStock.getText()));
                    p.setStockMinimo(Integer.parseInt(panelInventario.txtStockMin.getText()));
                    
                    actualizarTabla();
                    limpiarCajas();
                    JOptionPane.showMessageDialog(panelInventario, "Producto actualizado correctamente.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panelInventario, "Error en los datos numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(panelInventario, "Selecciona un producto de la tabla para actualizar.");
            }
        }
    }
}