package view;

import controller.PartidaControllerGUI;
import model.Filtro;
import model.JugadorHumanoGUI;
import model.Marcador;
import model.Personaje;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Ventana principal del juego. Usa CardLayout para navegar entre:
 * menu -> seleccion de secreto (modo humano) -> partida -> marcador.
 * Todo el trabajo del motor del juego pasa por PartidaControllerGUI;
 * esta clase solo pinta y reacciona a clics.
 */
public class VentanaPrincipal extends JFrame implements PartidaControllerGUI.PartidaListener {

    private static final Color COLOR_FONDO = new Color(238, 236, 227);

    private final PartidaControllerGUI controller = new PartidaControllerGUI();
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contenedor = new JPanel(cardLayout);

    private String nombreUsuario = "Jugador";

    // --- pantalla de partida (se reconstruye cada vez que arranca una partida) ---
    private JPanel panelPartida;
    private PanelTableroPersonajes tableroCandidatosHumano;
    private JTextArea bitacora;
    private JComboBox<Filtro> comboFiltros;
    private JButton botonPreguntar;
    private JLabel labelTurno;
    private JugadorHumanoGUI humanoActual;
    private boolean modoExhibicion;
    private JPanel panelPartidaMostrado; // referencia al card "partida"/"partida-exhibicion" actualmente en el contenedor

