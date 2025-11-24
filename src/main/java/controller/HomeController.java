package controller;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    private MainController mainController;

    // Método para recibir la referencia del MainController
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inicialización de la vista home
        System.out.println("HomeController inicializado - Vista Home cargada correctamente");
    }

    // Método para ir a la vista de cursos
    @FXML
    public void irACursos(ActionEvent event) {
        if (mainController != null) {
            mainController.cargarVistaCursos();
        } else {
            System.out.println("Error: MainController no está disponible");
            // Posible mejora: mostrar alerta al usuario
        }
    }

    // Método para ir a la vista about
    @FXML
    public void irAAcercaDe(ActionEvent event) {
        if (mainController != null) {
            mainController.cargarVistaAbout();
        } else {
            System.out.println("Error: MainController no está disponible");
            // Posible mejora: mostrar alerta al usuario
        }
    }

    // MÉTODO ADICIONAL: Para volver a home desde otras vistas (si es necesario)
    @FXML
    public void irAHome(ActionEvent event) {
        if (mainController != null) {
            mainController.cargarVistaHome();
        } else {
            System.out.println("Error: MainController no está disponible");
        }
    }

    // MÉTODO ADICIONAL: Para futuras expansiones (Sprint 3-4)
    @FXML
    public void explorarCursosPopulares(ActionEvent event) {
        System.out.println("Explorando cursos populares...");
        // Futura implementación para Sprint 3-4
        if (mainController != null) {
            mainController.cargarVistaCursos();
        }
    }

    // MÉTODO ADICIONAL: Para búsqueda rápida (Sprint 4)
    @FXML
    public void buscarCursos(ActionEvent event) {
        System.out.println("Función de búsqueda activada - Para implementar en Sprint 4");
        // Lógica de búsqueda para el Sprint 4
    }
}