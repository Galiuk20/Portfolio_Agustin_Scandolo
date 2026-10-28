package es.hotelsol.reservas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HotelSolApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                HotelSolApp.class.getResource("/es/hotelsol/reservas/reservas-view.fxml"));
        Scene scene = new Scene(loader.load(), 1100, 720);
        scene.getStylesheets().add(
                HotelSolApp.class.getResource("/css/estilos.css").toExternalForm());
        stage.setTitle("Hotel Sol | Gestión de habitaciones");
        stage.setMinWidth(900);
        stage.setMinHeight(620);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}