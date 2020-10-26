package sample;
//https://home.usn.no/lonnesta/kurs/OBJ2000/Oblig/Obligatorisk_oppgave_2020.pdf
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane mainPane = new BorderPane();

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(mainPane, 400, 450));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
