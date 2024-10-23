import java.util.HashMap;

public class Main {
    static HashMap<String, Usuarios> usuariosHashMap = new HashMap<>();

    public static void main(String[] args) {

        // En un archivo "vuelos.txt" se almacenan los vuelos disponibles simulando una base de datos
        Vuelos.leerVuelos();


        // En un archivo "usuarios.txt" se almacenan los usuarios, incluyendo de aquellas ejecuciones previas
        // del programa simulando una base de datos
        Usuarios.leerUsuarios();


        int opcion = -1;

        do {
            opcion = Menus.menuInicio();
            switch (opcion) {
                case 0:
                    System.out.println("\n***FIN DEL PROGRAMA***\n");
                    break;
                case 1:
                    Usuarios actual = Usuarios.iniciarSesion();
                    if(actual != null) {
                        if(Menus.menuUsuario(actual)) opcion = -1;
                    };
                    break;
                case 2:
                    Usuarios.registrarUsuario();
                    break;
                case 3:
                    if(Usuarios.iniciarAdministrador())
                        Menus.menuAdministrador();
                    break;
                default:
                    System.out.println("\n***NO PERMITIDO***\n");
            }
        } while (opcion != 0);

    }


}
