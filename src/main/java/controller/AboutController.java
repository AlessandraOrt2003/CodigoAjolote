package controller;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import java.net.URI;
import java.awt.Desktop;
import java.net.URL;
import controller.MainController;
import java.util.ResourceBundle;
public class AboutController implements Initializable {

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void openContact(ActionEvent event) {
        try {
            Desktop.getDesktop().mail(URI.create("mailto:andrea.ortiz.jaime@alumnos.uacm.edu.mx"));

        } catch (Exception e) {
            System.err.println("Error al abrir el cliente de correo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void volverAHome() {
        if (mainController != null) {
            mainController.cargarVistaHome();
        }
    }
}