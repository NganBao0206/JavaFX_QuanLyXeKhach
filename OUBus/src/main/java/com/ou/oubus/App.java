package com.ou.oubus;

import com.ou.utils.Executor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.stage.WindowEvent;

/**
 * phat JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        Executor.getInstance();
        scene = new Scene(loadFXML("login"));
        stage.setScene(getScene());
        stage.setTitle("OUBus");
        Image image = new Image(getClass().getResourceAsStream("/com/ou/images/logo3.png"));
        stage.getIcons().add(image);
        stage.show();
        App.stage = stage;
        stage.setOnCloseRequest((WindowEvent event) -> {
            Executor.getInstance().shutDownExecutor();
        });
    }

    static void setRoot(String fxml) throws IOException {
        Parent pr = loadFXML(fxml);
        scene = new Scene(pr, pr.prefWidth(-1), pr.prefHeight(-1));
        stage.setScene(getScene());
        getScene().setRoot(pr);
        stage.centerOnScreen();

    }

    static void setRoot(Parent root) throws IOException {
        scene = new Scene(root, root.prefWidth(-1), root.prefHeight(-1));
        stage.setScene(getScene());
        getScene().setRoot(root);
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
