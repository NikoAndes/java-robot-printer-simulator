package com.umng.laboratorio3.models;

/**
 * Enumeración para tipos de tarea del robot
 * 
 * @author Nicolás Isaza
 * Universidad Militar Nueva Granada - Laboratorio 3
 */
public enum TipoTarea {
    SENSORES("Exploración con Sensores"),
    COMUNICACIONES("Transmisión de Datos");
    
    private final String descripcion;
    
    TipoTarea(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}