package model;

import java.util.function.Predicate;

public class Filtro {
    private final String descripciones;
    private final Predicate<Personaje> condicion;

    public Filtro(String descripciones, Predicate<Personaje> condicion) {
        this.descripciones = descripciones;
        this.condicion = condicion;
    }
    public String getDescripciones() {
        return descripciones;
    }

    public boolean cumple(Personaje personaje) {
        return condicion.test(personaje);
    }
    
}
