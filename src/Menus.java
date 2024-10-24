import java.util.InputMismatchException;
import java.util.Scanner;

public class Menus {
    private static Scanner scanner = new Scanner(System.in);
    public static int menuInicio(){
        int opcion = -1;
        System.out.println("=======================================");
        System.out.println("      Bienvenido a Vuelos con CIT!  ");
        System.out.println("=======================================");
        do {
            System.out.println("  0- Salir");
            System.out.println("  1- Iniciar Sesión");
            System.out.println("  2- Registrarse");
            System.out.println("  3- Modo Administrador");
            System.out.println("=======================================");
            System.out.print("Elija su opción: ");
            try {
                opcion = scanner.nextInt();
                if (opcion < 0 || opcion > 3) System.out.println("\n***OPCION INCORRECTA***\n");
            } catch (Exception e) {
                System.out.println();
                System.out.println("***NO PERMITIDO***");
                System.out.println();
            } finally {
                scanner.nextLine();
            }

        } while (opcion < 0 || opcion > 3);

        return opcion;
    }

    public static boolean menuUsuario(Usuarios actual) {
        int opcion;
        do {
            System.out.println("=======================================");
            System.out.println("               Menú Usuario            ");
            System.out.println("=======================================");
            System.out.println("Usuario: " + actual.getNombre());
            System.out.println("=======================================");
            System.out.println("  0- Cerrar Sesión");
            System.out.println("  1- Buscar Vuelo");
            System.out.println("  2- Ver Reservas");
            System.out.println("  3- Pagar Vuelo");
            System.out.println("=======================================");
            System.out.print("Elija su opción: ");

            try {
                opcion = scanner.nextInt();
            } catch (InputMismatchException e) {
                opcion = -1;
            }
            finally {
                scanner.nextLine();
            }

            switch (opcion) {
                case 0:
                    return true;
                case 1:
                    actual.buscarVuelo();
                    break;
                case 2:
                    actual.verReservas();
                    break;
                case 3:
                    actual.pagarVuelo();
                    break;
                default:
                    System.out.println("\n***OPCIÓN INCORRECTA***\n");
            }

        } while (opcion != 0);

        return false;
    }

    public static boolean menuAdministrador() {
        int opcion;
        do {
            System.out.println("=======================================");
            System.out.println("               Menú Administrador            ");
            System.out.println("=======================================");
            System.out.println("=======================================");
            System.out.println("  0- Cerrar Sesión");
            System.out.println("  1- Agregar Vuelo");
            System.out.println("=======================================");
            System.out.print("Elija su opción: ");

            try {
                opcion = scanner.nextInt();
            } catch (InputMismatchException e) {
                opcion = -1;
            }
            finally {
                scanner.nextLine();
            }

            switch (opcion) {
                case 0:
                    return false;
                case 1:
                    GestionarVuelos.agregarVuelo();
                    return true;
                default:
                    System.out.println("\n***OPCIÓN INCORRECTA***\n");
            }

        } while (opcion != 0);
        return false;
    }
}
