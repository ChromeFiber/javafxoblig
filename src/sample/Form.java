package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Form {
    int venstre, topp;
    int bredde, høyde;
    int startX, startY, sluttX, sluttY;
    Color farge = Color.WHITE;
    public Form(double ey, double ex){
        venstre =(int) ey;
        topp = (int) ex;
    }
    public void omform() {
        this.venstre = getVenstre();
        this.topp =getTopp();
        this.bredde = 150;
        this.høyde = 100;
    }



    public void setY(double ey){
        this.venstre = (int)ey;
    }
    public void setX(double ex){
        this.topp = (int) ex;
    }

    public int getVenstre(){
        return venstre;
    }
    public int getTopp(){
        return topp;
    }


    public void setFarge(Color farge) {
        this.farge = farge;
    }

    void flytt(int dx, int dy) {
        // Move the shape by dx pixels horizontally and dy pixels vertically
        // (by changing the position of the top-left corner of the shape).
        venstre += dx;
        topp += dy;
    }

    public boolean harPunkt(int x, int y) {
        return x >= venstre && x < venstre + bredde && y >= topp && y < topp + høyde;
    }

    abstract void tegn(GraphicsContext g);

}

class Rektangel extends Form {
    public Rektangel(double ey, double ex) {
        super(ey, ex);
    }
    // Denne klassen representerer rektangel-figurer

    void tegn(GraphicsContext g) {
        g.setFill(farge);
        g.fillRect(getVenstre(), getTopp(), bredde, høyde);
        g.setStroke(Color.BLACK);
        g.strokeRect(getVenstre(), getTopp(), bredde, høyde);
    }
}

class Sirkel extends Form {
    public Sirkel(double ey, double ex) {
        super(ey, ex);
    }

    // Denne klassen representerer representerer sirkel-figurer
    void tegn(GraphicsContext g) {

        g.setFill(farge);
        g.fillOval(getVenstre(), getTopp(), bredde, høyde);
        g.setStroke(Color.BLACK);
        g.strokeOval(getVenstre(), getTopp(), bredde, høyde);
    }
}

class Linje extends Form {
    public Linje(double ey, double ex) {
        super(ey, ex);
    }

    //Denne klassen representerer linje-objekter
    void tegn(GraphicsContext g) {
        g.beginPath();

        g.strokeLine(10, 10, 100, 10);
        g.stroke();
    }
}