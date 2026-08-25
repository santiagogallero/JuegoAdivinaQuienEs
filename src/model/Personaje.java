package model;
import enums.ColorPelo;
import enums.Elegido;
import enums.Distintivo;
import enums.Genero;

public class Personaje {
    private String nombre;
    private Elegido estado;
    private Integer id;
    private Distintivo filtro;
    private ColorPelo colorPelo;
    private Genero genero;


    public Personaje(String nombre, Elegido estado, Integer id, Distintivo filtro, ColorPelo colorPelo, Genero genero) {
        this.nombre = nombre;
        this.estado = estado;
        this.id = id;
        this.filtro = filtro;
        this.colorPelo = colorPelo;
        this.genero = genero;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getId() {
        return id;
    }

    public Elegido getEstado() {
        return estado;
    }

    public void setEstado(Elegido estado) {
        this.estado = estado;
    }

    public Distintivo getFiltro() {
        return filtro;
    }


    public ColorPelo getColorPelo() {
        return colorPelo;
    }


    public Genero getGenero() {
        return genero;
    }
}
