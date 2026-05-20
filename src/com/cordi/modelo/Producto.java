package com.cordi.modelo;

public class Producto {
    
    private int id;
    private String codigo;
    private String categoria;
    private String nombre;
    private double precio;
    private int stock;
    private int stockMinimo;
    private String rutaImagen; // ruta del archivo (ej "img/coca.png") para no saturar la bd

    public Producto(int id, String codigo, String categoria, String nombre, double precio, int stock, int stockMinimo, String rutaImagen) {
        this.id = id;
        this.codigo = codigo;
        this.categoria = categoria;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.rutaImagen = rutaImagen;
    }

    public Producto() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }

    public String getRutaImagen() { return rutaImagen; }
    public void setRutaImagen(String rutaImagen) { this.rutaImagen = rutaImagen; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

}