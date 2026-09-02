package service;

import java.util.ArrayList;
import java.util.List;

public abstract class OrdenadorMergeSort<T> implements Ordenador<T> {

    @Override
    public List<T> ordenar(List<T> elementos) {
        if (elementos.size() <= 1) {
            return elementos;
        }

        int mitad = elementos.size() / 2;
        List<T> izquierda = ordenar(
                new ArrayList<>(elementos.subList(0, mitad)));
        List<T> derecha = ordenar(
                new ArrayList<>(elementos.subList(mitad, elementos.size())));

        return mezclar(izquierda, derecha);
    }

    private List<T> mezclar(List<T> izquierda, List<T> derecha) {
        List<T> resultado = new ArrayList<>();
        int i = 0;
        int j = 0;

        while (i < izquierda.size() && j < derecha.size()) {
            if (vaPrimero(izquierda.get(i), derecha.get(j))) {
                resultado.add(izquierda.get(i));
                i++;
            } else {
                resultado.add(derecha.get(j));
                j++;
            }
        }

        while (i < izquierda.size()) {
            resultado.add(izquierda.get(i));
            i++;
        }

        while (j < derecha.size()) {
            resultado.add(derecha.get(j));
            j++;
        }

        return resultado;
    }

    protected abstract boolean vaPrimero(T primero, T segundo);
}
