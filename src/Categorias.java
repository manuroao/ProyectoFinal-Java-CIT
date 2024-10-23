import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class Categorias {
    private String nombre;
    private int costo;
    private int cantidad;

    private final static String CATEGORIAS_FILE = "resources/categorias.txt";

    private static final HashMap<String, int[]> datosCategorias = new HashMap<>();


    public Categorias(String nombre) {
        this.nombre = nombre;
        cargarDatos();
        int[] datos = datosCategorias.get(nombre);
        if (datos != null) {
            this.costo = datos[0];
            this.cantidad = datos[1];
        } else {
            this.costo = 0;
            this.cantidad = 0;
        }
    }

    private void cargarDatos() {

        if(!datosCategorias.isEmpty()) return; // Se realiza la carga de los datos solamente una vez

        Path path = Path.of(CATEGORIAS_FILE);
        try (BufferedReader lector = Files.newBufferedReader(path)) {
            String linea = null;
            while ((linea = lector.readLine()) != null) {
                String[] lineaSplit = linea.split(",");
                String nombreCategoria = lineaSplit[0];
                int costo = Integer.parseInt(lineaSplit[1]);
                int cantidad = Integer.parseInt(lineaSplit[2]);
                datosCategorias.put(nombreCategoria, new int[]{costo, cantidad});
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void agregarCategorias() {

    }
    public String getNombre() {
        return this.nombre;
    }

    public double getCosto() {
        return this.costo;
    }

    public int getCantidad() {
        return this.cantidad;
    }

    public void setCantidad(int cantidad) {
        if (this.cantidad - cantidad < 0) { // Lo cual significa que el usuario ingreso una cantidad mayor a la disponible
            System.out.println("\n***No hay suficientes boletos***\n");
            return;
        }
        this.cantidad = this.cantidad - cantidad;
    }

    @Override
    public String toString() {
        if (this.getCantidad() > 0)
            return this.nombre + "\n\tCosto: " + this.getCosto() + "\n\tCantidad Disponible: " + this.getCantidad();
        else
            return this.nombre + ": Agotado\n";
    }
}
