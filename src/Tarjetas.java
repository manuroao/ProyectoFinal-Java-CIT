import java.util.ArrayList;

public class Tarjetas {
    private Usuarios usuario;
    private String nroTarjeta;
    private String fecha;
    private int monto;

    public static ArrayList<Tarjetas> tarjetas = new ArrayList<>();
    public Tarjetas(Usuarios usuario, String nroTarjeta, String fecha, int monto) {
        this.usuario = usuario;
        this.nroTarjeta = nroTarjeta;
        this.fecha = fecha;
        this.monto = monto;
    }

    public static void agregarTarjeta(Tarjetas tarjeta) {
        tarjetas.add(tarjeta);
    }
}
