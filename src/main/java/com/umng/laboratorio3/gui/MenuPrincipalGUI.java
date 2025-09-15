package com.umng.laboratorio3.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Menú principal con interfaz gráfica
 * 
 * Autores:
 * - Nicolas Isaza Sierra (7004625)
 * - Julián David Galindo Hernández (7004600)
 * - Saúl Alejandro Pérez Estupiñán (7004631)
 * Universidad Militar Nueva Granada - Laboratorio 3
 */
public class MenuPrincipalGUI extends JFrame {
    
    public MenuPrincipalGUI() {
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Laboratorio 3 - Estructuras de Datos UMNG");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelPrincipal.setBackground(Color.WHITE);
        
        // Título
        JPanel panelTitulo = new JPanel(new GridLayout(4, 1, 5, 5));
        panelTitulo.setBackground(Color.WHITE);
        
        JLabel lblTitulo = new JLabel("LABORATORIO 3 - ESTRUCTURAS DE DATOS", JLabel.CENTER);
        lblTitulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        lblTitulo.setForeground(new Color(0, 102, 204));
        
        JLabel lblSubtitulo = new JLabel("Pilas y Colas - Universidad Militar Nueva Granada", JLabel.CENTER);
        lblSubtitulo.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 14));
        lblSubtitulo.setForeground(new Color(102, 102, 102));
        
        JLabel lblAutor = new JLabel("Nicolás Isaza - Ingeniería Mecatrónica III", JLabel.CENTER);
        lblAutor.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        lblAutor.setForeground(new Color(102, 102, 102));
        
        JLabel lblConceptos = new JLabel("FIFO (First In, First Out) | LIFO (Last In, First Out)", JLabel.CENTER);
        lblConceptos.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        lblConceptos.setForeground(new Color(204, 102, 0));
        
        panelTitulo.add(lblTitulo);
        panelTitulo.add(lblSubtitulo);
        panelTitulo.add(lblAutor);
        panelTitulo.add(lblConceptos);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 15, 15));
        panelBotones.setBackground(Color.WHITE);
        
        JButton btnImpresora = new JButton("<html><div style='text-align: center;'>" +
            "<h2 style='color: #0066cc;'>🖨️ SIMULADOR DE IMPRESIÓN</h2>" +
            "<p><b>Cola FIFO</b></p>" +
            "<p>First In, First Out</p>" +
            "<p><i>Los documentos se imprimen por orden de llegada</i></p>" +
            "</div></html>");
        btnImpresora.setPreferredSize(new Dimension(280, 120));
        btnImpresora.setBackground(new Color(230, 245, 255));
        btnImpresora.setBorder(BorderFactory.createRaisedBevelBorder());
        btnImpresora.addActionListener(this::abrirSimuladorImpresion);
        
        JButton btnRobot = new JButton("<html><div style='text-align: center;'>" +
            "<h2 style='color: #cc6600;'>🤖 SIMULADOR DE ROBOT</h2>" +
            "<p><b>Pila LIFO</b></p>" +
            "<p>Last In, First Out</p>" +
            "<p><i>Las tareas recientes tienen prioridad</i></p>" +
            "</div></html>");
        btnRobot.setPreferredSize(new Dimension(280, 120));
        btnRobot.setBackground(new Color(255, 245, 230));
        btnRobot.setBorder(BorderFactory.createRaisedBevelBorder());
        btnRobot.addActionListener(this::abrirSimuladorRobot);
        
        JButton btnConsola = new JButton("<html><div style='text-align: center;'>" +
            "<h2 style='color: #009900;'>💻 VERSIÓN CONSOLA</h2>" +
            "<p><b>Simuladores de texto</b></p>" +
            "<p>Interfaces interactivas</p>" +
            "<p><i>Ejecutar Main.java</i></p>" +
            "</div></html>");
        btnConsola.setPreferredSize(new Dimension(280, 120));
        btnConsola.setBackground(new Color(245, 255, 245));
        btnConsola.setBorder(BorderFactory.createRaisedBevelBorder());
        btnConsola.addActionListener(this::abrirVersionConsola);
        
        JButton btnSalir = new JButton("<html><div style='text-align: center;'>" +
            "<h2 style='color: #cc0000;'>❌ SALIR</h2>" +
            "<p><b>Cerrar aplicación</b></p>" +
            "<p>Finalizar programa</p>" +
            "<p><i>Gracias por usar el laboratorio</i></p>" +
            "</div></html>");
        btnSalir.setPreferredSize(new Dimension(280, 120));
        btnSalir.setBackground(new Color(255, 245, 245));
        btnSalir.setBorder(BorderFactory.createRaisedBevelBorder());
        btnSalir.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea salir?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        
        panelBotones.add(btnImpresora);
        panelBotones.add(btnRobot);
        panelBotones.add(btnConsola);
        panelBotones.add(btnSalir);
        
        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelBotones, BorderLayout.CENTER);
        
        add(panelPrincipal);
        
        setSize(650, 450);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void abrirSimuladorImpresion(ActionEvent e) {
        try {
            SwingUtilities.invokeLater(() -> {
                InterfazImpresora interfaz = new InterfazImpresora();
                interfaz.setVisible(true);
            });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al abrir simulador de impresión:\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void abrirSimuladorRobot(ActionEvent e) {
        try {
            SwingUtilities.invokeLater(() -> {
                InterfazRobot interfaz = new InterfazRobot();
                interfaz.setVisible(true);
            });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al abrir simulador de robot:\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void abrirVersionConsola(ActionEvent e) {
        JOptionPane.showMessageDialog(this,
            "<html><body style='width: 400px;'>" +
            "<h3>Versión de Consola</h3>" +
            "<p>Para ejecutar la versión de consola:</p>" +
            "<ul>" +
            "<li><b>Main.java</b> - Menú principal completo</li>" +
            "<li><b>SimuladorImpresion.java</b> - Solo simulador de impresión</li>" +
            "<li><b>SimuladorRobot.java</b> - Solo simulador de robot</li>" +
            "</ul>" +
            "<p><i>La versión de consola incluye demostraciones automáticas<br>" +
            "y menús interactivos detallados.</i></p>" +
            "</body></html>",
            "Versión Consola", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Método principal
     */
    public static void main(String[] args) {
        // Configurar Look and Feel del sistema de forma segura
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si falla, usar el por defecto
        }
        
        // Configurar propiedades para mejor apariencia
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        
        SwingUtilities.invokeLater(() -> {
            try {
                new MenuPrincipalGUI().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Error al iniciar la aplicación:\n" + e.getMessage(),
                    "Error Fatal", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}