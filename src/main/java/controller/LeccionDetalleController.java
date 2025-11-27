package controller;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.web.WebView;
import model.Curso;
import model.Modulo;
import model.Leccion;
import service.UserDataManager;
import controller.MainController;
import java.net.URL;
import java.util.ResourceBundle;

public class LeccionDetalleController implements Initializable {

    @FXML private Label lblTituloLeccion;
    @FXML private Label lblTipoLeccion;
    @FXML private Label lblDuracionLeccion;
    @FXML private Label lblDescripcionLeccion;
    @FXML private Label lblCursoModulo;
    @FXML private WebView webViewContenido;
    @FXML private Button btnCompletarLeccion;
    @FXML private Button btnVolver;
    @FXML private Button btnLeccionAnterior;
    @FXML private Button btnLeccionSiguiente;

    private MainController mainController;
    private UserDataManager userDataManager;
    private Curso curso;
    private Modulo modulo;
    private Leccion leccion;
    private int indiceLeccion;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inicialización básica
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setUserDataManager(UserDataManager userDataManager) {
        this.userDataManager = userDataManager;
    }

    public void mostrarLeccion(Curso curso, Modulo modulo, Leccion leccion, int indiceLeccion) {
        this.curso = curso;
        this.modulo = modulo;
        this.leccion = leccion;
        this.indiceLeccion = indiceLeccion;

        // Actualizar interfaz
        lblTituloLeccion.setText(leccion.getTitulo());
        lblTipoLeccion.setText(leccion.getIconoTipo() + " " + leccion.getTipo().toUpperCase());
        lblDuracionLeccion.setText("Duración: " + leccion.getDuracionFormateada());
        lblDescripcionLeccion.setText(leccion.getDescripcion());
        lblCursoModulo.setText(curso.getTitulo() + " • " + modulo.getTitulo());

        // Cargar contenido según el tipo
        cargarContenidoLeccion();

        // Configurar botones de navegación
        configurarNavegacion();

        // Configurar botón de completar
        btnCompletarLeccion.setDisable(leccion.isCompletada());
        if (leccion.isCompletada()) {
            btnCompletarLeccion.setText("✅ YA COMPLETADA");
        } else {
            btnCompletarLeccion.setText("🎯 MARCAR COMO COMPLETADA");
        }
    }

    private void cargarContenidoLeccion() {
        String tipo = leccion.getTipo().toLowerCase();
        String contenidoHTML = "";

        switch (tipo) {
            case "video":
                contenidoHTML = generarContenidoVideo();
                break;
            case "texto":
                contenidoHTML = generarContenidoTexto();
                break;
            case "practica":
                contenidoHTML = generarContenidoPractica();
                break;
            case "quiz":
                contenidoHTML = generarContenidoQuiz();
                break;
            default:
                contenidoHTML = "<h3>Contenido no disponible</h3>";
        }

        webViewContenido.getEngine().loadContent(contenidoHTML);
    }

    private String generarContenidoVideo() {
        return "<html><body style='font-family: Arial; padding: 20px;'>" +
                "<h2>🎥 " + leccion.getTitulo() + "</h2>" +
                "<div style='background: #f0f0f0; padding: 15px; border-radius: 8px;'>" +
                "<p><strong>Descripción:</strong> " + leccion.getDescripcion() + "</p>" +
                "<p><strong>Duración:</strong> " + leccion.getDuracionFormateada() + "</p>" +
                "</div>" +
                "<div style='margin-top: 20px; text-align: center;'>" +
                "<p>🔗 <a href='" + (leccion.getRecursoUrl() != null ? leccion.getRecursoUrl() : "#") +
                "' target='_blank'>Abrir video externo</a></p>" +
                "</div>" +
                "</body></html>";
    }

    private String generarContenidoTexto() {
        String contenido = leccion.getContenido() != null ?
                leccion.getContenido() : "Contenido de lectura no disponible.";

        return "<html><body style='font-family: Arial; padding: 20px;'>" +
                "<h2>📖 " + leccion.getTitulo() + "</h2>" +
                "<div style='background: #f8f9fa; padding: 20px; border-radius: 8px; line-height: 1.6;'>" +
                contenido.replace("\n", "<br>") +
                "</div>" +
                "</body></html>";
    }

