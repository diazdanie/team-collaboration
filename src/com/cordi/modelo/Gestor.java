package com.cordi.modelo;

// hijo
public class Gestor extends Usuario {

    public Gestor(int id, String nombre, String password) {
    	// super para mandar los datos al constructor del padre
        super(id, nombre, password);
    }

    // sobreescribir metodo padre
    @Override
    public String obtenerRol() {
        return "Gestor";
    }
    
}