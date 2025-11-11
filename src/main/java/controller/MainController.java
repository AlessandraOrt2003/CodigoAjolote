package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private BorderPane mainContainer;
    @FXML private HBox navigationBar;
    @FXML private Button homeButton;
    @FXML private Button cursosButton;
    @FXML private Button aboutButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupNavigation();
        loadHomeView();
    }

    private void setupNavigation() {
        homeButton.setOnAction(e -> loadHomeView());
        cursosButton.setOnAction(e -> loadCursosView());
        aboutButton.setOnAction(e -> loadAboutView());
    }

    private void loadHomeView() {
        loadView("/home-view.fxml");
        setActiveButton(homeButton);
    }

    private void loadCursosView() {
        loadView("/cursos-view.fxml");
        setActiveButton(cursosButton);
    }

    private void loadAboutView() {
        loadView("/about-view.fxml");
        setActiveButton(aboutButton);
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node view = loader.load();
            mainContainer.setCenter(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setActiveButton(Button activeButton) {
        homeButton.getStyleClass().remove("active");
        cursosButton.getStyleClass().remove("active");
        aboutButton.getStyleClass().remove("active");
        activeButton.getStyleClass().add("active");
    }
}