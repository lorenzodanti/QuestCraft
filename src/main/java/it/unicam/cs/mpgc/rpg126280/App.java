package it.unicam.cs.mpgc.rpg126280;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");

        Label label = new Label("QuestCraft RPG Avviato!\nJavaFX version: " + javafxVersion + "\nJava version: " + javaVersion);
        Scene scene = new Scene(new StackPane(label), 400, 200);

        stage.setTitle("QuestCraft - Test Avvio");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
