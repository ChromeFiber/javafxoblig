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
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    ColorPicker fargeVelger = new ColorPicker();
    Color farge = fargeVelger.getValue();
    private Form figurBlirDratt = null;
    private ArrayList<Form> figurer = new ArrayList<>();
    private final Color defaultFarge = Color.YELLOW;
    private Canvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception {
        canvas = lagCanvas();
        tegnCanvas();
        StackPane canvasHolder = new StackPane(canvas);
        canvasHolder.setStyle("-fx-border-width: 2px; -fx-border-color: #444");
        BorderPane root = new BorderPane(canvasHolder);
        root.setStyle("-fx-border-width: 1px; -fx-border-color: black");
        root.setTop(lagVerktøylinje(canvas));

        primaryStage.setScene(new Scene(root, 400, 450));
        primaryStage.setTitle("Tegneprogram");
        primaryStage.show();

    }

    private HBox lagVerktøylinje(Canvas canvas) {
        //Lager ett panel som inneholder de knappene og andre verktøyene
        //man trenger for å lage figurene i programmet.
        ColorPicker fargeVelger = new ColorPicker();
        ToggleGroup toggleFigurer = new ToggleGroup();
        RadioButton rektangel = new RadioButton("Rektangel");
        RadioButton sirkel = new RadioButton("Sirkel");
        RadioButton linje = new RadioButton("Linje");
        rektangel.setToggleGroup(toggleFigurer);
        sirkel.setToggleGroup(toggleFigurer);
        linje.setToggleGroup(toggleFigurer);

        toggleFigurer.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {
                if(rektangel.isSelected()) {
                    canvas.setOnMousePressed(e-> {
                        leggTilFigur(new Rektangel());
                        tegnCanvas();
                    });
                } if (sirkel.isSelected()) {
                    canvas.setOnMousePressed(e-> {
                        leggTilFigur(new Sirkel());
                        tegnCanvas();
                    });
                } if (linje.isSelected()) {
                    canvas.setOnMousePressed(e -> {
                        leggTilFigur(new Linje());
                        tegnCanvas();
                    });
                }
        }));

        HBox verktøyLinje= new HBox(10);
        verktøyLinje.getChildren().addAll(fargeVelger, rektangel, sirkel, linje);
        return verktøyLinje;
    }

    private VBox endreFigurer(Canvas canvas) {

        VBox endre = new VBox(10);
        return endre;
    }

    private Canvas lagCanvas() {
        Canvas canvas = new Canvas(800, 600);
        canvas.setOnMousePressed(this::musTrykket);
        canvas.setOnMouseReleased(this::musTrykket);
        canvas.setOnMouseDragged(this::musTrykket);
        return canvas;
    }


    private int prevDragX;  // During dragging, these record the x and y coordinates of the
    private int prevDragY;

    private void musTrykket(MouseEvent mouseEvent) {
        int x = (int)mouseEvent.getX();
        int y = (int)mouseEvent.getY();

        for(int i = figurer.size() - 1; i >= 0; i--){

        }


    }


    private void musDratt(MouseEvent mouseEvent) {
        int x = (int)mouseEvent.getX();
        int y = (int)mouseEvent.getY();

        if(figurBlirDratt != null){
            figurBlirDratt.flytt(x - prevDragX, y - prevDragY);
            prevDragX = x;
            prevDragY = y;
            tegnCanvas();
        }


    }
    private void leggTilFigur(Form form){
        form.setFarge(farge);
        form.omform();
        figurer.add(form);
        tegnCanvas();
    }

    private void tegnCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0, canvas.getWidth(), canvas.getHeight());
        for (Form former : figurer) {
            former.tegn(gc);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

}
