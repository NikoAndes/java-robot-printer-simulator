package com.umng.laboratorio3.gui;

import com.umng.laboratorio3.robot.RobotExplorador;
import com.umng.laboratorio3.robot.ProcesadorTareas;
import com.umng.laboratorio3.models.Tarea;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Interfaz gráfica para el simulador del robot explorador con pila LIFO
 * 
 * Autores:
 * - Nicolas Isaza Sierra (7004625)
 * - Julián David Galindo Hernández (7004600)
 * - Saúl Alejandro Pérez Estupiñán (7004631)
 * Universidad Militar Nueva Granada - Laboratorio 3
 */
public class InterfazRobot extends JFrame {
    private RobotExplorador robot;
    private Timer actualizacionTimer;
    
    // Componentes de la interfaz
    private JList<String> listaTareas;
    private DefaultListModel<String> modeloLista;
    private JProgressBar barraProgreso;
    private JLabel lblEstadoRobot;
    private JLabel lblTareaActual;
    private JLabel lblUbicacion;
    private JLabel lblEstadisticas;
    private JSpinner spinnerTiempoSensores;
    private JSpinner spinnerTiempoComunicaciones;
    private JButton btnTareaSensores;
    private JButton btnTareaComunicaciones;
    private JButton btnActivar;
    private JButton btnDesactivar;
    private JButton btnEmergencia;
    private JButton btnLimpiarPila;
    private JTextField txtUbicacion;
    private JButton btnCambiarUbicacion;
    private JTextArea areaLogs;
    
    public InterfazRobot() {
        initComponents();
        initRobot();
        initTimer();
    }
    
