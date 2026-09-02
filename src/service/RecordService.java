package service;

import model.Marcador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordService {

    private final File archivo = new File("marcador.txt");

    public Map<String, Marcador> cargarMarcadores() {
        Map<String, Marcador> marcadores = new HashMap<>();

        if (!archivo.exists()) {
            return marcadores;
        }

        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                String[] datos = linea.split(",");
                String usuario = datos[0];
                int partidasGanadas = Integer.parseInt(datos[1]);

                Marcador marcador = new Marcador(usuario);
                for (int i = 0; i < partidasGanadas; i++) {
                    marcador.sumarVictoria();
                }

                marcadores.put(usuario, marcador);
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer el archivo de marcadores.");
        }

        return marcadores;
    }

    public void registrarVictoria(String usuario) {
        Map<String, Marcador> marcadores = cargarMarcadores();
        Marcador marcador = marcadores.get(usuario);

        if (marcador == null) {
            marcador = new Marcador(usuario);
            marcadores.put(usuario, marcador);
        }

        marcador.sumarVictoria();

        try (PrintWriter escritor = new PrintWriter(archivo)) {
            for (Marcador marcadorGuardado : marcadores.values()) {
                escritor.println(marcadorGuardado.getUsuario() + ","
                        + marcadorGuardado.getPartidasGanadas());
            }
        } catch (IOException e) {
            System.out.println("No se pudo guardar el archivo de marcadores.");
        }
    }

    public void mostrarTabla() {
        Map<String, Marcador> marcadores = cargarMarcadores();
        List<Marcador> tabla = new ArrayList<>(marcadores.values());

        tabla.sort(Comparator.comparingInt(Marcador::getPartidasGanadas).reversed());

        for (Marcador marcador : tabla) {
            System.out.println(marcador.getUsuario() + " - "
                    + marcador.getPartidasGanadas() + " victorias");
        }
    }
}
