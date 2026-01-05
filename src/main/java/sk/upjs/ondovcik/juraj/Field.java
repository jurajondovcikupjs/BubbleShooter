package sk.upjs.ondovcik.juraj;

import sk.upjs.jpaz2.AnimatedWinPane;
import sk.upjs.jpaz2.Turtle;
import sk.upjs.jpaz2.WinPane;
import sk.upjs.ondovcik.juraj.res.Theme;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Field extends WinPane {

    public static int SCORE = 0;
    public static int LEFT_BORDER = 40;
    public static int RIGHT_BORDER = 680;
    public static int TOP_BORDER = 100;
    public static int CHECK_LINE_BOTTOM = 1000;

    List<Bubble> bubbles = new ArrayList<Bubble>();
    Turtle turret = new Turret();
    Bubble nextBubble = new Bubble();
    public static final String FILE_PATH = "src/main/java/sk/upjs/ondovcik/juraj/res/bubbles.txt";
    public static final int BUBBLE_SIZE = 32; // Adjust if your bubble images are a different size

    public Field() {
        this.setTitle("Bubble Shooter");
        this.resize(720, 1280);
        this.setPosition(0,0);
        this.setResizable(false);
        this.setBackgroundColor(Theme.BACKGROUND_COLOR);
        this.add(turret);
        turret.setPosition(360,1200);

        nextBubble.generateRandomColor();
        this.add(nextBubble);
        nextBubble.setX(250);
        nextBubble.setY(1225);

        generateUI();

    }

    public void generateUI() {
        Turtle t = new Turtle();
        this.add(t);
        t.setPenWidth(5);

        t.setPenColor(Color.RED);
        t.setPosition(LEFT_BORDER,CHECK_LINE_BOTTOM);
        t.moveTo(RIGHT_BORDER,CHECK_LINE_BOTTOM);

        t.setPenColor(Theme.TERTIARY);
        t.setPosition(LEFT_BORDER,1280);
        t.moveTo(LEFT_BORDER,TOP_BORDER);
        t.moveTo(RIGHT_BORDER,TOP_BORDER);
        t.moveTo(RIGHT_BORDER,1280);
        this.remove(t);
    }

    public int gridCoord(int inputCoord, boolean isX, int otherCoord) {
        int offset = isX ? LEFT_BORDER : TOP_BORDER;
        int coord = inputCoord - offset;
        int gridIndex = Math.round(coord / (float) BUBBLE_SIZE);
        return gridIndex * BUBBLE_SIZE + offset;
    }

    @Override
    protected void onMouseMoved(int x, int y, MouseEvent detail) {
        super.onMouseMoved(x, y, detail);
        turret.setDirectionTowards(x, y);
    }

    @Override
    protected void onMouseClicked(int x, int y, MouseEvent detail) {
        if (x > LEFT_BORDER && x < RIGHT_BORDER && y > TOP_BORDER) {
            int snappedY = gridCoord(y, false, 0);
            int snappedX = gridCoord(x, true, snappedY);
            Bubble b = new Bubble(snappedX, snappedY, nextBubble.getColor());
            this.add(b);
            bubbles.add(b);
            b.setDirectionTowards(x, y);
            b.penUp();
            nextBubble.generateRandomColor();
        }
    }

    public void exportToFile() {
        
    }

    public void importFromFile() {

    }
}
