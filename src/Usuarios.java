import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Usuarios {
    private String nombre;
    private String clave;
    private static Scanner scanner = new Scanner(System.in);

    private static final String USERADMIN = "admin";

    private static final String CLAVEADMIN = "0000";

    static HashMap<String, Usuarios> usuariosHashMap = new HashMap<>();

    private static ArrayList<Vuelos> vuelos = Vuelos.getVuelos();

    private HashMap<Integer, Reservas> reservasHash = new HashMap<>();

    private ArrayList<Reservas> reservas = new ArrayList<>();

    public Usuarios(String nombre) {
        this.nombre = nombre;
        this.clave = generarClave();
        this.reservas = new ArrayList<>();
    }

    public Usuarios(String nombre, String clave) {
        this.nombre = nombre;
        this.clave = clave;
    }

    public static Usuarios iniciarSesion() {
        // Si la cantidad de usuario es 0
        if (usuariosHashMap.size() == 0) {
            System.out.println("\n\t***No hay usuarios registrados***\n");
            return null;
        }

        System.out.println("=======================================");
        System.out.println("               Iniciar Sesion        ");
        System.out.println("=======================================");


        String nombre;
        System.out.print("Ingrese su nombre de usuario: ");
        nombre = scanner.next().trim();

        if (nombre.isEmpty()) {
            System.out.println("No permitido");
            return null;
        }

        Usuarios actual = usuariosHashMap.get(nombre);

        if (actual != null) {
            if (verificarClave(actual)) {
                System.out.println("Sesión iniciada");
                return actual;
            } else {
                System.out.println("\n***Clave Incorrecta***");
            }
        }
        else
            System.out.println("\n***El usuario no existe o lo escribio mal***\n");

        return null;
    }

    public static void registrarUsuario() {
        String nombre;
        System.out.println("=======================================");
        System.out.println("               Registrar Usuario       ");
        System.out.println("=======================================");
        /*
        Pedir nombre de usuario, y almacenar en la ultima linea del archivo, si el nombre de usuario ya existe
        entonces se debe dar un aviso y volver a intentar, cada vez que se crea un usuario debo crear una instancia de la clase
        Usuarios, y una nueva línea en el archivo.
        * */
        System.out.print("Ingrese su nombre de usuario: ");
        nombre = scanner.next().trim();

        if (nombre.isEmpty()) {
            System.out.println("No permitido");
            return;
        }

        Usuarios actual = usuariosHashMap.get(nombre);

        if (actual != null) {
            System.out.println("\n***El usuario ya existe***\n");
            return;
        }

        actual = new Usuarios(nombre);
        usuariosHashMap.put(actual.getNombre(), actual);
        System.out.println("\n=======================================");
        System.out.println("      Usuario registrado con éxito");
        System.out.println("=======================================");
        System.out.println("Por favor recuerde sus datos de ingreso");
        System.out.println("======================================");
        System.out.println("Usuario: " + actual.getNombre());
        System.out.println("Clave: " + actual.getClave());
        System.out.println("======================================");

        try(BufferedWriter texto = new BufferedWriter(new FileWriter("resources/usuarios.txt", true))) {
            // Escribimos en el archivo, el último usuario creado
            texto.write(actual.getNombre() + "," + actual.getClave());
            texto.newLine();
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    public static boolean iniciarAdministrador() {
        System.out.println("=======================================");
        System.out.println("               Iniciar Administrador       ");
        System.out.println("=======================================");


        String nombre;
        System.out.print("Ingrese administrador: ");
        nombre = scanner.next().trim();

        if (nombre.isEmpty()) {
            System.out.println("No permitido");
            return false;
        }

        if (nombre.equals(USERADMIN)) {
            System.out.print("Ingrese la clave de administrador: ");
            String clave = scanner.next().trim();
            if (clave.equals(CLAVEADMIN)) {
                System.out.println("Iniciaste como Administrador");
                return true;
            } else {
                System.out.println("***CLAVE INCORRECTA***");
            }
        } else {
            System.out.println("\n***USUARIO INCORRECTO***\n");
        }

        return false;
    }


    /**
     * Se encarga de llamar a una método de la clase GestionarVuelos que sirve para modularizar más el código
     */
    public void buscarVuelo() {
        System.out.println("=======================================");
        System.out.println("  Usuario: " + this.nombre);
        System.out.println("=======================================");
        System.out.println("  Buscar Vuelo          ");
        System.out.println("=======================================");

        // Se almacenarán los vuelos disponibles que cumplan con los filtros del usuario
        ArrayList<Vuelos> vuelosDisponibles;

        vuelosDisponibles = GestionarVuelos.buscarVuelos();

        if (vuelosDisponibles.size() == 0) {
            System.out.println("\n***No hay vuelos disponibles***\n");
            return;
        }

        System.out.println("=======================================");
        System.out.println("Hay " + vuelosDisponibles.size() +" Vuelos Disponibles");
        System.out.println("=======================================");


        GestionarVuelos.imprimirVuelos(vuelosDisponibles);
        GestionarVuelos.reservarVuelo(this, vuelosDisponibles);

    }

    /**
     * Debe procesar el pago del usuario, en este caso, no se llego a culminar lo propuesto
     */
    public void pagarVuelo() {
        this.verReservas();
        int opcion;
        do {
            System.out.println("=======================================");
            System.out.println("0 - Salir: ");
            System.out.print("Seleccione la reserva a pagar: ");
            try {
                opcion = scanner.nextInt();
                if (opcion < 0 || opcion > this.reservas.size()) System.out.println("\n***OPCION INCORRECTA***\n");
            } catch (Exception e) {
                System.out.println();
                System.out.println("***NO PERMITIDO***");
                System.out.println();
                opcion = -1;
            } finally {
                scanner.nextLine();
            }
        } while (opcion < 0 || opcion > this.reservas.size());


    }

    /**
     * Imprimir del usuario instanciado
     */
    public void verReservas() {
        if (this.reservas.size() == 0) System.out.println("\n***No se encontraron reservas***\n");
        for(Reservas reserva: this.reservas) {
            System.out.println(reserva);
        }
    }

    /**
     * Almacena en el ArrayList de Reservas la nueva reserva que realizó el usuario y guarda en el archivo de texto
     * "reservas.txt" esto se hizo con la intención de almacenar las reservas en futuras ejecuciones
     * @param reserva
     */
    public void agregarReserva(Reservas reserva) {
        this.reservas.add(reserva);
        try(BufferedWriter texto = new BufferedWriter(new FileWriter("resources/reservas.txt", true))) {
            // Escribimos en el archivo, el último usuario creado
            texto.write(this.getNombre() + "," +
                    reserva.getCantidad() + "," +
                    reserva.getCategoria().getCosto() + "," +
                    reserva.getCategoria().getNombre() + "," +
                    reserva.getCosto() + "," +
                    reserva.getEstado() + "," +
                    reserva.getVuelo().getId()
            );
            texto.newLine();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Pide al usuario que ingrese una clave en un formato adecuado y lo retorna al cumplirse la condición
     * @return
     */
    private String generarClave() {
        String clave = "";
        do {
            try {
                System.out.print("Ingrese su clave (4 dígitos - XXXX): ");
                clave = scanner.next().trim();
            } catch (InputMismatchException e) {
                System.out.println();
                System.out.println("\n***Debe ser un número de 4 dígitos***");
                System.out.println();
            }
        } while (clave.length() != 4);
        return clave;
    }

    /**
     * Verifica que la clave ingresada por el usuario sea del formato adecuado y corresponda al usuario que llamó
     * @param actual
     * @return
     */
    private static boolean verificarClave(Usuarios actual) {
        String clave = "";
        do {
            try {
                System.out.print("Ingrese su clave (4 dígitos - XXXX): ");
                clave = scanner.next().trim();
            } catch (InputMismatchException e) {
                System.out.println();
                System.out.println("\n***Debe ser un número de 4 dígitos***");
                System.out.println();
            }
        } while (clave.length() != 4);
        if (clave.equals(actual.getClave()))
            return true;
        return false;
    }

    /**
     * Se leen los usuarios del archivo "usuarios.txt" y se almacena en el HashMap de Usuarios
     * donde la clave es el nombre del usuario y el valor es una instancia de la clase Usuarios
     */
    public static void leerUsuarios() {
        Path path = Path.of("resources/usuarios.txt");
        try (BufferedReader lector = Files.newBufferedReader(path)) {
            String linea = null;
            while ((linea = lector.readLine()) != null) {
                String[] lineaSplit = linea.split(",");
                String nombre = lineaSplit[0];
                String clave = lineaSplit[1];
                usuariosHashMap.put(nombre, new Usuarios(nombre, clave));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        leerReservas();
    }

    /**
     * Se leen las reservas del archivo "reservas.txt" y se almacena en las reservas del usuario
     */
    public static void leerReservas(){
        Path path = Path.of("resources/reservas.txt");
        try (BufferedReader lector = Files.newBufferedReader(path)) {
            String linea = null;
            while ((linea = lector.readLine()) != null) {
                String[] lineaSplit = linea.split(",");
                String nombre = lineaSplit[0];
                int cantidad = Integer.parseInt(lineaSplit[1]);
                Categorias categoria = new Categorias(lineaSplit[3]);
                boolean estado = Boolean.parseBoolean(lineaSplit[5]);
                int vueloID = Integer.parseInt(lineaSplit[6]);
                Reservas reserva = new Reservas(Vuelos.getVuelo(vueloID), cantidad, categoria, estado);
                Usuarios actual = usuariosHashMap.get(nombre);
                actual.reservas.add(reserva);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getClave() {
        return this.clave;
    }

}
