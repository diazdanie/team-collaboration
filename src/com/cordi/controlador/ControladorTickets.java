package com.cordi.controlador;

import com.cordi.bd.ConexionBD;
import com.cordi.vista.PanelTickets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class ControladorTickets implements ActionListener {
    
    private PanelTickets panelTickets;

    public ControladorTickets(PanelTickets panelTickets) {
        this.panelTickets = panelTickets;
        
        // cuando el usuario entre a esta pestaña, se ejecuta la consulta a la BD
        this.panelTickets.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                cargarTicketsDesdeBD("", "");
                panelTickets.txtFechaInicio.setText("");
                panelTickets.txtFechaFin.setText("");
            }
        });

        this.panelTickets.btnExcel.addActionListener(this);
        this.panelTickets.btnReimprimir.addActionListener(this);
        this.panelTickets.btnFiltrar.addActionListener(this);
        this.panelTickets.btnDevolucion.addActionListener(this); 
        this.panelTickets.btnJson.addActionListener(this); 
    }

    private void cargarTicketsDesdeBD(String fechaInicio, String fechaFin) {
        panelTickets.modeloTabla.setRowCount(0);
        
        String sql = "SELECT * FROM ventas"; 
        
        if (!fechaInicio.isEmpty() && !fechaFin.isEmpty()) {
            sql += " WHERE fecha BETWEEN STR_TO_DATE('" + fechaInicio + "', '%d/%m/%Y') " +
                   "AND STR_TO_DATE('" + fechaFin + "', '%d/%m/%Y')";
        }
        
        sql += " ORDER BY id_venta DESC";

        try (Connection con = ConexionBD.obtenerConexion();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
                
               boolean hayDatos = false;
               
               while (rs.next()) {
                   hayDatos = true;
                   panelTickets.modeloTabla.addRow(new Object[]{
                       rs.getInt("id_venta"),
                       rs.getString("fecha"),
                       rs.getString("hora"),
                       "$ " + String.format("%.2f", rs.getDouble("total_final")),
                       rs.getString("estado")
                   });
               }
               
               if (!hayDatos && !fechaInicio.isEmpty()) {
                   JOptionPane.showMessageDialog(panelTickets, "No hay ventas en el rango de fechas seleccionado", "Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
               }
               
           } catch (Exception ex) {
               JOptionPane.showMessageDialog(panelTickets, "Formato de fecha incorrecto (DD/MM/AAAA)", "Error de Filtro", JOptionPane.ERROR_MESSAGE);
           }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        // filtrar fechas
        if (e.getSource() == panelTickets.btnFiltrar) {
            String inicio = panelTickets.txtFechaInicio.getText().trim();
            String fin = panelTickets.txtFechaFin.getText().trim();
            
            if (inicio.isEmpty() || fin.isEmpty()) {
                cargarTicketsDesdeBD("", ""); 
            } else {
                cargarTicketsDesdeBD(inicio, fin);
            }
        }
        
        // procesar devolucion
        else if (e.getSource() == panelTickets.btnDevolucion) {
            int fila = panelTickets.tablaTickets.getSelectedRow();
            if (fila >= 0) {
                String estado = panelTickets.modeloTabla.getValueAt(fila, 4).toString();
                if (estado.equals("Devuelto")) {
                    JOptionPane.showMessageDialog(panelTickets, "Ticket ya devuelto previamente", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                String idVenta = panelTickets.modeloTabla.getValueAt(fila, 0).toString();
                
                if (JOptionPane.showConfirmDialog(panelTickets, "¿Anular el ticket #" + idVenta + " y regresar el stock al almacén?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    
                    String sqlAnular = "UPDATE ventas SET estado = 'Devuelto' WHERE id_venta = " + idVenta;
                    String sqlRestaurarStock = "UPDATE productos p JOIN detalle_ventas dv ON p.id = dv.id_producto " +
                                               "SET p.stock = p.stock + dv.cantidad WHERE dv.id_venta = " + idVenta;
                    
                    if (ConexionBD.ejecutarInstruccion(sqlAnular) && ConexionBD.ejecutarInstruccion(sqlRestaurarStock)) {
                        JOptionPane.showMessageDialog(panelTickets, "Devolucion procesada, Stock restaurado");

                        cargarTicketsDesdeBD(panelTickets.txtFechaInicio.getText().trim(), panelTickets.txtFechaFin.getText().trim());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(panelTickets, "Seleccione un ticket primero");
            }
        }
        
        // reimprimir ticket
        else if (e.getSource() == panelTickets.btnReimprimir) {
            int fila = panelTickets.tablaTickets.getSelectedRow();
            if (fila >= 0) {
                String idVenta = panelTickets.modeloTabla.getValueAt(fila, 0).toString();
                String fecha = panelTickets.modeloTabla.getValueAt(fila, 1).toString();
                String hora = panelTickets.modeloTabla.getValueAt(fila, 2).toString();
                String total = panelTickets.modeloTabla.getValueAt(fila, 3).toString();
                String estado = panelTickets.modeloTabla.getValueAt(fila, 4).toString();
                
                String nombreEmpresa = "LA BODEGA DE DON PAPU SA DE CV"; 
                try {
                    Properties prop = new Properties();
                    prop.load(new FileInputStream("configuracion.properties"));
                    nombreEmpresa = prop.getProperty("razon_social", "LA BODEGA DE DON PAPU SA DE CV");
                } catch (Exception ex) { }

                StringBuilder ticket = new StringBuilder();
                ticket.append("===================================\n");
                ticket.append("       ").append(nombreEmpresa.toUpperCase()).append("       \n");
                ticket.append("===================================\n");
                ticket.append("Ticket ID: ").append(idVenta).append("\n");
                ticket.append("Fecha: ").append(fecha).append(" Hora: ").append(hora).append("\n");
                ticket.append("Estado: ").append(estado.toUpperCase()).append("\n");
                ticket.append("-----------------------------------\n");
                ticket.append(String.format("%-6s %-15s %-8s\n", "CANT", "PRODUCTO", "IMPORTE"));
                ticket.append("-----------------------------------\n");
                
                String sqlDetalles = "SELECT p.nombre, dv.cantidad, dv.subtotal FROM detalle_ventas dv " +
                                     "JOIN productos p ON dv.id_producto = p.id WHERE dv.id_venta = " + idVenta;
                
                try (Connection con = ConexionBD.obtenerConexion();
                     Statement stmt = con.createStatement();
                     ResultSet rs = stmt.executeQuery(sqlDetalles)) {
                     
                    while (rs.next()) {
                        ticket.append(String.format("%-6s %-15.15s $%-8.2f\n", 
                            rs.getInt("cantidad") + "x", rs.getString("nombre"), rs.getDouble("subtotal")));
                    }
                } catch (Exception ex) { }

                ticket.append("-----------------------------------\n");
                ticket.append(String.format("%-25s %s\n", "TOTAL COBRADO:", total));
                ticket.append("===================================\n");
                ticket.append("         *** COPIA DE TICKET *** \n");

                JTextArea txtArea = new JTextArea(ticket.toString());
                txtArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                txtArea.setEditable(false);
                JOptionPane.showMessageDialog(panelTickets, new JScrollPane(txtArea), "Reimpresion de ticket", JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(panelTickets, "Seleccione un ticket primero");
            }
        } 
        
        // exportar a excel
        else if (e.getSource() == panelTickets.btnExcel) {
            try {
                FileWriter escritor = new FileWriter(new File("Reporte_Tickets.csv"));
                escritor.write("ID,Fecha,Hora,Total,Estado\n");
                for (int i = 0; i < panelTickets.modeloTabla.getRowCount(); i++) {
                    escritor.write(panelTickets.modeloTabla.getValueAt(i, 0) + "," +
                                   panelTickets.modeloTabla.getValueAt(i, 1) + "," +
                                   panelTickets.modeloTabla.getValueAt(i, 2) + "," +
                                   panelTickets.modeloTabla.getValueAt(i, 3).toString().replace("$ ", "") + "," +
                                   panelTickets.modeloTabla.getValueAt(i, 4) + "\n");
                }
                escritor.close();
                JOptionPane.showMessageDialog(panelTickets, "Exportado a Excel (CSV)");
            } catch (IOException ex) { }
        } 

        // --- ACCIÓN: EXPORTAR JSON ---
        else if (e.getSource() == panelTickets.btnJson) {
            try {
                FileWriter escritor = new FileWriter(new File("Reporte_Tickets.json"));
                StringBuilder json = new StringBuilder("[\n");
                for (int i = 0; i < panelTickets.modeloTabla.getRowCount(); i++) {
                    json.append("  {\n");
                    json.append("    \"id_ticket\": \"").append(panelTickets.modeloTabla.getValueAt(i, 0)).append("\",\n");
                    json.append("    \"fecha\": \"").append(panelTickets.modeloTabla.getValueAt(i, 1)).append("\",\n");
                    json.append("    \"hora\": \"").append(panelTickets.modeloTabla.getValueAt(i, 2)).append("\",\n");
                    String total = panelTickets.modeloTabla.getValueAt(i, 3).toString().replace("$ ", "");
                    json.append("    \"total\": ").append(total).append(",\n");
                    json.append("    \"estado\": \"").append(panelTickets.modeloTabla.getValueAt(i, 4)).append("\"\n");
                    json.append("  }");
                    if (i < panelTickets.modeloTabla.getRowCount() - 1) json.append(","); 
                    json.append("\n");
                }
                json.append("]");
                escritor.write(json.toString());
                escritor.close();
                JOptionPane.showMessageDialog(panelTickets, "Reporte exportado a JSON");
            } catch (IOException ex) { }
        }
    }
}