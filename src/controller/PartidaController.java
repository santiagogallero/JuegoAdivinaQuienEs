package controller;

import juego.Juego;
import model.Personaje;
import service.RecordService;
import java.util.Scanner;


public class PartidaController {

    private final Juego juego;
    private final RecordService recordService;

    public PartidaController() {
        this.juego = new Juego();
        this.recordService = new RecordService();
    }

    public void jugarPartida(String nombreUsuario) {
        Scanner sc = new Scanner(System.in);
        Personaje elegido = juego.obtenerElegido();
        int intento;

        do {
            System.out.println("Guess: (Ingrese 0 para utilizar el comodin)");
            intento = sc.nextInt();

            if (intento == 0) {
                System.out.println(juego.comodin(elegido.getId()));
            } else if (intento != elegido.getId()) {
                System.out.println(juego.conocerElegido(intento) + " no es el elegido");
            }
        } while (intento != elegido.getId());

        System.out.println("Ganaste, el elegido era: " + elegido.getNombre());
        recordService.registrarVictoria(nombreUsuario);
    }

    public void mostrarPersonajes() {
        juego.mostrarPersonajes();
    }
}