package com.cordi.utilidades;

// para atrapar errores propios
public class ExcepcionGeneral extends Exception {
    
    // msj que vera el usuario
    public ExcepcionGeneral(String mensajeError) {
        super(mensajeError);
    }
}