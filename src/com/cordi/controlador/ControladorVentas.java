package com.cordi.controlador;

import com.cordi.bd.ConexionBD;
import com.cordi.modelo.DetalleVenta;
import com.cordi.modelo.Producto;
import com.cordi.modelo.Venta;
import com.cordi.vista.PanelPuntoVenta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.HashMap;
import java.util.Map;

public class ControladorVentas implements ActionListener {

	private Map<String, ImageIcon> cacheImagenes = new HashMap<>();
    private PanelPuntoVenta panelVenta;
    private Venta ventaActual;
    private List<Producto> listaProductosBD; // cargar productos de mysql

    public ControladorVentas(PanelPuntoVenta panelVenta) {
        this.panelVenta = panelVenta;
        this.ventaActual = new Venta();
        this.listaProductosBD = new ArrayList<>();
        
        cargarProductosDesdeBD(""); // cargar todo al iniciar
        
        this.panelVenta.btnCobrar.addActionListener(this);
        this.panelVenta.btnCancelar.addActionListener(this);
        this.panelVenta.btnDescuento.addActionListener(this);

        // buscador inteligente en bd
        this.panelVenta.txtBuscador.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String texto = panelVenta.txtBuscador.getText().trim();
                cargarProductosDesdeBD(texto);
            }
        });

        this.panelVenta.tablaTicket.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { 
                    int fila = panelVenta.tablaTicket.getSelectedRow();
                    if (fila >= 0) {
                        if (JOptionPane.showConfirmDialog(panelVenta, "Quitar producto del carrito?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            ventaActual.quitarDelCarrito(fila);
                            actualizarTicketVisual();
                        }
                    }
                }
            }
        });
    }

    // select con filtro opcional
    private void cargarProductosDesdeBD(String filtro) {
        listaProductosBD.clear();
        String sql = "SELECT * FROM productos";
        
        // si usuario escribe algp, filtramos por nombre o cod
        if (!filtro.isEmpty()) {
            sql += " WHERE nombre LIKE '%" + filtro + "%' OR codigo LIKE '%" + filtro + "%'";
        }
        sql += " ORDER BY categoria ASC, nombre ASC";

        try (Connection con = ConexionBD.obtenerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
             
            while (rs.next()) {
                listaProductosBD.add(new Producto(
                    rs.getInt("id"),
                    rs.getString("codigo"),
                    rs.getString("categoria"),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getInt("stock"),
                    rs.getInt("stock_minimo"),
                    rs.getString("imagen_ruta")
                ));
            }
            dibujarCuadricula(listaProductosBD);
        } catch (Exception ex) {
            System.out.println("ERROR al filtrar productos: " + ex.getMessage());
        }
    }

    private void dibujarCuadricula(java.util.List<Producto> listaProductos) {
        panelVenta.panelCuadriculaProductos.removeAll();
        for (Producto p : listaProductos) {
            JPanel tarjeta = new JPanel(new BorderLayout(5, 5));
            tarjeta.setPreferredSize(new Dimension(180, 220)); 
            tarjeta.setBackground(Color.WHITE);
            tarjeta.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10) 
            ));

            JLabel lblImg = new JLabel();
            lblImg.setHorizontalAlignment(SwingConstants.CENTER);
            lblImg.setPreferredSize(new Dimension(100, 80));
            
            // porque tardaba mucho en cargar las imagenes
            if (cacheImagenes.containsKey(p.getCodigo())) {
                lblImg.setIcon(cacheImagenes.get(p.getCodigo()));
            } else {
                File archivoFoto = new File("img/" + p.getCodigo() + ".jpg");
                if (!archivoFoto.exists()) {
                    archivoFoto = new File("img/" + p.getCodigo() + ".jpeg");
                }
                if (!archivoFoto.exists()) {
                    archivoFoto = new File("img/" + p.getCodigo() + ".png");
                }
                
                if (archivoFoto.exists()) {
                    ImageIcon icono = new ImageIcon(archivoFoto.getAbsolutePath());
                    // usamos SCALE_FAST para que no se trabe la pc gamer
                    Image imgEscalada = icono.getImage().getScaledInstance(80, 80, Image.SCALE_FAST);
                    ImageIcon iconoFinal = new ImageIcon(imgEscalada);
                    
                    lblImg.setIcon(iconoFinal);
                    
                    // guardamos en cache para no tener que volver a escalarla denuevo
                    cacheImagenes.put(p.getCodigo(), iconoFinal);
                } else {
                    lblImg.setText("FOTO: " + p.getCodigo());
                    lblImg.setForeground(Color.LIGHT_GRAY);
                    lblImg.setFont(new Font("Arial", Font.BOLD, 12));
                }
            }

            JPanel panelInfo = new JPanel(new GridLayout(2, 1));
            panelInfo.setBackground(Color.WHITE);
            
            JLabel lblNombre = new JLabel(p.getNombre(), SwingConstants.CENTER);
            lblNombre.setFont(new Font("Arial", Font.BOLD, 11));
            JLabel lblPrecio = new JLabel("$" + String.format("%.2f", p.getPrecio()), SwingConstants.CENTER);
            lblPrecio.setFont(new Font("Arial", Font.BOLD, 15));
            lblPrecio.setForeground(new Color(37, 99, 235));
            
            panelInfo.add(lblNombre);
            panelInfo.add(lblPrecio);

            JButton btnAgregar = new JButton("+ AGREGAR");
            btnAgregar.setBackground(new Color(37, 99, 235));
            btnAgregar.setForeground(Color.WHITE);
            btnAgregar.setFocusPainted(false);
            
            if (p.getStock() <= 0) {
                btnAgregar.setText("AGOTADO");
                btnAgregar.setBackground(Color.LIGHT_GRAY);
                btnAgregar.setEnabled(false);
            } else {
                btnAgregar.addActionListener(e -> agregarProductoAlTicket(p));
            }

            tarjeta.add(lblImg, BorderLayout.NORTH);
            tarjeta.add(panelInfo, BorderLayout.CENTER);
            tarjeta.add(btnAgregar, BorderLayout.SOUTH);
            panelVenta.panelCuadriculaProductos.add(tarjeta);
        }
        panelVenta.panelCuadriculaProductos.revalidate();
        panelVenta.panelCuadriculaProductos.repaint();
    }

    private void agregarProductoAlTicket(Producto p) {
        String cantString = JOptionPane.showInputDialog(panelVenta, "DISPONIBLES: " + p.getStock() + " pzas.\nCuantas piezas de " + p.getNombre() + "?", "1");
        if (cantString != null && !cantString.isEmpty()) {
            try {
                int cantidad = Integer.parseInt(cantString);
                if (cantidad > 0 && cantidad <= p.getStock()) {
                    ventaActual.agregarAlCarrito(new DetalleVenta(p, cantidad));
                    actualizarTicketVisual();
                } else {
                    JOptionPane.showMessageDialog(panelVenta, "No puedes vender mas del stock disponible (" + p.getStock() + " pzas).", "Stock Insuficiente", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panelVenta, "Cantidad invalida");
            }
        }
    }

    private void actualizarTicketVisual() {
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
            if (!ventaActual.getListaProductos().isEmpty() && JOptionPane.showConfirmDialog(panelVenta, "Cancelar venta actual?") == JOptionPane.YES_OPTION) {
                ventaActual = new Venta();
                actualizarTicketVisual();
            }
        }
        else if (e.getSource() == panelVenta.btnDescuento) {
            if (ventaActual.getListaProductos().isEmpty()) return;
            String descStr = JOptionPane.showInputDialog(panelVenta, "Monto de descuento ($):", "0.00");
            if (descStr != null) {
                try {
                    ventaActual.setDescuento(Double.parseDouble(descStr));
                    actualizarTicketVisual();
                } catch (Exception ex) { }
            }
        }
        
        // proceso cobro real en mysql
        else if (e.getSource() == panelVenta.btnCobrar) {
            if (ventaActual.getListaProductos().isEmpty()) return;

            double totalAPagar = ventaActual.getTotalFinal();
            String pagoStr = JOptionPane.showInputDialog(panelVenta, "TOTAL A COBRAR: $" + totalAPagar + "\nEfectivo recibido:");
            
            if (pagoStr != null) {
                try {
                    double efectivo = Double.parseDouble(pagoStr);
                    if (efectivo >= totalAPagar) {
                        double cambio = efectivo - totalAPagar;
                        
                        SimpleDateFormat sdfFecha = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm:ss");
                        String fHoy = sdfFecha.format(new Date());
                        String hHoy = sdfHora.format(new Date());

                        // guardar ticket gral en ventas
                        String sqlVenta = "INSERT INTO ventas (fecha, hora, descuento, total_final, estado) " +
                                          "VALUES ('" + fHoy + "', '" + hHoy + "', " + ventaActual.getDescuento() + ", " + totalAPagar + ", 'Completado')";
                        
                        try (Connection con = ConexionBD.obtenerConexion();
                             Statement stmt = con.createStatement()) {
                            
                            // insertar venta
                            stmt.executeUpdate(sqlVenta, Statement.RETURN_GENERATED_KEYS);
                            
                            // obtener id que mysql le dio al ticket
                            ResultSet rsKeys = stmt.getGeneratedKeys();
                            int idVentaGenerado = 0;
                            if (rsKeys.next()) {
                                idVentaGenerado = rsKeys.getInt(1);
                            }

                            // insertar producto en detalle_ventas y actualizar el stock
                            for (DetalleVenta item : ventaActual.getListaProductos()) {
                                String sqlDetalle = "INSERT INTO detalle_ventas (id_venta, id_producto, cantidad, subtotal) " +
                                                    "VALUES (" + idVentaGenerado + ", " + item.getProducto().getId() + ", " + item.getCantidad() + ", " + item.getSubtotal() + ")";
                                stmt.executeUpdate(sqlDetalle);

                                // restar del stock en la tabla productos
                                String sqlUpdateStock = "UPDATE productos SET stock = stock - " + item.getCantidad() + " WHERE id = " + item.getProducto().getId();
                                stmt.executeUpdate(sqlUpdateStock);
                            }

                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(panelVenta, "Error financiero al guardar en BD: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // impresion ticket fisico en pantalla
                        String nombreEmpresa = "ABARROTES CORDI"; 
                        try {
                            Properties prop = new Properties();
                            prop.load(new FileInputStream("configuracion.properties"));
                            nombreEmpresa = prop.getProperty("razon_social", "LA BODEGA DE DON PAPU SA DE CV");
                        } catch (Exception ex) { }

                        StringBuilder ticket = new StringBuilder();
                        ticket.append("===================================\n");
                        ticket.append("       ").append(nombreEmpresa.toUpperCase()).append("       \n");
                        ticket.append("===================================\n");
                        ticket.append("Fecha: ").append(new SimpleDateFormat("dd/MM/yyyy").format(new Date())).append("\n");
                        ticket.append("Hora: ").append(hHoy).append("\n");
                        ticket.append("-----------------------------------\n");
                        ticket.append(String.format("%-6s %-15s %-8s\n", "CANT", "PRODUCTO (CÓDIGO)", "IMPORTE"));
                        ticket.append("-----------------------------------\n");
                        
                        for (DetalleVenta item : ventaActual.getListaProductos()) {
                            ticket.append(String.format("%-6s %-15.15s $%-8.2f\n", 
                                item.getCantidad() + "x", item.getProducto().getNombre(), item.getSubtotal()));
                            ticket.append("  Cód: ").append(item.getProducto().getCodigo()).append("\n");
                        }
                        
                        ticket.append("-----------------------------------\n");
                        ticket.append(String.format("%-25s $%.2f\n", "TOTAL A PAGAR:", totalAPagar));
                        ticket.append("-----------------------------------\n");
                        ticket.append(String.format("%-25s $%.2f\n", "Efectivo Recibido:", efectivo));
                        ticket.append(String.format("%-25s $%.2f\n", "Cambio a Entregar:", cambio));
                        ticket.append("===================================\n");
                        ticket.append("      ¡GRACIAS POR SU COMPRA!      \n");

                        JTextArea txtArea = new JTextArea(ticket.toString());
                        txtArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                        txtArea.setEditable(false);
                        JOptionPane.showMessageDialog(panelVenta, new JScrollPane(txtArea), "Venta Exitosa", JOptionPane.PLAIN_MESSAGE);
                        
                        // limpiar carrito y actualizar stocks
                        ventaActual = new Venta();
                        actualizarTicketVisual();
                        cargarProductosDesdeBD(""); 
                        
                    } else {
                        JOptionPane.showMessageDialog(panelVenta, "Monto insuficiente", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) { }
            }
        }
    }
}