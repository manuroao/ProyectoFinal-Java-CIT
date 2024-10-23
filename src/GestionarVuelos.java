import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class GestionarVuelos {

    private static Scanner scanner = new Scanner(System.in);

    public static void agregarVuelo() {
        // Se encarga de dar formato a la fecha -> MM/dd/yyyy
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String origen = validarNombre("Origen");
        String destino = validarNombre("Destino");
        String aerolinea = validarNombre("Aerolinea");
        LocalDate fecha = obtenerFecha();

        if (origen.isEmpty() || destino.isEmpty() || (fecha == null)) {
            System.out.println("\n***ES NECESARIO TODOS LOS DATOS DEL VUELO***\n");
            return;
        }

        Vuelos vuelo = new Vuelos(origen, destino, fecha, new Aerolineas(aerolinea));
        Vuelos.agregarVuelo(vuelo);
        try(BufferedWriter texto = new BufferedWriter(new FileWriter("resources/vuelos.txt", true))) {
            // Escribimos en el archivo, el último usuario creado
            texto.write("\n" + origen + "," +
                    destino + "," +
                    (String) fecha.format(formatoFecha) + "," +
                    aerolinea
            );
        } catch (IOException e) {
            System.out.println(e);
        }


    }

    public static void reservarVuelo(Usuarios actual, ArrayList<Vuelos> vuelosDisponibles) {
        int opcion = 0, cantidad;

        opcion = elegirVuelo(vuelosDisponibles);

        if (opcion == 0) return;

        Vuelos vuelo = vuelosDisponibles.get(opcion - 1); // Accede al vuelo seleccionado por el usuario

        opcion = elegirCategoria(vuelo);

        if (opcion == 0) return;

        Categorias categoria = vuelo.getAerolinea().getCategorias().get(opcion - 1); // Obtiene la categoría seleccionada por el usuario

        cantidad = elegirCantidad(categoria);

        categoria.setCantidad(cantidad);

        Reservas nuevaReserva = new Reservas(vuelo, cantidad, categoria);
        actual.agregarReserva(nuevaReserva);


    }

    public static ArrayList<Vuelos> buscarVuelos() {
        ArrayList<Vuelos> vuelosDisponibles = new ArrayList<>();
        String origen = validarNombre("Origen");
        String destino = validarNombre("Destino");
        LocalDate fecha = obtenerFecha();

        for (Vuelos vuelo : Vuelos.getVuelos()) {
            if (vueloCoincide(vuelo, origen, destino, fecha) && tieneAcientosDisponibles(vuelo)) {
                vuelosDisponibles.add(vuelo);
            }
        }
        return vuelosDisponibles;

    }

    /**
     * Devuelve el origen o el destino que el usuario ingresa, con su correspondiente validación
     * @param texto
     * @return String
     */
    private static String validarNombre(String texto) {
        String palabra;
        boolean flag;
        do {
            flag = false; // Se asume que el usuario ingresará correctamente la palabra
            System.out.print("Ingrese " + texto + ": ");
            palabra = scanner.nextLine().trim();
            // El origen o destino solo debe contener letras
            for(Character caracter: eliminarAcentos(palabra).toCharArray()) {
                if (!Character.isAlphabetic(caracter) && caracter != ' ') {
                    // Si el usuario ingresa mal la palabra, cambia el estado de la bandera,
                    // por consiguiente, el ciclo se repite
                    flag = true;
                    break;
                }
            }
        } while(flag);
        return palabra;
    }

    /**
     * Eliminar acentos para la comparación
     * @param texto
     * @return
     */
    private static String eliminarAcentos(String texto) {
        String textoNormalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(textoNormalizado).replaceAll("");
    }

    /**
     * Se encarga de verificar si la fecha ingresada por el usuario cumple con el formato especificado
     * @param fecha
     * @return boolean
     */
    private static boolean esFechaValida(String fecha) {
        int anho = Integer.parseInt(fecha.split("/")[2]);
        int anhoActual = LocalDate.now().getYear();
        // Validar que el año no sea menor al actual
        if (anho < anhoActual) {
            return false;
        }
        return true;
    }

    /**
     * Obtiene la fecha ingresada por el usuario comprobando que cumpla con el formato establecido
     * @return Date
     */
    private static LocalDate obtenerFecha(){
        boolean flag = false;
        LocalDate fecha = null;
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        do {
            System.out.print("Ingrese la fecha (MM/dd/yyyy): ");
            String fechaString = scanner.nextLine();
            if (fechaString.isEmpty()) break;
            try {
                fecha = LocalDate.parse(fechaString, formatoFecha);
                flag = esFechaValida(fechaString); // Validar formato
                if (!flag) System.out.println("Fecha no válida");;
            } catch (DateTimeParseException e) {
                System.out.println("Fecha no válida");
            }
        } while (!flag);
        return fecha;
    }

    private static boolean vueloCoincide(Vuelos vuelo, String origen, String destino, LocalDate fecha) {
        // Filtra por origen si fue ingresado
        if (!origen.isEmpty() && !vuelo.getOrigen().equalsIgnoreCase(origen)) {
            return false; // Si el origen no coincide, pasa al siguiente vuelo
        }

        // Filtra por destino si fue ingresado
        if (!destino.isEmpty() && !vuelo.getDestino().equalsIgnoreCase(destino)) {
            return false; // Si el destino no coincide, pasa al siguiente vuelo
        }

        // Filtra por fecha si fue ingresada
        if (fecha != null && !vuelo.getFecha().equals(fecha)) {
            return false; // Si la fecha no coincide, pasa al siguiente vuelo
        }
        return true;
    }

    private static int elegirCantidad(Categorias categoria) {
        int cantidad;
        do {
            System.out.println("=======================================");
            System.out.print("Elige la cantidad a reservar: ");
            try {
                cantidad = scanner.nextInt();
                if (cantidad <= 0 ||
                        cantidad > categoria.getCantidad())
                    System.out.println("\n***CANTIDAD INCORRECTA***\n");
            } catch (Exception e) {
                System.out.println();
                System.out.println("***NO PERMITIDO***");
                System.out.println();
                cantidad = -1;
            } finally {
                scanner.nextLine();
            }
        } while(cantidad > categoria.getCantidad() || cantidad <= 0);
        return cantidad;
    }

    private static int elegirCategoria(Vuelos vuelo) {
        ArrayList<Integer> categoriasDisponibles = new ArrayList<>();
        categoriasDisponibles.add(0); // Para que el usuario pueda cancelar la reserva
        int opcion;
        imprimirVuelo(vuelo);
        do {
            System.out.println("\n0 - Cancelar");
            System.out.println("=======================================");
            int categoriaID = 1;
            for (Categorias categoria: vuelo.getAerolinea().getCategorias()) {
                System.out.println(categoriaID + " - " + ((categoria.getCantidad() > 0) ? categoria : categoria.getNombre() + ": Agotado"));
                if(categoria.getCantidad() > 0) {
                    categoriasDisponibles.add(categoriaID);
                }
                System.out.println("=======================================");
                categoriaID++;
            }
            System.out.println("=======================================");
            System.out.print("Elige la categoría: ");
            try {
                opcion = scanner.nextInt();
                if (!categoriasDisponibles.contains(opcion))
                    System.out.println("\n***NO DISPONIBLE***\n");
            } catch (Exception e) {
                System.out.println();
                System.out.println("***NO PERMITIDO***");
                System.out.println();
                opcion = -1;
            } finally {
                scanner.nextLine();
            }
        } while (!categoriasDisponibles.contains(opcion));
        return opcion;
    }

    private static int elegirVuelo(ArrayList<Vuelos> vuelosDisponibles) {
        int opcion;
        do {
            System.out.println("=======================================");
            System.out.println("0 - Salir: ");
            System.out.print("Elija el vuelo: ");
            try {
                opcion = scanner.nextInt();
                if (opcion < 0 || opcion > vuelosDisponibles.size()) System.out.println("\n***OPCION INCORRECTA***\n");
            } catch (Exception e) {
                System.out.println();
                System.out.println("***NO PERMITIDO***");
                System.out.println();
                opcion = -1;
            } finally {
                scanner.nextLine();
            }
        } while (opcion < 0 || opcion > vuelosDisponibles.size());

        return opcion;
    }

    private static boolean tieneAcientosDisponibles(Vuelos vuelo) {

        for(Categorias categoria: vuelo.getAerolinea().getCategorias()) {
            if (categoria.getCantidad() > 0) {
                return true;
            }
        }
        return false;
    }

    public static void imprimirVuelos(ArrayList<Vuelos> vuelosDisponibles) {
//        int cantidad = 1;
//        for (Vuelos vuelo: vuelosDisponibles) {
////            System.out.print(cantidad);
//            System.out.println("=======================================");
//            System.out.println(vuelo);
//            System.out.println("Categorias disponibles: ");
//            System.out.println("=======================================");
//            for(Categorias categoria: vuelo.getAerolinea().getCategorias()) {
//                if (categoria.getCantidad() > 0) {
//                    System.out.println(categoria.getNombre() + " - " + "Costo: " + categoria.getCosto());
//                }
//            }
//            System.out.println();
//            cantidad++;
//        }
        int cantidad = 1;

        // Cabecera de la tabla
        System.out.printf("%-5s %-15s %-15s %-15s %-20s%n", "No.", "Origen", "Destino", "Fecha", "Aerolínea");
        System.out.println("=====================================================================");

        for (Vuelos vuelo : vuelosDisponibles) {
//            imprimirVuelo(vuelo);
            // Imprimir los detalles del vuelo
            System.out.printf("%-5d %-15s %-15s %-15s %-20s%n", cantidad, vuelo.getOrigen(), vuelo.getDestino(), vuelo.getFecha(), vuelo.getAerolinea().getNombre());

            // Imprimir las categorías disponibles
            System.out.println("  Categorías disponibles:");
            System.out.println("  -----------------------");
            for (Categorias categoria : vuelo.getAerolinea().getCategorias()) {
                if (categoria.getCantidad() > 0) {
                    System.out.printf("  %-15s %-15s %-15s %-15s %-15s%n", categoria.getNombre(), "Costo:", categoria.getCosto(), "Cantidad:", categoria.getCantidad());
                }
            }

            System.out.println();  // Línea en blanco para separar cada vuelo
            cantidad++;
        }
    }

    private static void imprimirVuelo(Vuelos vuelo) {
        System.out.printf("%-15s %-15s %-15s %-20s%n", "Origen", "Destino", "Fecha", "Aerolínea");
        System.out.println("=====================================================================");
        System.out.printf("%-15s %-15s %-15s %-20s%n", vuelo.getOrigen(), vuelo.getDestino(), vuelo.getFecha(), vuelo.getAerolinea().getNombre());
    }
}
