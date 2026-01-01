package sk.upjs.ondovcik.juraj;

import sk.upjs.jpaz2.WinPane;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Field extends WinPane {

    List<Bubble> bubbles = new ArrayList<Bubble>();

    public Field() {
        this.setTitle("Field");
        this.setWidth(720);
        this.setHeight(1280);
        this.setPosition(0,0);
        this.setResizable(false);
        this.setBackgroundColor(new Color(0xFF22bdb2));
    }

    @Override
    protected void onMouseClicked(int x, int y, MouseEvent detail) {
        Bubble b = new Bubble(x,y,"src/main/java/sk/upjs/ondovcik/juraj/res/blue.png");
        this.add(b);
        bubbles.add(b);
    }
}
