package com.cordi.controlador;

import com.cordi.vista.PanelCorte;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ControladorCorte implements ActionListener {

    private PanelCorte panelCorte;
    
    private double totalVendidoHoy = 864.50; 
    private int numeroVentasHoy = 5;
    private double fondoDeCaja = 500.0;

    public ControladorCorte(PanelCorte panelCorte, com.cordi.modelo.Usuario usuario) {
        this.panelCorte = panelCorte;
        
        if (usuario.obtenerRol().equals("Vendedor")) {
            this.panelCorte.btnFondoCaja.setEnabled(false);
            this.panelCorte.btnFondoCaja.setText("Fondo Fijo: $500");
        }

        cargarDatosDelDia();
        this.panelCorte.btnCerrarDia.addActionListener(this);
        this.panelCorte.btnFondoCaja.addActionListener(this);
    }

    private void cargarDatosDelDia() {
        panelCorte.lblTotalHoy.setText("$" + String.format("%.2f", totalVendidoHoy));
        panelCorte.lblVentasHoy.setText(String.valueOf(numeroVentasHoy));
        panelCorte.lblFondoCaja.setText("$" + String.format("%.2f", fondoDeCaja));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        // ingresar fondo
        if (e.getSource() == panelCorte.btnFondoCaja) {
            String montoStr = JOptionPane.showInputDialog(panelCorte, "Ingresa el dinero base (Fondo de caja) para dar cambio:", "500");
            if (montoStr != null && !montoStr.isEmpty()) {
                try {
                    fondoDeCaja = Double.parseDouble(montoStr);
                    cargarDatosDelDia();
                    JOptionPane.showMessageDialog(panelCorte, "Fondo de caja actualizado.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panelCorte, "Ingresa un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        
        // cerrar day
        else if (e.getSource() == panelCorte.btnCerrarDia) {
            
            if (totalVendidoHoy <= 0 && fondoDeCaja <= 0) {
                JOptionPane.showMessageDialog(panelCorte, "La caja está completamente vacía (sin ventas y sin fondo).", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double totalFisicoEsperado = totalVendidoHoy + fondoDeCaja;

            int confirmar = JOptionPane.showConfirmDialog(panelCorte, 
                "Resumen de Corte:\n" +
                "Ventas del día: $" + totalVendidoHoy + "\n" +
                "Fondo de Caja: $" + fondoDeCaja + "\n" +
                "---------------------------------\n" +
                "EFECTIVO TOTAL EN CAJÓN: $" + totalFisicoEsperado + "\n\n" +
                "¿Confirmar cierre de caja?", 
                "Corte de Caja", JOptionPane.YES_NO_OPTION);
            
            if (confirmar == JOptionPane.YES_OPTION) {
                SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                
                Object[] nuevoCorte = new Object[]{
                    "Cerrado", 
                    sdfHora.format(new Date()), 
                    numeroVentasHoy, 
                    "$" + totalVendidoHoy,
                    "$" + fondoDeCaja,
                    "$" + totalFisicoEsperado
                };
                
                panelCorte.modeloTabla.insertRow(0, nuevoCorte);

                // limpiar
                totalVendidoHoy = 0.0;
                numeroVentasHoy = 0;
                fondoDeCaja = 0.0; 
                cargarDatosDelDia();
                
                JOptionPane.showMessageDialog(panelCorte, "Corte realizado. Retira el dinero del cajón.");
            }
        }
    }
}