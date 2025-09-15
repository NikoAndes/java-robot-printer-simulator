package com.umng.laboratorio3.robot;

import com.umng.laboratorio3.models.Tarea;
import com.umng.laboratorio3.utils.MenuUtils;
import java.util.Scanner;

/**
 * Simulador del robot explorador con interfaz de consola
 * 
 * @author Nicolás Isaza
 * Universidad Militar Nueva Granada - Laboratorio 3
 */
public class SimuladorRobot {
    private RobotExplorador robot;
    private Scanner scanner;
    
    public SimuladorRobot() {
        this.robot = new RobotExplorador("Robot-Consola-Explorer");
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
            opcion = MenuUtils.leerEntero(scanner, ">>> Seleccione una opción: ", 0, 8);
            
            switch (opcion) {
                case 1: activarRobot(); break;
                case 2: desactivarRobot(); break;
                case 3: crearTareaSensores(); break;
                case 4: crearTareaComunicaciones(); break;
                case 5: cambiarUbicacion(); break;
                case 6: mostrarEstadoCompleto(); break;
                case 7: simularEmergencia(); break;
                case 8: limpiarPila(); break;
                case 0: System.out.println("🛑 Cerrando simulador de robot..."); break;
                default: System.out.println("❌ Opción no válida.");
            }
            
            if (opcion != 0) {
                MenuUtils.pausar(scanner);
            }
            
        } while (opcion != 0);
        
