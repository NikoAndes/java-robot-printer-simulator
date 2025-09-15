package com.umng.laboratorio3.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase que representa un documento para impresión
 * 
 * @author Nicolás Isaza
 * Universidad Militar Nueva Granada - Laboratorio 3
 */
public class Documento {
    private String nombre;
    private int totalPaginas;
    private int paginasImpresas;
    private LocalDateTime fechaCreacion;
    private boolean completo;
    
    /**
     * Constructor del documento
     */
    public Documento(String nombre, int totalPaginas) {
        this.nombre = nombre;
        this.totalPaginas = totalPaginas;
        this.paginasImpresas = 0;
        this.fechaCreacion = LocalDateTime.now();
        this.completo = false;
    }
    
    /**
     * Imprime la siguiente página
     */
    public boolean imprimirSiguientePagina() {
        if (paginasImpresas < totalPaginas) {
            paginasImpresas++;
            if (paginasImpresas == totalPaginas) {
                completo = true;
            }
            return true;
        }
        return false;
    }
    
    /**
     * Calcula el progreso de impresión
     */
    public double getProgreso() {
        if (totalPaginas == 0) return 100.0;
        return (paginasImpresas * 100.0) / totalPaginas;
    }
    
    /**
     * Obtiene el estado de impresión como texto
     */
    public String getEstadoImpresion() {
        return String.format("Página %d/%d (%.1f%%)", 
                           paginasImpresas, totalPaginas, getProgreso());
    }
    
    // Getters y Setters
    public String getNombre() { return nombre; }
    public int getTotalPaginas() { return totalPaginas; }
    public int getPaginasImpresas() { return paginasImpresas; }
    public boolean estaCompleto() { return completo; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    
    public void setCompleto(boolean completo) { this.completo = completo; }
    
    @Override
    public String toString() {
        return String.format("%s (%d páginas)", nombre, totalPaginas);
    }
    
    public String toStringDetallado() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return String.format("%s - %s - Creado: %s", 
                           toString(), getEstadoImpresion(), 
                           fechaCreacion.format(formatter));
    }
}