    private void initComponents() {
        setTitle("Simulador Robot Explorador LIFO - UMNG");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Panel superior - Controles
        JPanel panelControles = new JPanel();
        panelControles.setLayout(new BoxLayout(panelControles, BoxLayout.Y_AXIS));
        panelControles.setBorder(new TitledBorder("Controles del Robot Explorador LIFO"));
        panelControles.setBackground(Color.WHITE);
        
        // Activación del robot
        JPanel panelActivacion = new JPanel(new FlowLayout());
        panelActivacion.setBackground(Color.WHITE);
        
        btnActivar = new JButton("🔋 Activar Robot");
        btnActivar.setBackground(new Color(245, 255, 245));
        btnActivar.addActionListener(this::activarRobot);
        panelActivacion.add(btnActivar);
        
        btnDesactivar = new JButton("🛑 Desactivar Robot");
        btnDesactivar.setBackground(new Color(255, 245, 245));
        btnDesactivar.addActionListener(this::desactivarRobot);
        btnDesactivar.setEnabled(false);
        panelActivacion.add(btnDesactivar);
        
        btnEmergencia = new JButton("🚨 Emergencia");
        btnEmergencia.setBackground(new Color(255, 230, 230));
        btnEmergencia.addActionListener(this::simularEmergencia);
        btnEmergencia.setEnabled(false);
        panelActivacion.add(btnEmergencia);
        
        panelControles.add(panelActivacion);
        
        // Creación de tareas
        JPanel panelTareas = new JPanel(new FlowLayout());
        panelTareas.setBackground(Color.WHITE);
        
        panelTareas.add(new JLabel("🔍 Sensores (s):"));
        spinnerTiempoSensores = new JSpinner(new SpinnerNumberModel(3, 1, 60, 1));
        panelTareas.add(spinnerTiempoSensores);
        
        btnTareaSensores = new JButton("Crear Tarea Sensores");
        btnTareaSensores.setBackground(new Color(230, 245, 255));
        btnTareaSensores.addActionListener(this::crearTareaSensores);
        btnTareaSensores.setEnabled(false);
        panelTareas.add(btnTareaSensores);
        
        panelTareas.add(new JLabel("📡 Comunicaciones (s):"));
        spinnerTiempoComunicaciones = new JSpinner(new SpinnerNumberModel(2, 1, 60, 1));
        panelTareas.add(spinnerTiempoComunicaciones);
        
        btnTareaComunicaciones = new JButton("Crear Tarea Comunicaciones");
        btnTareaComunicaciones.setBackground(new Color(255, 245, 230));
        btnTareaComunicaciones.addActionListener(this::crearTareaComunicaciones);
        btnTareaComunicaciones.setEnabled(false);
        panelTareas.add(btnTareaComunicaciones);
        
        panelControles.add(panelTareas);
        
        // Ubicación y limpieza
        JPanel panelUbicacion = new JPanel(new FlowLayout());
        panelUbicacion.setBackground(Color.WHITE);
        
        panelUbicacion.add(new JLabel("🚀 Nueva Ubicación:"));
        txtUbicacion = new JTextField(15);
        panelUbicacion.add(txtUbicacion);
        
        btnCambiarUbicacion = new JButton("Cambiar Ubicación");
        btnCambiarUbicacion.setBackground(new Color(245, 255, 230));
        btnCambiarUbicacion.addActionListener(this::cambiarUbicacion);
        btnCambiarUbicacion.setEnabled(false);
        panelUbicacion.add(btnCambiarUbicacion);
        
        btnLimpiarPila = new JButton("🗑️ Limpiar Pila LIFO");
        btnLimpiarPila.setBackground(new Color(255, 245, 245));
        btnLimpiarPila.addActionListener(this::limpiarPila);
        btnLimpiarPila.setEnabled(false);
        panelUbicacion.add(btnLimpiarPila);
        
        panelControles.add(panelUbicacion);
        
        add(panelControles, BorderLayout.NORTH);
        
        // Panel central
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 10, 0));
        panelCentral.setBackground(Color.WHITE);
        
        // Estado del robot
        JPanel panelEstado = new JPanel(new BorderLayout());
        panelEstado.setBorder(new TitledBorder("Estado del Robot"));
        panelEstado.setBackground(Color.WHITE);
        
        JPanel infoPanel = new JPanel(new GridLayout(7, 1, 5, 5));
        infoPanel.setBackground(Color.WHITE);
        
        lblEstadoRobot = new JLabel("🤖 Estado: Inactivo");
        lblEstadoRobot.setFont(lblEstadoRobot.getFont().deriveFont(Font.BOLD, 14f));
        lblEstadoRobot.setForeground(new Color(102, 102, 102));
        infoPanel.add(lblEstadoRobot);
        
        lblUbicacion = new JLabel("📍 Ubicación: Base de operaciones");
        lblUbicacion.setForeground(new Color(0, 102, 204));
        infoPanel.add(lblUbicacion);
        
        lblTareaActual = new JLabel("⚙️ Tarea actual: Ninguna");
        lblTareaActual.setForeground(new Color(51, 51, 51));
        infoPanel.add(lblTareaActual);
        
        barraProgreso = new JProgressBar(0, 100);
        barraProgreso.setStringPainted(true);
        barraProgreso.setString("0% - Esperando");
        barraProgreso.setForeground(new Color(255, 102, 0));
        infoPanel.add(barraProgreso);
        
        lblEstadisticas = new JLabel("<html><b>📊 Estadísticas:</b><br>Completadas: 0<br>En pila: 0</html>");
        lblEstadisticas.setForeground(new Color(51, 51, 51));
        infoPanel.add(lblEstadisticas);
        
        JLabel lblConcepto = new JLabel("<html><i><b>LIFO:</b> Last In, First Out<br>El último en entrar es el primero en salir</i></html>");
        lblConcepto.setFont(lblConcepto.getFont().deriveFont(Font.ITALIC, 10f));
        lblConcepto.setForeground(new Color(153, 102, 0));
        infoPanel.add(lblConcepto);
        
        JLabel lblFuncionalidad = new JLabel("<html><i>Las tareas recientes tienen prioridad.<br>Ideal para emergencias e interrupciones.</i></html>");
        lblFuncionalidad.setFont(lblFuncionalidad.getFont().deriveFont(Font.ITALIC, 9f));
        lblFuncionalidad.setForeground(new Color(102, 102, 102));
        infoPanel.add(lblFuncionalidad);
        
        panelEstado.add(infoPanel, BorderLayout.NORTH);
        panelCentral.add(panelEstado);
        
        // Pila de tareas
        JPanel panelPila = new JPanel(new BorderLayout());
        panelPila.setBorder(new TitledBorder("Pila LIFO - Orden de Ejecución"));
        panelPila.setBackground(Color.WHITE);
        
        modeloLista = new DefaultListModel<>();
        listaTareas = new JList<>(modeloLista);
        listaTareas.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        listaTareas.setBackground(new Color(248, 248, 248));
        JScrollPane scrollPila = new JScrollPane(listaTareas);
        scrollPila.setPreferredSize(new Dimension(400, 200));
        panelPila.add(scrollPila, BorderLayout.CENTER);
        
        panelCentral.add(panelPila);
        add(panelCentral, BorderLayout.CENTER);
        
        // Panel inferior - Logs
        JPanel panelLogs = new JPanel(new BorderLayout());
        panelLogs.setBorder(new TitledBorder("Registro de Actividad LIFO"));
        panelLogs.setBackground(Color.WHITE);
        
        areaLogs = new JTextArea(6, 80);
        areaLogs.setEditable(false);
        areaLogs.setBackground(new Color(32, 32, 32));
        areaLogs.setForeground(new Color(255, 165, 0));
        areaLogs.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        
        JScrollPane scrollLogs = new JScrollPane(areaLogs);
        panelLogs.add(scrollLogs, BorderLayout.CENTER);
        add(panelLogs, BorderLayout.SOUTH);
        
        setSize(950, 750);
        setLocationRelativeTo(null);
        setResizable(true);
    }
    
    private void initRobot() {
        robot = new RobotExplorador("Robot-GUI-Explorer");
        agregarLog("🤖 Robot explorador inicializado");
        agregarLog("📍 Ubicación inicial: " + robot.getUbicacionActual());
        agregarLog("📋 Concepto: Last In, First Out - El último en entrar es el primero en salir");
    }
    
    private void initTimer() {
        actualizacionTimer = new Timer();
        actualizacionTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> actualizarInterfaz());
            }
        }, 500, 750);
    }
    
    private void actualizarInterfaz() {
        try {
            // Actualizar estado del robot
            if (robot.isSistemaActivo()) {
                lblEstadoRobot.setText("🤖 Estado: Activo (LIFO Operacional)");
                lblEstadoRobot.setForeground(new Color(0, 153, 76));
            } else {
                lblEstadoRobot.setText("🤖 Estado: Inactivo");
                lblEstadoRobot.setForeground(new Color(102, 102, 102));
            }
            
            lblUbicacion.setText("📍 Ubicación: " + robot.getUbicacionActual());
            
            // Actualizar tarea actual y progreso
            ProcesadorTareas procesador = robot.getProcesador();
            if (procesador.estaOcupado() && procesador.getTareaActual() != null) {
                Tarea tareaActual = procesador.getTareaActual();
                lblTareaActual.setText("⚙️ Ejecutando: " + tareaActual.getNombreCompleto());
                lblTareaActual.setForeground(new Color(0, 102, 204));
                
                double progreso = procesador.getProgresoTareaActual();
                barraProgreso.setValue((int) progreso);
                
                String tipoTarea = tareaActual.getNombreCompleto().contains("SENSORES") ? "Sensores" : "Comunicaciones";
                barraProgreso.setString(String.format("%.1f%% - %s", progreso, tipoTarea));
            } else {
                lblTareaActual.setText("⚙️ Tarea actual: Ninguna (Procesador libre)");
                lblTareaActual.setForeground(new Color(153, 153, 153));
                barraProgreso.setValue(0);
                barraProgreso.setString("0% - Esperando tarea");
            }
            
            // Actualizar estadísticas
            int tareasCompletadas = procesador.getTareasCompletadas().size();
            int tareasEnPila = procesador.getTamañoPila();
            lblEstadisticas.setText(String.format(
                "<html><b>📊 Estadísticas LIFO:</b><br>Completadas: %d<br>En pila: %d<br>Total procesadas: %d</html>", 
                tareasCompletadas, tareasEnPila, tareasCompletadas + tareasEnPila + (procesador.estaOcupado() ? 1 : 0)));
            
            // Actualizar lista de pila LIFO
            actualizarListaPila();
            
            // Actualizar estado de botones
            boolean robotActivo = robot.isSistemaActivo();
            btnActivar.setEnabled(!robotActivo);
            btnDesactivar.setEnabled(robotActivo);
            btnTareaSensores.setEnabled(robotActivo);
            btnTareaComunicaciones.setEnabled(robotActivo);
            btnCambiarUbicacion.setEnabled(robotActivo);
            btnEmergencia.setEnabled(robotActivo);
            btnLimpiarPila.setEnabled(robotActivo);
            
        } catch (Exception e) {
            agregarLog("❌ Error actualizando interfaz: " + e.getMessage());
        }
    }
    
    private void actualizarListaPila() {
        modeloLista.clear();
        
        ProcesadorTareas procesador = robot.getProcesador();
        
        // Mostrar tarea actual si existe
        if (procesador.estaOcupado() && procesador.getTareaActual() != null) {
            Tarea tareaActual = procesador.getTareaActual();
            modeloLista.addElement("⚙️ EJECUTANDO: " + tareaActual.toString() + 
                                 String.format(" [%.1f%%]", procesador.getProgresoTareaActual()));
        }
        
        // Mostrar tareas en pila LIFO
        int tareasEnPila = procesador.getTamañoPila();
        for (int i = 1; i <= tareasEnPila; i++) {
            String posicion = (i == 1) ? " (CIMA - SIGUIENTE EN LIFO)" : String.format(" (Posición LIFO: %d)", i);
            modeloLista.addElement("📦 Tarea #" + i + posicion);
        }
        
        if (modeloLista.isEmpty()) {
            modeloLista.addElement("📭 Pila LIFO vacía - No hay tareas pendientes");
        }
    }
    
    private void agregarLog(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            areaLogs.append(String.format("[%tT] %s\n", 
                          System.currentTimeMillis(), mensaje));
            areaLogs.setCaretPosition(areaLogs.getDocument().getLength());
        });
    }
    
    // Manejadores de eventos
    private void activarRobot(ActionEvent e) {
        try {
            robot.activar();
            agregarLog("🔋 Robot activado exitosamente");
            agregarLog("🚀 Sistema LIFO operacional - Las tareas recientes tendrán prioridad");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al activar robot: " + ex.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void desactivarRobot(ActionEvent e) {
        try {
            // Confirmar si hay tareas pendientes
            ProcesadorTareas procesador = robot.getProcesador();
            if (procesador.getTamañoPila() > 0 || procesador.estaOcupado()) {
                int confirmacion = JOptionPane.showConfirmDialog(this,
                    String.format("Hay %d tarea(s) pendiente(s) en la pila LIFO.\n¿Desea cancelarlas y desactivar el robot?", 
                                 procesador.getTamañoPila() + (procesador.estaOcupado() ? 1 : 0)),
                    "Confirmar desactivación", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
                
                if (confirmacion == JOptionPane.YES_OPTION) {
                    robot.cancelarTodasLasTareas();
                    agregarLog("🗑️ Tareas canceladas antes de desactivar");
                } else {
                    return;
                }
            }
            
            robot.desactivar();
            agregarLog("🛑 Robot desactivado");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al desactivar robot: " + ex.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void crearTareaSensores(ActionEvent e) {
        try {
            int tiempo = (Integer) spinnerTiempoSensores.getValue();
            Tarea tarea = robot.explorarConSensores(tiempo);
            agregarLog(String.format("🔍 Tarea de sensores agregada a pila LIFO: %s", 
                      tarea.getNombreCompleto()));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear tarea de sensores: " + ex.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void crearTareaComunicaciones(ActionEvent e) {
        try {
            int tiempo = (Integer) spinnerTiempoComunicaciones.getValue();
            Tarea tarea = robot.enviarDatos(tiempo);
            agregarLog(String.format("📡 Tarea de comunicaciones agregada a pila LIFO: %s", 
                      tarea.getNombreCompleto()));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear tarea de comunicaciones: " + ex.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cambiarUbicacion(ActionEvent e) {
        try {
            String nuevaUbicacion = txtUbicacion.getText().trim();
            if (nuevaUbicacion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese una ubicación válida", 
                                            "Ubicación requerida", JOptionPane.WARNING_MESSAGE);
                txtUbicacion.requestFocus();
                return;
            }
            
            robot.moverA(nuevaUbicacion);
            agregarLog(String.format("🚀 Robot movido a: %s", nuevaUbicacion));
            txtUbicacion.setText("");
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cambiar ubicación: " + ex.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void simularEmergencia(ActionEvent e) {
        try {
            int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de simular una emergencia?\nEsto interrumpirá la tarea actual.",
                "Confirmar emergencia", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                robot.emergencia();
                agregarLog("🚨 EMERGENCIA SIMULADA - Tarea actual interrumpida por protocolo LIFO");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error en emergencia: " + ex.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limpiarPila(ActionEvent e) {
        try {
            ProcesadorTareas procesador = robot.getProcesador();
            
            if (procesador.getTamañoPila() == 0) {
                JOptionPane.showMessageDialog(this, "La pila LIFO ya está vacía", 
                                            "Pila vacía", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            int confirmacion = JOptionPane.showConfirmDialog(this,
                String.format("¿Está seguro de limpiar la pila LIFO?\nSe cancelarán %d tarea(s) pendiente(s).", 
                             procesador.getTamañoPila()),
                "Confirmar limpieza de pila LIFO", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                robot.cancelarTodasLasTareas();
                agregarLog("🗑️ Pila LIFO limpiada - Todas las tareas pendientes canceladas");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al limpiar pila: " + ex.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    public void dispose() {
        if (actualizacionTimer != null) {
            actualizacionTimer.cancel();
        }
        if (robot != null && robot.isSistemaActivo()) {
            robot.desactivar();
        }
        super.dispose();
    }
    
    /**
     * Método principal para ejecutar la interfaz independientemente
     */
    public static void main(String[] args) {
        // Configurar Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Usar por defecto si falla
        }
        
        // Propiedades para mejor apariencia
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        
        SwingUtilities.invokeLater(() -> {
            try {
                new InterfazRobot().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Error al iniciar la interfaz del robot:\n" + e.getMessage(),
                    "Error Fatal", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}

