package controller;

import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import java.net.URI;
import java.awt.Desktop;
import java.net.URL;
import java.util.ResourceBundle;

public class AboutController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inicialización si es necesaria
    }

    public void openContact(ActionEvent event) {
        try {
            // CORRECCIÓN: Usar URI.create() para crear el URI correctamente
            Desktop.getDesktop().mail(URI.create("mailto:andrea.ortiz.jaime@alumnos.uacm.edu.mx"));

        } catch (Exception e) {
            System.err.println("Error al abrir el cliente de correo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}