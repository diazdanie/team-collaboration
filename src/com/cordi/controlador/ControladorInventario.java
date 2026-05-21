package com.cordi.controlador;

import com.cordi.bd.ConexionBD;
import com.cordi.modelo.Producto;
import com.cordi.vista.PanelInventario;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ControladorInventario implements ActionListener {

    private PanelInventario panelInventario;
    private boolean mostrandoBajoStock = false; 

    public ControladorInventario(PanelInventario panelInventario) {
        this.panelInventario = panelInventario;
        
        cargarProductosDesdeBD("", "Todas");

        this.panelInventario.btnGuardarNuevo.addActionListener(this);
        this.panelInventario.btnActualizar.addActionListener(this);
        this.panelInventario.btnEliminar.addActionListener(this);
        this.panelInventario.btnLimpiar.addActionListener(this);
        this.panelInventario.btnBajoStock.addActionListener(this);
        this.panelInventario.btnVerTodos.addActionListener(this);

        // buscador
        this.panelInventario.txtBuscador.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                aplicarFiltros();
            }
        });

        // filtro por menu
        this.panelInventario.cbFiltroCategoria.addActionListener(e -> aplicarFiltros());

        // llenar formulario al dar clic en la tabla
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

    // metodo que recolecta filtros visuales
    private void aplicarFiltros() {
        String texto = panelInventario.txtBuscador.getText().trim();
        String categoria = panelInventario.cbFiltroCategoria.getSelectedItem().toString();
        cargarProductosDesdeBD(texto, categoria);
    }

    // select dinamico
    private void cargarProductosDesdeBD(String busqueda, String categoria) {
        panelInventario.modeloTabla.setRowCount(0); 
        
        // base de la consulta
        String sql = "SELECT * FROM productos WHERE 1=1";
        
        // si el usuario escribio en el buscador
        if (!busqueda.isEmpty()) {
            sql += " AND (nombre LIKE '%" + busqueda + "%' OR codigo LIKE '%" + busqueda + "%')";
        }
        
        // si eligio una categoria especifica
        if (!categoria.equals("Todas")) {
            sql += " AND categoria LIKE '%" + categoria + "%'";
        }
        
        // si presiono btn de bajo stock
        if (mostrandoBajoStock) {
            sql += " AND stock <= stock_minimo";
        }
        
        sql += " ORDER BY nombre ASC"; // alfabeticamente
        
        try (Connection con = ConexionBD.obtenerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
             
            while (rs.next()) {
                panelInventario.modeloTabla.addRow(new Object[]{
                    rs.getString("codigo"),
                    rs.getString("categoria"),
                    rs.getString("nombre"),
                    "$" + rs.getDouble("precio"),
                    rs.getInt("stock"),
                    rs.getInt("stock_minimo")
                });
            }
        } catch (Exception ex) {
            System.out.println("ERROR al cargar inventario: " + ex.getMessage());
        }
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
        
        // activa modo bajo stock y recarga
        if (e.getSource() == panelInventario.btnBajoStock) {
            mostrandoBajoStock = true;
            aplicarFiltros();
            
            if (panelInventario.modeloTabla.getRowCount() == 0) {
                JOptionPane.showMessageDialog(panelInventario, "Todo bien, ningun producto tiene bajo stock", "Reporte Limpio", JOptionPane.INFORMATION_MESSAGE);
                mostrandoBajoStock = false;
                aplicarFiltros();
            }
        }
        
        // limpia todos los filtros y recarga la tabla original
        else if (e.getSource() == panelInventario.btnVerTodos) {
            mostrandoBajoStock = false;
            panelInventario.txtBuscador.setText("");
            panelInventario.cbFiltroCategoria.setSelectedIndex(0);
            aplicarFiltros();
        }

        else if (e.getSource() == panelInventario.btnLimpiar) {
            limpiarCajas();
        }
        
        // guardar
        else if (e.getSource() == panelInventario.btnGuardarNuevo) {
            try {
                String codigo = panelInventario.txtCodigo.getText();
                String categoria = panelInventario.cbCategoria.getSelectedItem().toString();
                String nombre = panelInventario.txtNombre.getText();
                double precio = Double.parseDouble(panelInventario.txtPrecio.getText());
                int stock = Integer.parseInt(panelInventario.txtStock.getText());
                int min = Integer.parseInt(panelInventario.txtStockMin.getText());
                String rutaImg = codigo + ".png"; 

                if(codigo.isEmpty() || nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(panelInventario, "Llena los datos basicos");
                    return;
                }

                String sql = "INSERT INTO productos (codigo, categoria, nombre, precio, stock, stock_minimo, imagen_ruta) " +
                             "VALUES ('" + codigo + "', '" + categoria + "', '" + nombre + "', " + precio + ", " + stock + ", " + min + ", '" + rutaImg + "')";
                
                if (ConexionBD.ejecutarInstruccion(sql)) {
                    JOptionPane.showMessageDialog(panelInventario, "Producto registrado");
                    aplicarFiltros(); 
                    limpiarCajas();
                } else {
                    JOptionPane.showMessageDialog(panelInventario, "ERROR, verifica que codigo no exista ya", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panelInventario, "Revisa que precio y stock sean numeros", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        // eliminar
        else if (e.getSource() == panelInventario.btnEliminar) {
            String codigoSel = panelInventario.txtCodigo.getText();
            if (!codigoSel.isEmpty()) {
                if (JOptionPane.showConfirmDialog(panelInventario, "Eliminar este producto?") == JOptionPane.YES_OPTION) {
                    String sql = "DELETE FROM productos WHERE codigo = '" + codigoSel + "'";
                    if (ConexionBD.ejecutarInstruccion(sql)) {
                        JOptionPane.showMessageDialog(panelInventario, "Producto eliminado");
                        aplicarFiltros();
                        limpiarCajas();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(panelInventario, "Selecciona un producto de la tabla primero");
            }
        }
        
        // actualizar
        else if (e.getSource() == panelInventario.btnActualizar) {
            String codigoSel = panelInventario.txtCodigo.getText();
            if (!codigoSel.isEmpty() && !panelInventario.txtCodigo.isEditable()) {
                try {
                    String categoria = panelInventario.cbCategoria.getSelectedItem().toString();
                    String nombre = panelInventario.txtNombre.getText();
                    double precio = Double.parseDouble(panelInventario.txtPrecio.getText());
                    int stock = Integer.parseInt(panelInventario.txtStock.getText());
                    int min = Integer.parseInt(panelInventario.txtStockMin.getText());
                    
                    String sql = "UPDATE productos SET categoria = '" + categoria + "', nombre = '" + nombre + 
                                 "', precio = " + precio + ", stock = " + stock + ", stock_minimo = " + min + 
                                 " WHERE codigo = '" + codigoSel + "'";
                                 
                    if (ConexionBD.ejecutarInstruccion(sql)) {
                        JOptionPane.showMessageDialog(panelInventario, "Producto actualizado");
                        aplicarFiltros();
                        limpiarCajas();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panelInventario, "ERROR en los datos numericos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(panelInventario, "Selecciona un producto de la tabla para actualizar");
            }
        }
    }
}