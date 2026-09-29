package model;
import service.EstrategiaMaquina;
import service.HistorialPreguntas;

import java.util.List;
import java.util.ArrayList;

public class JugadorMaquina extends Jugador implements HistorialPreguntas.Observador {

    private final EstrategiaMaquina estrategia;
    private final List<Filtro> filtrosUsados = new ArrayList<>();
    private final List<Personaje> candidatosRival;

    public JugadorMaquina(String nombre, Personaje secreto, List<Personaje> universoPersonajes, EstrategiaMaquina estrategia) {
        super(nombre, secreto, universoPersonajes);
        this.estrategia = estrategia;
        this.candidatosRival = new ArrayList<>(universoPersonajes);
    }

    @Override
    public Jugada decidirJugada(List<Filtro> filtrosDisponibles) {
        List<Filtro> filtrosRestantes = new ArrayList<>();
        for (Filtro filtro : filtrosDisponibles) {
            if (!filtrosUsados.contains(filtro)) {
                filtrosRestantes.add(filtro);
            }
        }

        Jugada jugada = estrategia.decidirJugada(this, filtrosRestantes);

        if (jugada.getTipo() == Jugada.Tipo.Preguntar) {
            filtrosUsados.add(jugada.getFiltro());
        }

        return jugada;
    }

    @Override
    public void onPreguntaRegistrada(String jugador, Filtro filtro) {
        if (jugador.equals(nombre)) {
            return; // mis propias preguntas ya las tengo en filtrosUsados
        }
        // La pregunta del rival es sobre MI secreto, asi que se cual fue la respuesta
        // y puedo saber cuantos candidatos le quedan
        boolean respuesta = respondeFiltro(filtro);
        candidatosRival.removeIf(p -> filtro.cumple(p) != respuesta);
    }

    public List<Personaje> getCandidatosRival() {
        return candidatosRival;
    }
}