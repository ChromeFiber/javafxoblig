package sample;

/*
* For å bruke programmet:
*
* For å lage Firkant = Hold nede Alt-tasten og trykk på skjermen
* For å lage Sirkel = Hold nede CTRL-tasten og trykk på skjermen
* For å lage Ellipse = Hold nede Alt- og CTRL tastene og trykk på skjermen
* For å lage Strek = Hold nede CTRL- og shift tastene og trykk på skjermen
* For å lage Tekst = Hold nede Alt- og Shift tastene og trykk på skjermen
* */

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;


public class Main extends Application {
    // Lager form-tabellen som holder på figurene. Kan lagre opp til 1000 figurer
    Form[] former = new Form[1000];

    // teller antall figurer som ligger i tabellen
    int antFigurer = 0;

    // Denne blir brukt for å vise informasjon om mus-koordinater og areal osb.
    TextArea musPosisjon;

    // Brukast for å legge figurer over de forrige figurene for hver gang det blir laget en ny figur
    StackPane canvasHolder;

    // Setter standard fargen til kvit
    private Color defaultFarge = Color.WHITE;

    //Denne blir brukt for å hente informasjon om skjerm bredde og høgde
    Rectangle2D skjerm = Screen.getPrimary().getVisualBounds();

    //lager canvas
    private Canvas canvas;

    //sjekker når figur blir dratt, og initierer en variabel til Form klassa
    private Form figurBlirDratt = null;

    //Lager fargehjulet som bruker velge farge med
    ColorPicker fargeVelger = new ColorPicker();

