package service;

import enums.ColorPelo;
import enums.Genero;
import model.Filtro;
import model.Personaje;

import java.util.ArrayList;
import java.util.List;

public class GeneradorFiltros {
    public static List<Filtro> generarTodos() {
        List<Filtro> filtros = new ArrayList<>();

        filtros.add(new Filtro("Es de genero masculino",
                personaje -> personaje.getGenero() == Genero.MASCULINO));

        filtros.add(new Filtro("Es calvo",
                personaje -> personaje.isCalvo()));

        filtros.add(new Filtro("Tiene lentes",
                personaje -> personaje.isUsaLentes()));

        filtros.add(new Filtro("Tiene pelo colorado",
                personaje -> personaje.getColorPelo() == ColorPelo.COLORADO));

        filtros.add(new Filtro("Tiene pelo negro",
                personaje -> personaje.getColorPelo() == ColorPelo.NEGRO));

        filtros.add(new Filtro("Tiene pelo amarillo",
                personaje -> personaje.getColorPelo() == ColorPelo.AMARILLO));

        return filtros;
    }
}
