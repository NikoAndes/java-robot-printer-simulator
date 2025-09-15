package com.umng.laboratorio3.impresora;

import com.umng.laboratorio3.models.Documento;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Controlador de impresión con cola FIFO
 * 
 * @author Nicolás Isaza
 * Universidad Militar Nueva Granada - Laboratorio 3
 */
public class ControladorImpresion {
    
    private final LinkedBlockingQueue<Documento> colaFIFO;
    private final Impresora impresora;
    private final List<Documento> documentosCompletados;
    private final AtomicBoolean procesando;
    private Thread hiloProcesamiento;
    
    /**
     * Constructor con impresora personalizada
     */
    public ControladorImpresion(Impresora impresora) {
        this.impresora = impresora;
        this.colaFIFO = new LinkedBlockingQueue<>();
        this.documentosCompletados = Collections.synchronizedList(new ArrayList<>());
        this.procesando = new AtomicBoolean(false);
    }
    
    /**
     * Constructor con impresora por defecto
     */
    public ControladorImpresion() {
        this(new Impresora("Impresora Principal", 1));
    }
    
    /**
     * Agrega un documento a la cola FIFO
     */
    public void agregarDocumento(Documento documento) {
        if (documento != null && !documento.estaCompleto()) {
            colaFIFO.offer(documento);
            System.out.println("📄 Documento agregado a la cola: " + documento.getNombre() + 
                             " (Posición: " + colaFIFO.size() + ")");
        }
    }
    
    /**
     * Crea y agrega un documento a la cola
     */
    public Documento crearYAgregarDocumento(String nombre, int paginas) {
        Documento documento = new Documento(nombre, paginas);
        agregarDocumento(documento);
        return documento;
    }
    
    /**
     * Inicia el procesamiento de la cola
     */
    public synchronized void iniciarProcesamiento() {
        if (!procesando.get()) {
            procesando.set(true);
            hiloProcesamiento = new Thread(this::procesarCola, "Hilo-Impresion-FIFO");
            hiloProcesamiento.setDaemon(true);
            hiloProcesamiento.start();
            System.out.println("▶️ Procesamiento FIFO iniciado");
        }
    }
    
    /**
     * Detiene el procesamiento de la cola
     */
    public synchronized void detenerProcesamiento() {
        if (procesando.get()) {
            procesando.set(false);
            if (hiloProcesamiento != null) {
                hiloProcesamiento.interrupt();
            }
            impresora.detenerImpresion();
            System.out.println("⏹️ Procesamiento detenido");
        }
    }
    
    /**
     * Procesa la cola FIFO en segundo plano
     */
    private void procesarCola() {
        while (procesando.get() && !Thread.currentThread().isInterrupted()) {
            try {
                // Si la impresora no está ocupada y hay documentos en cola
                if (!impresora.estaImprimiendo() && !colaFIFO.isEmpty()) {
                    Documento siguienteDocumento = colaFIFO.poll(); // FIFO: tomar el primero
                    if (siguienteDocumento != null) {
                        impresora.iniciarImpresion(siguienteDocumento);
                        System.out.println("🖨️ Iniciando impresión FIFO: " + siguienteDocumento.getNombre());
                    }
                }
                
                // Procesar la impresión actual
                if (impresora.estaImprimiendo()) {
                    boolean completado = impresora.procesarImpresion();
                    if (completado) {
                        Documento docCompletado = impresora.getDocumentoActual();
                        if (docCompletado != null) {
                            documentosCompletados.add(docCompletado);
                            System.out.println("✅ Impresión completada FIFO: " + docCompletado.getNombre());
                        }
                    }
                }
                
                Thread.sleep(200); // Pausa para no consumir demasiada CPU
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println("❌ Error en procesamiento FIFO: " + e.getMessage());
            }
        }
    }
    
    /**
     * Obtiene el estado actual del controlador
     */
    public String getEstadoActual() {
        StringBuilder estado = new StringBuilder();
        estado.append("🖨️ CONTROLADOR DE IMPRESIÓN FIFO\n");
        estado.append("═".repeat(35)).append("\n");
        estado.append("Estado: ").append(procesando.get() ? "PROCESANDO" : "DETENIDO").append("\n");
        estado.append("Documentos en cola: ").append(colaFIFO.size()).append("\n");
        
        if (impresora.estaImprimiendo()) {
            Documento docActual = impresora.getDocumentoActual();
            if (docActual != null) {
                estado.append("Imprimiendo: ").append(docActual.getNombre()).append("\n");
                estado.append("Progreso: ").append(String.format("%.1f%%", impresora.getProgreso())).append("\n");
                estado.append("Estado: ").append(docActual.getEstadoImpresion()).append("\n");
            }
        } else {
            estado.append("Impresora: INACTIVA\n");
        }
        
        estado.append("Completados: ").append(documentosCompletados.size()).append("\n");
        estado.append("Impresora: ").append(impresora.toString()).append("\n");
        
        return estado.toString();
    }
    
    /**
     * Muestra el estado de la cola FIFO
     */
    public void mostrarEstadoCola() {
        System.out.println("\n📋 COLA FIFO - First In, First Out");
        System.out.println("═".repeat(40));
        System.out.println("Documentos en cola: " + colaFIFO.size());
        
        if (impresora.estaImprimiendo()) {
            Documento docActual = impresora.getDocumentoActual();
            if (docActual != null) {
                System.out.println("🖨️ Imprimiendo (PRIMERO): " + docActual.getEstadoImpresion());
            }
        }
        
        if (!colaFIFO.isEmpty()) {
            System.out.println("\nPróximos documentos (orden FIFO):");
            int posicion = 1;
            for (Documento doc : colaFIFO) {
                String orden = (posicion == 1) ? " (SIGUIENTE)" : "";
                System.out.printf("  %d. %s%s\n", posicion, doc.toString(), orden);
                posicion++;
                if (posicion > 5) {
                    System.out.println("  ... y " + (colaFIFO.size() - 5) + " más");
                    break;
                }
            }
        } else {
            System.out.println("Cola vacía - No hay documentos pendientes");
        }
        System.out.println();
    }
    
    /**
     * Limpia toda la cola
     */
    public int limpiarCola() {
        int documentosCancelados = colaFIFO.size();
        colaFIFO.clear();
        return documentosCancelados;
    }
    
    /**
     * Obtiene estadísticas del sistema
     */
    public String getEstadisticas() {
        int totalDocumentos = documentosCompletados.size() + 
                             (impresora.estaImprimiendo() ? 1 : 0) + 
                             colaFIFO.size();
        
        return String.format(
            "📊 ESTADÍSTICAS FIFO:\n" +
            "══════════════════════\n" +
            "Total documentos: %d\n" +
            "Completados: %d\n" +
            "En cola: %d\n" +
            "Imprimiendo: %s\n" +
            "Impresora: %s\n" +
            "Sistema: %s\n",
            totalDocumentos,
            documentosCompletados.size(),
            colaFIFO.size(),
            impresora.estaImprimiendo() ? "SÍ" : "NO",
            impresora.getNombre(),
            procesando.get() ? "ACTIVO" : "DETENIDO"
        );
    }
    
    // Getters
    public boolean estaEjecutando() { 
        return procesando.get(); 
    }
    
    public int getTamañoCola() { 
        return colaFIFO.size(); 
    }
    
    public Impresora getImpresora() { 
        return impresora; 
    }
    
    public List<Documento> getDocumentosCompletados() {
        return new ArrayList<>(documentosCompletados);
    }
}