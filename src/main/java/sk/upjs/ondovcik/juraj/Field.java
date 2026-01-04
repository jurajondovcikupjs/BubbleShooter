package sk.upjs.ondovcik.juraj;

import sk.upjs.jpaz2.AnimatedWinPane;
import sk.upjs.jpaz2.Turtle;
import sk.upjs.jpaz2.WinPane;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Field extends WinPane {

    List<Bubble> bubbles = new ArrayList<Bubble>();
    Turtle turret = new Turret();
    Bubble nextBubble = new Bubble();
    public static final String FILE_PATH = "src/main/java/sk/upjs/ondovcik/juraj/res/bubbles.txt";

    public Field() {
        this.setTitle("Bubble Shooter");
        this.resize(720, 1280);
        this.setPosition(0,0);
        this.setResizable(false);
        this.setBackgroundColor(new Color(0xFF22bdb2));
        this.add(turret);
        turret.setPosition(360,1200);

        nextBubble.generateRandomColor();
        this.add(nextBubble);
        nextBubble.setX(250);
        nextBubble.setY(1225);

    }

    @Override
    protected void onMouseMoved(int x, int y, MouseEvent detail) {
        super.onMouseMoved(x, y, detail);
        turret.setDirectionTowards(x, y);
    }

    @Override
    protected void onMouseClicked(int x, int y, MouseEvent detail) {
        Bubble b = new Bubble(x,y, nextBubble.getColor());
        this.add(b);
        bubbles.add(b);
        b.setDirectionTowards(x, y);
        b.penUp();
        nextBubble.generateRandomColor();
    }

    public void exportToFile() {
        
    }

    public void importFromFile() {

    }
}