        if (robot.isSistemaActivo()) {
            robot.desactivar();
        }
    }
    
    private void mostrarTitulo() {
        MenuUtils.limpiarPantalla();
        MenuUtils.mostrarTitulo("🤖 SIMULADOR ROBOT EXPLORADOR - PILA LIFO");
        MenuUtils.mostrarSubtitulo("Universidad Militar Nueva Granada - Laboratorio 3");
    }
    
    private void mostrarInstrucciones() {
        System.out.println("📋 INSTRUCCIONES:");
        System.out.println("• Este simulador implementa una PILA LIFO para las tareas");
        System.out.println("• LIFO = Last In, First Out (el último en entrar es el primero en salir)");
        System.out.println("• Las tareas se ejecutan inmediatamente si el procesador está libre");
        System.out.println("• Si está ocupado, las nuevas tareas van a la pila");
        System.out.println("• Al completar una tarea, se toma la de la CIMA de la pila");
        System.out.println("• Puede agregar tareas mientras otras se ejecutan");
        MenuUtils.pausar(scanner);
    }
    
    private void mostrarMenu() {
        MenuUtils.limpiarPantalla();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║        🤖 ROBOT EXPLORADOR MENU       ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ 1. 🔋 Activar Robot                    ║");
        System.out.println("║ 2. 🛑 Desactivar Robot                 ║");
        System.out.println("║ 3. 🔍 Crear Tarea Sensores            ║");
        System.out.println("║ 4. 📡 Crear Tarea Comunicaciones      ║");
        System.out.println("║ 5. 🚀 Cambiar Ubicación               ║");
        System.out.println("║ 6. 📊 Ver Estado Completo             ║");
        System.out.println("║ 7. 🚨 Simular Emergencia              ║");
        System.out.println("║ 8. 🗑️ Limpiar Pila                    ║");
        System.out.println("║ 0. ❌ Salir                           ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        mostrarEstadoRapido();
    }
    
    private void mostrarEstadoRapido() {
        System.out.printf("\n🤖 Estado: %s | 📍 Ubicación: %s\n",
                         robot.isSistemaActivo() ? "ACTIVO" : "INACTIVO",
                         robot.getUbicacionActual());
        
        ProcesadorTareas procesador = robot.getProcesador();
        System.out.printf("⚙️ Procesador: %s | 📦 Pila: %d tarea%s\n",
                         procesador.estaOcupado() ? "OCUPADO" : "LIBRE",
                         procesador.getTamañoPila(),
                         procesador.getTamañoPila() == 1 ? "" : "s");
        System.out.println();
    }
    
    private void activarRobot() {
        if (robot.isSistemaActivo()) {
            System.out.println("⚠️ El robot ya está activo.");
            return;
        }
        
        robot.activar();
        System.out.println("✅ Robot activado exitosamente.");
        System.out.println("📍 Ubicación actual: " + robot.getUbicacionActual());
    }
    
    private void desactivarRobot() {
        if (!robot.isSistemaActivo()) {
            System.out.println("⚠️ El robot ya está inactivo.");
            return;
        }
        
        ProcesadorTareas procesador = robot.getProcesador();
        if (procesador.getTamañoPila() > 0 || procesador.estaOcupado()) {
            System.out.println("⚠️ Hay tareas en ejecución o pendientes.");
            if (MenuUtils.confirmar(scanner, "¿Desea cancelar todas las tareas?")) {
                robot.cancelarTodasLasTareas();
                System.out.println("🗑️ Tareas canceladas.");
            }
        }
        
        robot.desactivar();
        System.out.println("🛑 Robot desactivado.");
    }
    
    private void crearTareaSensores() {
        if (!verificarRobotActivo()) return;
        
        int tiempo = MenuUtils.leerEntero(scanner, "Tiempo de ejecución (segundos): ", 1, 60);
        Tarea tarea = robot.explorarConSensores(tiempo);
        
        System.out.println("✅ Tarea de sensores creada: " + tarea.getNombreCompleto());
        robot.getProcesador().mostrarEstadoPila();
    }
    
    private void crearTareaComunicaciones() {
        if (!verificarRobotActivo()) return;
        
        int tiempo = MenuUtils.leerEntero(scanner, "Tiempo de ejecución (segundos): ", 1, 60);
        Tarea tarea = robot.enviarDatos(tiempo);
        
        System.out.println("✅ Tarea de comunicaciones creada: " + tarea.getNombreCompleto());
        robot.getProcesador().mostrarEstadoPila();
    }
    
    private void cambiarUbicacion() {
        if (!verificarRobotActivo()) return;
        
        System.out.println("📍 Ubicación actual: " + robot.getUbicacionActual());
        String nuevaUbicacion = MenuUtils.leerTextoNoVacio(scanner, "Nueva ubicación: ");
        
        robot.moverA(nuevaUbicacion);
        System.out.println("🚀 Robot movido a: " + robot.getUbicacionActual());
    }
    
    private void mostrarEstadoCompleto() {
        MenuUtils.limpiarPantalla();
        MenuUtils.mostrarTitulo("📊 ESTADO COMPLETO DEL ROBOT");
        System.out.println(robot.getEstadoCompleto());
        System.out.println(robot.getProcesador().getEstadisticas());
    }
    
    private void simularEmergencia() {
        if (!verificarRobotActivo()) return;
        
        System.out.println("🚨 SIMULANDO EMERGENCIA...");
        robot.emergencia();
        System.out.println("⚠️ Emergencia ejecutada - Tarea actual interrumpida");
        robot.getProcesador().mostrarEstadoPila();
    }
    
    private void limpiarPila() {
        if (!verificarRobotActivo()) return;
        
        ProcesadorTareas procesador = robot.getProcesador();
        
        if (procesador.getTamañoPila() == 0) {
            System.out.println("ℹ️ La pila ya está vacía.");
            return;
        }
        
        System.out.printf("⚠️ Hay %d tarea%s en la pila LIFO.\n",
                        procesador.getTamañoPila(),
                        procesador.getTamañoPila() == 1 ? "" : "s");
        
        if (MenuUtils.confirmar(scanner, "¿Está seguro de limpiar la pila?")) {
            robot.cancelarTodasLasTareas();
            System.out.println("🗑️ Pila limpiada exitosamente.");
        } else {
            System.out.println("❌ Operación cancelada.");
        }
    }
    
    private boolean verificarRobotActivo() {
        if (!robot.isSistemaActivo()) {
            System.out.println("⚠️ El robot debe estar activo para realizar esta operación.");
            return false;
        }
        return true;
    }
    
    /**
     * Ejecuta una demostración automática del concepto LIFO
     */
    public void ejecutarDemostracion() {
        MenuUtils.limpiarPantalla();
        MenuUtils.mostrarTitulo("🎬 DEMOSTRACIÓN LIFO - Last In, First Out");
        System.out.println("Esta demostración mostrará cómo funciona la pila LIFO del robot\n");
        MenuUtils.pausar(scanner);
        
        try {
            System.out.println("1️⃣ Activando robot...");
            robot.activar();
            System.out.println("✅ Robot activado en: " + robot.getUbicacionActual());
            Thread.sleep(2000);
            
            System.out.println("\n2️⃣ Creando tareas para demostrar LIFO:");
            System.out.println("📝 Agregando Tarea A (Sensores, 3s)...");
            robot.explorarConSensores(3);
            Thread.sleep(1000);
            
            System.out.println("📝 Agregando Tarea B (Comunicaciones, 2s)...");
            robot.enviarDatos(2);
            Thread.sleep(1000);
            
            System.out.println("📝 Agregando Tarea C (Sensores, 4s)...");
            robot.explorarConSensores(4);
            Thread.sleep(1000);
            
            System.out.println("\n3️⃣ Estado de la pila LIFO:");
            robot.getProcesador().mostrarEstadoPila();
            Thread.sleep(3000);
            
            System.out.println("4️⃣ Procesando tareas en orden LIFO...");
            System.out.println("⏳ Observe cómo se procesan en orden inverso: C → B → A");
            
            for (int i = 0; i < 12 && robot.getProcesador().estaEjecutando(); i++) {
                Thread.sleep(1000);
                if (i % 2 == 0) {
                    System.out.println("📊 " + robot.getProcesador().getEstadoCompleto());
                }
            }
            
            System.out.println("\n5️⃣ Estadísticas finales:");
            System.out.println(robot.getProcesador().getEstadisticas());
            
            System.out.println("\n🎉 ¡Demostración LIFO completada!");
            System.out.println("💡 Concepto demostrado: Las tareas se ejecutaron en orden C → B → A");
            
        } catch (Exception e) {
            System.out.println("❌ Error en demostración: " + e.getMessage());
        } finally {
            if (robot.isSistemaActivo()) {
                robot.desactivar();
            }
        }
        
        MenuUtils.pausar(scanner);
    }
    
    /**
     * Método principal para ejecutar directamente
     */
    public static void main(String[] args) {
        SimuladorRobot simulador = new SimuladorRobot();
        
        MenuUtils.limpiarPantalla();
        MenuUtils.mostrarTitulo("🤖 SIMULADOR ROBOT EXPLORADOR");
        
        System.out.println("Opciones de inicio:");
        System.out.println("1. 🎬 Ver demostración automática LIFO");
        System.out.println("2. 🎮 Usar simulador interactivo");
        
        Scanner scanner = new Scanner(System.in);
        int opcion = MenuUtils.leerEntero(scanner, ">>> Seleccione una opción (1-2): ", 1, 2);
        
        if (opcion == 1) {
            simulador.ejecutarDemostracion();
            if (MenuUtils.confirmar(scanner, "¿Desea continuar con el simulador interactivo?")) {
                simulador = new SimuladorRobot();
                simulador.iniciar();
            }
        } else {
            simulador.iniciar();
        }
    }
}