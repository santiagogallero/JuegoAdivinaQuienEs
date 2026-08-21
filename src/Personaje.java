public class Personaje {
    private String nombre;
    private Elegido estado;
    private Integer id;
    //private String genero;
    //private int edad;
    //private String trabajo;
    //private Distintivo filtro;


    public Personaje(String nombre, Elegido estado, Integer id) {
        this.nombre = nombre;
        this.estado = estado;
        this.id = id;
    };


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



}
