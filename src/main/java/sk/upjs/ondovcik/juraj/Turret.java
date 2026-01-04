package sk.upjs.ondovcik.juraj;

import sk.upjs.jpaz2.ImageTurtleShape;
import sk.upjs.jpaz2.Turtle;

import java.awt.event.MouseAdapter;

public class Turret extends Turtle {

    String currentColor;

    public Turret() {
        this.currentColor = null;
        this.setShape(new ImageTurtleShape("src/main/java/sk/upjs/ondovcik/juraj/res/turret.png"));
    }

    public String getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(String currentColor) {
        this.currentColor = currentColor;
    }
}
