package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Cargar la vista principal
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main-view.fxml"));
        Parent root = loader.load();

        // Configurar la escena
        Scene scene = new Scene(root, 1200, 800);

        // Aplicar estilos CSS
        try {
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Error al cargar el archivo CSS: " + e.getMessage());
        }

        // Configurar la ventana principal
        primaryStage.setTitle("CODIGO AJOLOTE - Aprende, programa y crece con nosotros");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);

        // Mostrar la ventana
        primaryStage.show();

        System.out.println("Aplicación CODIGO AJOLOTE iniciada correctamente");
    }

    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            System.err.println("Error al iniciar la aplicación: " + e.getMessage());
            e.printStackTrace();
        }
    }
}