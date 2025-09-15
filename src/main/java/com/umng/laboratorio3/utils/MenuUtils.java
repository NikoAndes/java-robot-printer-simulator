package com.umng.laboratorio3.utils;

import java.util.Scanner;

/**
 * Utilidades para menús y entrada de datos
 * 
 * @author Nicolás Isaza
 * Universidad Militar Nueva Granada - Laboratorio 3
 */
public class MenuUtils {
    
    /**
     * Limpia la pantalla de la consola
     */
    public static void limpiarPantalla() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[2J\033[H");
                System.out.flush();
            }
        } catch (Exception e) {
            // Si no se puede limpiar, agregar líneas vacías
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
    
    /**
     * Pausa hasta que el usuario presione Enter
     */
    public static void pausar(Scanner scanner) {
        System.out.print("\n>>> Presiona Enter para continuar...");
        scanner.nextLine();
    }
    
    /**
     * Lee un entero con validación
     */
    public static int leerEntero(Scanner scanner, String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Por favor ingrese un número válido.");
            }
        }
    }
    
    /**
     * Lee un entero con validación y rango
     */
    public static int leerEntero(Scanner scanner, String mensaje, int min, int max) {
        while (true) {
            int valor = leerEntero(scanner, mensaje);
            if (valor >= min && valor <= max) {
                return valor;
            }
            System.out.printf("❌ El valor debe estar entre %d y %d.\n", min, max);
        }
    }
    
    /**
     * Lee texto con validación básica
     */
    public static String leerTexto(Scanner scanner, String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }
    
    /**
     * Lee texto no vacío
     */
    public static String leerTextoNoVacio(Scanner scanner, String mensaje) {
        while (true) {
            String texto = leerTexto(scanner, mensaje);
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("❌ El texto no puede estar vacío.");
        }
    }
    
    /**
     * Muestra un título centrado
     */
    public static void mostrarTitulo(String titulo) {
        int ancho = 60;
        String linea = "═".repeat(ancho);
        
        System.out.println(linea);
        int espacios = (ancho - titulo.length()) / 2;
        System.out.println(" ".repeat(espacios) + titulo);
        System.out.println(linea);
    }
    
    /**
     * Muestra un subtítulo
     */
    public static void mostrarSubtitulo(String subtitulo) {
        int ancho = 60;
        int espacios = (ancho - subtitulo.length()) / 2;
        System.out.println(" ".repeat(espacios) + subtitulo);
        System.out.println("═".repeat(ancho));
    }
    
    /**
     * Confirma una acción (s/n)
     */
    public static boolean confirmar(Scanner scanner, String mensaje) {
        while (true) {
            String respuesta = leerTexto(scanner, mensaje + " (s/n): ").toLowerCase();
            if (respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("y") || respuesta.equals("yes")) {
                return true;
            } else if (respuesta.equals("n") || respuesta.equals("no")) {
                return false;
            } else {
                System.out.println("❌ Por favor responda 's' para sí o 'n' para no.");
            }
        }
    }
}