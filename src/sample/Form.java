package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

import java.awt.*;

public abstract class Form {
    int venstre, topp;
    int bredde, høyde;
    String ellipse = "Ellipse";
    String sirkel = "Sirkel";
    String rektangel = "Rektangel";
    Color farge = Color.WHITE;

    public Form(double ey, double ex) {
        venstre = (int) ey;
        topp = (int) ex;
    }

    public Form() {
    }

    public void omform() {
        this.bredde = 150;
        this.høyde = 100;
    }


    public int getVenstre() {
        return venstre;
    }

    public int getTopp() {
        return topp;
    }

    // Setter farge på figuren
    public void setFarge(Color farge) {
        this.farge = farge;
    }

    public Color getFarge() {
        return farge;
    }

    void flytt(int dx, int dy) {
        // Flytter figuren med dx piksler horisontalt og dy piksler vertikalt
        // Med å forandre posisjonen av top-venstre hjørne av figuren

        venstre += dx;
        topp += dy;
    }

    void endreStørrelse(int dx, int dy) {
        bredde += dx;
        høyde += dy;
    }


    boolean harPunkt(int x, int y) {
        return x >= venstre && x < venstre + bredde && y >= topp && y < topp + høyde;
    }

    abstract void tegn(GraphicsContext g);

    abstract double getAreal();


    public abstract String getForm();
}

// Denne klassen representerer rektangel-figurer
class Rektangel extends Form {
    public Rektangel(double ey, double ex) {
        super(ey, ex);
    }


    void tegn(GraphicsContext g) {
        g.setFill(farge);
        g.fillRect(getVenstre(), getTopp(), bredde, høyde);
        g.setStroke(Color.BLACK);
        g.strokeRect(getVenstre(), getTopp(), bredde, høyde);

    }

    public double getAreal() {
        return this.bredde * this.høyde;
    }

    public String getForm() {
        return rektangel;
    }

}

// Denne klassen representerer representerer sirkel-figurer
class Sirkel extends Form {
    public Sirkel(double ey, double ex) {
        super(ey, ex);
    }


    void tegn(GraphicsContext g) {

        g.setFill(farge);
        g.fillOval(getVenstre(), getTopp(), bredde, bredde);
        g.setStroke(Color.BLACK);
        g.strokeOval(getVenstre(), getTopp(), bredde, bredde);
    }

    public double getAreal() {
        return Math.PI * (bredde / 2) * (bredde / 2);
    }

    public String getForm() {
        return sirkel;
    }
}

// Denne klassen representerer representerer ellipse-figurer
class Ellipse extends Form {
    public Ellipse(double ey, double ex) {
        super(ey, ex);
    }

    void tegn(GraphicsContext g) {
        g.setFill(farge);
        g.fillOval(getVenstre(), getTopp(), bredde, høyde);
        g.setStroke(Color.BLACK);
        g.strokeOval(getVenstre(), getTopp(), bredde, høyde);
    }

    boolean harPunkt(int x, int y) {
        // Denne funksjonen sjekker om (x,y) er på innsida av denne ovalklassa,
        // med å bruke den matematiske formellen av ein ellipse.
        // Om den slår til erstatter den definisjonen om 'harPunkt' som var arva fra 'Form' klassa
        double rx = bredde / 2.0;   // horisontal radius av ellipse
        double ry = høyde / 2.0;  // vertikal radius av ellipse
        double cx = venstre + rx;   // x-koord av senter av ellipse
        double cy = topp + ry;    // y-koord av senter av ellipse
        return (ry * (x - cx)) * (ry * (x - cx)) + (rx * (y - cy)) * (rx * (y - cy)) <= rx * rx * ry * ry;
    }

    public double getAreal() {
        return Math.PI * (bredde / 2) * (bredde / 2);
    }

    public String getForm() {
        return ellipse;
    }

}

class Linje extends Form {
    double startX, startY, endX, endY;

    public Linje(double ey, double ex) {
        super(ey, ex);
    }

    // Ny konstruktør for linje
    public Linje(double startX, double startY, double endX, double endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    // Denne klassen representerer linje-objekter
    void tegn(GraphicsContext g) {
        g.beginPath();
        g.strokeLine(startX, startY, endX, endY);
        g.setStroke(Color.BLACK);
    }

    public void handle(MouseEvent mouseEvent) {

    }

    @Override
    double getAreal() {
        return 0;
    }

    @Override
    public String getForm() {
        return null;
    }

}

// Denne klassen representerer tekst-objekter
class Tekst extends Form{

    TextField text;
    double x;
    double y;
    public Tekst(TextField text, double x, double y) {
        this.text=text;
        this.x = x;
        this.y = y;
    }

    @Override
    void tegn(GraphicsContext g) {
        g.setFont(Font.font(20));
        g.setStroke(farge);
        g.setFill(farge);
        g.fillText(text.getText(), x, y);
        g.strokeText(text.getText(), x, y);

    }

    @Override
    double getAreal() {
        return 0;
    }

    @Override
    public String getForm() {
        return null;
    }
}