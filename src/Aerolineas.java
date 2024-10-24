import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Aerolineas {
    private String nombre;

    // En un archivo "aerolineas.txt" se almacenan las categorias disponibles por aerolínea
    private ArrayList<Categorias> categorias = new ArrayList<>();
    private static final String AEROLINEAS_FILE = "resources/aerolineas2.txt";
    private int categoriaID = 1;

    public Aerolineas(String nombre) {
        this.nombre = nombre;
        this.categorias.add(new Categorias("Económica"));
        this.categorias.add(new Categorias("Primera Clase"));
//        leerCategorias(this.nombre);
    }


    public String getNombre() {
        return this.nombre;
    }

    public ArrayList<Categorias> getCategorias() {
        return this.categorias;
    }

    // Esta función se define en caso de usar un archivo de texto para almacenar las categorías por Aerolínea,
    // en este proyecto se considera solamente las categorías Económica y Primera Clase

//    public void leerCategorias(String nombre) {
//        Path path = Path.of(AEROLINEAS_FILE);
//        try (BufferedReader lector = Files.newBufferedReader(path)) {
//            String linea = null;
//            while ((linea = lector.readLine()) != null) {
//                String[] listCategorias = linea.split(",");
//                if (nombre.equals(listCategorias[0])) {
//                    for (int i = 1; i < listCategorias.length; i++) {
//                        this.categorias.add(new Categorias(listCategorias[i]));
//                    }
//                }
//            }
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
//    }

    public static void insertarAerolinea(Aerolineas aerolinea) {
        try(BufferedWriter texto = new BufferedWriter(new FileWriter("resources/vuelos2.txt", true))) {
            // Escribimos en el archivo, la nueva aerolínea creada
            texto.write("\n" + aerolinea.getNombre() + "," +
                    "Económica,Primera Clase"
            );
        } catch (IOException e) {
            System.out.println(e);
        }
    }

}
