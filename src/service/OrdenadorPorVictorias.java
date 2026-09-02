package service;

import model.Marcador;

public class OrdenadorPorVictorias extends OrdenadorMergeSort<Marcador> {

    @Override
    protected boolean vaPrimero(Marcador primero, Marcador segundo) {
        if (primero.getPartidasGanadas() != segundo.getPartidasGanadas()) {
            return primero.getPartidasGanadas() > segundo.getPartidasGanadas();
        }

        return primero.getUsuario().compareToIgnoreCase(segundo.getUsuario()) <= 0;
    }
}
