package ru.zakharov.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class MainClient extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(new File("A:\\Mine\\Java\\cloud_storage\\client\\src\\main\\resources\\main.fxml").toURI().toURL());
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Box Client");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
