package sample;
//https://home.usn.no/lonnesta/kurs/OBJ2000/Oblig/Obligatorisk_oppgave_2020.pdf

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;


public class Main extends Application {
    private final double HEIGHT = 700;
    private final double WIDTH = 650;
    Form[] former = new Form[1000000];
    int antFigurer = 0;
    Stage primaryStage;
    TextArea musPosisjon;
    StackPane canvasHolder;
    private Color defaultFarge = Color.WHITE;
    Rectangle2D skjerm = Screen.getPrimary().getVisualBounds();
    private Canvas canvas;
    private Form figurBlirDratt = null;
    private int prevDragX;  // During drag  ging, these record the x and y coordinates of the
    private int prevDragY;
    private ImageView imageView;
    private BorderPane root;
    double startY, startX, sluttY, sluttX;//start og slutt på linje
    Line tegnLinje = new Line();
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        canvas = lagCanvas();
        tegnCanvas();
        canvasHolder = new StackPane(canvas);
        canvasHolder.setStyle("-fx-border-width: 2px; -fx-border-color: #444");
        root = new BorderPane(canvasHolder);
        root.setStyle("-fx-border-width: 1px; -fx-border-color: black");
        root.setMinHeight(root.getMaxHeight());
        root.setMinWidth(root.getMaxWidth());
        root.setTop(lagVerktøylinje(canvas));
        root.setRight(lagBunnLinje(canvas));
        primaryStage.setScene(new Scene(root, skjerm.getWidth(), skjerm.getHeight() - 10));
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
        RadioButton ellipse = new RadioButton("Ellipse");
        RadioButton linje = new RadioButton("Linje");
        RadioButton friHånd = new RadioButton("Tegne");
        RadioButton ikkeTegne = new RadioButton("Ikke tegne");
        CheckBox viskeUt = new CheckBox("Viskelær");
        Button lagre = new Button("Lagre");
        Button lasteOpp = new Button("Laste opp");
        TextField setNavn = new TextField();
        setNavn.setPromptText("Skriv navn på bildet");
        lagre.setOnAction(e -> {
            onSave(setNavn);
        });
        lasteOpp.setOnAction(e -> {
            //henteBilde(setNavn);
            String bildeUrl = setNavn.getText();
            URL url = getClass().getResource("/tge.png");
            try {
                File file = new File("tge.png");
                Image img = new Image(getClass().getResource(bildeUrl).toString());
                imageView.setImage(img);
                root.getChildren().addAll(imageView);
            } catch (NullPointerException ex) {
                musPosisjon.setText("Fann ikkje fil " + ex.getMessage());
            }
        });

