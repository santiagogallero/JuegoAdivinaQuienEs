package view;

import enums.ColorPelo;
import model.Personaje;

import javax.swing.*;
import java.awt.*;

/**
 * Dibuja un avatar simple por codigo para un Personaje, reflejando sus
 * atributos reales: color de pelo, calvicie, lentes y genero.
 * No usa ningun archivo de imagen externo.
 */
public class AvatarPersonaje extends JComponent {

    private final Personaje personaje;
    private boolean tapado; // usado en el tablero para simular la ficha "bajada"

    public AvatarPersonaje(Personaje personaje, int tamano) {
        this.personaje = personaje;
        setPreferredSize(new Dimension(tamano, tamano));
        setOpaque(false);
    }

    public void setTapado(boolean tapado) {
        this.tapado = tapado;
        repaint();
    }

    public boolean isTapado() {
        return tapado;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        if (tapado) {
            g2.setColor(new Color(60, 60, 70));
            g2.fillRoundRect(0, 0, w, h, 14, 14);
            g2.setColor(new Color(120, 120, 135));
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(1, 1, w - 3, h - 3, 14, 14);
            g2.setFont(new Font("SansSerif", Font.BOLD, (int) (h * 0.4)));
            g2.setColor(new Color(200, 200, 210));
            FontMetrics fm = g2.getFontMetrics();
            String signo = "?";
            g2.drawString(signo, w / 2 - fm.stringWidth(signo) / 2, h / 2 + fm.getAscent() / 2 - 4);
            g2.dispose();
            return;
        }

        // fondo de la tarjeta
        g2.setColor(new Color(250, 248, 240));
        g2.fillRoundRect(0, 0, w, h, 14, 14);
        g2.setColor(new Color(210, 205, 190));
        g2.drawRoundRect(0, 0, w - 1, h - 1, 14, 14);

        int cx = w / 2;
        int caraTop = (int) (h * 0.22);
        int caraDiam = (int) (w * 0.56);
        int caraLeft = cx - caraDiam / 2;

        Color colorPiel = new Color(240, 200, 160);

        // pelo (detras de la cara), salvo que sea calvo
        if (!personaje.isCalvo()) {
            g2.setColor(colorAwt(personaje.getColorPelo()));
            int peloTop = caraTop - (int) (caraDiam * 0.22);
            int peloDiam = (int) (caraDiam * 1.12);
            g2.fillOval(cx - peloDiam / 2, peloTop, peloDiam, (int) (caraDiam * 0.85));
        }

        // cara
        g2.setColor(colorPiel);
        g2.fillOval(caraLeft, caraTop, caraDiam, caraDiam);
        g2.setColor(colorPiel.darker());
        g2.drawOval(caraLeft, caraTop, caraDiam, caraDiam);

        // si es calvo, redibujar un poco de "brillo" arriba
        if (personaje.isCalvo()) {
            g2.setColor(new Color(255, 255, 255, 90));
            g2.fillOval(caraLeft + caraDiam / 4, caraTop + 2, caraDiam / 4, caraDiam / 6);
        }

        // ojos
        int ojoY = caraTop + (int) (caraDiam * 0.42);
        int ojoDiam = Math.max(3, (int) (caraDiam * 0.12));
        int ojoOffsetX = (int) (caraDiam * 0.22);
        g2.setColor(new Color(40, 40, 40));
        g2.fillOval(cx - ojoOffsetX - ojoDiam / 2, ojoY, ojoDiam, ojoDiam);
        g2.fillOval(cx + ojoOffsetX - ojoDiam / 2, ojoY, ojoDiam, ojoDiam);

        // boca
        g2.setStroke(new BasicStroke(Math.max(1.5f, caraDiam * 0.03f)));
        int bocaY = caraTop + (int) (caraDiam * 0.68);
        int bocaMedioAncho = (int) (caraDiam * 0.18);
        g2.drawArc(cx - bocaMedioAncho, bocaY - bocaMedioAncho / 2, bocaMedioAncho * 2, bocaMedioAncho, 200, 140);

        // lentes
        if (personaje.isUsaLentes()) {
            g2.setColor(new Color(30, 30, 30));
            g2.setStroke(new BasicStroke(Math.max(1.5f, caraDiam * 0.035f)));
            int lenteDiam = (int) (caraDiam * 0.26);
            int lenteY = ojoY - lenteDiam / 3;
            int lenteOffsetX = ojoOffsetX;
            g2.drawOval(cx - lenteOffsetX - lenteDiam / 2, lenteY, lenteDiam, lenteDiam);
            g2.drawOval(cx + lenteOffsetX - lenteDiam / 2, lenteY, lenteDiam, lenteDiam);
            g2.drawLine(cx - lenteDiam / 4, lenteY + lenteDiam / 2, cx + lenteDiam / 4, lenteY + lenteDiam / 2);
        }

        // etiqueta de genero (una pequenia marca de color en la esquina, discreta)
        g2.setColor(personaje.getGenero().name().equals("FEMENINO") ? new Color(220, 90, 140) : new Color(80, 130, 200));
        g2.fillOval(w - 14, 4, 8, 8);

        g2.dispose();
    }

    private Color colorAwt(ColorPelo colorPelo) {
        switch (colorPelo) {
            case COLORADO:
                return new Color(190, 70, 40);
            case NEGRO:
                return new Color(35, 30, 30);
            case AMARILLO:
                return new Color(230, 190, 60);
            default:
                return Color.GRAY;
        }
    }

    /** Crea una tarjeta lista para usar: avatar + nombre debajo. */
    public static JPanel crearTarjeta(Personaje personaje, int tamanoAvatar) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        AvatarPersonaje avatar = new AvatarPersonaje(personaje, tamanoAvatar);
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nombre = new JLabel(personaje.getNombre());
        nombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        nombre.setFont(nombre.getFont().deriveFont(Font.PLAIN, 12f));
        nombre.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(avatar);
        panel.add(Box.createVerticalStrut(4));
        panel.add(nombre);
        panel.putClientProperty("avatar", avatar);

        return panel;
    }
}
