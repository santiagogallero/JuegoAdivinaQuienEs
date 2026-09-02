package model;
import java.util.List;
import java.util.Scanner;

public class JugadorHumano extends Jugador {
    private final Scanner scanner;

    public JugadorHumano(String nombre, Personaje secreto, List<Personaje> universoPersonajes, Scanner scanner) {
        super(nombre, secreto, universoPersonajes);
        this.scanner = scanner;
    }
    
    @Override
    public Jugada decidirJugada(List<Filtro> filtrosDisponibles) {
        System.out.println("Turno de " + nombre + "---");
        System.out.println("Candidatos restantes: " + candidatosRestantes.size());
        System.out.println("Preguntar un filtro)");
        System.out.println("Adivinar un personaje)");
        System.out.println("Elegi una Opcion: ");
        int opcion = Integer.parseInt(scanner.nextLine().trim());

        if (opcion == 2) {
            System.out.println("Personajes candidatos:");
            for (int p = 0; p < candidatosRestantes.size(); p++) {
                System.out.println(candidatosRestantes.get(p).getId() + ". "
                        + candidatosRestantes.get(p).getNombre());
            }
            System.out.println("Ingrese el id del personaje que crees que es ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            Personaje elegido = buscarPorId(id);
            return Jugada.crearJugadaAdivinanza(elegido);
        }
        System.out.println("Filtros disponibles:");
        for (int i = 0; i < filtrosDisponibles.size(); i++) {
            System.out.println((i + 1) + ". " + filtrosDisponibles.get(i).getDescripciones());
        }
        System.out.println("Elegi el numero de filtro: ");
        int indice =Integer.parseInt(scanner.nextLine().trim());
        return Jugada.crearJugadaPregunta(filtrosDisponibles.get(indice - 1));
    }



    private Personaje buscarPorId(int id) {
        for (Personaje p : candidatosRestantes) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
}