    private String generarContenidoPractica() {
        return "<html><body style='font-family: Arial; padding: 20px;'>" +
                "<h2>💻 " + leccion.getTitulo() + "</h2>" +
                "<div style='background: #e8f5e8; padding: 20px; border-radius: 8px;'>" +
                "<h3>🎯 Objetivo de la práctica:</h3>" +
                "<p>" + leccion.getDescripcion() + "</p>" +
                "<h3>📋 Instrucciones:</h3>" +
                "<ol>" +
                "<li>Abre tu IDE de Java preferido</li>" +
                "<li>Crea un nuevo proyecto</li>" +
                "<li>Sigue las especificaciones del ejercicio</li>" +
                "<li>Ejecuta y prueba tu código</li>" +
                "<li>Verifica que cumple con los requisitos</li>" +
                "</ol>" +
                "</div>" +
                "</body></html>";
    }

    private String generarContenidoQuiz() {
        return "<html><body style='font-family: Arial; padding: 20px;'>" +
                "<h2>📝 " + leccion.getTitulo() + "</h2>" +
                "<div style='background: #fff3cd; padding: 20px; border-radius: 8px;'>" +
                "<h3>Evaluación de Conocimientos</h3>" +
                "<p><strong>Duración estimada:</strong> " + leccion.getDuracionFormateada() + "</p>" +
                "<p><strong>Formato:</strong> 10 preguntas de opción múltiple</p>" +
                "<p><strong>Puntuación mínima:</strong> 70% para aprobar</p>" +
                "<h4>📋 Instrucciones:</h4>" +
                "<ul>" +
                "<li>Lee cada pregunta cuidadosamente</li>" +
                "<li>Selecciona la mejor respuesta</li>" +
                "<li>Solo tienes un intento por pregunta</li>" +
                "</ul>" +
                "</div>" +
                "</body></html>";
    }

    private void configurarNavegacion() {
        // Botón lección anterior
        btnLeccionAnterior.setDisable(indiceLeccion == 0);

        // Botón lección siguiente
        boolean esUltimaLeccion = indiceLeccion == modulo.getLecciones().size() - 1;
        btnLeccionSiguiente.setDisable(esUltimaLeccion);
    }

    @FXML
    private void completarLeccion(ActionEvent event) {
        if (leccion != null && !leccion.isCompletada()) {
            leccion.marcarCompletada();
            btnCompletarLeccion.setText("✅ YA COMPLETADA");
            btnCompletarLeccion.setDisable(true);

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("¡Felicidades!");
            alert.setHeaderText("🎉 Lección Completada");
            alert.setContentText(
                    "Has completado: " + leccion.getTitulo() + "\n\n" +
                            "📊 Progreso actual:\n" +
                            "• Lecciones completadas: " + userDataManager.getTotalLeccionesCompletadas() + "\n" +
                            "• Progreso general: " + String.format("%.1f", userDataManager.getProgresoGeneral()) + "%\n\n" +
                            "🏆 ¡Sigue avanzando!"
            );
            alert.showAndWait();
        }
    }

    @FXML
    private void volverACursos(ActionEvent event) {
        if (mainController != null) {
            mainController.cargarVistaCursos();
        }
    }

    @FXML
    private void leccionAnterior(ActionEvent event) {
        if (indiceLeccion > 0) {
            Leccion leccionAnterior = modulo.getLecciones().get(indiceLeccion - 1);
            mostrarLeccion(curso, modulo, leccionAnterior, indiceLeccion - 1);
        }
    }

    @FXML
    private void leccionSiguiente(ActionEvent event) {
        if (indiceLeccion < modulo.getLecciones().size() - 1) {
            Leccion leccionSiguiente = modulo.getLecciones().get(indiceLeccion + 1);
            mostrarLeccion(curso, modulo, leccionSiguiente, indiceLeccion + 1);
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("¡Felicidades!");
            alert.setHeaderText("🎊 Módulo Completado");
            alert.setContentText("Has completado todas las lecciones de este módulo.\n\n¡Excelente trabajo!");
            alert.showAndWait();
        }
    }
}