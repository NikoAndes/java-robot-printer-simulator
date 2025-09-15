package com.umng.laboratorio3.robot;

import com.umng.laboratorio3.models.Tarea;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Procesador de tareas con pila LIFO
 * 
 * @author Nicolás Isaza
 * Universidad Militar Nueva Granada - Laboratorio 3
 */
public class ProcesadorTareas {
    
    private final ConcurrentLinkedDeque<Tarea> pilaLIFO; // Usamos como pila LIFO
    private final List<Tarea> tareasCompletadas;
    private final AtomicBoolean procesando;
    private Tarea tareaActual;
    private Thread hiloProcesamiento;
    
    /**
     * Constructor del procesador
     */
    public ProcesadorTareas() {
        this.pilaLIFO = new ConcurrentLinkedDeque<>();
        this.tareasCompletadas = Collections.synchronizedList(new ArrayList<>());
        this.procesando = new AtomicBoolean(false);
        this.tareaActual = null;
    }
    
    /**
     * Agrega una tarea (LIFO - Last In, First Out)
     */
    public void agregarTarea(Tarea tarea) {
        if (tarea == null) return;
        
        if (tareaActual == null) {
            // Ejecutar inmediatamente
            iniciarTarea(tarea);
            System.out.println("⚡ Ejecutando inmediatamente: " + tarea.getNombreCompleto());
        } else {
            // Agregar a la pila LIFO (addFirst para que sea el primero en salir)
            pilaLIFO.addFirst(tarea);
            System.out.println("📦 Agregado a pila LIFO: " + tarea.getNombreCompleto() + 
                             " (Posición: 1 - CIMA)");
        }
    }
    
    /**
     * Crea y agrega una tarea de sensores
     */
    public Tarea crearYAgregarTareaSensores(int tiempoEjecucion) {
        Tarea tarea = Tarea.crearTareaSensores(tiempoEjecucion);
        agregarTarea(tarea);
        return tarea;
    }
    
    /**
     * Crea y agrega una tarea de comunicaciones
     */
    public Tarea crearYAgregarTareaComunicaciones(int tiempoEjecucion) {
        Tarea tarea = Tarea.crearTareaComunicaciones(tiempoEjecucion);
        agregarTarea(tarea);
        return tarea;
    }
    
    /**
     * Inicia el procesamiento de tareas
     */
    public synchronized void iniciarProcesamiento() {
        if (!procesando.get()) {
            procesando.set(true);
            hiloProcesamiento = new Thread(this::procesarTareas, "Hilo-Robot-LIFO");
            hiloProcesamiento.setDaemon(true);
            hiloProcesamiento.start();
            System.out.println("🤖 Procesador LIFO iniciado");
        }
    }
    
    /**
     * Detiene el procesamiento de tareas
     */
    public synchronized void detenerProcesamiento() {
        if (procesando.get()) {
            procesando.set(false);
            if (hiloProcesamiento != null) {
                hiloProcesamiento.interrupt();
            }
            if (tareaActual != null) {
                System.out.println("🛑 Tarea actual interrumpida: " + tareaActual.getNombreCompleto());
                tareaActual = null;
            }
            System.out.println("🛑 Procesador detenido");
        }
    }
    
    /**
     * Procesa las tareas en segundo plano
     */
    private void procesarTareas() {
        while (procesando.get() && !Thread.currentThread().isInterrupted()) {
            try {
                // Verificar si la tarea actual está completa
                if (tareaActual != null && tareaActual.debeEstarCompleta()) {
                    completarTareaActual();
                }
                
                // Si no hay tarea actual y hay tareas en la pila
                if (tareaActual == null && !pilaLIFO.isEmpty()) {
                    Tarea siguienteTarea = pilaLIFO.removeFirst(); // LIFO - tomar de la cima
                    if (siguienteTarea != null) {
                        iniciarTarea(siguienteTarea);
                        System.out.println("🔄 Iniciando de pila LIFO: " + siguienteTarea.getNombreCompleto());
                    }
                }
                
                Thread.sleep(300); // Pausa para no consumir CPU
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println("❌ Error en procesamiento LIFO: " + e.getMessage());
            }
        }
    }
    
    /**
     * Inicia una tarea
     */
    private void iniciarTarea(Tarea tarea) {
        this.tareaActual = tarea;
        tarea.iniciarEjecucion();
    }
    
    /**
     * Completa la tarea actual
     */
    private void completarTareaActual() {
        if (tareaActual != null) {
            tareaActual.completarTarea();
            tareasCompletadas.add(tareaActual);
            System.out.println("✅ Completada LIFO: " + tareaActual.getNombreCompleto());
            tareaActual = null;
        }
    }
    
