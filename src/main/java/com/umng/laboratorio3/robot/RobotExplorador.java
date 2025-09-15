package com.umng.laboratorio3.robot;

import com.umng.laboratorio3.models.Tarea;

/**
 * Robot explorador con procesador de tareas LIFO
 * 
 * @author Nicolás Isaza
 * Universidad Militar Nueva Granada - Laboratorio 3
 */
public class RobotExplorador {
    private String nombre;
    private String ubicacionActual;
    private ProcesadorTareas procesador;
    private boolean sistemaActivo;
    
    /**
     * Constructor del robot
     */
    public RobotExplorador(String nombre) {
        this.nombre = nombre;
        this.ubicacionActual = "Base de operaciones";
        this.procesador = new ProcesadorTareas();
        this.sistemaActivo = false;
    }
    
    /**
     * Activa el robot y su procesador
     */
    public void activar() {
        if (!sistemaActivo) {
            sistemaActivo = true;
            procesador.iniciarProcesamiento();
            System.out.println("🔋 Robot " + nombre + " activado");
        }
    }
    
    /**
     * Desactiva el robot y su procesador
     */
    public void desactivar() {
        if (sistemaActivo) {
            sistemaActivo = false;
            procesador.detenerProcesamiento();
            System.out.println("🛑 Robot " + nombre + " desactivado");
        }
    }
    
    /**
     * Mueve el robot a una nueva ubicación
     */
    public void moverA(String nuevaUbicacion) {
        if (!sistemaActivo) {
            throw new IllegalStateException("El robot debe estar activo para moverse");
        }
        
        this.ubicacionActual = nuevaUbicacion;
        System.out.println("🚀 Robot movido a: " + ubicacionActual);
    }
    
    /**
     * Crea una tarea de exploración con sensores
     */
    public Tarea explorarConSensores(int tiempoEjecucion) {
        if (!sistemaActivo) {
            throw new IllegalStateException("El robot debe estar activo para crear tareas");
        }
        
        return procesador.crearYAgregarTareaSensores(tiempoEjecucion);
    }
    
    /**
     * Crea una tarea de envío de datos
     */
    public Tarea enviarDatos(int tiempoEjecucion) {
        if (!sistemaActivo) {
            throw new IllegalStateException("El robot debe estar activo para crear tareas");
        }
        
        return procesador.crearYAgregarTareaComunicaciones(tiempoEjecucion);
    }
    
    /**
     * Simula una emergencia interrumpiendo la tarea actual
     */
    public void emergencia() {
        if (!sistemaActivo) {
            System.out.println("⚠️ Robot inactivo - no se puede procesar emergencia");
            return;
        }
        
        System.out.println("🚨 EMERGENCIA DETECTADA en " + ubicacionActual);
        procesador.interrumpirTareaActual();
    }
    
    /**
     * Cancela todas las tareas pendientes
     */
    public void cancelarTodasLasTareas() {
        if (!sistemaActivo) {
            System.out.println("⚠️ Robot inactivo - no hay tareas que cancelar");
            return;
        }
        
        int tareasCanceladas = procesador.limpiarPila();
        procesador.interrumpirTareaActual();
        System.out.println("🗑️ Canceladas " + tareasCanceladas + " tareas de la pila");
    }
    
    /**
     * Obtiene el estado completo del robot
     */
    public String getEstadoCompleto() {
        StringBuilder estado = new StringBuilder();
        estado.append("🤖 ROBOT EXPLORADOR\n");
        estado.append("═".repeat(25)).append("\n");
        estado.append("Nombre: ").append(nombre).append("\n");
        estado.append("Ubicación: ").append(ubicacionActual).append("\n");
        estado.append("Sistema: ").append(sistemaActivo ? "ACTIVO" : "INACTIVO").append("\n");
        estado.append("\n").append(procesador.getEstadoCompleto());
        
        return estado.toString();
    }
    
    /**
     * Muestra estadísticas del robot
     */
    public void mostrarEstadisticas() {
        System.out.println(getEstadoCompleto());
        System.out.println(procesador.getEstadisticas());
    }
    
    // Getters
    public String getNombre() { return nombre; }
    public String getUbicacionActual() { return ubicacionActual; }
    public ProcesadorTareas getProcesador() { return procesador; }
    public boolean isSistemaActivo() { return sistemaActivo; }
    
    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    @Override
    public String toString() {
        return String.format("Robot %s en %s (%s)", 
                           nombre, ubicacionActual, 
                           sistemaActivo ? "ACTIVO" : "INACTIVO");
    }
}