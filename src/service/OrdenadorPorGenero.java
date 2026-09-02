package service;

import enums.Genero;
import model.Personaje;

public class OrdenadorPorGenero extends OrdenadorMergeSort<Personaje> {

    @Override
    protected boolean vaPrimero(Personaje primero, Personaje segundo) {
        return prioridad(primero.getGenero()) <= prioridad(segundo.getGenero());
    }

    private int prioridad (Genero genero) {
        return genero == Genero.MASCULINO ? 0 : 1;
    }
}