        rektangel.setCursor(Cursor.HAND);
        sirkel.setCursor(Cursor.HAND);
        rektangel.setToggleGroup(toggleFigurer);
        sirkel.setToggleGroup(toggleFigurer);
        linje.setToggleGroup(toggleFigurer);
        ellipse.setToggleGroup(toggleFigurer);
        friHånd.setToggleGroup(toggleFigurer);
        ikkeTegne.setToggleGroup(toggleFigurer);
        toggleFigurer.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {
                    canvas.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                        if (rektangel.isSelected() && event.isAltDown()) {
                            leggTilFigur(new Rektangel(event.getX(), event.getY()));
                            defaultFarge = fargeVelger.getValue();
                        }
                    });

                    canvas.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                        if (sirkel.isSelected() && event.isControlDown()) {
                            defaultFarge = fargeVelger.getValue();
                            leggTilFigur(new Sirkel(event.getX(), event.getY()));
                        }
                    });

                    canvas.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                        if (ellipse.isSelected() && event.isControlDown() && event.isAltDown()) {
                            defaultFarge = fargeVelger.getValue();
                            leggTilFigur(new Ellipse(event.getX(), event.getY()));
                        }
                    });


                    canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, ev -> {
                        if (linje.isSelected() && ev.isControlDown() && ev.isShiftDown()) {
                            startX = ev.getX();
                            startY = ev.getY();
                            tegnLinje.setStartX(ev.getX());
                            tegnLinje.setStartY(ev.getY());
                        }
                    });
                  /*  canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, ev -> {
                        if (ev.isControlDown() && ev.isShiftDown()) {
                            if (tegnLinje == null) {
                                leggTilFigur(new Linje(ev.getX(), ev.getX()));
                            } *//*else {
                                tegnLinje.setEndX(ev.getX());
                                tegnLinje.setEndY(ev.getY());
                            }*//*
                        }});*/
                    canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,ev -> {
                        if (linje.isSelected() && ev.isControlDown() && ev.isShiftDown()) {
                            sluttX = ev.getX();
                            sluttY = ev.getY();
                            tegnLinje.setEndX(sluttX);
                            tegnLinje.setEndY(sluttY);
                            tegnLinje = new Line(startX, startY, sluttX, sluttY);
                            leggTilFigur(new Linje(tegnLinje.getStartX(), tegnLinje.getStartY(), tegnLinje.getEndX(), tegnLinje.getEndY()));
                        }
                    });



            /*canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
                if (friHånd.isSelected() && e.isMiddleButtonDown() && e.isControlDown()) {
                    GraphicsContext g = canvas.getGraphicsContext2D();
                    double size = Double.parseDouble(børsteStørrelse.getText());
                    double x = e.getX() - size / 2;
                    double y = e.getY() - size / 2;
                    g.setFill(fargeVelger.getValue());
                    g.fillRect(x, y, size, size);

                    if (viskeUt.isSelected()) {
                        g.clearRect(x, y, size, size);
                    }
                }
            });*/
        }));

        HBox verktøyLinje = new HBox(10);
        verktøyLinje.getChildren().addAll(fargeVelger, rektangel, sirkel, ellipse, linje, setNavn, lagre, lasteOpp);
        return verktøyLinje;
        //friHånd, viskeUt,
    }

    private VBox lagBunnLinje(Canvas canvas) {
        musPosisjon = new TextArea();
        Canvas visFigur = new Canvas();
        canvasHolder = new StackPane(visFigur);
        musPosisjon.setEditable(false);
        musPosisjon.setMinWidth(canvas.getWidth());
        canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            try {
                musPosisjon.setText(
                        "X-posisjon: " + event.getX() +  "\n" +
                        "Y-posisjon: " + event.getY() + "\n" +
                        "Areal: " + (int)figurBlirDratt.getAreal()/100 + " Cm" + "\n" +
                        "Type figur: " + figurBlirDratt.getForm()  + "\n" +
                        "Antall figur på skjermen: " + antFigurer);
            } catch (NullPointerException e) {
                musPosisjon.setText(
                        "X-posisjon: " + event.getX() + "\n" +
                        "Y-posisjon: " + event.getY() + "\n" +
                                "Antall figur på skjermen: " + antFigurer);
            }

        });
        VBox bunnlinje = new VBox();
        tegnCanvas();
        bunnlinje.getChildren().addAll(musPosisjon, canvasHolder);
        return bunnlinje;
    }

    private VBox endreFigurer(Canvas canvas) {

        VBox endre = new VBox(10);
        return endre;
    }

    private Canvas lagCanvas() {
        Canvas canvas = new Canvas(skjerm.getWidth() - 250, skjerm.getHeight());
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
            if (mouseEvent.isMiddleButtonDown()) {
                figurBlirDratt.endreStørrelse(x - prevDragX, y - prevDragY);
            } else {
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

    public void nyStørrelse(MouseEvent scrollEvent) {
        int x = (int) scrollEvent.getX();
        int y = (int) scrollEvent.getY();
        if (figurBlirDratt != null) {
            figurBlirDratt.endreStørrelse(x + prevDragX, y + prevDragY);
            prevDragX = x;
            prevDragY = y;
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
        for (int i = 0; i < antFigurer; i++) {
            Form f = former[i];
            f.tegn(gc);
        }
    }

    public void onSave(TextField navn) {
        String lagreNavn = navn.getText();
        try {
            Image screenShot = canvas.snapshot(null, null);
            ImageIO.write(SwingFXUtils.fromFXImage(screenShot, null), "png", new File(lagreNavn + ".png"));
        } catch (Exception e) {
            System.out.println("Failed to save image: " + e);
        }
    }

    public void henteBilde(TextField textField) {
       /* String bildeUrl = textField.getText();
        try {
            FileInputStream fileInputStream = new FileInputStream(bildeUrl + ".png");
            Image image = new Image(fileInputStream);
            imageView.setImage(image);
        } catch (NullPointerException | FileNotFoundException e) {
            musPosisjon.setText("Fann ikkje fil");
        }*/
    }

    private void addLine(double x, double y) {
        tegnLinje = new Line(startX, startY, x, y);
        root.getChildren().add(tegnLinje);
    }

}

