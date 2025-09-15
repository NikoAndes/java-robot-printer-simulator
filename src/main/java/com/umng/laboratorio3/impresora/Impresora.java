package com.umng.laboratorio3.impresora;

import com.umng.laboratorio3.models.Documento;

/**
 * Simulador de una impresora física
 * 
 * @author Nicolás Isaza
 * Universidad Militar Nueva Granada - Laboratorio 3
 */
public class Impresora {
    private String nombre;
    private int tiempoPorPagina; // en segundos
    private Documento documentoActual;
    private boolean imprimiendo;
    private long tiempoInicioImpresion;
    
    /**
     * Constructor de la impresora
     */
    public Impresora(String nombre, int tiempoPorPagina) {
        this.nombre = nombre;
        this.tiempoPorPagina = tiempoPorPagina;
        this.documentoActual = null;
        this.imprimiendo = false;
    }
    
    /**
     * Inicia la impresión de un documento
     */
    public synchronized boolean iniciarImpresion(Documento documento) {
        if (imprimiendo) {
            return false;
        }
        
        this.documentoActual = documento;
        this.imprimiendo = true;
        this.tiempoInicioImpresion = System.currentTimeMillis();
        
        return true;
    }
    
    /**
     * Procesa la impresión actual
     * @return true si se completó un documento, false si aún está imprimiendo
     */
    public synchronized boolean procesarImpresion() {
        if (!imprimiendo || documentoActual == null) {
            return false;
        }
        
        // Calcular cuántas páginas deberían estar impresas según el tiempo transcurrido
        long tiempoTranscurrido = System.currentTimeMillis() - tiempoInicioImpresion;
        int paginasQueDeberianEstarImpresas = (int) (tiempoTranscurrido / (tiempoPorPagina * 1000L));
        
        // Imprimir páginas hasta alcanzar el número que debería estar impreso
        while (documentoActual.getPaginasImpresas() < paginasQueDeberianEstarImpresas && 
               !documentoActual.estaCompleto()) {
            documentoActual.imprimirSiguientePagina();
        }
        
        // Verificar si el documento está completo
        if (documentoActual.estaCompleto()) {
            finalizarImpresion();
            return true;
        }
        
        return false;
    }
    
    /**
     * Detiene la impresión actual
     */
    public synchronized void detenerImpresion() {
        this.imprimiendo = false;
        this.documentoActual = null;
    }
    
    /**
     * Finaliza la impresión del documento actual
     */
    private void finalizarImpresion() {
        this.imprimiendo = false;
        this.documentoActual = null;
    }
    
    /**
     * Calcula el progreso de impresión
     */
    public double getProgreso() {
        if (!imprimiendo || documentoActual == null) {
            return 0.0;
        }
        return documentoActual.getProgreso();
    }
    
    /**
     * Obtiene información del estado actual
     */
    public String getEstadoActual() {
        if (!imprimiendo) {
            return "Impresora inactiva";
        }
        
        if (documentoActual == null) {
            return "Error: imprimiendo pero sin documento";
        }
        
        return String.format("Imprimiendo: %s", documentoActual.getEstadoImpresion());
    }
    
    // Getters
    public String getNombre() { return nombre; }
    public int getTiempoPorPagina() { return tiempoPorPagina; }
    public Documento getDocumentoActual() { return documentoActual; }
    public boolean estaImprimiendo() { return imprimiendo; }
    
    // Setters
    public void setTiempoPorPagina(int tiempoPorPagina) {
        this.tiempoPorPagina = tiempoPorPagina;
    }
    
    @Override
    public String toString() {
        return String.format("%s (Velocidad: %ds/página)", nombre, tiempoPorPagina);
    }
}