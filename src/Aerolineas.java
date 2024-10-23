import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Aerolineas {
    private String nombre;

    // En un archivo "categorias.txt" se almacenan las categorias disponibles por aerolinea, incluyendo de aquellas ejecuciones previas
    // del programa simulando una base de datos
    private ArrayList<Categorias> categorias = new ArrayList<>();
    private static final String AEROLINEAS_FILE = "resources/aerolineas.txt";
    private int categoriaID = 1;

    public Aerolineas(String nombre) {
        this.nombre = nombre;
        leerCategorias(this.nombre);
    }


    public String getNombre() {
        return this.nombre;
    }

    public ArrayList<Categorias> getCategorias() {
        return this.categorias;
    }

    public void leerCategorias(String nombre) {
        Path path = Path.of(AEROLINEAS_FILE);
        try (BufferedReader lector = Files.newBufferedReader(path)) {
            String linea = null;
            while ((linea = lector.readLine()) != null) {
                String[] listCategorias = linea.split(",");
                if (nombre.equals(listCategorias[0])) {
                    for (int i = 1; i < listCategorias.length; i++) {
                        this.categorias.add(new Categorias(listCategorias[i]));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
