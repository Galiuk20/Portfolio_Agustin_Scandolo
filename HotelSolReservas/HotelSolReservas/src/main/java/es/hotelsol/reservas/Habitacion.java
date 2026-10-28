package es.hotelsol.reservas;

public class Habitacion {
    private final int numero;
    private final String estado;
    private final int planta;
    private final String tipo;
    private final int capacidad;
    private final double precioNoche;
    private final String vista;
    private final String accesible;

    public Habitacion(int numero, String estado, int planta, String tipo, int capacidad,
                      double precioNoche, String vista, String accesible) {
        this.numero = numero;
        this.estado = estado;
        this.planta = planta;
        this.tipo = tipo;
        this.capacidad = capacidad;
        this.precioNoche = precioNoche;
        this.vista = vista;
        this.accesible = accesible;
    }

    public int getNumero() { return numero; }
    public String getEstado() { return estado; }
    public int getPlanta() { return planta; }
    public String getTipo() { return tipo; }
    public int getCapacidad() { return capacidad; }
    public double getPrecioNoche() { return precioNoche; }
    public String getVista() { return vista; }
    public String getAccesible() { return accesible; }

    public String getEstadoVisible() {
        return switch (estado.toLowerCase()) {
            case "disponible" -> "Disponible";
            case "ocupada" -> "Ocupada";
            case "mantenimiento" -> "Mantenimiento";
            default -> estado;
        };
    }
}
