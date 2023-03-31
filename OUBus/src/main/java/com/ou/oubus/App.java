package com.ou.oubus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


/**
 * JavaFX App
 */
public class App extends Application {

    

    private static Scene scene;
    private static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("login"));
        stage.setScene(getScene());
        stage.show();
        App.stage = stage;
    }

    static void setRoot(String fxml) throws IOException {
        Parent pr = loadFXML(fxml);
        scene = new Scene(pr, pr.prefWidth(-1), pr.prefHeight(-1));
        stage.setScene(getScene());
        getScene().setRoot(pr);
        stage.centerOnScreen();
        
    }
    

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) throws SQLException {
        launch();
    }

    public static Scene getScene() {
        return scene;
    }
}