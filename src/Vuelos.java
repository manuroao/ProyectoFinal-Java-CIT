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

    /**
     * Agrega un nuevo vuelo al ArrayList vuelos donde se almacenan todos los vuelos, tambíen se utilizó un HashMap
     * para agregar funcionalidades, pero no se concluyo con lo propuesto
     * @param vuelo
     */
    public static void agregarVuelo(Vuelos vuelo) {
        vuelos.add(vuelo);
        vuelosHash.put(vuelo.getId(), vuelo);
    }

    public static void eliminarVuelo(Vuelos vuelo) {
        vuelos.remove(vuelo);
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

    /**
     * Lee los vuelos del archivo "vuelos.txt" simulando una base de datos, donde los datos están separados
     * por coma
     */
    public static void leerVuelos() {
        Path path = Path.of("resources/vuelos2.txt");
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("MM/dd/yyyy"); // MM-dd-yyyy
        try (BufferedReader lector = Files.newBufferedReader(path)) {
            String linea = null;
            while ((linea = lector.readLine()) != null) {
                if (linea.isEmpty()) continue;
                String[] datos = linea.split(",");
                String origen = datos[0];
                String destino = datos[1];
                LocalDate fecha = LocalDate.parse(datos[2], formatoFecha);
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
