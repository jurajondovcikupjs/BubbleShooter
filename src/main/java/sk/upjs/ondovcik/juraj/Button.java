package sk.upjs.ondovcik.juraj;

import sk.upjs.jpaz2.Turtle;

public class Button extends Turtle {

    private double x;
    private double y;
    private String texture;

    public Button(double x, double y, String texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.setShape(new sk.upjs.jpaz2.ImageTurtleShape(texture));
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    @Override
    public double getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public double getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean checkNearButtonCircle(int x, int y, int radius) {
        return this.distanceTo(x, y) <= radius;
    }

    public boolean checkNearButtonRectangle(int x, int y) {
        return x >= this.x - 16 && x <= this.x + 16 && y >= this.y - 16 && y <= this.y + 16;
    }
}
