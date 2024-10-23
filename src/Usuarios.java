import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Usuarios {
    private String nombre;
    private int clave;
    private static Scanner scanner = new Scanner(System.in);

    private static final String USERADMIN = "admin";

    private static final String CLAVEADMIN = "0000";

    static HashMap<String, Usuarios> usuariosHashMap = new HashMap<>();

    private static ArrayList<Vuelos> vuelos = Vuelos.getVuelos();

    private static HashMap<String, ArrayList<Reservas>> reservasHash = new HashMap<>();

    private ArrayList<Reservas> reservas = new ArrayList<>();

    public Usuarios(String nombre) {
        this.nombre = nombre;
        this.clave = generarClave();
        this.reservas = new ArrayList<>();
    }

    public Usuarios(String nombre, int clave) {
        this.nombre = nombre;
        this.clave = clave;
    }

    public String getNombre() {
        return this.nombre;
    }

    public int getClave() {
        return this.clave;
    }

    public static boolean iniciarAdministrador() {
        System.out.println("=======================================");
        System.out.println("               Iniciar Sesion        ");
        System.out.println("=======================================");


        String nombre;
        System.out.print("Ingrese su nombre de usuario: ");
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

    public void buscarVuelo() {
        System.out.println("=======================================");
        System.out.println("  Usuario: " + this.nombre);
        System.out.println("=======================================");
        System.out.println("  Buscar Vuelo          ");
        System.out.println("=======================================");

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

    public void verReservas() {
        if (this.reservas.size() == 0) System.out.println("\n***No se encontraron reservas***\n");
        for(Reservas reserva: this.reservas) {
            System.out.println(reserva);
        }
    }

    public void agregarReserva(Reservas reserva) {
        this.reservas.add(reserva);
        reservasHash.put(this.nombre, this.reservas);
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

    private int generarClave() {
        int clave = -1;
        do {
            try {
                System.out.print("Ingrese su clave (4 dígitos - XXXX): ");
                clave = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println();
                System.out.println("\n***Debe ser un número de 4 dígitos***");
                System.out.println();
                clave = 1; // Para que se repita el ciclo
            } finally {
                scanner.nextLine();
            }
        } while (clave < 1000 || clave > 9999);
        return clave;
    }

    private static boolean verificarClave(Usuarios actual) {
        int clave = -1;
        do {
            try {
                System.out.print("Ingrese su clave (4 dígitos - XXXX): ");
                clave = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println();
                System.out.println("\n***Debe ser un número de 4 dígitos***");
                System.out.println();
                clave = 1; // Para que se repita el ciclo
            } finally {
                scanner.nextLine();
            }
        } while (clave < 1000 || clave > 9999);
        if (clave == actual.getClave())
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
                int clave = Integer.parseInt(lineaSplit[1]);
                usuariosHashMap.put(nombre, new Usuarios(nombre, clave));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        leerReservas();
    }

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

}
