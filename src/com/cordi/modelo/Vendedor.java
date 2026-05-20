package com.cordi.modelo;

// hijo
public class Vendedor extends Usuario {

    public Vendedor(int id, String nombre, String password) {
        // super para mandar los datos al constructor del padre
        super(id, nombre, password);
    }

    // sobreescribir metodo padre
    @Override
    public String obtenerRol() {
        return "Vendedor";
    }
    
}