package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import java.net.URL;
import java.util.ResourceBundle;
import controller.HomeController;
import controller.CursosController;
import controller.LeccionDetalleController;
import controller.AboutController;

public class MainController implements Initializable {

    @FXML
    private BorderPane mainBorderPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("MainController inicializado - Cargando vista inicial...");
        cargarVistaHome();
    }

    public void cargarVista(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent vista = loader.load();

            // Pasar referencia del MainController a controladores hijos
            Object controlador = loader.getController();
            if (controlador instanceof HomeController) {
                ((HomeController) controlador).setMainController(this);
            }
            else if (controlador instanceof CursosController) {
                ((CursosController) controlador).setMainController(this);
            }
            else if (controlador instanceof LeccionDetalleController) {
                ((LeccionDetalleController) controlador).setMainController(this);
            }
            else if (controlador instanceof AboutController) {
                ((AboutController) controlador).setMainController(this);
            }

            mainBorderPane.setCenter(vista);
            System.out.println("Vista cargada: " + fxmlPath);

        } catch (Exception e) {
            System.err.println("Error al cargar la vista: " + fxmlPath);
            e.printStackTrace();
        }
    }

    public void cambiarVistaCentral(Parent nuevaVista) {
        mainBorderPane.setCenter(nuevaVista);
        System.out.println("Vista central cambiada dinámicamente");
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

    // MÉTODOS PARA EL MENÚ FXML
    @FXML
    private void cargarHomeDesdeMenu() {
        System.out.println("🏠 Navegando a Home desde menú...");
        cargarVistaHome();
    }

    @FXML
    private void cargarCursosDesdeMenu() {
        System.out.println("📚 Navegando a Cursos desde menú...");
        cargarVistaCursos();
    }

    @FXML
    private void cargarAboutDesdeMenu() {
        System.out.println("ℹ️ Navegando a About desde menú...");
        cargarVistaAbout();
    }
}