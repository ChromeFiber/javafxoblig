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

public class Main extends Application {
    Scene scene;
    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane mainPane = new BorderPane();
        createHbox();
        Canvas painting = new Canvas(400, 450);

  
        GraphicsContext gc = painting.getGraphicsContext2D();
        drawing(gc);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        mainPane.setOnMousePressed(e->{
            gc.beginPath();
            gc.lineTo(e.getSceneX(), e.getSceneY());
            gc.stroke();
        });

        mainPane.setOnMouseDragged(e->{
            gc.lineTo(e.getSceneX(), e.getSceneY());
            gc.stroke();
        });
        Button button = new Button("Hello");

        mainPane.getChildren().addAll(painting, button);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(mainPane, 400, 450));
        primaryStage.show();
    }

    private void createHbox() {
        HBox hBox = new HBox();
    }


    public static void main(String[] args) {
        launch(args);
    }

    private static void drawing(GraphicsContext gc) {
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(5);
        gc.strokeLine(40,10,10, 40);
        gc.fillOval(10, 60, 30, 30);
        gc.strokeOval(60,60,30,30);
        gc.fillRoundRect(110,60,30,30,10,10);
        gc.strokeRoundRect(160,60,30,30,10,10);
        gc.fillArc(10, 110, 30, 30, 45, 240, ArcType.OPEN);
        gc.fillArc(60,110, 30, 30,45,240, ArcType.CHORD);
        gc.fillArc(110, 110, 30, 30, 45, 240, ArcType.ROUND);
        gc.strokeArc(10, 160, 30, 30, 45, 240, ArcType.OPEN);
        gc.strokeArc(60, 160, 30, 30, 45, 240, ArcType.CHORD);
        gc.strokeArc(110, 160, 30, 30, 45, 240, ArcType.ROUND);

        gc.fillPolygon(new double[] {10, 40, 10, 40},
                new double[] {210, 210, 240, 240}, 4);
        gc.strokePolygon(new double[]{60, 90, 60, 90},
                new double[]{210, 210, 240, 240}, 4);
        gc.strokePolyline(new double[]{110, 140, 110, 140},
                new double[]{210, 210, 240, 240}, 4);
    }
}
