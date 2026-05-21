package com.cordi.controlador;

import com.cordi.bd.ConexionBD;
import com.cordi.modelo.Usuario;
import com.cordi.vista.PanelCorte;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ControladorCorte implements ActionListener {

    private PanelCorte panelCorte;
    private double totalVendidoHoy = 0.0; 
    private int numeroVentasHoy = 0;
    private double fondoDeCaja = 500.0; 

    public ControladorCorte(PanelCorte panelCorte, Usuario usuario) {
        this.panelCorte = panelCorte;
        
        if (usuario.obtenerRol().equals("Vendedor")) {
            this.panelCorte.btnFondoCaja.setEnabled(false);
            this.panelCorte.btnFondoCaja.setText("Fondo Fijo: $500");
        }

        this.panelCorte.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                cargarDatosDelTurno();
                cargarHistorialCortes();
            }
        });

        this.panelCorte.btnCerrarDia.addActionListener(this);
        this.panelCorte.btnFondoCaja.addActionListener(this);
    }

    private void cargarDatosDelTurno() {
        String ultimaFechaCierre = "2000-01-01 00:00:00"; 
        try (Connection con = ConexionBD.obtenerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT MAX(fecha_cierre) AS ultimo FROM cortes_caja")) {
            if (rs.next() && rs.getString("ultimo") != null) {
                ultimaFechaCierre = rs.getString("ultimo").replace(".0", ""); 
            }
        } catch (Exception ex) { }

        String sql = "SELECT COUNT(id_venta) AS num_ventas, SUM(total_final) AS total_dia " +
                     "FROM ventas WHERE estado = 'Completado' " +
                     "AND CAST(CONCAT(fecha, ' ', hora) AS DATETIME) > CAST('" + ultimaFechaCierre + "' AS DATETIME)";
                     
        try (Connection con = ConexionBD.obtenerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                numeroVentasHoy = rs.getInt("num_ventas");
                totalVendidoHoy = rs.getDouble("total_dia"); 
            }
        } catch (Exception ex) { }

        panelCorte.lblTotalHoy.setText("$" + String.format("%.2f", totalVendidoHoy));
        panelCorte.lblVentasHoy.setText(String.valueOf(numeroVentasHoy));
        panelCorte.lblFondoCaja.setText("$" + String.format("%.2f", fondoDeCaja));
    }

    private void cargarHistorialCortes() {
        panelCorte.modeloTabla.setRowCount(0); 
        String sql = "SELECT * FROM cortes_caja ORDER BY id_corte DESC";
        
        try (Connection con = ConexionBD.obtenerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
             
            while (rs.next()) {
                panelCorte.modeloTabla.addRow(new Object[]{
                    rs.getString("estado"),
                    rs.getString("fecha_cierre").replace(".0", ""),
                    "-",
                    "$ " + String.format("%.2f", rs.getDouble("ventas_totales")),
                    "$ " + String.format("%.2f", rs.getDouble("fondo_inicial")),
                    "$ " + String.format("%.2f", rs.getDouble("ventas_totales") + rs.getDouble("fondo_inicial"))
                });
            }
            // actualizar tabla
            panelCorte.tablaHistorial.revalidate();
            panelCorte.tablaHistorial.repaint();
            
        } catch (Exception ex) { 
            System.err.println("ERROR HISTORIAL: " + ex.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == panelCorte.btnFondoCaja) {
            String montoStr = JOptionPane.showInputDialog(panelCorte, "Ingresa el dinero base:", fondoDeCaja);
            if (montoStr != null) {
                try {
                    fondoDeCaja = Double.parseDouble(montoStr);
                    cargarDatosDelTurno();
                } catch (Exception ex) { }
            }
        } else if (e.getSource() == panelCorte.btnCerrarDia) {
            if (totalVendidoHoy <= 0) {
                JOptionPane.showMessageDialog(panelCorte, "No hay ventas nuevas");
                return;
            }
            if (JOptionPane.showConfirmDialog(panelCorte, "Confirmar cierre?") == JOptionPane.YES_OPTION) {
                String fechaHoy = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String fechaCierre = fechaHoy + " " + new SimpleDateFormat("HH:mm:ss").format(new Date());
                String sqlInsert = "INSERT INTO cortes_caja (fecha_apertura, fecha_cierre, fondo_inicial, ventas_totales, estado) " +
                                   "VALUES ('" + fechaHoy + " 00:00:00', '" + fechaCierre + "', " + fondoDeCaja + ", " + totalVendidoHoy + ", 'Cerrado')";
                if (ConexionBD.ejecutarInstruccion(sqlInsert)) {
                    fondoDeCaja = 500.0;
                    cargarDatosDelTurno(); 
                    cargarHistorialCortes();
                    JOptionPane.showMessageDialog(panelCorte, "Corte registrado");
                } 
            }
        }
    }
}