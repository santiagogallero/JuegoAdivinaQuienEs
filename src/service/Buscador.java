package service;

import model.Personaje;
import java.util.List;
public class Buscador {
    public Personaje buscarPorId(List<Personaje> personajes, int id) {
        return buscarPorId(personajes, id, 0, personajes.size() - 1);
    }

    private Personaje buscarPorId(List<Personaje> personajes, int id, int inicio, int fin) {
        if (inicio > fin) {
            return null; // No se encontró el personaje
        }

        int mid = (inicio + fin) / 2;
        Personaje candidato = personajes.get(mid);

        if (candidato.getId() == id) {
            return candidato; // Se encontró el personaje
        } else if (candidato.getId() < id) {
            return buscarPorId(personajes, id, mid + 1, fin); // Buscar en la mitad derecha
        } else {
            return buscarPorId(personajes, id, inicio, mid - 1); // Buscar en la mitad izquierda
        }
    }
    }
