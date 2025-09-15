package com.umng.laboratorio3;

import com.umng.laboratorio3.impresora.SimuladorImpresion;
import com.umng.laboratorio3.robot.SimuladorRobot;
import com.umng.laboratorio3.utils.MenuUtils;
import java.util.Scanner;

/**
 * Clase principal del Laboratorio 3
 * 
 * Autores:
 * - Nicolas Isaza Sierra (7004625)
 * - Julián David Galindo Hernández (7004600)
 * - Saúl Alejandro Pérez Estupiñán (7004631)
 * Universidad Militar Nueva Granada - Laboratorio 3
 */
public class Main {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        mostrarTituloPrincipal();
        mostrarObjetivos();
        MenuUtils.pausar(scanner);
        
        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = MenuUtils.leerEntero(scanner, ">>> Seleccione una opción: ", 0, 6);
            
            switch (opcion) {
                case 1:
                    ejecutarSimuladorImpresion();
                    break;
                case 2:
                    ejecutarSimuladorRobot();
                    break;
                case 3:
                    ejecutarDemostracionCompleta(scanner);
                    break;
                case 4:
                    mostrarConceptosTeoria();
                    MenuUtils.pausar(scanner);
                    break;
                case 5:
                    mostrarInformacionLaboratorio();
                    MenuUtils.pausar(scanner);
                    break;
                case 6:
                    abrirInterfacesGraficas();
                    MenuUtils.pausar(scanner);
                    break;
                case 0:
                    System.out.println("\n🎓 ¡Gracias por usar el Laboratorio 3!");
                    System.out.println("Universidad Militar Nueva Granada");
                    System.out.println("Nicolás Isaza - Ingeniería Mecatrónica III");
                    break;
                default:
                    System.out.println("❌ Opción no válida.");
                    MenuUtils.pausar(scanner);
            }
            
        } while (opcion != 0);
        
        scanner.close();
    }
    
    private static void mostrarTituloPrincipal() {
        MenuUtils.limpiarPantalla();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                        LABORATORIO 3                        ║");
        System.out.println("║                 ESTRUCTURAS DE DATOS DINÁMICAS              ║");
        System.out.println("║                      (Pilas y Colas)                        ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║                Universidad Militar Nueva Granada            ║");
        System.out.println("║          Facultad de Ingeniería - Programa Mecatrónica      ║");
        System.out.println("║            Asignatura: Programación III - Semestre III      ║");
        System.out.println("║                  Estudiante: Nicolás Isaza                  ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
    }
    
    private static void mostrarObjetivos() {
        System.out.println("\n🎯 OBJETIVOS DEL LABORATORIO:");
        System.out.println("═".repeat(35));
        System.out.println("General:");
        System.out.println("• Identificar las principales estructuras de datos y su aplicabilidad");
        System.out.println();
        System.out.println("Específicos:");
        System.out.println("• Identificar la utilidad del uso de estructura de datos tipo pila");
        System.out.println("• Identificar la utilidad del uso de estructura de datos tipo cola");
    }
    
    private static void mostrarMenuPrincipal() {
        MenuUtils.limpiarPantalla();
        mostrarTituloPrincipal();
        
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                        MENÚ PRINCIPAL                       ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║ 1. 🖨️  Simulador de Impresión (Cola FIFO)                  ║");
        System.out.println("║ 2. 🤖 Simulador de Robot Explorador (Pila LIFO)            ║");
        System.out.println("║ 3. 🎬 Demostración Completa (Ambos conceptos)              ║");
        System.out.println("║ 4. 📚 Conceptos Teóricos (FIFO vs LIFO)                    ║");
        System.out.println("║ 5. ℹ️  Información del Laboratorio                          ║");
        System.out.println("║ 6. 🖼️  Abrir Interfaces Gráficas                            ║");
        System.out.println("║ 0. 🚪 Salir del Programa                                   ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    private static void ejecutarSimuladorImpresion() {
        System.out.println("\n🖨️ Iniciando Simulador de Impresión (Cola FIFO)...");
        System.out.println("═".repeat(60));
        MenuUtils.pausar(new Scanner(System.in));
        
        try {
            new SimuladorImpresion().iniciar();
        } catch (Exception e) {
            System.out.println("❌ Error al ejecutar simulador de impresión: " + e.getMessage());
        }
    }
    
    private static void ejecutarSimuladorRobot() {
        System.out.println("\n🤖 Iniciando Simulador de Robot Explorador (Pila LIFO)...");
        System.out.println("═".repeat(60));
        MenuUtils.pausar(new Scanner(System.in));
        
        try {
            new SimuladorRobot().iniciar();
        } catch (Exception e) {
            System.out.println("❌ Error al ejecutar simulador de robot: " + e.getMessage());
        }
    }
    
    private static void ejecutarDemostracionCompleta(Scanner scanner) {
        MenuUtils.limpiarPantalla();
        MenuUtils.mostrarTitulo("🎬 DEMOSTRACIÓN COMPLETA");
        System.out.println("Esta demostración mostrará ambos conceptos en secuencia:");
        System.out.println("1️⃣ Cola FIFO - Simulador de Impresión");
        System.out.println("2️⃣ Pila LIFO - Simulador de Robot Explorador");
        System.out.println();
        MenuUtils.pausar(scanner);
        
        try {
            // Demostración FIFO
            System.out.println("═".repeat(60));
            System.out.println("🖨️ DEMOSTRANDO COLA FIFO (First In, First Out)");
            System.out.println("═".repeat(60));
            
            new SimuladorImpresion().ejecutarDemostracion();
            
            if (MenuUtils.confirmar(scanner, "¿Continuar con la demostración de Pila LIFO?")) {
                // Demostración LIFO
                System.out.println("\n" + "═".repeat(60));
                System.out.println("🤖 DEMOSTRANDO PILA LIFO (Last In, First Out)");
                System.out.println("═".repeat(60));
                
                new SimuladorRobot().ejecutarDemostracion();
            }
            
            mostrarResumenConceptos(scanner);
            
        } catch (Exception e) {
            System.out.println("❌ Error en demostración: " + e.getMessage());
            MenuUtils.pausar(scanner);
        }
    }
    
    private static void mostrarResumenConceptos(Scanner scanner) {
        MenuUtils.limpiarPantalla();
        MenuUtils.mostrarTitulo("📚 RESUMEN DE CONCEPTOS");
        
        System.out.println("🔄 COMPARACIÓN FIFO vs LIFO:");
        System.out.println("═".repeat(40));
        System.out.println();
        
        System.out.println("🖨️ COLA FIFO (First In, First Out):");
        System.out.println("   ✅ El PRIMERO en entrar es el PRIMERO en salir");
        System.out.println("   ✅ Ejemplo: Cola de impresión - documentos por orden de llegada");
        System.out.println("   ✅ Justo y ordenado - respeta el orden de llegada");
        System.out.println("   ✅ Ideal para: Sistemas de colas, buffers, spoolers");
        System.out.println();
        
        System.out.println("🤖 PILA LIFO (Last In, First Out):");
        System.out.println("   ✅ El ÚLTIMO en entrar es el PRIMERO en salir");
        System.out.println("   ✅ Ejemplo: Robot - tareas recientes tienen prioridad");
        System.out.println("   ✅ Las interrupciones y emergencias se atienden primero");
        System.out.println("   ✅ Ideal para: Sistemas de interrupciones, undo/redo, recursión");
        System.out.println();
        
        System.out.println("🎯 CONCLUSIONES:");
        System.out.println("   📌 FIFO es ideal cuando el orden de llegada es importante");
        System.out.println("   📌 LIFO es ideal cuando las tareas recientes son prioritarias");
        System.out.println("   📌 Ambas son estructuras fundamentales en programación");
        System.out.println();
        
        MenuUtils.pausar(scanner);
    }
    
    private static void mostrarConceptosTeoria() {
        MenuUtils.limpiarPantalla();
        MenuUtils.mostrarTitulo("📚 CONCEPTOS TEÓRICOS");
        
        System.out.println("🔍 ESTRUCTURAS DE DATOS DINÁMICAS:");
        System.out.println("═".repeat(45));
        System.out.println("Las estructuras de datos dinámicas permiten que el tamaño");
        System.out.println("de la estructura cambie durante la ejecución del programa.");
        System.out.println();
        
        System.out.println("📦 COLA (QUEUE) - FIFO:");
        System.out.println("═".repeat(25));
        System.out.println("• First In, First Out - El primero en entrar, primero en salir");
        System.out.println("• Inserción por un extremo (rear/cola)");
        System.out.println("• Eliminación por el otro extremo (front/frente)");
        System.out.println("• Ejemplo real: Cola de banco, spooler de impresión");
        System.out.println("• Operaciones principales:");
        System.out.println("  - enqueue(): agregar elemento al final");
        System.out.println("  - dequeue(): remover elemento del frente");
        System.out.println("  - peek(): ver el frente sin remover");
        System.out.println();
        
        System.out.println("📚 PILA (STACK) - LIFO:");
        System.out.println("═".repeat(25));
        System.out.println("• Last In, First Out - El último en entrar, primero en salir");
        System.out.println("• Inserción y eliminación por el mismo extremo (top/cima)");
        System.out.println("• Ejemplo real: Pila de platos, historial del navegador");
        System.out.println("• Operaciones principales:");
        System.out.println("  - push(): agregar elemento a la cima");
        System.out.println("  - pop(): remover elemento de la cima");
        System.out.println("  - peek(): ver la cima sin remover");
        System.out.println();
    }
    
    private static void mostrarInformacionLaboratorio() {
        MenuUtils.limpiarPantalla();
        MenuUtils.mostrarTitulo("ℹ️ INFORMACIÓN DEL LABORATORIO");
        
        System.out.println("📚 DETALLES ACADÉMICOS:");
        System.out.println("═".repeat(30));
        System.out.println("Universidad: Universidad Militar Nueva Granada (UMNG)");
        System.out.println("Facultad: Ingeniería");
        System.out.println("Programa: Ingeniería Mecatrónica");
        System.out.println("Asignatura: Programación III");
        System.out.println("Semestre: III");
        System.out.println("Laboratorio: 3 - Estructuras de datos dinámicas");
        System.out.println();
        
        System.out.println("💻 CARACTERÍSTICAS IMPLEMENTADAS:");
        System.out.println("═".repeat(40));
        System.out.println("✅ Simulador de Cola FIFO para impresión");
        System.out.println("✅ Simulador de Pila LIFO para robot explorador");
        System.out.println("✅ Multithreading para procesamiento en tiempo real");
        System.out.println("✅ Interfaces gráficas modernas con Swing");
        System.out.println("✅ Interfaces de consola interactivas");
        System.out.println("✅ Demostraciones automáticas de conceptos");
        System.out.println("✅ Validación completa de entrada y manejo de errores");
        System.out.println("✅ Documentación profesional y código orientado a objetos");
        System.out.println();
        
        System.out.println("👨‍🎓 DESARROLLADO POR:");
        System.out.println("═".repeat(20));
        System.out.println("Nicolas Isaza Sierra (7004625)");
        System.out.println("Julián David Galindo Hernández (7004600)");
        System.out.println("Saúl Alejandro Pérez Estupiñán (7004631)");
        System.out.println("Carrera: Ingeniería Mecatrónica III");
        System.out.println("Institución: UMNG - Sede Cajicá");
        System.out.println();
    }
    
    private static void abrirInterfacesGraficas() {
        MenuUtils.limpiarPantalla();
        MenuUtils.mostrarTitulo("🖼️ INTERFACES GRÁFICAS");

        System.out.println("Seleccione la interfaz gráfica a abrir:");
        System.out.println("═".repeat(65));
        System.out.println("1. Menú principal gráfico");
        System.out.println("2. Simulador de impresión (Cola FIFO)");
        System.out.println("3. Simulador de robot (Pila LIFO)");
        System.out.println("0. Volver al menú principal");
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        int opcion = MenuUtils.leerEntero(scanner, ">>> Seleccione una opción: ", 0, 3);

        switch (opcion) {
            case 1:
                System.out.println("Abriendo Menú principal gráfico...");
                abrirVentanaGrafica(com.umng.laboratorio3.gui.MenuPrincipalGUI.class);
                break;
            case 2:
                System.out.println("Abriendo Simulador de impresión...");
                abrirVentanaGrafica(com.umng.laboratorio3.gui.InterfazImpresora.class);
                break;
            case 3:
                System.out.println("Abriendo Simulador de robot...");
                abrirVentanaGrafica(com.umng.laboratorio3.gui.InterfazRobot.class);
                break;
            case 0:
                System.out.println("Volviendo al menú principal...");
                return;
            default:
                System.out.println("❌ Opción no válida.");
        }
    }

    /**
     * Abre una ventana gráfica y espera hasta que se cierre.
     */
    private static void abrirVentanaGrafica(Class<? extends javax.swing.JFrame> claseVentana) {
        final Object lock = new Object();
        javax.swing.JFrame[] ventana = new javax.swing.JFrame[1];

        try {
            javax.swing.SwingUtilities.invokeAndWait(() -> {
                try {
                    ventana[0] = claseVentana.getDeclaredConstructor().newInstance();
                    ventana[0].setVisible(true);
                    ventana[0].addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent e) {
                            synchronized (lock) {
                                lock.notify();
                            }
                        }
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent e) {
                            synchronized (lock) {
                                lock.notify();
                            }
                        }
                    });
                } catch (Exception ex) {
                    javax.swing.JOptionPane.showMessageDialog(null,
                        "Error al abrir la interfaz gráfica:\n" + ex.getMessage(),
                        "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                    synchronized (lock) {
                        lock.notify();
                    }
                }
            });
            synchronized (lock) {
                lock.wait();
            }
        } catch (Exception e) {
            System.out.println("❌ Error al abrir la ventana gráfica: " + e.getMessage());
        }
    }
}

