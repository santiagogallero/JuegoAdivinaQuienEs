package model;
import enums.CantidadPelo;
import enums.ColorPelo;
import enums.Genero;

public class Personaje {
    private  static  int contador = 1;
    private final String nombre;

    private final Integer id;
    private final ColorPelo colorPelo;
    private final CantidadPelo cantidadPelo;
    private final Genero genero;
    private final boolean usaLentes;




    public Personaje(String nombre, boolean usaLentes, ColorPelo colorPelo,
                     Genero genero, boolean calvo) {
        this(nombre, usaLentes, colorPelo, genero,
                calvo ? CantidadPelo.SIN_PELO : CantidadPelo.POCO);
    }

    public Personaje(String nombre, boolean usaLentes, ColorPelo colorPelo,
                     Genero genero, CantidadPelo cantidadPelo) {
        this.id = contador++;
        this.nombre = nombre;
        this.usaLentes = usaLentes;
        this.colorPelo = colorPelo;
        this.cantidadPelo = cantidadPelo;
        this.genero = genero;
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
        return cantidadPelo == CantidadPelo.SIN_PELO;
    }

    public CantidadPelo getCantidadPelo() {
        return cantidadPelo;
    }


    public Genero getGenero() {
        return genero;
    }
}
