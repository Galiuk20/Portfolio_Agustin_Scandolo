package es.hotelsol.reservas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class ReservasController {
    @FXML private Label hotelSubtitle;
    @FXML private Label totalHabitaciones;
    @FXML private Label totalDisponibles;
    @FXML private Label totalOcupadas;
    @FXML private Label totalMantenimiento;
    @FXML private TextField campoBusqueda;
    @FXML private ComboBox<String> filtroEstado;
    @FXML private TableView<Habitacion> tablaHabitaciones;
    @FXML private TableColumn<Habitacion, Number> colNumero;
    @FXML private TableColumn<Habitacion, String> colTipo;
    @FXML private TableColumn<Habitacion, Number> colPlanta;
    @FXML private TableColumn<Habitacion, Number> colCapacidad;
    @FXML private TableColumn<Habitacion, Number> colPrecio;
    @FXML private TableColumn<Habitacion, String> colEstado;
    @FXML private TableColumn<Habitacion, String> colVista;
    @FXML private TableColumn<Habitacion, String> colAccesible;
    @FXML private Label resultadoBusqueda;
    @FXML private Label numeroResultado;
    @FXML private Label estadoResultado;
    @FXML private Label detalleResultado;
    @FXML private HBox tarjetaResultado;

    private final ObservableList<Habitacion> datos = FXCollections.observableArrayList();
    private FilteredList<Habitacion> filtradas;

    @FXML
    private void initialize() {
        colNumero.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getNumero()));
        colTipo.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getTipo()));
        colPlanta.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getPlanta()));
        colCapacidad.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getCapacidad()));
        colPrecio.setCellValueFactory(cell -> new javafx.beans.property.SimpleDoubleProperty(cell.getValue().getPrecioNoche()));
        colEstado.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getEstadoVisible()));
        colVista.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getVista()));
        colAccesible.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getAccesible().equalsIgnoreCase("sí") ? "Sí" : "No"));

        colPrecio.setCellFactory(column -> new TableCell<>() {
            @Override protected void updateItem(Number precio, boolean empty) {
                super.updateItem(precio, empty);
                setText(empty || precio == null ? null : formatoEuro(precio.doubleValue()));
            }
        });

        colEstado.setCellFactory(column -> new TableCell<>() {
            @Override protected void updateItem(String estado, boolean empty) {
                super.updateItem(estado, empty);
                setText(empty ? null : estado);
                getStyleClass().removeAll("estado-disponible", "estado-ocupada", "estado-mantenimiento");
                if (!empty && estado != null) {
                    switch (estado) {
                        case "Disponible" -> getStyleClass().add("estado-disponible");
                        case "Ocupada" -> getStyleClass().add("estado-ocupada");
                        default -> getStyleClass().add("estado-mantenimiento");
                    }
                }
            }
        });

        filtroEstado.setItems(FXCollections.observableArrayList(
                "Todos los estados", "Disponible", "Ocupada", "Mantenimiento"));
        filtroEstado.getSelectionModel().selectFirst();

        try {
            List<Habitacion> cargadas = new HotelXmlService().cargarHabitaciones();
            datos.setAll(cargadas);
            filtradas = new FilteredList<>(datos, h -> true);
            tablaHabitaciones.setItems(filtradas);
            hotelSubtitle.setText("Madrid · Calle Mayor 25 · 4 estrellas");
            actualizarResumen();
            campoBusqueda.textProperty().addListener((obs, oldValue, newValue) -> aplicarFiltros());
            filtroEstado.valueProperty().addListener((obs, oldValue, newValue) -> aplicarFiltros());
            mostrarMensajeInicial();
        } catch (Exception ex) {
            resultadoBusqueda.setText("No se han podido cargar los datos del XML.");
            detalleResultado.setText(ex.getMessage());
        }
    }

    @FXML
    private void buscarHabitacion() {
        String texto = campoBusqueda.getText() == null ? "" : campoBusqueda.getText().trim();
        if (texto.isEmpty()) {
            mostrarAviso("Introduce el número de habitación que quieres consultar.");
            campoBusqueda.requestFocus();
            return;
        }
        if (!texto.matches("\\d+")) {
            mostrarAviso("El número de habitación debe contener solo cifras.");
            return;
        }

        int numero = Integer.parseInt(texto);
        Optional<Habitacion> encontrada = datos.stream()
                .filter(h -> h.getNumero() == numero).findFirst();

        if (encontrada.isEmpty()) {
            tarjetaResultado.getStyleClass().setAll("result-card", "result-neutral");
            resultadoBusqueda.setText("Habitación inexistente");
            numeroResultado.setText("N.º " + numero);
            estadoResultado.setText("No existe");
            detalleResultado.setText("No hay ninguna habitación con ese número en los datos del Hotel Sol.");
            return;
        }

        Habitacion h = encontrada.get();
        numeroResultado.setText("Habitación " + h.getNumero());
        estadoResultado.setText(h.getEstadoVisible());
        detalleResultado.setText(h.getTipo() + " · Planta " + h.getPlanta() + " · "
                + h.getCapacidad() + (h.getCapacidad() == 1 ? " persona" : " personas")
                + " · " + formatoEuro(h.getPrecioNoche()) + "/noche · Vista: " + h.getVista());

        switch (h.getEstado().toLowerCase()) {
            case "disponible" -> {
                tarjetaResultado.getStyleClass().setAll("result-card", "result-success");
                resultadoBusqueda.setText("La habitación está disponible.");
            }
            case "ocupada" -> {
                tarjetaResultado.getStyleClass().setAll("result-card", "result-danger");
                resultadoBusqueda.setText("La habitación está ocupada.");
            }
            default -> {
                tarjetaResultado.getStyleClass().setAll("result-card", "result-warning");
                resultadoBusqueda.setText("La habitación no está disponible: está en mantenimiento.");
            }
        }

        tablaHabitaciones.getSelectionModel().select(h);
        tablaHabitaciones.scrollTo(h);
    }

    @FXML
    private void limpiarBusqueda() {
        campoBusqueda.clear();
        filtroEstado.getSelectionModel().selectFirst();
        tablaHabitaciones.getSelectionModel().clearSelection();
        mostrarMensajeInicial();
    }

    private void aplicarFiltros() {
        if (filtradas == null) return;
        String texto = campoBusqueda.getText() == null ? "" : campoBusqueda.getText().trim().toLowerCase(Locale.ROOT);
        String estado = filtroEstado.getValue();
        filtradas.setPredicate(h -> {
            boolean coincideNumero = texto.isEmpty() || String.valueOf(h.getNumero()).contains(texto);
            boolean coincideEstado = estado == null || estado.equals("Todos los estados")
                    || h.getEstadoVisible().equals(estado);
            return coincideNumero && coincideEstado;
        });
    }

    private void actualizarResumen() {
        totalHabitaciones.setText(String.valueOf(datos.size()));
        totalDisponibles.setText(String.valueOf(datos.stream().filter(h -> h.getEstado().equalsIgnoreCase("disponible")).count()));
        totalOcupadas.setText(String.valueOf(datos.stream().filter(h -> h.getEstado().equalsIgnoreCase("ocupada")).count()));
        totalMantenimiento.setText(String.valueOf(datos.stream().filter(h -> h.getEstado().equalsIgnoreCase("mantenimiento")).count()));
    }

    private void mostrarMensajeInicial() {
        tarjetaResultado.getStyleClass().setAll("result-card", "result-neutral");
        resultadoBusqueda.setText("Busca una habitación por su número");
        numeroResultado.setText("—");
        estadoResultado.setText("Sin consultar");
        detalleResultado.setText("El estado y los detalles de la habitación aparecerán aquí.");
    }

    private void mostrarAviso(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Consulta de habitaciones");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private String formatoEuro(double valor) {
        NumberFormat formato = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-ES"));
        return formato.format(valor);
    }
}
