package sk.upjs.ondovcik.juraj;

import sk.upjs.jpaz2.ImageTurtleShape;
import sk.upjs.jpaz2.Turtle;

public class Button extends Turtle {

    private double x;
    private double y;
    private String texture;

    public Button(double x, double y, String texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.setShape(new sk.upjs.jpaz2.ImageTurtleShape(getClass().getResource(texture)));
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
        this.setShape(new ImageTurtleShape(getClass().getResource(texture)));
    }

    @Override
    public double getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        this.moveTo(this.x, this.y);
    }

    @Override
    public double getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        this.moveTo(this.x, this.y);
    }

    public boolean checkNearButtonRectangle(int x, int y, int r) {
        return x >= this.x - r && x <= this.x + r && y >= this.y - r && y <= this.y + r;
    }
}