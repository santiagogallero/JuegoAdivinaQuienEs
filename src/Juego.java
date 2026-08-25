import enums.Elegido;
import model.Personaje;
import java.util.Random;
import java.util.ArrayList;

public class Juego {
    Random rand = new Random();
    ArrayList<Personaje> personajes = new ArrayList<>();

    Personaje personaje1 = new Personaje("Mario", Elegido.NO, 1);
    Personaje personaje2 = new Personaje("Felipe", Elegido.NO, 2);
    Personaje personaje3 = new Personaje("Pedro", Elegido.NO, 3);
    Personaje personaje4 = new Personaje("Santi", Elegido.NO, 4);
    Personaje personaje5 = new Personaje("Martina", Elegido.NO, 5);
    Personaje personaje6 = new Personaje("Malena", Elegido.NO, 6);
    Personaje personaje7 = new Personaje("Lucia", Elegido.NO, 7);

    public Juego() {
        personajes.add(personaje1);
        personajes.add(personaje2);
        personajes.add(personaje3);
        personajes.add(personaje4);
        personajes.add(personaje5);
        personajes.add(personaje6);
        personajes.add(personaje7);

        int posicionElegida = rand.nextInt(personajes.size());

        personajes.get(posicionElegida).setEstado(Elegido.SI);

        
    }

    
    
    public Personaje obtenerElegido(){

        for (Personaje pers : personajes) {
            if  (pers.getEstado() == Elegido.SI) {
                return pers;
            }
        }
        return null;
    }

    public String conocerElegido(int intento) {
        for (Personaje pers : personajes) {
            if (pers.getId() == intento)
                return pers.getNombre();
        }
        return null;
    }

    public String comodin(int comodini) {
        if ((comodini >= 1) && (comodini <= 3)) {
            return "El elegido se encuentra en el grupo 1";
        } else if ((comodini >= 4) && (comodini <= 6)) {
            return "El elegido se encuentra en el grupo 2";
        } else return "El elegido se encuentra en el grupo 3";
    }


}
