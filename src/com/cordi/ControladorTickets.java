package com.cordi.controlador;

import com.cordi.vista.PanelTickets;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ControladorTickets implements ActionListener {
    
    private PanelTickets panelTickets;

    public ControladorTickets(PanelTickets panelTickets) {
        this.panelTickets = panelTickets;
        cargarDatosDePrueba(); 

        this.panelTickets.btnExcel.addActionListener(this);
        this.panelTickets.btnReimprimir.addActionListener(this);
        this.panelTickets.btnFiltrar.addActionListener(this);
        this.panelTickets.btnDevolucion.addActionListener(this);
    }

    private void cargarDatosDePrueba() {
        panelTickets.modeloTabla.addRow(new Object[]{"TK-001", "17/05/2026", "14:30:00", "$ 192.00", "Completado"});
        panelTickets.modeloTabla.addRow(new Object[]{"TK-002", "17/05/2026", "15:45:12", "$ 55.50", "Completado"});
        panelTickets.modeloTabla.addRow(new Object[]{"TK-003", "16/05/2026", "09:15:00", "$ 320.00", "Completado"});
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        // procesado de dev
        if (e.getSource() == panelTickets.btnDevolucion) {
            int fila = panelTickets.tablaTickets.getSelectedRow();
            if (fila >= 0) {
                String estadoActual = panelTickets.modeloTabla.getValueAt(fila, 4).toString();
                
                if (estadoActual.equals("Devuelto")) {
                    JOptionPane.showMessageDialog(panelTickets, "Este ticket ya fue devuelto y anulado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String idTicket = panelTickets.modeloTabla.getValueAt(fila, 0).toString();
                int confirmar = JOptionPane.showConfirmDialog(panelTickets, 
                    "¿Procesar la devolución total del ticket " + idTicket + "?\nEl stock de los productos regresará al almacén.", 
                    "Confirmar Devolución", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
                if (confirmar == JOptionPane.YES_OPTION) {
                    // UPDATE A LA BD PARA BAJO STOCK
                    panelTickets.modeloTabla.setValueAt("Devuelto", fila, 4);
                    JOptionPane.showMessageDialog(panelTickets, "Devolución procesada con éxito.\nProductos regresados al almacén.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(panelTickets, "Seleccione un ticket de la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        }
        
        // reimprimir ticket
        else if (e.getSource() == panelTickets.btnReimprimir) {
            int fila = panelTickets.tablaTickets.getSelectedRow();
            if (fila >= 0) {
                String id = panelTickets.modeloTabla.getValueAt(fila, 0).toString();
                String fecha = panelTickets.modeloTabla.getValueAt(fila, 1).toString();
                String hora = panelTickets.modeloTabla.getValueAt(fila, 2).toString();
                String total = panelTickets.modeloTabla.getValueAt(fila, 3).toString();
                String estado = panelTickets.modeloTabla.getValueAt(fila, 4).toString();
                
                String ticket = "--- COPIA DE TICKET ---\n\n";
                ticket += "Ticket ID: " + id + "\n";
                ticket += "Fecha: " + fecha + " Hora: " + hora + "\n";
                ticket += "Total cobrado: " + total + "\n";
                ticket += "Estado actual: " + estado + "\n\n";
                ticket += "*** REIMPRESIÓN ***";
                
                JOptionPane.showMessageDialog(panelTickets, ticket, "Re-imprimir Ticket", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(panelTickets, "Seleccione un ticket de la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } 
        
        // exportar excel
        else if (e.getSource() == panelTickets.btnExcel) {
            try {
                File archivo = new File("ReporteVentas_Abarrotes.csv");
                FileWriter escritor = new FileWriter(archivo);
                
                escritor.write("ID Ticket,Fecha,Hora,Total,Estado\n");
                
                for (int i = 0; i < panelTickets.modeloTabla.getRowCount(); i++) {
                    escritor.write(panelTickets.modeloTabla.getValueAt(i, 0).toString() + ",");
                    escritor.write(panelTickets.modeloTabla.getValueAt(i, 1).toString() + ",");
                    escritor.write(panelTickets.modeloTabla.getValueAt(i, 2).toString() + ",");
                    escritor.write(panelTickets.modeloTabla.getValueAt(i, 3).toString().replace("$ ", "") + ",");
                    escritor.write(panelTickets.modeloTabla.getValueAt(i, 4).toString() + "\n");
                }
                
                escritor.close();
                JOptionPane.showMessageDialog(panelTickets, "¡Reporte exportado con éxito!", "Excel Generado", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(panelTickets, "Error al generar el archivo Excel.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } 
        
        else if (e.getSource() == panelTickets.btnFiltrar) {
            JOptionPane.showMessageDialog(panelTickets, "Próximamente filtrado por fechas en BD.");
        }
    }
}