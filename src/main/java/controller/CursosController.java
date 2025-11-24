package controller;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Curso;
import model.Modulo;
import model.Leccion;

import java.net.URL;
import java.util.ResourceBundle;

public class CursosController implements Initializable {

    @FXML
    private ListView<Curso> cursosListView;

    @FXML
    private Label tituloCursoLabel;

    @FXML
    private Label descripcionCursoLabel;

    @FXML
    private Label instructorLabel;

    @FXML
    private Label duracionLabel;

    @FXML
    private Label ratingLabel;

    @FXML
    private Label estudiantesLabel;

    @FXML
    private ImageView cursoImageView;

    @FXML
    private Button verDetalleButton;

    @FXML
    private Button inscribirButton;

    // DATOS ESTÁTICOS - 5 cursos de ejemplo para Sprint 2
    private final ObservableList<Curso> cursos = FXCollections.observableArrayList(
            crearCursoJavaBasico(),
            crearCursoJavaFX(),
            crearCursoBaseDatos(),
            crearCursoDesarrolloWeb(),
            crearCursoPatrones()
    );

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarListView();
        configurarBotones();
    }

    private void configurarListView() {
        // Configurar cómo mostrar los cursos en la ListView
        cursosListView.setItems(cursos);
        cursosListView.setCellFactory(lv -> new javafx.scene.control.ListCell<Curso>() {
            @Override
            protected void updateItem(Curso curso, boolean empty) {
                super.updateItem(curso, empty);
                if (empty || curso == null) {
                    setText(null);
                } else {
                    setText(curso.getTitulo() + " - " + curso.getInstructor());
                }
            }
        });

        // Configurar selección de cursos
        cursosListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        mostrarDetalleCurso(newValue);
                    }
                }
        );

        // Seleccionar el primer curso por defecto
        if (!cursos.isEmpty()) {
            cursosListView.getSelectionModel().selectFirst();
        }
    }

    private void configurarBotones() {
        verDetalleButton.setOnAction(event -> verDetalleCurso());
        inscribirButton.setOnAction(event -> inscribirEnCurso());
    }

    private void mostrarDetalleCurso(Curso curso) {
        // Actualizar la interfaz con los detalles del curso seleccionado
        tituloCursoLabel.setText(curso.getTitulo());
        descripcionCursoLabel.setText(curso.getDescripcionLarga() != null ?
                curso.getDescripcionLarga() : curso.getDescripcion());
        instructorLabel.setText("Instructor: " + curso.getInstructor());
        duracionLabel.setText("Duración: " + curso.getDuracionFormateada());
        ratingLabel.setText("Rating: ⭐ " + curso.getRating() + "/5.0");
        estudiantesLabel.setText("Estudiantes: " + curso.getTotalEstudiantes());

        // Cargar imagen del curso (placeholder por ahora)
        if (curso.getImagenUrl() != null && !curso.getImagenUrl().isEmpty()) {
            try {
                Image imagen = new Image(curso.getImagenUrl(), true);
                cursoImageView.setImage(imagen);
            } catch (Exception e) {
                // Usar imagen por defecto si hay error
                cursoImageView.setImage(new Image("https://via.placeholder.com/300x200/3498db/ffffff?text=Curso"));
            }
        }

        // Habilitar botones
        verDetalleButton.setDisable(false);
        inscribirButton.setDisable(false);
    }

    // MÉTODO PARA NAVEGAR AL DETALLE COMPLETO DEL CURSO
    @FXML
    private void verDetalleCurso() {
        Curso cursoSeleccionado = cursosListView.getSelectionModel().getSelectedItem();
        if (cursoSeleccionado != null) {
            System.out.println("🔍 Navegando al detalle del curso: " + cursoSeleccionado.getTitulo());
            System.out.println("📊 Módulos: " + cursoSeleccionado.getTotalModulos());
            System.out.println("📚 Lecciones: " + cursoSeleccionado.getTotalLecciones());

            // TODO: En el futuro, navegar a una vista detallada del curso
            // mainController.cargarVistaDetalleCurso(cursoSeleccionado.getId());
        }
    }

    // MÉTODO PARA SIMULAR INSCRIPCIÓN AL CURSO
    @FXML
    private void inscribirEnCurso() {
        Curso cursoSeleccionado = cursosListView.getSelectionModel().getSelectedItem();
        if (cursoSeleccionado != null) {
            System.out.println("🎯 Inscribiendo al curso: " + cursoSeleccionado.getTitulo());
            System.out.println("👨‍🏫 Instructor: " + cursoSeleccionado.getInstructor());
            System.out.println("⏱️ Duración: " + cursoSeleccionado.getDuracionFormateada());

            // Simular incremento de estudiantes
            cursoSeleccionado.setTotalEstudiantes(cursoSeleccionado.getTotalEstudiantes() + 1);
            estudiantesLabel.setText("Estudiantes: " + cursoSeleccionado.getTotalEstudiantes());

            // TODO: En Sprint 4, guardar en base de datos local
        }
    }

    // ===== MÉTODOS PARA CREAR CURSOS DE EJEMPLO =====

    private static Curso crearCursoJavaBasico() {
        Curso curso = new Curso(
                "JAVA-001",
                "Java Básico para Principiantes",
                "Aprende los fundamentos de Java desde cero",
                "Programación",
                "Alessandra Ortiz",
                20,
                12
        );
        curso.setDescripcionLarga("En este curso aprenderás todos los conceptos fundamentales de Java: variables, estructuras de control, POO, y mucho más. Al finalizar serás capaz de crear tus primeras aplicaciones en Java.");
        curso.setRating(4.8);
        curso.setTotalEstudiantes(1250);
        curso.setImagenUrl("https://via.placeholder.com/300x200/3498db/ffffff?text=Java+Básico");

        // Agregar módulos
        Modulo modulo1 = new Modulo("M1-JAVA", "Introducción a Java", "Conceptos básicos y configuración", 120, 1);
        modulo1.agregarLeccion(new Leccion("L1-1", "¿Qué es Java?", "Introducción al lenguaje Java", "video", 15, 1));
        modulo1.agregarLeccion(new Leccion("L1-2", "Instalación del JDK", "Configuración del entorno", "texto", 10, 2));

        Modulo modulo2 = new Modulo("M2-JAVA", "Sintaxis Básica", "Variables y estructuras fundamentales", 180, 2);
        modulo2.agregarLeccion(new Leccion("L2-1", "Variables y Tipos de Datos", "Declaración y uso de variables", "video", 20, 1));
        modulo2.agregarLeccion(new Leccion("L2-2", "Operadores", "Operadores aritméticos y lógicos", "texto", 15, 2));

        curso.agregarModulo(modulo1);
        curso.agregarModulo(modulo2);

        return curso;
    }

    private static Curso crearCursoJavaFX() {
        Curso curso = new Curso(
                "JAVAFX-001",
                "JavaFX - Interfaces Gráficas Modernas",
                "Crea aplicaciones desktop modernas con JavaFX",
                "Desarrollo Desktop",
                "Andrea Ortiz",
                30,
                15
        );
        curso.setDescripcionLarga("Domina JavaFX para crear interfaces de usuario modernas y responsivas. Aprende FXML, CSS, patrones MVC y mejores prácticas de desarrollo.");
        curso.setRating(4.9);
        curso.setTotalEstudiantes(890);
        curso.setImagenUrl("https://via.placeholder.com/300x200/9b59b6/ffffff?text=JavaFX");
        return curso;
    }

    private static Curso crearCursoBaseDatos() {
        Curso curso = new Curso(
                "BD-001",
                "Base de Datos y SQL desde Cero",
                "Aprende SQL y diseño de bases de datos relacionales",
                "Base de Datos",
                "Codigo Ajolote Team",
                25,
                18
        );
        curso.setDescripcionLarga("Curso completo de bases de datos. Desde conceptos básicos hasta consultas avanzadas, normalización y optimización.");
        curso.setRating(4.7);
        curso.setTotalEstudiantes(2100);
        curso.setImagenUrl("https://via.placeholder.com/300x200/e74c3c/ffffff?text=SQL+BD");
        return curso;
    }

    private static Curso crearCursoDesarrolloWeb() {
        Curso curso = new Curso(
                "WEB-001",
                "Introducción al Desarrollo Web",
                "HTML, CSS y JavaScript para principiantes",
                "Desarrollo Web",
                "Alessandra Ortiz",
                35,
                20
        );
        curso.setRating(4.6);
        curso.setTotalEstudiantes(1800);
        curso.setImagenUrl("https://via.placeholder.com/300x200/f39c12/ffffff?text=Desarrollo+Web");
        return curso;
    }

    private static Curso crearCursoPatrones() {
        Curso curso = new Curso(
                "PAT-001",
                "Patrones de Diseño en Java",
                "Patrones creacionales, estructurales y de comportamiento",
                "Arquitectura",
                "Expert Team",
                15,
                8
        );
        curso.setRating(4.9);
        curso.setTotalEstudiantes(650);
        curso.setImagenUrl("https://via.placeholder.com/300x200/27ae60/ffffff?text=Patrones");
        return curso;
    }
}