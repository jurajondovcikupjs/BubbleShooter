package sk.upjs.ondovcik.juraj;

import sk.upjs.jpaz2.ImageTurtleShape;
import sk.upjs.jpaz2.Turtle;

public class Bubble extends Turtle {

    private double x;
    private double y;
    private String color;
    String[] colors = { "red", "blue", "green", "yellow", "pink", "purple" };

    public Bubble() {
    }

    public Bubble(double x, double y, String color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.setPosition(x, y);
        this.setShape(new ImageTurtleShape(getClass().getResource(chooseColor(color))));
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    public String getColor() {
        return color;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setColor(String color) {
        this.color = color;
        this.setShape(new ImageTurtleShape(getClass().getResource(chooseColor(color))));
    }

    public String chooseColor(String color) {
        switch (color) {
            case "red":
                return "/sk/upjs/ondovcik/juraj/res/bubbles/red.png";
            case "blue":
                return "/sk/upjs/ondovcik/juraj/res/bubbles/blue.png";
            case "green":
                return "/sk/upjs/ondovcik/juraj/res/bubbles/green.png";
            case "yellow":
                return "/sk/upjs/ondovcik/juraj/res/bubbles/yellow.png";
            case "grey":
                return "/sk/upjs/ondovcik/juraj/res/bubbles/grey.png";
            case "pink":
                return "/sk/upjs/ondovcik/juraj/res/bubbles/pink.png";
            case "purple":
                return "/sk/upjs/ondovcik/juraj/res/bubbles/purple.png";
            default:
                return null;
        }
    }

    public void generateRandomColor() {
        String[] colors = { "red", "blue", "green", "yellow", "pink", "purple" };
        int randomIndex = (int) (Math.random() * colors.length);
        this.color = colors[randomIndex];
        this.setShape(new ImageTurtleShape(getClass().getResource(chooseColor(this.color))));
    }

}