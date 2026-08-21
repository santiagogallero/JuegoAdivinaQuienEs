import java.util.Scanner;



public class Main {
    public static void main(String[] args) {


        System.out.println("Bienvenido Usuario");
        Juego juego = new Juego();
        Personaje elegido = juego.obtenerElegido();
        int intento;
        do {
            Scanner sc = new Scanner(System.in);
            System.out.println("Guess: (Ingrese 0 para utilizar el comodin)");
            intento = sc.nextInt();
            if (intento == 0) {
                System.out.println(juego.comodin(elegido.getId()));

            } else if (intento != elegido.getId()) {
                System.out.println(juego.conocerElegido(intento) +  " no es el elegido");
            }

        } while (intento != elegido.getId());
        System.out.println("Ganaste, el eledigo era: " + elegido.getNombre());

    }
}