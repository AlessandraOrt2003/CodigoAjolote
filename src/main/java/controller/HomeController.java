package controller;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import service.UserDataManager;
import controller.MainController;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    private MainController mainController;
    private UserDataManager userDataManager;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.userDataManager = new UserDataManager();
        mostrarPanelProgreso();
    }

    private void mostrarPanelProgreso() {
        userDataManager.mostrarEstadisticas();
        // Los labels fueron eliminados porque no existen en el nuevo home-view.fxml
    }

    @FXML
    public void irACursos(ActionEvent event) {
        if (mainController != null) {
            mainController.cargarVistaCursos();
        }
    }

    @FXML
    public void irAAcercaDe(ActionEvent event) {
        if (mainController != null) {
            mainController.cargarVistaAbout();
        }
    }

    @FXML
    public void irAHome(ActionEvent event) {
        if (mainController != null) {
            mainController.cargarVistaHome();
        }
    }

    @FXML
    public void explorarCursosPopulares(ActionEvent event) {
        System.out.println("Explorando cursos populares...");
        if (mainController != null) {
            mainController.cargarVistaCursos();
        }
    }

    @FXML
    public void buscarCursos(ActionEvent event) {
        System.out.println("🔍 Función de búsqueda activada desde Home");
        if (mainController != null) {
            mainController.cargarVistaCursos();
        }
    }

    @FXML
    public void verEstadisticasCompletas(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Estadísticas Completas");
        alert.setHeaderText("📊 Tu Progreso de Aprendizaje");
        alert.setContentText(
                "👤 Usuario: " + userDataManager.getUserData().getNombreUsuario() + "\n" +
                        "📈 Progreso General: " + String.format("%.1f", userDataManager.getProgresoGeneral()) + "%\n" +
                        "✅ Lecciones Completadas: " + userDataManager.getTotalLeccionesCompletadas() + "\n" +
                        "🎓 Cursos Inscritos: " + userDataManager.getTotalCursosInscritos() + "\n\n" +
                        "🏆 ¡Sigue así! Cada lección te acerca a tus metas."
        );
        alert.showAndWait();
    }
}