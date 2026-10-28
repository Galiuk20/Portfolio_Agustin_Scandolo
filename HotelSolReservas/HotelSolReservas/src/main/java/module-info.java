module es.hotelsol.reservas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;

    exports es.hotelsol.reservas;
    opens es.hotelsol.reservas to javafx.fxml;
}
