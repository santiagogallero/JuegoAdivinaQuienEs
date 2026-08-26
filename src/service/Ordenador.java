package service;
import java.util.List;
import model.Personaje;


public interface Ordenador {
    List<Personaje> ordenarPersonajes(List<Personaje> personajes);
    
}