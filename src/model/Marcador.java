package model;
import java.io.Serializable;

public class Marcador implements Serializable {
    private String usuario;
    private int partidasGanadas;

    public Marcador(String usuario) {
        this.usuario = usuario;
        this.partidasGanadas = 0;
    }
    public void sumarVictoria() { this.partidasGanadas++; }
    public String getUsuario() { return usuario; }
    public int getPartidasGanadas() { return partidasGanadas; }
}