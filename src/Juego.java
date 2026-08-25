import enums.Elegido;
import model.Personaje;
import enums.Distintivo;
import enums.ColorPelo;
import enums.Genero;
import java.util.Random;
import java.util.ArrayList;

public class Juego {
    Random rand = new Random();
    ArrayList<Personaje> personajes = new ArrayList<>();

    Personaje personaje1 = new Personaje("Mario", Elegido.NO, 17, Distintivo.LENTES, ColorPelo.NEGRO, Genero.MASCULINO);
    Personaje personaje2 = new Personaje("Felipe", Elegido.NO, 4, Distintivo.BARBA, ColorPelo.CASTANO, Genero.MASCULINO);
    Personaje personaje3 = new Personaje("Pedro", Elegido.NO, 21, Distintivo.GORRA, ColorPelo.RUBIO, Genero.MASCULINO);
    Personaje personaje4 = new Personaje("Santi", Elegido.NO, 8, Distintivo.RULOS, ColorPelo.NEGRO, Genero.MASCULINO);
    Personaje personaje5 = new Personaje("Martina", Elegido.NO, 13, Distintivo.PECAS, ColorPelo.RUBIO, Genero.FEMENINO);
    Personaje personaje6 = new Personaje("Malena", Elegido.NO, 2, Distintivo.NINGUNO, ColorPelo.CASTANO, Genero.FEMENINO);
    Personaje personaje7 = new Personaje("Lucia", Elegido.NO, 19, Distintivo.LENTES, ColorPelo.NEGRO, Genero.FEMENINO);
    Personaje personaje8 = new Personaje("Tomas", Elegido.NO, 6, Distintivo.BIGOTE, ColorPelo.COLORADO, Genero.MASCULINO);
    Personaje personaje9 = new Personaje("Camila", Elegido.NO, 23, Distintivo.PECAS, ColorPelo.CASTANO, Genero.FEMENINO);
    Personaje personaje10 = new Personaje("Nicolas", Elegido.NO, 10, Distintivo.GORRA, ColorPelo.NEGRO, Genero.MASCULINO);
    Personaje personaje11 = new Personaje("Agustina", Elegido.NO, 1, Distintivo.RULOS, ColorPelo.RUBIO, Genero.FEMENINO);
    Personaje personaje12 = new Personaje("Lucas", Elegido.NO, 15, Distintivo.PELADO, ColorPelo.CASTANO, Genero.MASCULINO);
    Personaje personaje13 = new Personaje("Valentina", Elegido.NO, 7, Distintivo.NINGUNO, ColorPelo.COLORADO, Genero.FEMENINO);
    Personaje personaje14 = new Personaje("Franco", Elegido.NO, 20, Distintivo.BARBA, ColorPelo.NEGRO, Genero.MASCULINO);
    Personaje personaje15 = new Personaje("Sofia", Elegido.NO, 11, Distintivo.LENTES, ColorPelo.RUBIO, Genero.FEMENINO);
    Personaje personaje16 = new Personaje("Mateo", Elegido.NO, 5, Distintivo.BIGOTE, ColorPelo.CASTANO, Genero.MASCULINO);
    Personaje personaje17 = new Personaje("Julieta", Elegido.NO, 18, Distintivo.PECAS, ColorPelo.NEGRO, Genero.FEMENINO);
    Personaje personaje18 = new Personaje("Joaquin", Elegido.NO, 3, Distintivo.PELADO, ColorPelo.RUBIO, Genero.MASCULINO);
    Personaje personaje19 = new Personaje("Florencia", Elegido.NO, 22, Distintivo.RULOS, ColorPelo.COLORADO, Genero.FEMENINO);
    Personaje personaje20 = new Personaje("Lautaro", Elegido.NO, 9, Distintivo.GORRA, ColorPelo.CASTANO, Genero.MASCULINO);
    Personaje personaje21 = new Personaje("Micaela", Elegido.NO, 16, Distintivo.NINGUNO, ColorPelo.NEGRO, Genero.FEMENINO);
    Personaje personaje22 = new Personaje("Bruno", Elegido.NO, 12, Distintivo.BARBA, ColorPelo.COLORADO, Genero.MASCULINO);
    Personaje personaje23 = new Personaje("Carolina", Elegido.NO, 14, Distintivo.PELADO, ColorPelo.CASTANO, Genero.FEMENINO);

    public Juego() {
        personajes.add(personaje1);
        personajes.add(personaje2);
        personajes.add(personaje3);
        personajes.add(personaje4);
        personajes.add(personaje5);
        personajes.add(personaje6);
        personajes.add(personaje7);
        personajes.add(personaje8);
        personajes.add(personaje9);
        personajes.add(personaje10);
        personajes.add(personaje11);
        personajes.add(personaje12);
        personajes.add(personaje13);
        personajes.add(personaje14);
        personajes.add(personaje15);
        personajes.add(personaje16);
        personajes.add(personaje17);
        personajes.add(personaje18);
        personajes.add(personaje19);
        personajes.add(personaje20);
        personajes.add(personaje21);
        personajes.add(personaje22);
        personajes.add(personaje23);


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


    //borrar despues de completar interfa de ordenador

    public String comodin(int comodini) {
        if ((comodini >= 1) && (comodini <= 3)) {
            return "El elegido se encuentra en el grupo 1";
        } else if ((comodini >= 4) && (comodini <= 6)) {
            return "El elegido se encuentra en el grupo 2";
        } else return "El elegido se encuentra en el grupo 3";
    }


}