    /**
     * Interrumpe la tarea actual (para emergencias)
     */
    public void interrumpirTareaActual() {
        if (tareaActual != null) {
            System.out.println("🚨 Interrumpiendo tarea: " + tareaActual.getNombreCompleto());
            tareaActual = null;
        }
    }
    
    /**
     * Obtiene el estado completo del procesador
     */
    public String getEstadoCompleto() {
        StringBuilder estado = new StringBuilder();
        estado.append("🤖 PROCESADOR DE TAREAS LIFO\n");
        estado.append("═".repeat(35)).append("\n");
        
        if (tareaActual != null) {
            estado.append("Ejecutando: ").append(tareaActual.getNombreCompleto()).append("\n");
            estado.append("Progreso: ").append(String.format("%.1f%%", tareaActual.getProgreso())).append("\n");
            estado.append("Tiempo transcurrido: ").append(String.format("%.1fs", tareaActual.getTiempoTranscurrido())).append("\n");
        } else {
            estado.append("Estado: LIBRE\n");
        }
        
        estado.append("Tareas en pila: ").append(pilaLIFO.size()).append("\n");
        
        if (!pilaLIFO.isEmpty()) {
            estado.append("Próximas tareas (orden LIFO):\n");
            int posicion = 1;
            for (Tarea tarea : pilaLIFO) {
                String orden = (posicion == 1) ? " (CIMA - SIGUIENTE)" : "";
                estado.append("  ").append(posicion).append(". ").append(tarea.toString()).append(orden).append("\n");
                posicion++;
                if (posicion > 3) {
                    estado.append("  ... y ").append(pilaLIFO.size() - 3).append(" más\n");
                    break;
                }
            }
        }
        
        estado.append("Completadas: ").append(tareasCompletadas.size()).append("\n");
        estado.append("Sistema: ").append(procesando.get() ? "ACTIVO" : "DETENIDO").append("\n");
        
        return estado.toString();
    }
    
    /**
     * Muestra el estado de la pila de forma simple
     */
    public void mostrarEstadoPila() {
        System.out.println("\n📦 PILA LIFO - Last In, First Out");
        System.out.println("═".repeat(40));
        
        if (tareaActual != null) {
            System.out.println("⚙️ Ejecutando: " + tareaActual.toStringDetallado());
        }
        
        System.out.println("Tareas en pila: " + pilaLIFO.size());
        
        if (!pilaLIFO.isEmpty()) {
            System.out.println("Próximas tareas (orden LIFO):");
            int posicion = 1;
            for (Tarea tarea : pilaLIFO) {
                String orden = (posicion == 1) ? " (CIMA)" : "";
                System.out.printf("  %d. %s%s\n", posicion, tarea.toString(), orden);
                posicion++;
                if (posicion > 3) break;
            }
        } else {
            System.out.println("Pila vacía - No hay tareas pendientes");
        }
        System.out.println();
    }
    
    /**
     * Obtiene estadísticas del procesador
     */
    public String getEstadisticas() {
        int totalTareas = tareasCompletadas.size() + 
                         (tareaActual != null ? 1 : 0) + 
                         pilaLIFO.size();
        
        return String.format(
            "📊 ESTADÍSTICAS LIFO:\n" +
            "═══════════════════════\n" +
            "Total tareas: %d\n" +
            "Completadas: %d\n" +
            "En pila: %d\n" +
            "Ejecutando: %s\n" +
            "Sistema: %s\n",
            totalTareas,
            tareasCompletadas.size(),
            pilaLIFO.size(),
            tareaActual != null ? "SÍ" : "NO",
            procesando.get() ? "ACTIVO" : "DETENIDO"
        );
    }
    
    /**
     * Limpia la pila de tareas
     */
    public int limpiarPila() {
        int tareasCanceladas = pilaLIFO.size();
        pilaLIFO.clear();
        return tareasCanceladas;
    }
    
    // Getters
    public boolean estaEjecutando() { 
        return procesando.get(); 
    }
    
    public boolean estaLibre() { 
        return tareaActual == null; 
    }
    
    public boolean estaOcupado() { 
        return tareaActual != null; 
    }
    
    public int getTamañoPila() { 
        return pilaLIFO.size(); 
    }
    
    public Tarea getTareaActual() { 
        return tareaActual; 
    }
    
    public List<Tarea> getTareasCompletadas() {
        return new ArrayList<>(tareasCompletadas);
    }
    
    public double getProgresoTareaActual() {
        return tareaActual != null ? tareaActual.getProgreso() : 0.0;
    }
}