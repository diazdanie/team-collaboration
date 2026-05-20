package com.cordi.modelo;

// para solo un renglon del ticket
public class DetalleVenta {
    
    private Producto producto;
    private int cantidad;
    private double subtotal;

    public DetalleVenta(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;

        this.subtotal = producto.getPrecio() * cantidad;
    }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { 
        this.cantidad = cantidad; 
        this.subtotal = this.producto.getPrecio() * cantidad;
    }

    public double getSubtotal() { return subtotal; }
}