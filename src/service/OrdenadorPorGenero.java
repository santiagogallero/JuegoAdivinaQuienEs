package service;

import enums.Genero;
import model.Personaje;

import java.util.ArrayList;
import java.util.List;

public class OrdenadorPorGenero implements Ordenador {
    
    @Override
    public List<Personaje> ordenarPersonajes(List<Personaje> personajes) {
        return mergeSort(personajes);
    }

    private  List<Personaje> mergeSort(List<Personaje> personajes) {
        if (personajes.size() <= 1) {
            return personajes;
        }

        int mid = personajes.size() / 2;
        List<Personaje> izquierda = mergeSort(personajes.subList(0, mid));
        List<Personaje> derecha = mergeSort(personajes.subList(mid, personajes.size()));

        return mezclar(izquierda, derecha);
    }

    private  List<Personaje> mezclar(List<Personaje> izquierda, List<Personaje> derecha) {
        List<Personaje> resultado = new ArrayList<>();
        int i = 0, j = 0;

        while (i < izquierda.size() && j < derecha.size()) {
            Personaje pi = izquierda.get(i);
            Personaje pj = derecha.get(j);
            if (prioridad(pi.getGenero()) <= prioridad(pj.getGenero())) {
                resultado.add(pi);
                i++;
            } else {
                resultado.add(pj);
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
    private int prioridad (Genero genero) {
        return genero == Genero.MASCULINO ? 0 : 1;
    }
}
