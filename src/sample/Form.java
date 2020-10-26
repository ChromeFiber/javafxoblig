package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public abstract class Form {
    int venstre, topp;
    int bredde, høyde;
    int startX, startY, sluttX, sluttY;
    Color farge = Color.WHITE;

    public void omform() {
        this.venstre = 10;
        this.topp = 10;
        this.bredde = 150;
        this.høyde = 100;
    }

    public void flytte(int dx, int dy) {
        venstre += dx;
        topp += dy;
    }

    public void setFarge(Color farge) {
        this.farge = farge;
    }

    public void flytt(int dx, int dy) {
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
    // Denne klassen representerer rektangel-figurer

    void tegn(GraphicsContext g) {
        g.setFill(farge);
        g.fillRect(venstre, topp, bredde, høyde);
        g.setStroke(Color.BLACK);
        g.strokeRect(venstre, topp, bredde, høyde);
    }
}

class Sirkel extends Form {
    // Denne klassen representerer representerer sirkel-figurer
    void tegn(GraphicsContext g) {
        g.setFill(farge);
        g.fillOval(venstre, topp, bredde, høyde);
        g.setStroke(Color.BLACK);
        g.strokeOval(venstre, topp, bredde, høyde);
    }
}

class Linje extends Form {
    //Denne klassen representerer linje-objekter
    void tegn(GraphicsContext g) {
        g.beginPath();

        g.strokeLine(10, 10, 100, 10);
        g.stroke();
    }
}