package com.umng.laboratorio3.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase que representa una tarea del robot explorador
 * 
 * @author Nicolás Isaza
 * Universidad Militar Nueva Granada - Laboratorio 3
 */
public class Tarea {
    private static int contadorId = 1;
    
    private int id;
    private TipoTarea tipo;
    private int tiempoEjecucion; // en segundos
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFinalizacion;
    private boolean completada;
    private boolean enEjecucion;
    
    /**
     * Constructor privado
     */
    private Tarea(TipoTarea tipo, int tiempoEjecucion) {
        this.id = contadorId++;
        this.tipo = tipo;
        this.tiempoEjecucion = tiempoEjecucion;
        this.fechaCreacion = LocalDateTime.now();
        this.completada = false;
        this.enEjecucion = false;
    }
    
    /**
     * Crea una tarea de sensores
     */
    public static Tarea crearTareaSensores(int tiempoEjecucion) {
        return new Tarea(TipoTarea.SENSORES, tiempoEjecucion);
    }
    
    /**
     * Crea una tarea de comunicaciones
     */
    public static Tarea crearTareaComunicaciones(int tiempoEjecucion) {
        return new Tarea(TipoTarea.COMUNICACIONES, tiempoEjecucion);
    }
    
    /**
     * Inicia la ejecución de la tarea
     */
    public void iniciarEjecucion() {
        this.enEjecucion = true;
        this.fechaInicio = LocalDateTime.now();
    }
    
    /**
     * Completa la tarea
     */
    public void completarTarea() {
        this.completada = true;
        this.enEjecucion = false;
        this.fechaFinalizacion = LocalDateTime.now();
    }
    
    /**
     * Calcula el tiempo transcurrido desde el inicio
     */
    public double getTiempoTranscurrido() {
        if (fechaInicio == null) return 0;
        LocalDateTime ahora = completada ? fechaFinalizacion : LocalDateTime.now();
        return java.time.Duration.between(fechaInicio, ahora).toMillis() / 1000.0;
    }
    
    /**
     * Calcula el progreso de ejecución (0-100%)
     */
    public double getProgreso() {
        if (!enEjecucion && !completada) return 0;
        if (completada) return 100;
        
        double transcurrido = getTiempoTranscurrido();
        double progreso = (transcurrido / tiempoEjecucion) * 100;
        return Math.min(progreso, 100);
    }
    
    /**
     * Verifica si la tarea debe estar completa
     */
    public boolean debeEstarCompleta() {
        return enEjecucion && getTiempoTranscurrido() >= tiempoEjecucion;
    }
    
    /**
     * Obtiene el nombre completo de la tarea
     */
    public String getNombreCompleto() {
        return String.format("Tarea_%03d_%s", id, tipo.name());
    }
    
    // Getters
    public int getId() { return id; }
    public TipoTarea getTipo() { return tipo; }
    public int getTiempoEjecucion() { return tiempoEjecucion; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public LocalDateTime getFechaFinalizacion() { return fechaFinalizacion; }
    public boolean estaCompletada() { return completada; }
    public boolean estaEnEjecucion() { return enEjecucion; }
    
    @Override
    public String toString() {
        return String.format("%s (%ds)", getNombreCompleto(), tiempoEjecucion);
    }
    
    public String toStringDetallado() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String estado = completada ? "COMPLETADA" : 
                       enEjecucion ? String.format("EJECUTANDO (%.1f%%)", getProgreso()) : 
                       "PENDIENTE";
        
        return String.format("%s - %s - Creada: %s", 
                           toString(), estado, fechaCreacion.format(formatter));
    }
}