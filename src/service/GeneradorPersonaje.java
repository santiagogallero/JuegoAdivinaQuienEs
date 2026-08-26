package service;

import enums.ColorPelo;
import enums.Genero;
import model.Personaje;

import java.util.ArrayList;
import java.util.List;

public class GeneradorPersonaje {

    public static List<Personaje> generarPersonajes() {
        List<Personaje> personajes = new ArrayList<>();

        personajes.add(new Personaje("Mario", true, ColorPelo.COLORADO, Genero.MASCULINO, true));
        personajes.add(new Personaje("Felipe", false, ColorPelo.NEGRO, Genero.MASCULINO, false));
        personajes.add(new Personaje("Pedro", true, ColorPelo.AMARILLO, Genero.MASCULINO, false));
        personajes.add(new Personaje("Santi", false, ColorPelo.NEGRO, Genero.MASCULINO, true));
        personajes.add(new Personaje("Tomas", true, ColorPelo.COLORADO, Genero.MASCULINO, false));
        personajes.add(new Personaje("Nicolas", false, ColorPelo.AMARILLO, Genero.MASCULINO, false));
        personajes.add(new Personaje("Lucas", true, ColorPelo.NEGRO, Genero.MASCULINO, true));
        personajes.add(new Personaje("Franco", false, ColorPelo.COLORADO, Genero.MASCULINO, false));
        personajes.add(new Personaje("Mateo", true, ColorPelo.NEGRO, Genero.MASCULINO, false));
        personajes.add(new Personaje("Joaquin", false, ColorPelo.AMARILLO, Genero.MASCULINO, true));
        personajes.add(new Personaje("Lautaro", false, ColorPelo.NEGRO, Genero.MASCULINO, false));
        personajes.add(new Personaje("Bruno", true, ColorPelo.COLORADO, Genero.MASCULINO, false));

        personajes.add(new Personaje("Martina", false, ColorPelo.NEGRO, Genero.FEMENINO, false));
        personajes.add(new Personaje("Malena", true, ColorPelo.AMARILLO, Genero.FEMENINO, false));
        personajes.add(new Personaje("Lucia", false, ColorPelo.COLORADO, Genero.FEMENINO, true));
        personajes.add(new Personaje("Camila", false, ColorPelo.NEGRO, Genero.FEMENINO, false));
        personajes.add(new Personaje("Agustina", true, ColorPelo.AMARILLO, Genero.FEMENINO, false));
        personajes.add(new Personaje("Valentina", false, ColorPelo.COLORADO, Genero.FEMENINO, false));
        personajes.add(new Personaje("Sofia", true, ColorPelo.NEGRO, Genero.FEMENINO, true));
        personajes.add(new Personaje("Julieta", false, ColorPelo.AMARILLO, Genero.FEMENINO, false));
        personajes.add(new Personaje("Florencia", true, ColorPelo.COLORADO, Genero.FEMENINO, false));
        personajes.add(new Personaje("Micaela", false, ColorPelo.NEGRO, Genero.FEMENINO, true));
        personajes.add(new Personaje("Carolina", false, ColorPelo.AMARILLO, Genero.FEMENINO, false));

        return personajes;
    }
}