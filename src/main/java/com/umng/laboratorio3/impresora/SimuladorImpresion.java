package com.umng.laboratorio3.impresora;

import com.umng.laboratorio3.models.Documento;
import com.umng.laboratorio3.utils.MenuUtils;
import java.util.Scanner;

/**
 * Simulador de impresión con interfaz de consola
 * 
 * @author Nicolás Isaza
 * Universidad Militar Nueva Granada - Laboratorio 3
 */
public class SimuladorImpresion {
    private ControladorImpresion controlador;
    private Scanner scanner;
    
    public SimuladorImpresion() {
        this.controlador = new ControladorImpresion();
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Inicia el simulador interactivo
     */
    public void iniciar() {
        mostrarTitulo();
        mostrarInstrucciones();
        
        int opcion;
        do {
            mostrarMenu();
            opcion = MenuUtils.leerEntero(scanner, ">>> Seleccione una opción: ", 0, 7);
            
            switch (opcion) {
                case 1: iniciarProcesamiento(); break;
                case 2: detenerProcesamiento(); break;
                case 3: agregarDocumento(); break;
                case 4: mostrarEstadoCola(); break;
                case 5: mostrarEstadoCompleto(); break;
                case 6: configurarImpresora(); break;
                case 7: limpiarCola(); break;
                case 0: System.out.println("🛑 Cerrando simulador de impresión..."); break;
                default: System.out.println("❌ Opción no válida.");
            }
            
            if (opcion != 0) {
                MenuUtils.pausar(scanner);
            }
            
        } while (opcion != 0);
        
        if (controlador.estaEjecutando()) {
            controlador.detenerProcesamiento();
        }
    }
    
    private void mostrarTitulo() {
        MenuUtils.limpiarPantalla();
        MenuUtils.mostrarTitulo("🖨️ SIMULADOR DE IMPRESIÓN - COLA FIFO");
        MenuUtils.mostrarSubtitulo("Universidad Militar Nueva Granada - Laboratorio 3");
    }
    
    private void mostrarInstrucciones() {
        System.out.println("📋 INSTRUCCIONES:");
        System.out.println("• Este simulador implementa una COLA FIFO para documentos");
        System.out.println("• FIFO = First In, First Out (el primero en entrar es el primero en salir)");
        System.out.println("• Los documentos se procesan en orden de llegada");
        System.out.println("• Puede agregar documentos mientras otros se imprimen");
        System.out.println("• La impresión simula el tiempo real por página");
        MenuUtils.pausar(scanner);
    }
    
    private void mostrarMenu() {
        MenuUtils.limpiarPantalla();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║       🖨️ SIMULADOR DE IMPRESIÓN        ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ 1. ▶️ Iniciar Procesamiento            ║");
        System.out.println("║ 2. ⏹️ Detener Procesamiento            ║");
        System.out.println("║ 3. 📄 Agregar Documento                ║");
        System.out.println("║ 4. 📋 Ver Cola FIFO                   ║");
        System.out.println("║ 5. 📊 Ver Estado Completo             ║");
        System.out.println("║ 6. ⚙️ Configurar Impresora             ║");
        System.out.println("║ 7. 🗑️ Limpiar Cola                     ║");
        System.out.println("║ 0. ❌ Salir                           ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        mostrarEstadoRapido();
    }
    
    private void mostrarEstadoRapido() {
        System.out.printf("\n🖨️ Estado: %s | 📄 Cola: %d documento%s\n",
                         controlador.estaEjecutando() ? "PROCESANDO" : "DETENIDO",
                         controlador.getTamañoCola(),
                         controlador.getTamañoCola() == 1 ? "" : "s");
        
        if (controlador.getImpresora().estaImprimiendo()) {
            Documento docActual = controlador.getImpresora().getDocumentoActual();
            if (docActual != null) {
                System.out.printf("📖 Imprimiendo: %s (%.1f%%)\n",
                                docActual.getNombre(),
                                controlador.getImpresora().getProgreso());
            }
        }
        System.out.println();
    }
    
    private void iniciarProcesamiento() {
        if (controlador.estaEjecutando()) {
            System.out.println("⚠️ El procesamiento ya está activo.");
            return;
        }
        
        controlador.iniciarProcesamiento();
        System.out.println("✅ Procesamiento FIFO iniciado.");
        System.out.println("📄 Los documentos se procesarán en orden de llegada.");
    }
    
    private void detenerProcesamiento() {
        if (!controlador.estaEjecutando()) {
            System.out.println("⚠️ El procesamiento ya está detenido.");
            return;
        }
        
        controlador.detenerProcesamiento();
        System.out.println("⏹️ Procesamiento detenido.");
        System.out.println("📄 Los documentos permanecen en cola.");
    }
    
    private void agregarDocumento() {
        System.out.println("📄 Agregar nuevo documento a la cola FIFO:");
        System.out.println("═".repeat(45));
        
        String nombre = MenuUtils.leerTextoNoVacio(scanner, "Nombre del documento: ");
        int paginas = MenuUtils.leerEntero(scanner, "Número de páginas: ", 1, 1000);
        
        Documento documento = controlador.crearYAgregarDocumento(nombre, paginas);
        System.out.println("✅ Documento agregado: " + documento.toString());
        System.out.println("📍 Posición en cola FIFO: " + controlador.getTamañoCola());
    }
    
    private void mostrarEstadoCola() {
        MenuUtils.limpiarPantalla();
        MenuUtils.mostrarTitulo("📋 ESTADO DE LA COLA FIFO");
        controlador.mostrarEstadoCola();
    }
    
    private void mostrarEstadoCompleto() {
        MenuUtils.limpiarPantalla();
        MenuUtils.mostrarTitulo("📊 ESTADO COMPLETO");
        System.out.println(controlador.getEstadoActual());
        System.out.println(controlador.getEstadisticas());
    }
    
    private void configurarImpresora() {
        System.out.println("⚙️ Configurar Impresora:");
        System.out.println("═".repeat(25));
        
        Impresora impresora = controlador.getImpresora();
        System.out.println("Impresora actual: " + impresora.getNombre());
        System.out.println("Velocidad actual: " + impresora.getTiempoPorPagina() + " segundos por página");
        
        if (MenuUtils.confirmar(scanner, "¿Desea cambiar la velocidad de impresión?")) {
            int nuevaVelocidad = MenuUtils.leerEntero(scanner, 
                "Nueva velocidad (segundos por página): ", 1, 10);
            
            boolean estabaEjecutando = controlador.estaEjecutando();
            if (estabaEjecutando) {
                controlador.detenerProcesamiento();
            }
            
            impresora.setTiempoPorPagina(nuevaVelocidad);
            
            if (estabaEjecutando) {
                controlador.iniciarProcesamiento();
            }
            
            System.out.println("✅ Velocidad cambiada a " + nuevaVelocidad + " segundos por página.");
        }
    }
    
    private void limpiarCola() {
        if (controlador.getTamañoCola() == 0) {
            System.out.println("ℹ️ La cola ya está vacía.");
            return;
        }
        
        System.out.printf("⚠️ Hay %d documento%s en la cola FIFO.\n",
                        controlador.getTamañoCola(),
                        controlador.getTamañoCola() == 1 ? "" : "s");
        
        if (MenuUtils.confirmar(scanner, "¿Está seguro de limpiar la cola?")) {
            int documentosCancelados = controlador.limpiarCola();
            System.out.println("🗑️ Se cancelaron " + documentosCancelados + " documento(s).");
        } else {
            System.out.println("❌ Operación cancelada.");
        }
    }
    
    /**
     * Ejecuta una demostración automática del concepto FIFO
     */
    public void ejecutarDemostracion() {
        MenuUtils.limpiarPantalla();
        MenuUtils.mostrarTitulo("🎬 DEMOSTRACIÓN FIFO - First In, First Out");
        System.out.println("Esta demostración mostrará cómo funciona la cola FIFO\n");
        MenuUtils.pausar(scanner);
        
        try {
            System.out.println("1️⃣ Creando documentos de ejemplo...");
            controlador.crearYAgregarDocumento("Documento_A", 3);
            Thread.sleep(1000);
            controlador.crearYAgregarDocumento("Documento_B", 2);
            Thread.sleep(1000);
            controlador.crearYAgregarDocumento("Documento_C", 4);
            Thread.sleep(1000);
            
            System.out.println("\n2️⃣ Estado de la cola FIFO:");
            controlador.mostrarEstadoCola();
            Thread.sleep(2000);
            
            System.out.println("3️⃣ Iniciando procesamiento FIFO...");
            controlador.iniciarProcesamiento();
            Thread.sleep(1000);
            
            System.out.println("\n4️⃣ Procesando documentos en orden FIFO...");
            System.out.println("⏳ Observe cómo se procesan en orden de llegada: A → B → C");
            
            for (int i = 0; i < 10 && (controlador.estaEjecutando() || controlador.getTamañoCola() > 0); i++) {
                Thread.sleep(1500);
                System.out.println("📊 " + controlador.getEstadoActual());
            }
            
            System.out.println("\n5️⃣ Estadísticas finales:");
            System.out.println(controlador.getEstadisticas());
            
            System.out.println("\n🎉 ¡Demostración FIFO completada!");
            System.out.println("💡 Concepto demostrado: Los documentos se procesaron en orden A → B → C");
            
        } catch (Exception e) {
            System.out.println("❌ Error en demostración: " + e.getMessage());
        } finally {
            if (controlador.estaEjecutando()) {
                controlador.detenerProcesamiento();
            }
        }
        
        MenuUtils.pausar(scanner);
    }
    
    /**
     * Método principal para ejecutar directamente
     */
    public static void main(String[] args) {
        SimuladorImpresion simulador = new SimuladorImpresion();
        
        MenuUtils.limpiarPantalla();
        MenuUtils.mostrarTitulo("🖨️ SIMULADOR DE IMPRESIÓN");
        
        System.out.println("Opciones de inicio:");
        System.out.println("1. 🎬 Ver demostración automática FIFO");
        System.out.println("2. 🎮 Usar simulador interactivo");
        
        Scanner scanner = new Scanner(System.in);
        int opcion = MenuUtils.leerEntero(scanner, ">>> Seleccione una opción (1-2): ", 1, 2);
        
        if (opcion == 1) {
            simulador.ejecutarDemostracion();
            if (MenuUtils.confirmar(scanner, "¿Desea continuar con el simulador interactivo?")) {
                simulador = new SimuladorImpresion();
                simulador.iniciar();
            }
        } else {
            simulador.iniciar();
        }
    }
}