    public VentanaPrincipal() {
        super("Adivina Quien Es");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));

        contenedor.setBackground(COLOR_FONDO);
        contenedor.add(crearPantallaMenu(), "menu");
        add(contenedor);

        mostrarMenu();
    }

    private void mostrarMenu() {
        cardLayout.show(contenedor, "menu");
    }

    // ---------------------------------------------------------------
    // Pantalla: Menu principal
    // ---------------------------------------------------------------

    private JPanel crearPantallaMenu() {
        JPanel panel = new JPanel();
        panel.setBackground(COLOR_FONDO);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(40, 80, 40, 80));

        JLabel titulo = new JLabel("ADIVINA QUIEN ES");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 32));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField campoNombre = new JTextField("Jugador");
        campoNombre.setMaximumSize(new Dimension(300, 32));
        campoNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        campoNombre.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizar(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizar(); }
            private void actualizar() {
                String texto = campoNombre.getText().trim();
                nombreUsuario = texto.isEmpty() ? "Jugador" : texto;
            }
        });

        JButton btnBasica = new JButton("Jugar contra Maquina Basica");
        JButton btnAsertiva = new JButton("Jugar contra Maquina Asertiva");
        JButton btnExhibicion = new JButton("Modo Maquina vs Maquina (exhibicion)");
        JButton btnMarcador = new JButton("Ver marcador");

        for (JButton b : new JButton[]{btnBasica, btnAsertiva, btnExhibicion, btnMarcador}) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(320, 40));
        }

        btnBasica.addActionListener(e -> irAPantallaSeleccionSecreto(false));
        btnAsertiva.addActionListener(e -> irAPantallaSeleccionSecreto(true));
        btnExhibicion.addActionListener(e -> arrancarModoExhibicion());
        btnMarcador.addActionListener(e -> mostrarPantallaMarcador());

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("") {{ setAlignmentX(Component.CENTER_ALIGNMENT); }});
        JLabel labelNombre = new JLabel("Tu nombre:");
        labelNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(labelNombre);
        panel.add(Box.createVerticalStrut(4));
        panel.add(campoNombre);
        panel.add(Box.createVerticalStrut(30));
        panel.add(btnBasica);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnAsertiva);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnExhibicion);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnMarcador);

        JPanel centrado = new JPanel(new GridBagLayout());
        centrado.setBackground(COLOR_FONDO);
        centrado.add(panel);
        return centrado;
    }

    // ---------------------------------------------------------------
    // Pantalla: eleccion de personaje secreto (modo Humano vs Maquina)
    // ---------------------------------------------------------------

    private JPanel panelSeleccionSecretoMostrado;

    private void irAPantallaSeleccionSecreto(boolean maquinaAsertiva) {
        if (panelSeleccionSecretoMostrado != null) {
            contenedor.remove(panelSeleccionSecretoMostrado);
            panelSeleccionSecretoMostrado = null;
        }
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel titulo = new JLabel("Elegi tu personaje secreto", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        panel.add(titulo, BorderLayout.NORTH);

        List<Personaje> personajes = controller.getPersonajesPorGenero();
        PanelTableroPersonajes tablero = new PanelTableroPersonajes(personajes, personajeElegido -> {
            contenedor.remove(panel);
            panelSeleccionSecretoMostrado = null;
            iniciarPartidaHumanoVsMaquina(personajeElegido, maquinaAsertiva);
        });

        JScrollPane scroll = new JScrollPane(tablero);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scroll, BorderLayout.CENTER);

        JButton volver = new JButton("Volver al menu");
        volver.addActionListener(e -> {
            contenedor.remove(panel);
            panelSeleccionSecretoMostrado = null;
            mostrarMenu();
        });
        panel.add(volver, BorderLayout.SOUTH);

        panelSeleccionSecretoMostrado = panel;
        contenedor.add(panel, "seleccion-secreto");
        cardLayout.show(contenedor, "seleccion-secreto");
    }

    // ---------------------------------------------------------------
    // Pantalla: partida en curso (humano vs maquina o exhibicion)
    // ---------------------------------------------------------------

    private void iniciarPartidaHumanoVsMaquina(Personaje secretoHumano, boolean maquinaAsertiva) {
        modoExhibicion = false;
        quitarPanelPartidaAnterior();
        construirPantallaPartida(true);
        panelPartidaMostrado = panelPartida;
        humanoActual = controller.iniciarHumanoVsMaquina(nombreUsuario, secretoHumano, maquinaAsertiva, this);
        cardLayout.show(contenedor, "partida");
    }

    private void arrancarModoExhibicion() {
        modoExhibicion = true;
        humanoActual = null;
        tableroCandidatosHumano = null;
        botonPreguntar = null;
        comboFiltros = null;
        quitarPanelPartidaAnterior();
        construirPantallaPartida(false);
        panelPartidaMostrado = panelPartida;
        contenedor.add(panelPartida, "partida-exhibicion");
        controller.iniciarMaquinaVsMaquina(this);
        cardLayout.show(contenedor, "partida-exhibicion");
    }

    private void quitarPanelPartidaAnterior() {
        if (panelPartidaMostrado != null) {
            contenedor.remove(panelPartidaMostrado);
            panelPartidaMostrado = null;
        }
    }

    private void construirPantallaPartida(boolean esHumano) {
        panelPartida = new JPanel(new BorderLayout(8, 8));
        panelPartida.setBackground(COLOR_FONDO);
        panelPartida.setBorder(new EmptyBorder(12, 12, 12, 12));

        labelTurno = new JLabel("Preparando partida...", SwingConstants.CENTER);
        labelTurno.setFont(new Font("SansSerif", Font.BOLD, 18));
        panelPartida.add(labelTurno, BorderLayout.NORTH);

        bitacora = new JTextArea();
        bitacora.setEditable(false);
        bitacora.setLineWrap(true);
        bitacora.setWrapStyleWord(true);
        bitacora.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollBitacora = new JScrollPane(bitacora);
        scrollBitacora.setPreferredSize(new Dimension(300, 100));

        if (esHumano) {
            tableroCandidatosHumano = new PanelTableroPersonajes(
                    controller.getPersonajesPorGenero(), this::elegirAdivinanzaHumano);
            JScrollPane scrollTablero = new JScrollPane(tableroCandidatosHumano);
            scrollTablero.setBorder(BorderFactory.createTitledBorder("Tus candidatos (clic para adivinar)"));

            JPanel panelAcciones = new JPanel();
            panelAcciones.setBackground(COLOR_FONDO);
            panelAcciones.setLayout(new BoxLayout(panelAcciones, BoxLayout.Y_AXIS));
            panelAcciones.setBorder(BorderFactory.createTitledBorder("Preguntar un filtro"));

            comboFiltros = new JComboBox<>(controller.getFiltrosBase().toArray(new Filtro[0]));
            comboFiltros.setMaximumSize(new Dimension(280, 28));
            comboFiltros.setAlignmentX(Component.CENTER_ALIGNMENT);
            comboFiltros.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                                boolean isSelected, boolean cellHasFocus) {
                    String texto = (value instanceof Filtro) ? ((Filtro) value).getDescripciones() : String.valueOf(value);
                    return super.getListCellRendererComponent(list, texto, index, isSelected, cellHasFocus);
                }
            });

            botonPreguntar = new JButton("Preguntar este filtro");
            botonPreguntar.setAlignmentX(Component.CENTER_ALIGNMENT);
            botonPreguntar.addActionListener(e -> {
                Filtro filtro = (Filtro) comboFiltros.getSelectedItem();
                if (filtro != null && humanoActual != null) {
                    deshabilitarAcciones();
                    humanoActual.responderConPregunta(filtro);
                }
            });

            JLabel textoAyuda = new JLabel("O hace clic directo en un personaje para arriesgar tu adivinanza.");
            textoAyuda.setAlignmentX(Component.CENTER_ALIGNMENT);
            textoAyuda.setFont(textoAyuda.getFont().deriveFont(Font.ITALIC, 12f));

            panelAcciones.add(Box.createVerticalStrut(6));
            panelAcciones.add(comboFiltros);
            panelAcciones.add(Box.createVerticalStrut(6));
            panelAcciones.add(botonPreguntar);
            panelAcciones.add(Box.createVerticalStrut(12));
            panelAcciones.add(textoAyuda);

            JPanel centro = new JPanel(new BorderLayout(8, 8));
            centro.setOpaque(false);
            centro.add(scrollTablero, BorderLayout.CENTER);
            centro.add(panelAcciones, BorderLayout.SOUTH);
            centro.add(scrollBitacora, BorderLayout.NORTH);

            panelPartida.add(centro, BorderLayout.CENTER);
        } else {
            panelPartida.add(scrollBitacora, BorderLayout.CENTER);
        }

        JButton volver = new JButton("Volver al menu");
        volver.addActionListener(e -> {
            quitarPanelPartidaAnterior();
            mostrarMenu();
        });
        panelPartida.add(volver, BorderLayout.SOUTH);

        if (esHumano) {
            contenedor.add(panelPartida, "partida");
        }
    }

    private void elegirAdivinanzaHumano(Personaje personaje) {
        if (humanoActual != null) {
            deshabilitarAcciones();
            humanoActual.responderConAdivinanza(personaje);
        }
    }

    private void deshabilitarAcciones() {
        if (botonPreguntar != null) botonPreguntar.setEnabled(false);
        if (comboFiltros != null) comboFiltros.setEnabled(false);
        if (tableroCandidatosHumano != null) tableroCandidatosHumano.setEnabled(false);
    }

    private void habilitarAcciones() {
        if (botonPreguntar != null) botonPreguntar.setEnabled(true);
        if (comboFiltros != null) comboFiltros.setEnabled(true);
        if (tableroCandidatosHumano != null) tableroCandidatosHumano.setEnabled(true);
    }

    // ---------------------------------------------------------------
    // Pantalla: marcador
    // ---------------------------------------------------------------

    private JPanel panelMarcadorMostrado;

    private void mostrarPantallaMarcador() {
        if (panelMarcadorMostrado != null) {
            contenedor.remove(panelMarcadorMostrado);
            panelMarcadorMostrado = null;
        }
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel titulo = new JLabel("Tabla de posiciones", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        panel.add(titulo, BorderLayout.NORTH);

        List<Marcador> tabla = controller.obtenerMarcadores();
        String[] columnas = {"Jugador", "Victorias"};
        Object[][] datos = new Object[tabla.size()][2];
        for (int i = 0; i < tabla.size(); i++) {
            datos[i][0] = tabla.get(i).getUsuario();
            datos[i][1] = tabla.get(i).getPartidasGanadas();
        }
        JTable jTable = new JTable(datos, columnas);
        jTable.setEnabled(false);
        jTable.setRowHeight(26);
        panel.add(new JScrollPane(jTable), BorderLayout.CENTER);

        JButton volver = new JButton("Volver al menu");
        volver.addActionListener(e -> {
            contenedor.remove(panel);
            panelMarcadorMostrado = null;
            mostrarMenu();
        });
        panel.add(volver, BorderLayout.SOUTH);

        panelMarcadorMostrado = panel;
        contenedor.add(panel, "marcador");
        cardLayout.show(contenedor, "marcador");
    }

    // ---------------------------------------------------------------
    // PartidaListener: estos metodos los llama el hilo de la partida,
    // por eso todo el trabajo se reenvia al Event Dispatch Thread con invokeLater.
    // ---------------------------------------------------------------

    @Override
    public void onTurno(String nombreJugadorEnTurno, int candidatosRestantes, boolean esHumano) {
        SwingUtilities.invokeLater(() -> {
            labelTurno.setText("Turno de " + nombreJugadorEnTurno + " - candidatos restantes: " + candidatosRestantes);
            agregarLinea("--- Turno de " + nombreJugadorEnTurno + " (" + candidatosRestantes + " candidatos) ---");
            if (esHumano) {
                habilitarAcciones();
            } else {
                deshabilitarAcciones();
            }
        });
    }

    @Override
    public void onPregunta(String nombreQuePregunta, String descripcionFiltro, boolean respuesta) {
        SwingUtilities.invokeLater(() -> {
            agregarLinea(nombreQuePregunta + " pregunta: \"" + descripcionFiltro + "\" -> " + (respuesta ? "SI" : "NO"));
            if (tableroCandidatosHumano != null && humanoActual != null) {
                tableroCandidatosHumano.actualizarCandidatosVigentes(humanoActual.getCandidatosRestantes());
            }
        });
    }

    @Override
    public void onAdivinanza(String nombreQueAdivina, String personajeAdivinado, boolean acerto) {
        SwingUtilities.invokeLater(() -> {
            agregarLinea(nombreQueAdivina + " arriesga: " + personajeAdivinado
                    + " -> " + (acerto ? "ACERTO" : "SE EQUIVOCO"));
        });
    }

    @Override
    public void onFinDePartida(String ganador) {
        SwingUtilities.invokeLater(() -> {
            agregarLinea("");
            agregarLinea(">>> Gana " + ganador + " <<<");
            deshabilitarAcciones();
            JOptionPane.showMessageDialog(this, "Gana " + ganador, "Fin de la partida", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void agregarLinea(String texto) {
        bitacora.append(texto + "\n");
        bitacora.setCaretPosition(bitacora.getDocument().getLength());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}
