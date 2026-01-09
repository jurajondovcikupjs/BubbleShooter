package sk.upjs.ondovcik.juraj;

import sk.upjs.jpaz2.AnimatedWinPane;
import sk.upjs.jpaz2.Turtle;
import sk.upjs.jpaz2.WinPane;
import sk.upjs.ondovcik.juraj.res.Theme;
import com.logitech.gaming.LogiLED;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Field extends WinPane {

    public int SCORE = 0;
    final int LEFT_BORDER = 40;
    final int RIGHT_BORDER = 680;
    final int TOP_BORDER = 100;
    final int CHECK_LINE_BOTTOM = 1000;
    public static final String FILE_PATH = "src/main/java/sk/upjs/ondovcik/juraj/res/bubbles.txt";
    public static final int BUBBLE_SIZE = 54;
    final boolean USE_LIGHTING = false;

    List<Bubble> bubbles = new ArrayList<Bubble>();
    Turtle turret = new Turret();
    Bubble nextBubble = new Bubble();


    public Field() {
        if (USE_LIGHTING) LogiLED.LogiLedInit();

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
        setLogitechLighting(nextBubble.getColor());

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

    @Override
    protected void onMouseMoved(int x, int y, MouseEvent detail) {
        super.onMouseMoved(x, y, detail);
        turret.setDirectionTowards(x, y);
    }

    @Override
    protected void onMouseClicked(int x, int y, MouseEvent detail) {
        if (x > LEFT_BORDER && x < RIGHT_BORDER && y > TOP_BORDER) {
            int snappedY = snapBubble(x, y, false);
            int snappedX = snapBubble(x, y, true);
            Bubble b = new Bubble(snappedX, snappedY, nextBubble.getColor());
            this.add(b);
            bubbles.add(b);
            b.setDirectionTowards(x, y);
            b.penUp();
            nextBubble.generateRandomColor();

            setLogitechLighting(nextBubble.getColor());
        }
    }

    public void setLogitechLighting(String color) {
        if (!USE_LIGHTING) return;
        if (Objects.equals(color, "red")) {
            LogiLED.LogiLedSetLighting(75,0,0);
        } else if (Objects.equals(color, "blue")) {
            LogiLED.LogiLedSetLighting(0,0,75);
        } else if (Objects.equals(color, "green")) {
            LogiLED.LogiLedSetLighting(0,75,0);
        } else if (Objects.equals(color, "yellow")) {
            LogiLED.LogiLedSetLighting(75,63,0);
        }
    }


    public int snapBubble(int x, int y, boolean isX) {
        int tempX = x - LEFT_BORDER;
        int tempY = y - TOP_BORDER;
        boolean shiftX = false;
        tempX = tempX / BUBBLE_SIZE;
        //System.out.println(tempX);
        tempX = tempX * BUBBLE_SIZE + 2 * LEFT_BORDER;

        tempY = tempY / BUBBLE_SIZE;
        //System.out.println(tempY);
        if (tempY%2 == 1) {
            shiftX = true;
        }
        tempY = tempY * BUBBLE_SIZE + (3*TOP_BORDER/2);

        if (isX) {
            if (shiftX) tempX+= BUBBLE_SIZE/2;
            return tempX;
        } else {
            return tempY;
        }
    }

    public void exportToFile() {
        
    }

    public void importFromFile() {

    }
}
