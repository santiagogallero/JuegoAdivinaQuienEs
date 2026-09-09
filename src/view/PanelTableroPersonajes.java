package view;

import model.Personaje;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * Grilla de tarjetas de personajes (avatar + nombre) que se puede usar tanto
 * para elegir el personaje secreto como para arriesgar una adivinanza.
 * Cuando la lista de "candidatos vigentes" cambia, los que ya fueron
 * descartados se muestran atenuados/tapados en vez de desaparecer, para que
 * el jugador vea el proceso de descarte.
 */
public class PanelTableroPersonajes extends JPanel {

    private final List<Personaje> todosLosPersonajes;
    private final Consumer<Personaje> alHacerClic;

    public PanelTableroPersonajes(List<Personaje> todosLosPersonajes, Consumer<Personaje> alHacerClic) {
        this.todosLosPersonajes = todosLosPersonajes;
        this.alHacerClic = alHacerClic;
        setLayout(new GridLayout(0, 6, 8, 8));
        setBorder(new EmptyBorder(8, 8, 8, 8));
        setOpaque(false);
        construir(todosLosPersonajes);
    }

    private void construir(List<Personaje> vigentes) {
        removeAll();
        for (Personaje p : todosLosPersonajes) {
            JPanel tarjeta = AvatarPersonaje.crearTarjeta(p, 64);
            boolean esVigente = vigentes.contains(p);

            AvatarPersonaje avatar = (AvatarPersonaje) tarjeta.getClientProperty("avatar");
            avatar.setTapado(!esVigente);

            if (esVigente) {
                tarjeta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                tarjeta.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        alHacerClic.accept(p);
                    }
                });
            }
            add(tarjeta);
        }
        revalidate();
        repaint();
    }

    /** Actualiza cuales personajes siguen siendo candidatos validos (los demas se tapan). */
    public void actualizarCandidatosVigentes(List<Personaje> vigentes) {
        construir(vigentes);
    }
}
