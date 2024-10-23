public class Reservas {
    private Vuelos vuelo;
    private boolean estado;
    private double costo;
    private int cantidad;

    private Categorias categoria;

    public Reservas(Vuelos vuelo, int cantidad, Categorias categoria, boolean estado) {
        this.vuelo = vuelo;
        this.cantidad = cantidad;
        this.categoria = categoria;
        this.costo = categoria.getCosto() * cantidad;
        this.estado = estado;
    }
    public Reservas(Vuelos vuelo, int cantidad, Categorias categoria) {
        this.vuelo = vuelo;
        this.estado = false;
        this.cantidad = cantidad;
        this.costo = categoria.getCosto() * cantidad;
        this.categoria = categoria;
    }

    public Vuelos getVuelo() {
        return vuelo;
    }

    public void setVuelo(Vuelos vuelo) {
        this.vuelo = vuelo;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Categorias getCategoria() {
        return categoria;
    }

    public void setCategoria(Categorias categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "=======================" +
                "\nVuelo: " + this.vuelo +
                "\nCantidad: " + this.cantidad +
                "\nCosto: " + this.categoria.getCosto() +
                "\nCategoria: " + this.categoria.getNombre() +
                "\nTotal: " + this.costo +
                "\nEstado: " + (this.estado ? "Pagado" : "Pendiente");
    }
}
