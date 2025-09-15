package com.umng.laboratorio3.gui;

import com.umng.laboratorio3.impresora.ControladorImpresion;
import com.umng.laboratorio3.impresora.Impresora;
import com.umng.laboratorio3.models.Documento;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Interfaz gráfica para el simulador de cola de impresión FIFO
 * 
 * Autores:
 * - Nicolas Isaza Sierra (7004625)
 * - Julián David Galindo Hernández (7004600)
 * - Saúl Alejandro Pérez Estupiñán (7004631)
 * Universidad Militar Nueva Granada - Laboratorio 3
 */
public class InterfazImpresora extends JFrame {
    private ControladorImpresion controlador;
    private Timer actualizacionTimer;
    
    // Componentes de la interfaz
    private JList<String> listaDocumentos;
    private DefaultListModel<String> modeloLista;
    private JProgressBar barraProgreso;
    private JLabel lblEstadoImpresora;
    private JLabel lblDocumentoActual;
    private JLabel lblEstadisticas;
    private JTextField txtNombreDoc;
    private JSpinner spinnerPaginas;
    private JSpinner spinnerVelocidad;
    private JButton btnAgregar;
    private JButton btnIniciar;
    private JButton btnDetener;
    private JButton btnLimpiar;
    private JTextArea areaLogs;
    
    public InterfazImpresora() {
        initComponents();
        initControlador();
        initTimer();
    }
    
