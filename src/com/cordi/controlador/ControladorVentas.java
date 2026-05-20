package com.cordi.controlador;

import com.cordi.modelo.DetalleVenta;
import com.cordi.modelo.Producto;
import com.cordi.modelo.Venta;
import com.cordi.vista.PanelPuntoVenta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ControladorVentas implements ActionListener {

    private PanelPuntoVenta panelVenta;
    private Venta ventaActual;
    private List<Producto> inventarioTemporal;

    public ControladorVentas(PanelPuntoVenta panelVenta) {
        this.panelVenta = panelVenta;
        this.ventaActual = new Venta();
        
        cargarProductosDePrueba();
        dibujarCuadricula(inventarioTemporal);

        this.panelVenta.btnCobrar.addActionListener(this);
        this.panelVenta.btnCancelar.addActionListener(this);
        this.panelVenta.btnDescuento.addActionListener(this);

        this.panelVenta.txtBuscador.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String texto = panelVenta.txtBuscador.getText().toLowerCase();
                List<Producto> filtrados = new ArrayList<>();
                for (Producto p : inventarioTemporal) {
                    if (p.getNombre().toLowerCase().contains(texto) || p.getCodigo().contains(texto)) {
                        filtrados.add(p);
                    }
                }
                dibujarCuadricula(filtrados);
            }
        });

        this.panelVenta.tablaTicket.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { 
                    int fila = panelVenta.tablaTicket.getSelectedRow();
                    if (fila >= 0) {
                        if (JOptionPane.showConfirmDialog(panelVenta, "¿Quitar producto?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            ventaActual.quitarDelCarrito(fila);
                            actualizarTicket();
                        }
                    }
                }
            }
        });
    }

    private void cargarProductosDePrueba() {
        inventarioTemporal = new ArrayList<>();
        inventarioTemporal.add(new Producto(1, "1001", "Lácteos", "Leche Entera 1L", 25.50, 50, 5, "Sin Imagen"));
        inventarioTemporal.add(new Producto(2, "1002", "Lácteos", "Huevo Blanco 12pz", 32.00, 40, 5, "Sin Imagen"));
        inventarioTemporal.add(new Producto(3, "2001", "Abarrotes", "Aceite Vegetal 900ml", 38.50, 35, 5, "Sin Imagen"));
    }

    private void dibujarCuadricula(List<Producto> listaProductos) {
        panelVenta.panelCuadriculaProductos.removeAll();
        for (Producto p : listaProductos) {
            JPanel tarjeta = new JPanel(new BorderLayout(5, 5));
            tarjeta.setPreferredSize(new Dimension(180, 220)); 
            tarjeta.setBackground(Color.WHITE);
            tarjeta.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10) 
            ));

            JLabel lblImg = new JLabel("FOTO", SwingConstants.CENTER);
            lblImg.setFont(new Font("Arial", Font.PLAIN, 40));
            lblImg.setForeground(Color.LIGHT_GRAY);
            lblImg.setPreferredSize(new Dimension(100, 80));

            JPanel panelInfo = new JPanel(new GridLayout(2, 1));
            panelInfo.setBackground(Color.WHITE);
            
            JLabel lblNombre = new JLabel(p.getNombre(), SwingConstants.CENTER);
            lblNombre.setFont(new Font("Arial", Font.BOLD, 12));
            JLabel lblPrecio = new JLabel("$" + String.format("%.2f", p.getPrecio()), SwingConstants.CENTER);
            lblPrecio.setFont(new Font("Arial", Font.BOLD, 16));
            lblPrecio.setForeground(new Color(37, 99, 235));
            
            panelInfo.add(lblNombre);
            panelInfo.add(lblPrecio);

            JButton btnAgregar = new JButton("+ AGREGAR");
            btnAgregar.setBackground(new Color(37, 99, 235));
            btnAgregar.setForeground(Color.WHITE);
            btnAgregar.setFocusPainted(false);
            btnAgregar.addActionListener(e -> agregarProductoAlTicket(p));

            tarjeta.add(lblImg, BorderLayout.NORTH);
            tarjeta.add(panelInfo, BorderLayout.CENTER);
            tarjeta.add(btnAgregar, BorderLayout.SOUTH);
            panelVenta.panelCuadriculaProductos.add(tarjeta);
        }
        panelVenta.panelCuadriculaProductos.revalidate();
        panelVenta.panelCuadriculaProductos.repaint();
    }

    private void agregarProductoAlTicket(Producto p) {
        String cantString = JOptionPane.showInputDialog(panelVenta, "¿Cuántas piezas?", "1");
        if (cantString != null && !cantString.isEmpty()) {
            try {
                int cantidad = Integer.parseInt(cantString);
                if (cantidad > 0 && cantidad <= p.getStock()) {
                    ventaActual.agregarAlCarrito(new DetalleVenta(p, cantidad));
                    actualizarTicket();
                } else {
                    JOptionPane.showMessageDialog(panelVenta, "Stock insuficiente.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panelVenta, "Ingresa un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actualizarTicket() {
        panelVenta.modeloTabla.setRowCount(0);

        for (DetalleVenta item : ventaActual.getListaProductos()) {
            panelVenta.modeloTabla.addRow(new Object[]{item.getCantidad() + "x", item.getProducto().getNombre(), "$" + item.getSubtotal()});
        }
        panelVenta.lblTotal.setText("$ " + String.format("%.2f", ventaActual.getTotalFinal()));
        panelVenta.lblDescuento.setText("Descuento: -$ " + String.format("%.2f", ventaActual.getDescuento()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == panelVenta.btnCancelar) {
            if (!ventaActual.getListaProductos().isEmpty() && JOptionPane.showConfirmDialog(panelVenta, "¿Cancelar venta?") == JOptionPane.YES_OPTION) {
                ventaActual = new Venta();
                actualizarTicket();
            }
        }
        
        else if (e.getSource() == panelVenta.btnDescuento) {
            if (ventaActual.getListaProductos().isEmpty()) return;
            String descStr = JOptionPane.showInputDialog(panelVenta, "Monto de descuento ($):", "0.00");
            if (descStr != null) {
                try {
                    ventaActual.setDescuento(Double.parseDouble(descStr));
                    actualizarTicket();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panelVenta, "Número inválido.");
                }
            }
        }
        
        else if (e.getSource() == panelVenta.btnCobrar) {
            if (ventaActual.getListaProductos().isEmpty()) return;

            double totalAPagar = ventaActual.getTotalFinal();
            String pagoStr = JOptionPane.showInputDialog(panelVenta, "TOTAL: $" + totalAPagar + "\nEfectivo recibido:");
            
            if (pagoStr != null) {
                try {
                    double efectivo = Double.parseDouble(pagoStr);
                    if (efectivo >= totalAPagar) {
                        double cambio = efectivo - totalAPagar;
                        
                        SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy");
                        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm:ss");
                        
                        StringBuilder ticket = new StringBuilder();
                        ticket.append("===================================\n");
                        ticket.append("       SISTEMA PUNTO DE VENTA      \n");
                        ticket.append("===================================\n");
                        ticket.append("Fecha: ").append(sdfFecha.format(new Date())).append("\n");
                        ticket.append("Hora: ").append(sdfHora.format(new Date())).append("\n");
                        ticket.append("-----------------------------------\n");
                        ticket.append(String.format("%-6s %-15s %-8s\n", "CANT", "PRODUCTO (CÓDIGO)", "IMPORTE"));
                        ticket.append("-----------------------------------\n");
                        
                        for (DetalleVenta item : ventaActual.getListaProductos()) {
                            ticket.append(String.format("%-6s %-15.15s $%-8.2f\n", 
                                item.getCantidad() + "x", 
                                item.getProducto().getNombre(), 
                                item.getSubtotal()));
                            ticket.append("  Cód: ").append(item.getProducto().getCodigo()).append("\n");
                        }
                        
                        ticket.append("-----------------------------------\n");
                        double subtotalBruto = ventaActual.getTotalFinal() + ventaActual.getDescuento();
                        ticket.append(String.format("%-25s $%.2f\n", "Subtotal:", subtotalBruto));
                        ticket.append(String.format("%-25s -$%.2f\n", "Descuento aplicado:", ventaActual.getDescuento()));
                        ticket.append(String.format("%-25s $%.2f\n", "TOTAL A PAGAR:", totalAPagar));
                        ticket.append("-----------------------------------\n");
                        ticket.append(String.format("%-25s $%.2f\n", "Efectivo Recibido:", efectivo));
                        ticket.append(String.format("%-25s $%.2f\n", "Cambio a Entregar:", cambio));
                        ticket.append("===================================\n");
                        ticket.append("      ¡GRACIAS POR SU COMPRA!      \n");

                        JTextArea txtArea = new JTextArea(ticket.toString());
                        txtArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                        txtArea.setEditable(false);
                        JOptionPane.showMessageDialog(panelVenta, new JScrollPane(txtArea), "Impresión de Ticket", JOptionPane.PLAIN_MESSAGE);
                        
                        ventaActual = new Venta();
                        actualizarTicket();
                        
                    } else {
                        JOptionPane.showMessageDialog(panelVenta, "Falta dinero.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panelVenta, "Monto inválido.");
                }
            }
        }
    }
}