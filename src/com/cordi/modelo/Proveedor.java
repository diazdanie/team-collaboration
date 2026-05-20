package com.cordi.modelo;

public class Proveedor {
    private int id;
    private String nombreEmpresa;
    private String nombreContacto;
    private String email;
    private String tipoProductos;
    private String horario;

    public Proveedor(int id, String nombreEmpresa, String nombreContacto, String email, String tipoProductos, String horario) {
        this.id = id;
        this.nombreEmpresa = nombreEmpresa;
        this.nombreContacto = nombreContacto;
        this.email = email;
        this.tipoProductos = tipoProductos;
        this.horario = horario;
    }

    public int getId() { return id; }
    public String getNombreEmpresa() { return nombreEmpresa; }
    
    public String getNombreContacto() { return nombreContacto; }
    public void setNombreContacto(String nombreContacto) { this.nombreContacto = nombreContacto; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getTipoProductos() { return tipoProductos; }
    public void setTipoProductos(String tipoProductos) { this.tipoProductos = tipoProductos; }
    
    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }
}