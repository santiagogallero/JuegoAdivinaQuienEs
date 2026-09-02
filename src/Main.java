import controller.PartidaController;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PartidaController controller = new PartidaController(scanner);

        System.out.println("=== ADIVINA QUIEN ===");
        System.out.print("Ingresa tu nombre de usuario: ");
        String nombreUsuario = scanner.nextLine().trim();

        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- Menu ---");
            System.out.println("1) Jugar contra Maquina Basica");
            System.out.println("2) Jugar contra Maquina Asertiva");
            System.out.println("3) Modo Maquina vs Maquina (exhibicion)");
            System.out.println("4) Ver marcador");
            System.out.println("5) Salir");
            System.out.print("Elegi una opcion: ");

            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1":
                    controller.jugarHumanoVsMaquina(nombreUsuario, false);
                    break;
                case "2":
                    controller.jugarHumanoVsMaquina(nombreUsuario, true);
                    break;
                case "3":
                    controller.jugarMaquinaVsMaquina();
                    break;
                case "4":
                    controller.mostrarMarcador();
                    break;
                case "5":
                    salir = true;
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        }

        System.out.println("Gracias por jugar, " + nombreUsuario + "!");
    }
}
