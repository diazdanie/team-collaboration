package com.cordi.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelInventario extends JPanel {

    public JTextField txtCodigo, txtNombre, txtPrecio, txtStock, txtStockMin;
    public JComboBox<String> cbCategoria; 
    public JButton btnGuardarNuevo, btnActualizar, btnEliminar, btnLimpiar;
    
    public JButton btnBajoStock, btnVerTodos;
    
    public JTable tablaProductos;
    public DefaultTableModel modeloTabla;

    public PanelInventario() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250)); 
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(245, 247, 250));
        JLabel lblTitulo = new JLabel("📦 Inventario General");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        panelHeader.add(lblTitulo, BorderLayout.WEST);
        add(panelHeader, BorderLayout.NORTH);

        // formulario izq
        JPanel panelFormulario = new JPanel(new GridLayout(13, 1, 0, 5));
        panelFormulario.setPreferredSize(new Dimension(300, 0));
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        panelFormulario.add(new JLabel("Código de Barras:"));
        txtCodigo = new JTextField(); panelFormulario.add(txtCodigo);

        panelFormulario.add(new JLabel("Categoría:"));
        cbCategoria = new JComboBox<>(new String[]{"Lácteos", "Abarrotes", "Panadería", "Limpieza", "Bebidas"});
        cbCategoria.setBackground(Color.WHITE); panelFormulario.add(cbCategoria);

        panelFormulario.add(new JLabel("Nombre del Producto:"));
        txtNombre = new JTextField(); panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Precio de Venta ($):"));
        txtPrecio = new JTextField(); panelFormulario.add(txtPrecio);

        JPanel panelStocks = new JPanel(new GridLayout(1, 2, 10, 0));
        panelStocks.setBackground(Color.WHITE);
        
        JPanel pStock = new JPanel(new BorderLayout()); pStock.setBackground(Color.WHITE);
        pStock.add(new JLabel("Stock Actual:"), BorderLayout.NORTH);
        txtStock = new JTextField(); pStock.add(txtStock, BorderLayout.CENTER);
        
        JPanel pMin = new JPanel(new BorderLayout()); pMin.setBackground(Color.WHITE);
        pMin.add(new JLabel("Stock Mínimo:"), BorderLayout.NORTH);
        txtStockMin = new JTextField(); pMin.add(txtStockMin, BorderLayout.CENTER);
        
        panelStocks.add(pStock); panelStocks.add(pMin); panelFormulario.add(panelStocks);

        panelFormulario.add(new JLabel(""));

        btnGuardarNuevo = new JButton("+ Guardar Nuevo");
        btnGuardarNuevo.setBackground(new Color(37, 99, 235)); 
        btnGuardarNuevo.setForeground(Color.WHITE);
        btnGuardarNuevo.setFocusPainted(false); panelFormulario.add(btnGuardarNuevo);

        btnActualizar = new JButton("Actualizar Seleccionado");
        btnActualizar.setBackground(new Color(34, 197, 94)); 
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false); panelFormulario.add(btnActualizar);

        btnLimpiar = new JButton("Limpiar Cajas");
        btnLimpiar.setBackground(Color.LIGHT_GRAY);
        btnLimpiar.setFocusPainted(false); panelFormulario.add(btnLimpiar);

        add(panelFormulario, BorderLayout.WEST);

        // tabla der
        JPanel panelTabla = new JPanel(new BorderLayout(0, 10));
        panelTabla.setBackground(new Color(245, 247, 250));

        // botones de arriba
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelFiltros.setBackground(new Color(245, 247, 250));
        
        btnVerTodos = new JButton("📋 Ver Todos");
        btnVerTodos.setBackground(Color.WHITE);
        btnVerTodos.setFocusPainted(false);
        
        btnBajoStock = new JButton("⚠️ Reporte Bajo Stock");
        btnBajoStock.setBackground(new Color(245, 158, 11));
        btnBajoStock.setForeground(Color.WHITE);
        btnBajoStock.setFocusPainted(false);
        
        panelFiltros.add(btnVerTodos);
        panelFiltros.add(btnBajoStock);
        panelTabla.add(panelFiltros, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new String[]{"Código", "Categoría", "Nombre", "Precio", "Stock", "Mínimo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setRowHeight(30);
        tablaProductos.setShowGrid(false); 
        tablaProductos.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollTabla = new JScrollPane(tablaProductos);
        scrollTabla.getViewport().setBackground(Color.WHITE);
        scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panelTabla.add(scrollTabla, BorderLayout.CENTER);

        btnEliminar = new JButton("Eliminar Producto Seleccionado");
        btnEliminar.setBackground(new Color(220, 38, 38)); 
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setPreferredSize(new Dimension(0, 45));
        btnEliminar.setFocusPainted(false);
        panelTabla.add(btnEliminar, BorderLayout.SOUTH);

        add(panelTabla, BorderLayout.CENTER);
    }
}