package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private BorderPane mainBorderPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("MainController inicializado - Cargando vista inicial...");
        // Cargar la vista home por defecto al iniciar
        cargarVistaHome();
    }

    // Método para cambiar las vistas centrales
    public void cargarVista(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent vista = loader.load();

            // Pasar la referencia del MainController a los controladores hijos
            Object controlador = loader.getController();
            if (controlador instanceof HomeController) {
                ((HomeController) controlador).setMainController(this);
            }
            // AGREGADO: Pasar referencia a CursosController
            else if (controlador instanceof CursosController) {
                // Preparado para futuras expansiones en Sprint 2
                System.out.println("CursosController cargado - Listo para Sprint 2");
            }
            // AGREGADO: Pasar referencia a AboutController
            else if (controlador instanceof AboutController) {
                // Preparado para futuras expansiones
                System.out.println("AboutController cargado");
            }

            mainBorderPane.setCenter(vista);
            System.out.println("Vista cargada: " + fxmlPath);

        } catch (Exception e) {
            System.err.println("Error al cargar la vista: " + fxmlPath);
            e.printStackTrace();
        }
    }

    // Métodos específicos para cada vista
    public void cargarVistaHome() {
        cargarVista("/home-view.fxml");
    }

    public void cargarVistaCursos() {
        cargarVista("/cursos-view.fxml");
    }

    public void cargarVistaAbout() {
        cargarVista("/about-view.fxml");
    }

    // MÉTODO AGREGADO: Para navegación desde el menú principal (si existe)
    @FXML
    private void cargarHomeDesdeMenu() {
        cargarVistaHome();
    }

    @FXML
    private void cargarCursosDesdeMenu() {
        cargarVistaCursos();
    }

    @FXML
    private void cargarAboutDesdeMenu() {
        cargarVistaAbout();
    }

    // MÉTODO AGREGADO: Para futuras expansiones (Sprint 3-4)
    public BorderPane getMainBorderPane() {
        return mainBorderPane;
    }
}