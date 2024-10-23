import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class Vuelos {
    private String origen;
    private String destino;
    private LocalDate fecha;
    private Aerolineas aerolinea;
    private int id;
    private static int IDs = 1;
    private static ArrayList<Vuelos> vuelos = new ArrayList<>();
    private static HashMap<Integer, Vuelos> vuelosHash = new HashMap<>();

    public Vuelos(String origen, String destino, LocalDate fecha, Aerolineas aerolinea) {
        this.origen = origen;
        this.destino = destino;
        this.fecha = fecha;
        this.aerolinea = aerolinea;
        this.id = IDs;
        IDs++;
    }

    public static void agregarVuelo(Vuelos vuelo) {
        vuelos.add(vuelo);
        vuelosHash.put(vuelo.getId(), vuelo);
    }

    public String getOrigen() {
        return this.origen;
    }

    public String getDestino() {
        return this.destino;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public Aerolineas getAerolinea() {
        return this.aerolinea;
    }

    public int getId() {
        return this.id;
    }

    public static Vuelos getVuelo(int id) {
        return vuelosHash.get(id);
    }

    public static ArrayList<Vuelos> getVuelos() {
        return vuelos;
    }

    public static void leerVuelos() {
        Path path = Path.of("resources/vuelos.txt");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        try (BufferedReader lector = Files.newBufferedReader(path)) {
            String linea = null;
            while ((linea = lector.readLine()) != null) {
                String[] datos = linea.split(",");
                String origen = datos[0];
                String destino = datos[1];
                LocalDate fecha = LocalDate.parse(datos[2], formatter);
                Aerolineas aerolinea = new Aerolineas(datos[3]);
                Vuelos vuelo = new Vuelos(origen, destino, fecha, aerolinea);
                vuelos.add(vuelo);
                vuelosHash.put(vuelo.getId(), vuelo);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return String.format("%s -> %s | %s | %s",
                this.origen, this.destino, this.fecha, this.aerolinea.getNombre());
    }

}