    //desse to prev-variablene tar vare på dei forrige x- og y koordinatane til datamusa
    private int prevDragX;
    private int prevDragY;


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
        root.setRight(lagSidePanel(canvas));
        primaryStage.setScene(new Scene(root, skjerm.getWidth(), skjerm.getHeight() - 10));
        primaryStage.setTitle("Tegneprogram");
        primaryStage.show();
    }

    // Her konstrueres verktøylinjen som ligger øverst i programmet.
    // Radioknappene settes inn i en togglegroup som hindrer i at to stk. blir valgt.
    // Ulike eventhånderere sjekker hva som er valgt
    // og legger til de respektive figurene i canvaset ved hjelp av 'leggTilFigur'.
    private HBox lagVerktøylinje(Canvas canvas) {
        //Lager ett panel som inneholder de knappene og andre verktøyene
        //man trenger for å lage figurene i programmet.
        fargeVelger = new ColorPicker();
        TextField børsteStørrelse = new TextField();
        børsteStørrelse.setPromptText("Sett bredde på børsten");
        børsteStørrelse.setFont(Font.font(børsteStørrelse.getText()));
        ToggleGroup toggleFigurer = new ToggleGroup();
        RadioButton rektangel = new RadioButton("Rektangel");
        RadioButton sirkel = new RadioButton("Sirkel");
        RadioButton ellipse = new RadioButton("Ellipse");
        RadioButton linje = new RadioButton("Linje");
        RadioButton tekst = new RadioButton("Tekst");
        TextField tfTekst = new TextField();
        tfTekst.setPromptText("Hva vil du skrive?");
        Button lagre = new Button("Lagre");
        TextField setNavn = new TextField();
        setNavn.setPromptText("Skriv navn på bildet");
        lagre.setOnAction(e -> {
            onSave(setNavn);
        });

        rektangel.setCursor(Cursor.HAND);
        sirkel.setCursor(Cursor.HAND);
        rektangel.setToggleGroup(toggleFigurer);
        sirkel.setToggleGroup(toggleFigurer);
        linje.setToggleGroup(toggleFigurer);
        ellipse.setToggleGroup(toggleFigurer);
        tekst.setToggleGroup(toggleFigurer);
        toggleFigurer.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {
            canvas.setOnMouseClicked(event -> {
                defaultFarge = fargeVelger.getValue();
                if (rektangel.isSelected() && event.isAltDown()) {
                    leggTilFigur(new Rektangel(event.getX(), event.getY()));
                }

                if (sirkel.isSelected() && event.isControlDown()) {
                    leggTilFigur(new Sirkel(event.getX(), event.getY()));
                }

                if (ellipse.isSelected() && event.isControlDown() && event.isAltDown()) {
                    leggTilFigur(new Ellipse(event.getX(), event.getY()));
                }

                if (tekst.isSelected() && event.isAltDown() && event.isShiftDown()) {
                    leggTilFigur(new Tekst(tfTekst, event.getX(), event.getY()));
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

            canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, ev -> {
                if (linje.isSelected() && ev.isControlDown() && ev.isShiftDown()) {
                    sluttX = ev.getX();
                    sluttY = ev.getY();
                    tegnLinje.setEndX(sluttX);
                    tegnLinje.setEndY(sluttY);
                    tegnLinje = new Line(startX, startY, sluttX, sluttY);
                    leggTilFigur(new Linje(tegnLinje.getStartX(), tegnLinje.getStartY(), tegnLinje.getEndX(), tegnLinje.getEndY()));
                }
            });

        }));
        // Legger alle knappene inn i en HBox og legger til padding.
        HBox verktøyLinje = new HBox(10);
        verktøyLinje.getChildren().addAll(fargeVelger, rektangel, sirkel, ellipse, linje, tekst, tfTekst, setNavn, lagre);
        return verktøyLinje;
    }

    // Denne funksjonen lager panelet som skal vise informasjon om dei ulike figurane som brukeren trykker på
    // Om brukeren holder nede musa over ingen figurer vil kompilatoren gje "NullPointerExceptions".
    // Jeg fanger dei og setter ny tekst, men uten areal og navn på figur.
    private VBox lagSidePanel(Canvas canvas) {
        musPosisjon = new TextArea();
        musPosisjon.setEditable(false);
        musPosisjon.setMinWidth(canvas.getWidth());
        canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            try {
                musPosisjon.setText(
                        "X-posisjon: " + event.getX() + "\n" +
                                "Y-posisjon: " + event.getY() + "\n" +
                                "Areal: " + (int) figurBlirDratt.getAreal() / 1000 + " Cm" + "\n" +
                                "Type figur: " + figurBlirDratt.getForm() + "\n" +
                                "Farge: " + figurBlirDratt.getFarge() + "\n" +
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
        bunnlinje.getChildren().addAll(musPosisjon);
        return bunnlinje;
    }


    // Denne metoden lager canvaset som figurene tegnes på.
    // Og kobler de ulike eventene til funksjonene.
    private Canvas lagCanvas() {
        Canvas canvas = new Canvas(skjerm.getWidth() - 250, skjerm.getHeight());
        canvas.setOnMousePressed(this::musTrykket);
        canvas.setOnMouseReleased(this::musSluppet);
        canvas.setOnMouseDragged(this::musDratt);
        canvas.setOnMouseEntered(this::nyStørrelse);
        return canvas;
    }

    // Denne funksjonen tar seg av vanlige 'click-events' og om shift-tasten er nede
    // vil den putte den figuren som datamusa er over fremst på canvasset.
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

    // Denne metoden endrer størrelse på figuren når
    // den midterste museknappen er trykket ned.
    // Hvis den midterste museknappen ikke er nede
    // (og en av de to andre museknappene er trykket ned)
    // kan man flytte figuren man har valgt.
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

    // Når brukeren ikke trykker lengre på figuren vil 'figurBlirDratt' få null-verdi og da er den ikke i bevegelse eller endrer størrelse
    private void musSluppet(MouseEvent mouseEvent) {
        figurBlirDratt = null;
    }

    // Denne metoden gjør ingenting, men uten den så
    // bugger programmet seg.
    public void nyStørrelse(MouseEvent scrollEvent) {
        int x = (int) scrollEvent.getX();
        int y = (int) scrollEvent.getY();
        if (figurBlirDratt != null) {
            figurBlirDratt.endreStørrelse(x + prevDragX, y + prevDragY);
            prevDragX = x;
            prevDragY = y;
        }
    }

    // denne funksjonen vil legge til figur og figurtabellen vil bli oppdatert. Grunnen til at eg bruker ein tabell er for å lettere
    // ta vare på figuren og kunne sette figuren fremst på skjermen.
    private void leggTilFigur(Form form) {
        form.setFarge(defaultFarge);
        form.omform();
        former[antFigurer] = form;
        antFigurer++;
        tegnCanvas();
    }

    // Her tegner man canvasset om at igjen for hver gang en tegner en figur
    public void tegnCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int i = 0; i < antFigurer; i++) {
            Form f = former[i];
            f.tegn(gc);
        }
    }

    // Denne knappen lagrer bildet i src-mappen til prosjektet.
    // Bildet lagres med navnet du skriver inn i tekstfeltet + .png.
    public void onSave(TextField navn) {
        String lagreNavn = navn.getText();
        try {
            Image screenShot = canvas.snapshot(null, null);
            ImageIO.write(SwingFXUtils.fromFXImage(screenShot, null), "png", new File(lagreNavn + ".png"));
        } catch (Exception e) {
            System.out.println("Failed to save image: " + e);
        }
    }

}

