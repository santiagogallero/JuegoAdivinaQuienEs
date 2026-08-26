package model;
import java.util.List;
import java.util.ArrayList;

public class Jugador {
    
    protected final String nombre;
    private final Personaje secreto;
    protected List<Personaje> candidatosRestantes;
    
    protected Jugador(String nombre, Personaje secreto, List<Personaje> universoPersonajes) {
        this.nombre = nombre;
        this.secreto = secreto;
        this.candidatosRestantes = new ArrayList<>(universoPersonajes);
    }
    
    public boolean esMiSecreto( Personaje candidato) {
        return secreto.getId().equals(candidato.getId());
    }
    public boolean respondeFiltro(Filtro filtro) {
        return filtro.cumple(secreto);
    }

    public void aplicarFiltro(Filtro filtro, boolean restuestaObtenida) {
        candidatosRestantes.removeIf(p -> filtro.cumple(p) != restuestaObtenida);
    }
    public String getNombre() {
        return nombre;
    }
    public List<Personaje> getCandidatosRestantes() {
        return candidatosRestantes;
    }
}
