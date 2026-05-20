package com.cordi.modelo;

import java.util.ArrayList;
import java.util.List;

public class Venta {
    
    private int idVenta;
    private String fecha;
    private String hora;
    private double descuento;
    private double totalFinal;
    // temporal por falta de BD
    private List<DetalleVenta> listaProductos; 

    public Venta() {
        this.listaProductos = new ArrayList<>();
        this.descuento = 0.0;
        this.totalFinal = 0.0;
    }

    public void agregarAlCarrito(DetalleVenta detalle) {
        this.listaProductos.add(detalle);
        calcularTotal();
    }

    public void quitarDelCarrito(int index) {
        if(index >= 0 && index < listaProductos.size()) {
            this.listaProductos.remove(index);
            calcularTotal();
        }
    }

    public void calcularTotal() {
        double suma = 0;
        for (DetalleVenta item : listaProductos) {
            suma = suma + item.getSubtotal();
        }
        this.totalFinal = suma - this.descuento;
    }

    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public double getDescuento() { return descuento; }
    public void setDescuento(double descuento) { 
        this.descuento = descuento;
        calcularTotal();
    }

    public double getTotalFinal() { return totalFinal; }

    public List<DetalleVenta> getListaProductos() { return listaProductos; }
}