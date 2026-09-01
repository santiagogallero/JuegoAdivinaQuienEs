package model;

public class Jugada {
    public enum Tipo {Preguntar, Adivinar}
    
    private final Tipo tipo;
    private final Filtro filtro;
    private final Personaje personajeAdivinado;

    private Jugada(Tipo tipo, Filtro filtro, Personaje personajeAdivinado) {
        this.tipo = tipo;
        this.filtro = filtro;
        this.personajeAdivinado = personajeAdivinado;
    }

    public static Jugada crearJugadaPregunta(Filtro filtro) {
        return new Jugada(Tipo.Preguntar, filtro, null);
    }

    public static Jugada crearJugadaAdivinanza(Personaje personaje) {
        return new Jugada(Tipo.Adivinar, null, personaje);
    }

    public Tipo getTipo() {
        return tipo;
    }
    public Filtro getFiltro() {
        return filtro;
    }
    public Personaje getPersonajeAdivinado() {
        return personajeAdivinado;
    }

}
