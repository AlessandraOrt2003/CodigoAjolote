package controller;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import model.Curso;
import model.Modulo;
import model.Leccion;
import service.UserDataManager;
import controller.MainController;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Optional;

public class CursosController implements Initializable {

    @FXML
    private ListView<Curso> cursosListView;

    @FXML
    private TableView<Modulo> modulosTableView;

    @FXML
    private TableColumn<Modulo, String> colModuloTitulo;

    @FXML
    private TableColumn<Modulo, String> colModuloDescripcion;

    @FXML
    private TableColumn<Modulo, String> colModuloDuracion;

    @FXML
    private TableColumn<Modulo, Integer> colModuloLecciones;

    @FXML
    private TableView<Leccion> leccionesTableView;

    @FXML
    private TableColumn<Leccion, String> colLeccionTitulo;

    @FXML
    private TableColumn<Leccion, String> colLeccionTipo;

    @FXML
    private TableColumn<Leccion, String> colLeccionDuracion;

    @FXML
    private TableColumn<Leccion, Boolean> colLeccionCompletada;

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

    @FXML
    private Button verTutorialButton;

    @FXML
    private Button empezarLeccionButton;

    @FXML
    private Button buscarButton;

    @FXML
    private Button filtrarButton;

    @FXML
    private Button limpiarButton;

    // DATOS ESTÁTICOS - 5 cursos completos con módulos y lecciones
    private final ObservableList<Curso> cursos = FXCollections.observableArrayList(
            crearCursoJavaBasico(),
            crearCursoJavaFX(),
            crearCursoBaseDatos(),
            crearCursoDesarrolloWeb(),
            crearCursoPatrones()
    );

