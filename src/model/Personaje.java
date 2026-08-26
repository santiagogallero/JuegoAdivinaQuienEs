package model;
import enums.ColorPelo;
import enums.Genero;

public class Personaje {
    private  static  int contador = 1;
    private final String nombre;

    private final Integer id;
    private final ColorPelo colorPelo;
    private final Genero genero;
    private final boolean calvo;
    private final boolean usaLentes;




    public Personaje(String nombre,boolean usaLentes , ColorPelo colorPelo, Genero genero, boolean calvo) {
        
        this.id = contador++;
        this.nombre = nombre;
        this.usaLentes = usaLentes;
        this.colorPelo = colorPelo;
        this.genero = genero;
        this.calvo = calvo;
    }

        public String getNombre() {
        return nombre;
    }

    public Integer getId() {
        return id;
    }

    public boolean isUsaLentes() {
        return usaLentes;
    }


    public ColorPelo getColorPelo() {
        return colorPelo;
    }

    public boolean isCalvo() {
        return calvo;
    }


    public Genero getGenero() {
        return genero;
    }
}
