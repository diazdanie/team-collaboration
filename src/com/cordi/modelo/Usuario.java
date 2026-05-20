package com.cordi.modelo;

// padre
public class Usuario {
    
    protected int id;
    protected String nombre;
    protected String password;

    public Usuario(int id, String nombre, String password) {
        this.id = id;
        this.nombre = nombre;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPassword() {
        return password;
    }

    // para polimorfismo en hijos
    public String obtenerRol() {
        return "Desconocido";
    }
}