    private Curso cursoSeleccionado;
    private Modulo moduloSeleccionado;
    private Leccion leccionSeleccionada;
    private MainController mainController;
    private UserDataManager userDataManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.userDataManager = new UserDataManager();
        configurarListView();
        configurarTableViews();
        configurarBotones();
        inicializarPersistenciaLecciones();
        userDataManager.mostrarEstadisticas();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private void configurarListView() {
        cursosListView.setItems(cursos);
        cursosListView.setCellFactory(lv -> new javafx.scene.control.ListCell<Curso>() {
            @Override
            protected void updateItem(Curso curso, boolean empty) {
                super.updateItem(curso, empty);
                if (empty || curso == null) {
                    setText(null);
                } else {
                    setText(curso.getTitulo() + " - " + curso.getInstructor() +
                            " [" + curso.getLeccionesCompletadas() + "/" + curso.getTotalLecciones() + "]");
                }
            }
        });

        cursosListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        cursoSeleccionado = newValue;
                        mostrarDetalleCurso(newValue);
                    }
                }
        );

        if (!cursos.isEmpty()) {
            cursosListView.getSelectionModel().selectFirst();
        }
    }

    private void configurarTableViews() {
        colModuloTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colModuloDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colModuloDuracion.setCellValueFactory(new PropertyValueFactory<>("duracionFormateada"));
        colModuloLecciones.setCellValueFactory(new PropertyValueFactory<>("totalLecciones"));

        colLeccionTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colLeccionTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colLeccionDuracion.setCellValueFactory(new PropertyValueFactory<>("duracionFormateada"));
        colLeccionCompletada.setCellValueFactory(new PropertyValueFactory<>("completada"));

        configurarSeleccionTablas();
    }

    private void configurarSeleccionTablas() {
        // Cuando se selecciona un módulo, mostrar sus lecciones
        modulosTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, nuevoModulo) -> {
                    if (nuevoModulo != null) {
                        moduloSeleccionado = nuevoModulo;
                        mostrarLeccionesModulo(nuevoModulo);
                        empezarLeccionButton.setDisable(false);
                    }
                }
        );

        leccionesTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, nuevaLeccion) -> {
                    if (nuevaLeccion != null) {
                        leccionSeleccionada = nuevaLeccion;
                        verTutorialButton.setDisable(false);

                        // Configurar doble click para abrir detalle
                        leccionesTableView.setOnMouseClicked(event -> {
                            if (event.getClickCount() == 2) { // Doble click
                                abrirLeccionDetalle();
                            }
                        });
                    }
                }
        );
    }

    private void configurarBotones() {
        verDetalleButton.setOnAction(event -> verDetalleCurso());
        inscribirButton.setOnAction(event -> inscribirEnCurso());
        verTutorialButton.setOnAction(event -> verTutorialLeccion());
        empezarLeccionButton.setOnAction(event -> empezarLeccionActual());
        buscarButton.setOnAction(event -> buscarCursos());
        filtrarButton.setOnAction(event -> filtrarPorCategoria());
        limpiarButton.setOnAction(event -> limpiarFiltros());
    }

    private void inicializarPersistenciaLecciones() {
        for (Curso curso : cursos) {
            for (Modulo modulo : curso.getModulos()) {
                for (Leccion leccion : modulo.getLecciones()) {
                    leccion.setUserDataManager(userDataManager);
                    // Sincronizar estado de completado desde persistencia
                    leccion.setCompletada(userDataManager.isLeccionCompletada(leccion.getId()));
                }
            }
        }
    }

    @FXML
    private void abrirLeccionDetalle() {
        if (leccionSeleccionada != null && moduloSeleccionado != null && cursoSeleccionado != null) {
            try {
                // Cargar la vista de detalle de lección
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/leccion-detalle-view.fxml"));
                Parent vistaDetalle = loader.load();

                // Obtener el controller y pasar los datos
                LeccionDetalleController detalleController = loader.getController();
                detalleController.setMainController(mainController);
                detalleController.setUserDataManager(userDataManager);

                // Encontrar el índice de la lección seleccionada
                int indiceLeccion = moduloSeleccionado.getLecciones().indexOf(leccionSeleccionada);

                // Mostrar la lección
                detalleController.mostrarLeccion(cursoSeleccionado, moduloSeleccionado, leccionSeleccionada, indiceLeccion);

                // Cambiar a la vista de detalle
                if (mainController != null) {
                    mainController.cambiarVistaCentral(vistaDetalle);
                }

            } catch (Exception e) {
                System.err.println("Error al cargar vista de detalle: " + e.getMessage());
                e.printStackTrace();

                // Fallback: mostrar alerta
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No se pudo cargar la lección");
                alert.setContentText("Hubo un problema al cargar el contenido de la lección.");
                alert.showAndWait();
            }
        }
    }

    private void mostrarLeccionesModulo(Modulo modulo) {
        ObservableList<Leccion> lecciones = FXCollections.observableArrayList(modulo.getLecciones());
        leccionesTableView.setItems(lecciones);

        if (!lecciones.isEmpty()) {
            leccionesTableView.getSelectionModel().selectFirst();
        }
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

        ObservableList<Modulo> modulos = FXCollections.observableArrayList(curso.getModulos());
        modulosTableView.setItems(modulos);

        leccionesTableView.getItems().clear();

        moduloSeleccionado = null;
        leccionSeleccionada = null;

        if (curso.getImagenUrl() != null && !curso.getImagenUrl().isEmpty()) {
            try {
                Image imagen = new Image(curso.getImagenUrl(), true);
                cursoImageView.setImage(imagen);
            } catch (Exception e) {
                cursoImageView.setImage(new Image("https://via.placeholder.com/300x200/3498db/ffffff?text=Curso"));
            }
        }

        // Habilitar botones
        verDetalleButton.setDisable(false);
        inscribirButton.setDisable(false);
        verTutorialButton.setDisable(true);
        empezarLeccionButton.setDisable(true);
    }

    // ===== MÉTODOS DE PERSISTENCIA Y PROGRESO =====

    @FXML
    private void verTutorialLeccion() {
        if (leccionSeleccionada != null) {
            String tipo = leccionSeleccionada.getTipo();
            String titulo = leccionSeleccionada.getTitulo();

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Tutorial - " + titulo);
            alert.setHeaderText("Iniciando " + leccionSeleccionada.getIconoTipo() + " " + titulo);

            String contenido = "";
            switch (tipo.toLowerCase()) {
                case "video":
                    contenido = "🎥 Reproduciendo video tutorial...\n\n" +
                            "📺 Título: " + titulo + "\n" +
                            "⏱️ Duración: " + leccionSeleccionada.getDuracionFormateada() + "\n" +
                            "📝 Descripción: " + leccionSeleccionada.getDescripcion() + "\n\n" +
                            "🔗 Enlace del video: " +
                            (leccionSeleccionada.getRecursoUrl() != null ?
                                    leccionSeleccionada.getRecursoUrl() : "Video integrado en la plataforma");
                    break;
                case "texto":
                    contenido = "📖 Cargando material de lectura...\n\n" +
                            "📄 Título: " + titulo + "\n" +
                            "⏱️ Tiempo estimado: " + leccionSeleccionada.getDuracionFormateada() + "\n" +
                            "📝 Contenido: " +
                            (leccionSeleccionada.getContenido() != null ?
                                    leccionSeleccionada.getContenido() : "Material de estudio disponible") + "\n\n" +
                            "📚 Puedes descargar el material PDF desde la sección de recursos.";
                    break;
                case "practica":
                    contenido = "💻 Iniciando ejercicio práctico...\n\n" +
                            "⚡ Título: " + titulo + "\n" +
                            "⏱️ Duración estimada: " + leccionSeleccionada.getDuracionFormateada() + "\n" +
                            "🎯 Objetivo: " + leccionSeleccionada.getDescripcion() + "\n\n" +
                            "📋 Instrucciones:\n" +
                            "1. Abre tu IDE favorito\n" +
                            "2. Crea un nuevo proyecto Java\n" +
                            "3. Sigue las instrucciones paso a paso\n" +
                            "4. Ejecuta y prueba tu código\n\n" +
                            "✅ Al completar, marca la lección como terminada.";
                    break;
                case "quiz":
                    contenido = "📝 Iniciando evaluación...\n\n" +
                            "❓ Título: " + titulo + "\n" +
                            "⏱️ Tiempo límite: " + leccionSeleccionada.getDuracionFormateada() + "\n" +
                            "📊 Preguntas: 10 preguntas de opción múltiple\n\n" +
                            "📋 Instrucciones:\n" +
                            "• Lee cada pregunta cuidadosamente\n" +
                            "• Selecciona la respuesta correcta\n" +
                            "• Tienes un intento por pregunta\n" +
                            "• Necesitas 70% para aprobar\n\n" +
                            "🎯 ¡Buena suerte!";
                    break;
                default:
                    contenido = "📚 Cargando contenido educativo...\n\n" +
                            "📖 " + titulo + "\n" +
                            "⏱️ " + leccionSeleccionada.getDuracionFormateada() + "\n" +
                            "📝 " + leccionSeleccionada.getDescripcion();
            }

            alert.setContentText(contenido);
            alert.showAndWait();

            // Marcar lección como completada después de ver el tutorial CON PERSISTENCIA
            leccionSeleccionada.marcarCompletada();
            leccionesTableView.refresh();
            cursosListView.refresh();

            // Mostrar estadísticas actualizadas
            mostrarEstadisticasProgreso();
        }
    }

    private void mostrarEstadisticasProgreso() {
        userDataManager.mostrarEstadisticas();

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Progreso Actualizado");
        alert.setHeaderText("🎉 ¡Lección completada!");
        alert.setContentText(
                "✅ Lección: " + leccionSeleccionada.getTitulo() + "\n" +
                        "📊 Progreso general: " + String.format("%.1f", userDataManager.getProgresoGeneral()) + "%\n" +
                        "🎓 Lecciones completadas: " + userDataManager.getTotalLeccionesCompletadas() + "\n" +
                        "📚 Cursos inscritos: " + userDataManager.getTotalCursosInscritos()
        );
        alert.showAndWait();
    }

    // ===== MÉTODOS DE BÚSQUEDA Y FILTRADO =====

    @FXML
    private void buscarCursos() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Buscar Cursos");
        dialog.setHeaderText("Buscar en todos los cursos");
        dialog.setContentText("Ingresa tu búsqueda:");

        Optional<String> resultado = dialog.showAndWait();
        resultado.ifPresent(terminoBusqueda -> {
            filtrarCursos(terminoBusqueda);
        });
    }

    private void filtrarCursos(String termino) {
        if (termino == null || termino.trim().isEmpty()) {
            cursosListView.setItems(cursos);
            return;
        }

        String terminoLower = termino.toLowerCase();
        ObservableList<Curso> cursosFiltrados = FXCollections.observableArrayList();

        for (Curso curso : cursos) {
            if (curso.getTitulo().toLowerCase().contains(terminoLower) ||
                    curso.getInstructor().toLowerCase().contains(terminoLower) ||
                    curso.getCategoria().toLowerCase().contains(terminoLower) ||
                    curso.getDescripcion().toLowerCase().contains(terminoLower)) {
                cursosFiltrados.add(curso);
            }
        }

        cursosListView.setItems(cursosFiltrados);

        if (cursosFiltrados.isEmpty()) {
            mostrarMensajeExito("No se encontraron cursos para: " + termino);
        } else {
            mostrarMensajeExito("Se encontraron " + cursosFiltrados.size() + " cursos para: " + termino);
        }
    }

    @FXML
    private void filtrarPorCategoria() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Programación",
                "Programación", "Desarrollo Desktop", "Base de Datos", "Desarrollo Web", "Arquitectura");
        dialog.setTitle("Filtrar por Categoría");
        dialog.setHeaderText("Selecciona una categoría");
        dialog.setContentText("Categoría:");

        Optional<String> resultado = dialog.showAndWait();
        resultado.ifPresent(categoria -> {
            ObservableList<Curso> cursosFiltrados = FXCollections.observableArrayList();
            for (Curso curso : cursos) {
                if (curso.getCategoria().equals(categoria)) {
                    cursosFiltrados.add(curso);
                }
            }
            cursosListView.setItems(cursosFiltrados);
            mostrarMensajeExito("Filtrado por categoría: " + categoria + " (" + cursosFiltrados.size() + " cursos)");
        });
    }

    @FXML
    private void limpiarFiltros() {
        cursosListView.setItems(cursos);
        mostrarMensajeExito("Filtros limpiados - Mostrando todos los cursos");
    }

    // ===== MÉTODOS EXISTENTES (actualizados) =====

    @FXML
    private void empezarLeccionActual() {
        if (moduloSeleccionado != null) {
            if (!moduloSeleccionado.getLecciones().isEmpty()) {
                Leccion primeraLeccion = moduloSeleccionado.getLecciones().get(0);
                leccionesTableView.getSelectionModel().select(primeraLeccion);

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Iniciando Módulo");
                alert.setHeaderText("🎯 Comenzando: " + moduloSeleccionado.getTitulo());
                alert.setContentText("📚 Módulo: " + moduloSeleccionado.getTitulo() + "\n" +
                        "📖 Lecciones: " + moduloSeleccionado.getTotalLecciones() + "\n" +
                        "⏱️ Duración total: " + moduloSeleccionado.getDuracionFormateada() + "\n\n" +
                        "💡 Recomendación: Completa las lecciones en orden para el mejor aprendizaje.");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void verDetalleCurso() {
        if (cursoSeleccionado != null) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Detalles Completos");
            alert.setHeaderText("📊 Estadísticas de: " + cursoSeleccionado.getTitulo());
            alert.setContentText(
                    "👨‍🏫 Instructor: " + cursoSeleccionado.getInstructor() + "\n" +
                            "📚 Módulos: " + cursoSeleccionado.getTotalModulos() + "\n" +
                            "📖 Lecciones: " + cursoSeleccionado.getTotalLecciones() + "\n" +
                            "✅ Completadas: " + cursoSeleccionado.getLeccionesCompletadas() + "\n" +
                            "📈 Progreso: " + String.format("%.1f", cursoSeleccionado.getProgresoCurso()) + "%\n" +
                            "⏱️ Duración: " + cursoSeleccionado.getDuracionFormateada() + "\n" +
                            "⭐ Rating: " + cursoSeleccionado.getRating() + "/5.0\n" +
                            "👥 Estudiantes: " + cursoSeleccionado.getTotalEstudiantes() + "\n\n" +
                            "🎯 Categoría: " + cursoSeleccionado.getCategoria()
            );
            alert.showAndWait();
        }
    }

    @FXML
    private void inscribirEnCurso() {
        if (cursoSeleccionado != null) {
            // Simular incremento de estudiantes
            cursoSeleccionado.setTotalEstudiantes(cursoSeleccionado.getTotalEstudiantes() + 1);
            estudiantesLabel.setText("Estudiantes: " + cursoSeleccionado.getTotalEstudiantes());

            // INCREMENTAR EN PERSISTENCIA
            userDataManager.incrementarCursosInscritos();

            mostrarMensajeExito(
                    "🎉 ¡Inscripción exitosa!\n\n" +
                            "📚 Curso: " + cursoSeleccionado.getTitulo() + "\n" +
                            "👨‍🏫 Instructor: " + cursoSeleccionado.getInstructor() + "\n" +
                            "⏱️ Duración: " + cursoSeleccionado.getDuracionFormateada() + "\n\n" +
                            "🚀 ¡Ya puedes comenzar con el primer módulo!"
            );
        }
    }

    // MÉTODO AUXILIAR PARA MOSTRAR MENSAJES DE ÉXITO
    private void mostrarMensajeExito(String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // ===== MÉTODOS PARA CREAR CURSOS COMPLETOS =====

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

        // MÓDULO 1: Introducción
        Modulo modulo1 = new Modulo("M1-JAVA", "Introducción a Java", "Conceptos básicos y configuración del entorno", 120, 1);
        Leccion l1 = new Leccion("L1-1", "¿Qué es Java?", "Introducción al lenguaje Java y su ecosistema", "video", 15, 1);
        l1.setRecursoUrl("https://www.youtube.com/watch?v=video-java-intro");
        modulo1.agregarLeccion(l1);

        Leccion l2 = new Leccion("L1-2", "Instalación del JDK", "Configuración del entorno de desarrollo", "texto", 10, 2);
        l2.setContenido("Guía paso a paso para instalar Java Development Kit en tu sistema operativo...");
        modulo1.agregarLeccion(l2);

        Leccion l3 = new Leccion("L1-3", "Primer Programa", "Creando tu primer Hola Mundo en Java", "practica", 20, 3);
        l3.setContenido("Ejercicio práctico: Crea tu primera aplicación Java desde cero");
        modulo1.agregarLeccion(l3);

        // MÓDULO 2: Sintaxis Básica
        Modulo modulo2 = new Modulo("M2-JAVA", "Sintaxis Básica", "Variables y estructuras fundamentales", 180, 2);
        Leccion l4 = new Leccion("L2-1", "Variables y Tipos de Datos", "Declaración y uso de variables", "video", 20, 1);
        l4.setRecursoUrl("https://www.youtube.com/watch?v=video-variables-java");
        modulo2.agregarLeccion(l4);

        Leccion l5 = new Leccion("L2-2", "Operadores", "Operadores aritméticos y lógicos", "texto", 15, 2);
        l5.setContenido("Explicación detallada de todos los operadores disponibles en Java...");
        modulo2.agregarLeccion(l5);

        Leccion l6 = new Leccion("L2-3", "Ejercicios Prácticos", "Practica con variables y operadores", "practica", 25, 3);
        modulo2.agregarLeccion(l6);

        Leccion l7 = new Leccion("L2-4", "Evaluación de Sintaxis", "Test de conocimientos básicos", "quiz", 10, 4);
        modulo2.agregarLeccion(l7);

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

        // MÓDULOS PARA JAVAFX
        Modulo modulo1 = new Modulo("M1-JFX", "Introducción a JavaFX", "Conceptos básicos de interfaces gráficas", 180, 1);
        Leccion l1 = new Leccion("L1-1", "¿Qué es JavaFX?", "Introducción a la plataforma", "video", 20, 1);
        l1.setRecursoUrl("https://www.youtube.com/watch?v=video-javafx-intro");
        modulo1.agregarLeccion(l1);

        Leccion l2 = new Leccion("L1-2", "Primera Ventana", "Creando tu primera aplicación JavaFX", "practica", 25, 2);
        l2.setContenido("Crea tu primera ventana con JavaFX paso a paso");
        modulo1.agregarLeccion(l2);

        Leccion l3 = new Leccion("L1-3", "Conceptos Básicos", "Stage, Scene y Nodes", "texto", 15, 3);
        modulo1.agregarLeccion(l3);

        Modulo modulo2 = new Modulo("M2-JFX", "Controles Básicos", "Botones, labels y campos de texto", 240, 2);
        Leccion l4 = new Leccion("L2-1", "Controles Comunes", "Uso de controles básicos", "video", 30, 1);
        l4.setRecursoUrl("https://www.youtube.com/watch?v=video-controles-javafx");
        modulo2.agregarLeccion(l4);

        Leccion l5 = new Leccion("L2-2", "Manejo de Eventos", "Eventos y listeners", "texto", 15, 2);
        modulo2.agregarLeccion(l5);

        Leccion l6 = new Leccion("L2-3", "Ejercicio Práctico", "Formulario de registro", "practica", 45, 3);
        modulo2.agregarLeccion(l6);

        curso.agregarModulo(modulo1);
        curso.agregarModulo(modulo2);

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

        // MÓDULOS PARA BASE DE DATOS
        Modulo modulo1 = new Modulo("M1-BD", "Introducción a BD", "Conceptos fundamentales", 150, 1);
        Leccion l1 = new Leccion("L1-1", "¿Qué es una Base de Datos?", "Conceptos básicos", "video", 20, 1);
        l1.setRecursoUrl("https://www.youtube.com/watch?v=video-bd-intro");
        modulo1.agregarLeccion(l1);

        Leccion l2 = new Leccion("L1-2", "Tipos de Bases de Datos", "Relacionales vs NoSQL", "texto", 10, 2);
        modulo1.agregarLeccion(l2);

        Leccion l3 = new Leccion("L1-3", "Modelo Relacional", "Tablas, claves y relaciones", "texto", 15, 3);
        modulo1.agregarLeccion(l3);

        Modulo modulo2 = new Modulo("M2-BD", "SQL Básico", "Consultas fundamentales", 200, 2);
        Leccion l4 = new Leccion("L2-1", "SELECT y FROM", "Consultas básicas", "video", 25, 1);
        l4.setRecursoUrl("https://www.youtube.com/watch?v=video-sql-select");
        modulo2.agregarLeccion(l4);

        Leccion l5 = new Leccion("L2-2", "WHERE y ORDER BY", "Filtros y ordenamiento", "practica", 30, 2);
        modulo2.agregarLeccion(l5);

        Leccion l6 = new Leccion("L2-3", "JOIN entre Tablas", "Combinación de datos", "video", 35, 3);
        l6.setRecursoUrl("https://www.youtube.com/watch?v=video-sql-join");
        modulo2.agregarLeccion(l6);

        curso.agregarModulo(modulo1);
        curso.agregarModulo(modulo2);

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
        curso.setDescripcionLarga("Aprende a crear sitios web modernos desde cero. Domina HTML5, CSS3 y JavaScript para construir interfaces web interactivas y responsivas.");
        curso.setRating(4.6);
        curso.setTotalEstudiantes(1800);
        curso.setImagenUrl("https://via.placeholder.com/300x200/f39c12/ffffff?text=Desarrollo+Web");

        // MÓDULOS PARA DESARROLLO WEB
        Modulo modulo1 = new Modulo("M1-WEB", "HTML5", "Estructura web semántica", 180, 1);
        Leccion l1 = new Leccion("L1-1", "Estructura HTML", "Etiquetas básicas", "video", 25, 1);
        l1.setRecursoUrl("https://www.youtube.com/watch?v=video-html-basico");
        modulo1.agregarLeccion(l1);

        Leccion l2 = new Leccion("L1-2", "Formularios HTML", "Creación de formularios", "practica", 35, 2);
        modulo1.agregarLeccion(l2);

        Leccion l3 = new Leccion("L1-3", "HTML Semántico", "Mejores prácticas", "texto", 20, 3);
        modulo1.agregarLeccion(l3);

        Modulo modulo2 = new Modulo("M2-WEB", "CSS3", "Estilos y diseño", 220, 2);
        Leccion l4 = new Leccion("L2-1", "Selectores CSS", "Tipos de selectores", "video", 30, 1);
        l4.setRecursoUrl("https://www.youtube.com/watch?v=video-css-selectores");
        modulo2.agregarLeccion(l4);

        Leccion l5 = new Leccion("L2-2", "Flexbox y Grid", "Layouts modernos", "practica", 40, 2);
        modulo2.agregarLeccion(l5);

        curso.agregarModulo(modulo1);
        curso.agregarModulo(modulo2);

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
        curso.setDescripcionLarga("Domina los patrones de diseño más importantes en Java. Aprende cuándo y cómo aplicar cada patrón para escribir código más mantenible y escalable.");
        curso.setRating(4.9);
        curso.setTotalEstudiantes(650);
        curso.setImagenUrl("https://via.placeholder.com/300x200/27ae60/ffffff?text=Patrones");

        // MÓDULOS PARA PATRONES
        Modulo modulo1 = new Modulo("M1-PAT", "Patrones Creacionales", "Singleton, Factory, Builder", 120, 1);
        Leccion l1 = new Leccion("L1-1", "Singleton Pattern", "Patrón singleton", "video", 20, 1);
        l1.setRecursoUrl("https://www.youtube.com/watch?v=video-singleton-pattern");
        modulo1.agregarLeccion(l1);

        Leccion l2 = new Leccion("L1-2", "Factory Method", "Patrón fábrica", "texto", 15, 2);
        modulo1.agregarLeccion(l2);

        Leccion l3 = new Leccion("L1-3", "Builder Pattern", "Construcción de objetos", "practica", 25, 3);
        modulo1.agregarLeccion(l3);

        Modulo modulo2 = new Modulo("M2-PAT", "Patrones Estructurales", "Adapter, Decorator, Facade", 140, 2);
        Leccion l4 = new Leccion("L2-1", "Adapter Pattern", "Adaptación de interfaces", "video", 22, 1);
        l4.setRecursoUrl("https://www.youtube.com/watch?v=video-adapter-pattern");
        modulo2.agregarLeccion(l4);

        Leccion l5 = new Leccion("L2-2", "Decorator Pattern", "Funcionalidad dinámica", "texto", 18, 2);
        modulo2.agregarLeccion(l5);

        curso.agregarModulo(modulo1);
        curso.agregarModulo(modulo2);

        return curso;
    }
}