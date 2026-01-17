package sk.upjs.ondovcik.juraj;

import sk.upjs.jpaz2.ImageTurtleShape;
import sk.upjs.jpaz2.Turtle;

public class Bubble extends Turtle {

    private int x;
    private int y;
    private String color;

    public Bubble() {}

    public Bubble(int x, int y, String color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.setShape(new ImageTurtleShape(chooseColor(color)));
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
        this.setShape(new ImageTurtleShape(chooseColor(color)));
    }

    public String chooseColor(String color) {
        switch (color) {
            case "red":
                return "src/main/java/sk/upjs/ondovcik/juraj/res/red.png";
            case "blue":
                return "src/main/java/sk/upjs/ondovcik/juraj/res/blue.png";
            case "green":
                return "src/main/java/sk/upjs/ondovcik/juraj/res/green.png";
            case "yellow":
                return "src/main/java/sk/upjs/ondovcik/juraj/res/yellow.png";
            default:
                return "null";
        }
    }

    public void generateRandomColor() {
        String[] colors = {"red", "blue", "green", "yellow"};
        int randomIndex = (int) (Math.random() * colors.length);
        this.color = colors[randomIndex];
        this.setShape(new ImageTurtleShape(chooseColor(this.color)));
    }

    public String chooseLogitechColor(String color) {
        switch (color) {
            case "red":
                return "src/main/java/sk/upjs/ondovcik/juraj/res/red.png";
            case "blue":
                return "src/main/java/sk/upjs/ondovcik/juraj/res/blue.png";
            case "green":
                return "src/main/java/sk/upjs/ondovcik/juraj/res/green.png";
            case "yellow":
                return "src/main/java/sk/upjs/ondovcik/juraj/res/yellow.png";
            default:
                return "null";
        }
    }

    public boolean isInRange(Bubble b) {
        double distance = Math.sqrt(this.getX()*this.getX() + this.getY() * this.getY());
        return distance <= 54;
    }

}
