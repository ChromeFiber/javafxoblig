package sample;
//https://home.usn.no/lonnesta/kurs/OBJ2000/Oblig/Obligatorisk_oppgave_2020.pdf

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.io.File;


public class Main extends Application {
    private final double HEIGHT = 700;
    private final double WIDTH = 650;
    Form[] former = new Form[500];
    int antFigurer = 0;
    private Color defaultFarge = Color.WHITE;
    private Canvas canvas;
    private Form figurBlirDratt = null;
    private int prevDragX;  // During drag  ging, these record the x and y coordinates of the
    private int prevDragY;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        canvas = lagCanvas();
        tegnCanvas();
        StackPane canvasHolder = new StackPane(canvas);
        canvasHolder.setStyle("-fx-border-width: 2px; -fx-border-color: #444");
        BorderPane root = new BorderPane(canvasHolder);
        root.setStyle("-fx-border-width: 1px; -fx-border-color: black");
        root.setTop(lagVerktøylinje(canvas));
        root.setBottom(lagBunnLinje(canvas));
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
        ToggleGroup toggleFigurer = new ToggleGroup();
        RadioButton rektangel = new RadioButton("Rektangel");
        RadioButton sirkel = new RadioButton("Sirkel");
        RadioButton linje = new RadioButton("Linje");
        RadioButton friHånd = new RadioButton("Tegne");
        RadioButton ikkeTegne = new RadioButton("Ikke tegne");
        CheckBox viskeUt = new CheckBox("Viskelær");
        Button lagre = new Button("lagre");
        TextField setNavn = new TextField();

        lagre.setOnAction(e->{
            onSave(setNavn);
        });
        rektangel.setCursor(Cursor.HAND);
        sirkel.setCursor(Cursor.HAND);
        rektangel.setToggleGroup(toggleFigurer);
        sirkel.setToggleGroup(toggleFigurer);
        linje.setToggleGroup(toggleFigurer);
        friHånd.setToggleGroup(toggleFigurer);
        ikkeTegne.setToggleGroup(toggleFigurer);
        toggleFigurer.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {
            canvas.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                if (rektangel.isSelected() && event.isAltDown()) {
                    leggTilFigur(new Rektangel(event.getX(), event.getY()));
                    defaultFarge = fargeVelger.getValue();
                    tegnCanvas();
                }
            });

            canvas.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                if (sirkel.isSelected() && event.isControlDown()) {
                    defaultFarge = fargeVelger.getValue();
                    leggTilFigur(new Sirkel(event.getX(), event.getY()));
                    tegnCanvas();
                }
            });
            if (linje.isSelected()) {
                canvas.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                    if (event.isAltDown() && event.isShiftDown()) {
                        defaultFarge = fargeVelger.getValue();
                        leggTilFigur(new Linje(event.getX(), event.getY()));
                        tegnCanvas();
                    }
                });
            }
            canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, e ->{
               if(friHånd.isSelected() && e.isMiddleButtonDown() && e.isControlDown()){
                            GraphicsContext g = canvas.getGraphicsContext2D();
                            double size = Double.parseDouble(børsteStørrelse.getText());
                            double x = e.getX() - size / 2;
                            double y = e.getY() - size / 2;
                            g.setFill(fargeVelger.getValue());
                            g.fillRect(x, y, size, size);

                            if (viskeUt.isSelected()) {
                                g.clearRect(x, y, size, size);
                            }
                    }});

        }));

       /* Button rektangel = new Button("Rektangel");
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
        });*/
        HBox verktøyLinje = new HBox(10);
        verktøyLinje.getChildren().addAll(fargeVelger, rektangel, sirkel, linje, setNavn, lagre);
        return verktøyLinje;
        //friHånd, viskeUt,
    }


    private HBox lagBunnLinje(Canvas canvas){
        TextArea musPosisjon = new TextArea();
        musPosisjon.setEditable(false);
        musPosisjon.setMinWidth(WIDTH);
        canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            musPosisjon.setText("X-posisjon: " + event.getX() +", Y-posisjon: " + event.getY() + ". Areal: " + figurBlirDratt.getAreal());
        });
        HBox bunnlinje = new HBox();
        bunnlinje.getChildren().addAll(musPosisjon);
        return bunnlinje;
    }

    private VBox endreFigurer(Canvas canvas) {

        VBox endre = new VBox(10);
        return endre;
    }

    private Canvas lagCanvas() {
        Canvas canvas = new Canvas(WIDTH-50, HEIGHT-50);
        canvas.setOnMousePressed(this::musTrykket);
        canvas.setOnMouseReleased(this::musSluppet);
        canvas.setOnMouseDragged(this::musDratt);
        canvas.setOnMouseEntered(this::nyStørrelse);
        return canvas;
    }

    private void musTrykket(MouseEvent mouseEvent) {
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();
        for (int i = antFigurer - 1; i >= 0; i--) {
            Form f = former[i];
            if (f.harPunkt(x, y)) {
                figurBlirDratt = f;
                prevDragY = y;
                prevDragX = x;
                if (mouseEvent.isShiftDown()) {
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
            int x = (int) mouseEvent.getX();
            int y = (int) mouseEvent.getY();
                if (figurBlirDratt != null) {
                    if(mouseEvent.isMiddleButtonDown()){
                        figurBlirDratt.endreStørrelse(x - prevDragX, y - prevDragY);
                    }else {
                        figurBlirDratt.flytt(x - prevDragX, y - prevDragY);
                    }
                    prevDragX = x;
                    prevDragY = y;
                    tegnCanvas();
                }
    }


    private void musSluppet(MouseEvent mouseEvent) {
        figurBlirDratt = null;
    }

    public void nyStørrelse(MouseEvent scrollEvent){
        int x = (int) scrollEvent.getX();
        int y = (int) scrollEvent.getY();
        if (figurBlirDratt != null) {
            figurBlirDratt.endreStørrelse(x + prevDragX, y + prevDragY);
            prevDragX = x;
            prevDragY = y;
            System.out.println(prevDragX);
            System.out.println(prevDragY);
        }
    }

    private void leggTilFigur(Form form) {
        form.setFarge(defaultFarge);
        form.omform();
        former[antFigurer] = form;
        antFigurer++;
        tegnCanvas();
    }

    public void tegnCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        /*for (Form former : figurer) {
            former.tegn(gc);
        }*/
        for (int i = 0; i < antFigurer; i++) {
            Form f = former[i];
            f.tegn(gc);
        }
    }
    public void onSave(TextField navn) {
        String lagreNavn = navn.getText();
        try {
            Image screenShot = canvas.snapshot(null, null);
            ImageIO.write(SwingFXUtils.fromFXImage(screenShot, null), "png", new File(lagreNavn+".png"));
        } catch (Exception e) {
            System.out.println("Failed to save image: " + e);
        }
    }
}

