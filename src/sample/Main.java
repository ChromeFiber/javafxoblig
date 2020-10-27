package sample;
//https://home.usn.no/lonnesta/kurs/OBJ2000/Oblig/Obligatorisk_oppgave_2020.pdf
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main extends Application {
    private Color defaultFarge = Color.YELLOW;
    private Canvas canvas;
    Form[] former = new Form[500];
    private final double HEIGHT = 700;
    private final double WIDTH = 650;

    int antFigurer = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        canvas = lagCanvas();
        tegnCanvas();
        StackPane canvasHolder = new StackPane(canvas);
        canvasHolder.setStyle("-fx-border-width: 2px; -fx-border-color: #444");
        BorderPane root = new BorderPane(canvasHolder);
        root.setStyle("-fx-border-width: 1px; -fx-border-color: black");
        root.setTop(lagVerktøylinje(canvas));


        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.setTitle("Tegneprogram");
        primaryStage.show();

    }




    private HBox lagVerktøylinje(Canvas canvas) {
        //Lager ett panel som inneholder de knappene og andre verktøyene
        //man trenger for å lage figurene i programmet.
        ColorPicker fargeVelger = new ColorPicker();
        TextField børsteStørrelse = new TextField();
        børsteStørrelse.setPromptText("Sett bredde på børsten");
        børsteStørrelse.setFont(Font.font(børsteStørrelse.getText()));
       /* ToggleGroup toggleFigurer = new ToggleGroup();
        RadioButton rektangel = new RadioButton("Rektangel");
        RadioButton sirkel = new RadioButton("Sirkel");
        RadioButton linje = new RadioButton("Linje");
        RadioButton friHånd = new RadioButton("Tegne");
        CheckBox viskeUt = new CheckBox("Viskelær");
        rektangel.setToggleGroup(toggleFigurer);
        sirkel.setToggleGroup(toggleFigurer);
        linje.setToggleGroup(toggleFigurer);
        friHånd.setToggleGroup(toggleFigurer);

        toggleFigurer.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {
                if(rektangel.isSelected()) {
                    canvas.setOnMousePressed(e-> {
                        defaultFarge = fargeVelger.getValue();
                        leggTilFigur(new Rektangel());
                        tegnCanvas();
                    });
                } if(sirkel.isSelected()) {
                    canvas.setOnMousePressed(e-> {
                        defaultFarge = fargeVelger.getValue();
                        leggTilFigur(new Sirkel());
                        tegnCanvas();
                    });
                } if(linje.isSelected()) {
                    canvas.setOnMousePressed(e -> {
                        defaultFarge = fargeVelger.getValue();
                        leggTilFigur(new Linje());
                        tegnCanvas();
                    });
                }
                if(friHånd.isSelected()){
                    canvas.setOnMouseDragged(e ->{
                        GraphicsContext g = canvas.getGraphicsContext2D();
                        double size = Double.parseDouble(børsteStørrelse.getText());
                        double x = e.getX() - size / 2;
                        double y = e.getY() - size / 2;
                        if (friHånd.isSelected()) {
                            g.setFill(fargeVelger.getValue());
                            g.fillRect(x, y, size, size);
                        }
                        if(viskeUt.isSelected()) {
                            g.clearRect(x, y, size, size);
                        }
                    });
                }
        }));*/

        Button rektangel = new Button("Rektangel");
rektangel.setOnAction(e -> {
    defaultFarge = fargeVelger.getValue();
    leggTilFigur(new Rektangel());

});
        Button sirkel = new Button("Rektangel");
        sirkel.setOnAction(e -> {
            defaultFarge = fargeVelger.getValue();
            leggTilFigur(new Sirkel());
        });
        Button linje = new Button("Rektangel");
        linje.setOnAction(e -> {
            defaultFarge = fargeVelger.getValue();
            leggTilFigur(new Linje());
        });

        HBox verktøyLinje= new HBox(10);
        verktøyLinje.getChildren().addAll(fargeVelger, rektangel, sirkel, linje, børsteStørrelse);
        return verktøyLinje;
        //friHånd, viskeUt,
    }

    private VBox endreFigurer(Canvas canvas) {

        VBox endre = new VBox(10);
        return endre;
    }

    private Canvas lagCanvas() {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        canvas.setOnMousePressed( this::musTrykket );
        canvas.setOnMouseReleased( this::musSluppet );
        canvas.setOnMouseDragged( this::musDratt );
        return canvas;
    }

    private Form figurBlirDratt = null;
    private int prevDragX;  // During dragging, these record the x and y coordinates of the
    private int prevDragY;

    private void musTrykket(MouseEvent mouseEvent) {
        int x = (int)mouseEvent.getX();
        int y = (int)mouseEvent.getY();
        for(int i = antFigurer - 1; i >= 0; i--){
            Form f = former[i];
            if(f.harPunkt(x,y)){
                figurBlirDratt = f;
                prevDragY = y;
                prevDragX = x;
                if(mouseEvent.isShiftDown()) {
                    if (antFigurer - 1 - i >= 0)
                        System.arraycopy(former, i + 1, former, i, antFigurer - 1 - i);
                    former[antFigurer - 1] = f;
                    tegnCanvas();
                }
                    return;
                }
            }
        }


    private void musDratt(MouseEvent mouseEvent) {
        int x = (int)mouseEvent.getX();
        int y = (int)mouseEvent.getY();
        if(figurBlirDratt != null){
            figurBlirDratt.flytt(x - prevDragX, y - prevDragY);
            prevDragX = x;
            prevDragY = y;
            System.out.println(prevDragX);
            System.out.println(prevDragY);
            tegnCanvas();
        }
    }

    private void musSluppet(MouseEvent mouseEvent){
        figurBlirDratt = null;
    }

    private void leggTilFigur(Form form){
        form.setFarge(defaultFarge);
        form.omform();
        former[antFigurer] =form;
        antFigurer++;
        tegnCanvas();
    }

    public void tegnCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0, canvas.getWidth(), canvas.getHeight());
        /*for (Form former : figurer) {
            former.tegn(gc);
        }*/
        for(int i = 0; i < antFigurer; i++){
            Form f = former[i];
            f.tegn(gc);
        }
    }




    public static void main(String[] args) {
        launch(args);
    }

}
