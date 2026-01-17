package sk.upjs.ondovcik.juraj;

import javazoom.jl.player.Player;
import sk.upjs.jpaz2.Turtle;
import sk.upjs.jpaz2.WinPane;
import com.logitech.gaming.LogiLED;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
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
    Bubble flyingBubble = null;
    double flyingBubbleVX = 0;
    double flyingBubbleVY = 0;
    final double FLYING_SPEED = 18.0; // pixels per frame
    javax.swing.Timer flyingTimer;

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
        generateBubbles(4);

        // Timer for flying bubble
        flyingTimer = new javax.swing.Timer(15, e -> updateFlyingBubble());
        flyingTimer.start();
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
        if (flyingBubble != null) return; // Only one flying bubble at a time
        if (x > LEFT_BORDER && x < RIGHT_BORDER && y > TOP_BORDER) {
            // Launch the nextBubble from the turret
            double startX = turret.getX();
            double startY = turret.getY() - 40; // slightly above turret
            double dx = x - startX;
            double dy = y - startY;
            double len = Math.sqrt(dx*dx + dy*dy);
            flyingBubbleVX = FLYING_SPEED * dx / len;
            flyingBubbleVY = FLYING_SPEED * dy / len;
            flyingBubble = new Bubble((int)startX, (int)startY, nextBubble.getColor());
            this.add(flyingBubble);
            flyingBubble.penUp();
            setLogitechLighting(nextBubble.getColor());

            nextBubble.setX(-1000);
            nextBubble.setX(nextBubble.getX() + 1000);
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
        return snapBubble((double)x, (double)y, isX);
    }

    public int snapBubble(double x, double y, boolean isX) {
        int tempY = (int)(y - TOP_BORDER);
        int row = tempY / BUBBLE_SIZE;
        boolean shiftX = (row % 2 == 1);
        int snappedY = row * BUBBLE_SIZE + TOP_BORDER + BUBBLE_SIZE / 2;

        if (isX) {
            double gridOrigin = LEFT_BORDER + BUBBLE_SIZE / 2 + (shiftX ? BUBBLE_SIZE / 2.0 : 0);
            double colRaw = (x - gridOrigin) / BUBBLE_SIZE;
            int col = (int) Math.floor(colRaw + 0.5); // Improved: center-based snapping
            return (int) (gridOrigin + col * BUBBLE_SIZE);
        } else {
            return snappedY;
        }
    }

    public void generateBubbles(int amountOfRows) {
        for (int i = 0; i < amountOfRows; i++) {
            int bubblesInRow = (i % 2 == 0) ? 11 : 10;
            boolean shiftX = (i % 2 == 1);
            double rowOrigin = LEFT_BORDER + BUBBLE_SIZE / 2 + (shiftX ? BUBBLE_SIZE / 2.0 : 0);
            int y = TOP_BORDER + i * BUBBLE_SIZE + BUBBLE_SIZE / 2;
            for (int j = 0; j < bubblesInRow; j++) {
                int x = (int) (rowOrigin + j * BUBBLE_SIZE);
                int snappedY = snapBubble(x, y, false);
                int snappedX = snapBubble(x, y, true);
                Bubble b = new Bubble(snappedX, snappedY, nextBubble.getColor());
                this.add(b);
                bubbles.add(b);
                nextBubble.generateRandomColor();
            }
        }
    }

    private void updateFlyingBubble() {
        if (flyingBubble == null) return;
        double x = flyingBubble.getX() + flyingBubbleVX;
        double y = flyingBubble.getY() + flyingBubbleVY;
        // Bounce off left/right borders
        if (x < LEFT_BORDER + BUBBLE_SIZE/2) {
            x = LEFT_BORDER + BUBBLE_SIZE/2;
            flyingBubbleVX = -flyingBubbleVX;
        }
        if (x > RIGHT_BORDER - BUBBLE_SIZE/2) {
            x = RIGHT_BORDER - BUBBLE_SIZE/2;
            flyingBubbleVX = -flyingBubbleVX;
        }
        flyingBubble.setX((int)x);
        flyingBubble.setY((int)y);
        // Check collision with top border
        if (y < TOP_BORDER + BUBBLE_SIZE/2) {
            snapFlyingBubble();
            return;
        }
        // Check collision with other bubbles
        for (Bubble b : bubbles) {
            double dist = Math.hypot(b.getX() - x, b.getY() - y);
            if (dist < BUBBLE_SIZE - 2) { // slightly less than diameter
                snapFlyingBubble();
                return;
            }
        }
    }

    private List<Bubble> getConnectedSameColorBubbles(Bubble start) {
        List<Bubble> connected = new ArrayList<>();
        List<Bubble> toVisit = new ArrayList<>();
        toVisit.add(start);
        connected.add(start);
        while (!toVisit.isEmpty()) {
            Bubble current = toVisit.remove(0);
            for (Bubble b : bubbles) {
                if (connected.contains(b)) continue;
                if (!Objects.equals(b.getColor(), start.getColor())) continue;
                // Hex grid neighbor check
                double dx = Math.abs(b.getX() - current.getX());
                double dy = Math.abs(b.getY() - current.getY());
                boolean sameRow = b.getY() == current.getY();
                boolean adjacentRow = Math.abs(b.getY() - current.getY()) == BUBBLE_SIZE;
                boolean neighbor = false;
                if (sameRow && Math.abs(dx - BUBBLE_SIZE) < 1e-3) neighbor = true; // left/right
                if (adjacentRow && (Math.abs(dx) < 1e-3 || Math.abs(dx - BUBBLE_SIZE / 2.0) < 1e-3)) neighbor = true; // up/down left/right (hex offset)
                if (neighbor) {
                    connected.add(b);
                    toVisit.add(b);
                }
            }
        }
        return connected;
    }

    private List<Bubble> getConnectedToTopRow() {
        List<Bubble> connected = new ArrayList<>();
        List<Bubble> toVisit = new ArrayList<>();
        // Find all bubbles in the top row
        for (Bubble b : bubbles) {
            if (Math.abs(b.getY() - (TOP_BORDER + BUBBLE_SIZE / 2)) < 1e-3) {
                connected.add(b);
                toVisit.add(b);
            }
        }
        // BFS for all connected bubbles
        while (!toVisit.isEmpty()) {
            Bubble current = toVisit.remove(0);
            for (Bubble b : bubbles) {
                if (connected.contains(b)) continue;
                double dx = Math.abs(b.getX() - current.getX());
                double dy = Math.abs(b.getY() - current.getY());
                boolean sameRow = b.getY() == current.getY();
                boolean adjacentRow = Math.abs(b.getY() - current.getY()) == BUBBLE_SIZE;
                boolean neighbor = false;
                if (sameRow && Math.abs(dx - BUBBLE_SIZE) < 1e-3) neighbor = true;
                if (adjacentRow && (Math.abs(dx) < 1e-3 || Math.abs(dx - BUBBLE_SIZE / 2.0) < 1e-3)) neighbor = true;
                if (neighbor) {
                    connected.add(b);
                    toVisit.add(b);
                }
            }
        }
        return connected;
    }

    private void snapFlyingBubble() {
        int snappedY = snapBubble(flyingBubble.getX(), flyingBubble.getY(), false);
        int snappedX = snapBubble(flyingBubble.getX(), flyingBubble.getY(), true);
        flyingBubble.setX(snappedX);
        flyingBubble.setY(snappedY);
        bubbles.add(flyingBubble);
        // Check for connected bubbles of the same color
        List<Bubble> group = getConnectedSameColorBubbles(flyingBubble);
        if (group.size() >= 3) {
            for (Bubble b : group) {
                this.remove(b);
            }
            bubbles.removeAll(group);
            playAudio("src/main/java/sk/upjs/ondovcik/juraj/res/bubble-pop.mp3");
            SCORE += group.size();
            System.out.println("SCORE: " + SCORE);
            // Remove flying bubbles (not connected to top row)
            List<Bubble> connectedToTop = getConnectedToTopRow();
            List<Bubble> flying = new ArrayList<>();
            for (Bubble b : new ArrayList<>(bubbles)) {
                if (!connectedToTop.contains(b)) {
                    flying.add(b);
                }
            }
            for (Bubble b : flying) {
                this.remove(b);
                bubbles.remove(b);
            }
        } else {
            int soundNumber = (int)(Math.random() * 2) + 1;
            playAudio("src/main/java/sk/upjs/ondovcik/juraj/res/bubble-place-" + soundNumber + ".mp3");
        }
        flyingBubble = null;
        // Prepare next bubble
        nextBubble.generateRandomColor();
        nextBubble.setX(250);
        nextBubble.setY(1225);
        setLogitechLighting(nextBubble.getColor());
    }

    public void playAudio(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            Player playMP3 = new Player(fis);
            playMP3.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeBubble(Bubble bubble) {
        this.remove(bubble);
        bubbles.remove(bubble);
    }


    //public void exportToFile() {
    //
    //}
//
    //public void importFromFile() {
//
    //}
}
