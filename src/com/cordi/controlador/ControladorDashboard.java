package com.cordi.controlador;

import com.cordi.vista.FrmDashboard;
import com.cordi.vista.PanelPuntoVenta;
import com.cordi.vista.PanelTickets;
import com.cordi.vista.PanelInventario;
import com.cordi.vista.PanelProveedores;
import com.cordi.vista.FrmLogin;
import com.cordi.vista.PanelConfiguracion;
import com.cordi.vista.PanelCorte;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorDashboard implements ActionListener {

    private FrmDashboard vistaDashboard;
    private PanelPuntoVenta panelVenta;
    private PanelInventario panelInventario;
    private PanelTickets panelTickets;
    private PanelProveedores panelProveedores;
    private PanelCorte panelCorte;
    private PanelConfiguracion panelConfig;

    public ControladorDashboard(FrmDashboard vistaDashboard) {
        this.vistaDashboard = vistaDashboard;

        panelVenta = new PanelPuntoVenta();
        
        new ControladorVentas(panelVenta);
        
        panelInventario = new PanelInventario();
        new ControladorInventario(panelInventario);


        this.vistaDashboard.panelDerecho.add(panelVenta, "VENTA");
        this.vistaDashboard.panelDerecho.add(panelInventario, "INVENTARIO");
        
        panelTickets = new PanelTickets();
        new ControladorTickets(panelTickets);
        this.vistaDashboard.panelDerecho.add(panelTickets, "TICKETS");
        
        panelProveedores = new PanelProveedores();
        new ControladorProveedores(panelProveedores);
        this.vistaDashboard.panelDerecho.add(panelProveedores, "PROVEEDORES");
        
        panelCorte = new PanelCorte();
        new ControladorCorte(panelCorte, vistaDashboard.usuarioActivo);
        this.vistaDashboard.panelDerecho.add(panelCorte, "CORTE");
        
        panelConfig = new PanelConfiguracion();
        new ControladorConfiguracion(panelConfig);
        this.vistaDashboard.panelDerecho.add(panelConfig, "CONFIG");
        this.vistaDashboard.btnConfig.addActionListener(this);

        this.vistaDashboard.btnCorte.addActionListener(this);

        this.vistaDashboard.btnProveedores.addActionListener(this);

        this.vistaDashboard.btnTickets.addActionListener(this);

        this.vistaDashboard.btnVenta.addActionListener(this);
        this.vistaDashboard.btnInventario.addActionListener(this);
        this.vistaDashboard.btnSalir.addActionListener(this);
        
        // mosttrar pantallaventas por defecto
        this.vistaDashboard.layoutTarjetas.show(this.vistaDashboard.panelDerecho, "VENTA");
        marcarBotonActivo(this.vistaDashboard.btnVenta);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vistaDashboard.btnVenta) {
            vistaDashboard.layoutTarjetas.show(vistaDashboard.panelDerecho, "VENTA");
            marcarBotonActivo(vistaDashboard.btnVenta);
        }
        else if (e.getSource() == vistaDashboard.btnInventario) {
            // CAMBIAR EL TEXTO DE CONSOLA POR ESTO:
            vistaDashboard.layoutTarjetas.show(vistaDashboard.panelDerecho, "INVENTARIO");
            marcarBotonActivo(vistaDashboard.btnInventario);
        }
        else if (e.getSource() == vistaDashboard.btnSalir) {
            vistaDashboard.dispose();
            FrmLogin login = new FrmLogin();
            new ControladorLogin(login);
            login.setVisible(true);
        }
        else if (e.getSource() == vistaDashboard.btnTickets) {
            vistaDashboard.layoutTarjetas.show(vistaDashboard.panelDerecho, "TICKETS");
            marcarBotonActivo(vistaDashboard.btnTickets);
        }
        else if (e.getSource() == vistaDashboard.btnProveedores) {
            vistaDashboard.layoutTarjetas.show(vistaDashboard.panelDerecho, "PROVEEDORES");
            marcarBotonActivo(vistaDashboard.btnProveedores);
        }
        else if (e.getSource() == vistaDashboard.btnCorte) {
            vistaDashboard.layoutTarjetas.show(vistaDashboard.panelDerecho, "CORTE");
            marcarBotonActivo(vistaDashboard.btnCorte);
        }
        else if (e.getSource() == vistaDashboard.btnConfig) {
            vistaDashboard.layoutTarjetas.show(vistaDashboard.panelDerecho, "CONFIG");
            marcarBotonActivo(vistaDashboard.btnConfig);
        }
    }

    private void marcarBotonActivo(javax.swing.JButton botonClickeado) {
        vistaDashboard.btnVenta.setBackground(Color.WHITE);
        vistaDashboard.btnVenta.setForeground(Color.DARK_GRAY);
        
        vistaDashboard.btnInventario.setBackground(Color.WHITE);
        vistaDashboard.btnInventario.setForeground(Color.DARK_GRAY);
        
        vistaDashboard.btnTickets.setBackground(Color.WHITE);
        vistaDashboard.btnTickets.setForeground(Color.DARK_GRAY);
        
        vistaDashboard.btnProveedores.setBackground(Color.WHITE);
        vistaDashboard.btnProveedores.setForeground(Color.DARK_GRAY);
        
        vistaDashboard.btnCorte.setBackground(Color.WHITE);
        vistaDashboard.btnCorte.setForeground(Color.DARK_GRAY);
        
        vistaDashboard.btnConfig.setBackground(Color.WHITE);
        vistaDashboard.btnConfig.setForeground(Color.DARK_GRAY);
        
        botonClickeado.setBackground(new Color(37, 99, 235));
        botonClickeado.setForeground(Color.WHITE);
    }
}