package sk.upjs.ondovcik.juraj;

import sk.upjs.jpaz2.ImageTurtleShape;
import sk.upjs.jpaz2.Turtle;
import sk.upjs.jpaz2.TurtleShape;

public class Bubble extends Turtle {

    private int x;
    private int y;
    private String color;

    public Bubble(int x, int y, String color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.setShape(new ImageTurtleShape(color));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getColor() {
        return color;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setColor(String color) {
        this.color = color;
    }


}