    private void initComponents() {
        setTitle("Simulador de Cola de Impresión FIFO - UMNG");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Panel superior - Controles
        JPanel panelControles = new JPanel();
        panelControles.setLayout(new BoxLayout(panelControles, BoxLayout.Y_AXIS));
        panelControles.setBorder(new TitledBorder("Controles de Impresión FIFO"));
        panelControles.setBackground(Color.WHITE);
        
        // Controles de documento
        JPanel panelDocumento = new JPanel(new FlowLayout());
        panelDocumento.setBackground(Color.WHITE);
        panelDocumento.add(new JLabel("Nombre:"));
        txtNombreDoc = new JTextField(15);
        panelDocumento.add(txtNombreDoc);
        
        panelDocumento.add(new JLabel("Páginas:"));
        spinnerPaginas = new JSpinner(new SpinnerNumberModel(5, 1, 100, 1));
        panelDocumento.add(spinnerPaginas);
        
        btnAgregar = new JButton("📄 Agregar a Cola FIFO");
        btnAgregar.setBackground(new Color(230, 245, 255));
        btnAgregar.addActionListener(this::agregarDocumento);
        panelDocumento.add(btnAgregar);
        
        panelControles.add(panelDocumento);
        
        // Controles de procesamiento
        JPanel panelProcesamiento = new JPanel(new FlowLayout());
        panelProcesamiento.setBackground(Color.WHITE);
        
        btnIniciar = new JButton("▶️ Iniciar FIFO");
        btnIniciar.setBackground(new Color(245, 255, 245));
        btnIniciar.addActionListener(this::iniciarProcesamiento);
        panelProcesamiento.add(btnIniciar);
        
        btnDetener = new JButton("⏹️ Detener");
        btnDetener.setBackground(new Color(255, 245, 245));
        btnDetener.addActionListener(this::detenerProcesamiento);
        btnDetener.setEnabled(false);
        panelProcesamiento.add(btnDetener);
        
        panelProcesamiento.add(new JLabel("Velocidad (s/pág):"));
        spinnerVelocidad = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        spinnerVelocidad.addChangeListener(e -> cambiarVelocidad());
        panelProcesamiento.add(spinnerVelocidad);
        
        btnLimpiar = new JButton("🗑️ Limpiar Cola");
        btnLimpiar.setBackground(new Color(255, 245, 230));
        btnLimpiar.addActionListener(this::limpiarCola);
        panelProcesamiento.add(btnLimpiar);
        
        panelControles.add(panelProcesamiento);
        
        add(panelControles, BorderLayout.NORTH);
        
        // Panel central
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 10, 0));
        panelCentral.setBackground(Color.WHITE);
        
        // Estado de impresión
        JPanel panelEstado = new JPanel(new BorderLayout());
        panelEstado.setBorder(new TitledBorder("Estado de Impresión"));
        panelEstado.setBackground(Color.WHITE);
        
        JPanel infoPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        infoPanel.setBackground(Color.WHITE);
        
        lblEstadoImpresora = new JLabel("🖨️ Estado: Inactiva");
        lblEstadoImpresora.setFont(lblEstadoImpresora.getFont().deriveFont(Font.BOLD, 14f));
        lblEstadoImpresora.setForeground(new Color(102, 102, 102));
        infoPanel.add(lblEstadoImpresora);
        
        lblDocumentoActual = new JLabel("📄 Documento: Ninguno");
        lblDocumentoActual.setForeground(new Color(0, 102, 204));
        infoPanel.add(lblDocumentoActual);
        
        barraProgreso = new JProgressBar(0, 100);
        barraProgreso.setStringPainted(true);
        barraProgreso.setString("0% - Esperando");
        barraProgreso.setForeground(new Color(0, 153, 76));
        infoPanel.add(barraProgreso);
        
        lblEstadisticas = new JLabel("<html><b>📊 Estadísticas:</b><br>Completados: 0<br>En cola: 0</html>");
        lblEstadisticas.setForeground(new Color(51, 51, 51));
        infoPanel.add(lblEstadisticas);
        
        JLabel lblConcepto = new JLabel("<html><i><b>FIFO:</b> First In, First Out<br>El primero en entrar es el primero en salir</i></html>");
        lblConcepto.setFont(lblConcepto.getFont().deriveFont(Font.ITALIC, 10f));
        lblConcepto.setForeground(new Color(153, 102, 0));
        infoPanel.add(lblConcepto);
        
        panelEstado.add(infoPanel, BorderLayout.NORTH);
        panelCentral.add(panelEstado);
        
        // Cola de documentos
        JPanel panelCola = new JPanel(new BorderLayout());
        panelCola.setBorder(new TitledBorder("Cola FIFO - Orden de Procesamiento"));
        panelCola.setBackground(Color.WHITE);
        
        modeloLista = new DefaultListModel<>();
        listaDocumentos = new JList<>(modeloLista);
        listaDocumentos.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        listaDocumentos.setBackground(new Color(248, 248, 248));
        JScrollPane scrollCola = new JScrollPane(listaDocumentos);
        scrollCola.setPreferredSize(new Dimension(350, 200));
        panelCola.add(scrollCola, BorderLayout.CENTER);
        
        panelCentral.add(panelCola);
        add(panelCentral, BorderLayout.CENTER);
        
        // Panel inferior - Logs
        JPanel panelLogs = new JPanel(new BorderLayout());
        panelLogs.setBorder(new TitledBorder("Registro de Actividad FIFO"));
        panelLogs.setBackground(Color.WHITE);
        
        areaLogs = new JTextArea(6, 80);
        areaLogs.setEditable(false);
        areaLogs.setBackground(new Color(32, 32, 32));
        areaLogs.setForeground(new Color(0, 255, 128));
        areaLogs.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        
        JScrollPane scrollLogs = new JScrollPane(areaLogs);
        panelLogs.add(scrollLogs, BorderLayout.CENTER);
        add(panelLogs, BorderLayout.SOUTH);
        
        setSize(900, 700);
        setLocationRelativeTo(null);
        setResizable(true);
    }
    
    private void initControlador() {
        Impresora impresora = new Impresora("Impresora-GUI-FIFO", 1);
        controlador = new ControladorImpresion(impresora);
        agregarLog("🖨️ Simulador de impresión FIFO iniciado");
        agregarLog("📋 Concepto: First In, First Out - El primero en entrar es el primero en salir");
    }
    
    private void initTimer() {
        actualizacionTimer = new Timer();
        actualizacionTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> actualizarInterfaz());
            }
        }, 500, 500);
    }
    
    private void actualizarInterfaz() {
        try {
            // Actualizar estado de la impresora
            Impresora impresora = controlador.getImpresora();
            if (impresora.estaImprimiendo()) {
                lblEstadoImpresora.setText("🖨️ Estado: Imprimiendo (FIFO Activo)");
                lblEstadoImpresora.setForeground(new Color(0, 153, 76));
            } else {
                lblEstadoImpresora.setText("🖨️ Estado: " + (controlador.estaEjecutando() ? "Esperando documentos" : "Inactiva"));
                lblEstadoImpresora.setForeground(new Color(102, 102, 102));
            }
            
            // Actualizar documento actual y progreso
            if (impresora.estaImprimiendo() && impresora.getDocumentoActual() != null) {
                Documento docActual = impresora.getDocumentoActual();
                lblDocumentoActual.setText("📄 Documento: " + docActual.getNombre());
                lblDocumentoActual.setForeground(new Color(0, 102, 204));
                
                double progreso = impresora.getProgreso();
                barraProgreso.setValue((int) progreso);
                barraProgreso.setString(String.format("%.1f%% - %s", 
                                      progreso, docActual.getEstadoImpresion()));
            } else {
                lblDocumentoActual.setText("📄 Documento: Ninguno");
                lblDocumentoActual.setForeground(new Color(153, 153, 153));
                barraProgreso.setValue(0);
                barraProgreso.setString("0% - Esperando documento");
            }
            
            // Actualizar estadísticas
            int docsCompletados = controlador.getDocumentosCompletados().size();
            int docsEnCola = controlador.getTamañoCola();
            lblEstadisticas.setText(String.format(
                "<html><b>📊 Estadísticas FIFO:</b><br>Completados: %d<br>En cola: %d<br>Total procesados: %d</html>", 
                docsCompletados, docsEnCola, docsCompletados + docsEnCola + (impresora.estaImprimiendo() ? 1 : 0)));
            
            // Actualizar lista de cola FIFO
            actualizarListaCola();
            
            // Actualizar estado de botones
            btnIniciar.setEnabled(!controlador.estaEjecutando());
            btnDetener.setEnabled(controlador.estaEjecutando());
            
        } catch (Exception e) {
            agregarLog("❌ Error actualizando interfaz: " + e.getMessage());
        }
    }
    
    private void actualizarListaCola() {
        modeloLista.clear();
        
        // Mostrar documento que se está imprimiendo
        if (controlador.getImpresora().estaImprimiendo() && 
            controlador.getImpresora().getDocumentoActual() != null) {
            Documento docActual = controlador.getImpresora().getDocumentoActual();
            modeloLista.addElement("🖨️ IMPRIMIENDO: " + docActual.toString() + 
                                 String.format(" [%.1f%%]", controlador.getImpresora().getProgreso()));
        }
        
        // Mostrar documentos en cola FIFO
        int docsEnCola = controlador.getTamañoCola();
        for (int i = 1; i <= docsEnCola; i++) {
            String posicion = (i == 1) ? " (SIGUIENTE EN FIFO)" : String.format(" (Posición FIFO: %d)", i);
            modeloLista.addElement("📄 Documento #" + i + posicion);
        }
        
        if (modeloLista.isEmpty()) {
            modeloLista.addElement("📭 Cola FIFO vacía - No hay documentos pendientes");
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
    private void agregarDocumento(ActionEvent e) {
        try {
            String nombre = txtNombreDoc.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese un nombre para el documento", 
                                            "Nombre requerido", JOptionPane.WARNING_MESSAGE);
                txtNombreDoc.requestFocus();
                return;
            }
            
            int paginas = (Integer) spinnerPaginas.getValue();
            
            controlador.crearYAgregarDocumento(nombre, paginas);
            agregarLog(String.format("📄 Documento agregado a cola FIFO: %s (%d páginas)", nombre, paginas));
            
            // Limpiar campos
            txtNombreDoc.setText("");
            spinnerPaginas.setValue(5);
            txtNombreDoc.requestFocus();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar documento: " + ex.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void iniciarProcesamiento(ActionEvent e) {
        try {
            controlador.iniciarProcesamiento();
            agregarLog("▶️ Procesamiento FIFO iniciado - Los documentos se procesarán por orden de llegada");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al iniciar procesamiento: " + ex.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void detenerProcesamiento(ActionEvent e) {
        try {
            controlador.detenerProcesamiento();
            agregarLog("⏹️ Procesamiento FIFO detenido");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al detener procesamiento: " + ex.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cambiarVelocidad() {
        try {
            int nuevaVelocidad = (Integer) spinnerVelocidad.getValue();
            controlador.getImpresora().setTiempoPorPagina(nuevaVelocidad);
            agregarLog(String.format("⚙️ Velocidad de impresión cambiada a %d segundo%s por página", 
                      nuevaVelocidad, nuevaVelocidad == 1 ? "" : "s"));
        } catch (Exception ex) {
            agregarLog("❌ Error al cambiar velocidad: " + ex.getMessage());
        }
    }
    
    private void limpiarCola(ActionEvent e) {
        try {
            if (controlador.getTamañoCola() == 0) {
                JOptionPane.showMessageDialog(this, "La cola FIFO ya está vacía", 
                                            "Cola vacía", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            int confirmacion = JOptionPane.showConfirmDialog(this,
                String.format("¿Está seguro de limpiar la cola FIFO?\nSe cancelarán %d documento(s) pendiente(s).", 
                             controlador.getTamañoCola()),
                "Confirmar limpieza de cola FIFO", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                int cancelados = controlador.limpiarCola();
                agregarLog(String.format("🗑️ Cola FIFO limpiada - %d documento%s cancelado%s", 
                          cancelados, cancelados == 1 ? "" : "s", cancelados == 1 ? "" : "s"));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al limpiar cola: " + ex.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    public void dispose() {
        if (actualizacionTimer != null) {
            actualizacionTimer.cancel();
        }
        if (controlador != null && controlador.estaEjecutando()) {
            controlador.detenerProcesamiento();
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
                new InterfazImpresora().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Error al iniciar la interfaz de impresión:\n" + e.getMessage(),
                        "Error Fatal", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}
