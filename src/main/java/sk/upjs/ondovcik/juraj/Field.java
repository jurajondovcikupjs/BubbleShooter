package sk.upjs.ondovcik.juraj;

import sk.upjs.jpaz2.Turtle;
import sk.upjs.jpaz2.WinPane;
import com.logitech.gaming.LogiLED;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.JFileChooser;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Field extends WinPane {

    public static void main(String[] args) {
        // init
        Field field = new Field();

    }

    private final int WINDOW_WIDTH = 504; // 720
    private final int WINDOW_HEIGHT = 896; // 1280
    private int SCORE = 0;
    private final int LEFT_BORDER = 40;
    private final int RIGHT_BORDER = WINDOW_WIDTH - LEFT_BORDER;
    private final int TOP_BORDER = 100;
    private final int CHECK_LINE_BOTTOM = WINDOW_HEIGHT - 2 * TOP_BORDER;
    private final int BUBBLE_SIZE = 38;
    private boolean USE_LIGHTING = false;
    final double FLYING_SPEED = 15.0; // pixels per frame

    List<Bubble> bubbles = new ArrayList<Bubble>();
    Turtle turret = new Turret();
    Bubble nextBubble = new Bubble();
    Bubble flyingBubble = null;
    Button exitButton = new Button(WINDOW_WIDTH - 60, 80, "/sk/upjs/ondovcik/juraj/res/buttons/exit.png");
    Button screenshotButton = new Button(WINDOW_WIDTH - 85, 80, "/sk/upjs/ondovcik/juraj/res/buttons/screenshot.png");
    Button lightingButton = new Button(WINDOW_WIDTH - 110, 80, "/sk/upjs/ondovcik/juraj/res/buttons/lighting.png");
    Button exportButton = new Button(WINDOW_WIDTH - 135, 80, "/sk/upjs/ondovcik/juraj/res/buttons/export.png");
    Button importButton = new Button(WINDOW_WIDTH - 160, 80, "/sk/upjs/ondovcik/juraj/res/buttons/import.png");
    Button reloadButton = new Button(WINDOW_WIDTH - 185, 80, "/sk/upjs/ondovcik/juraj/res/buttons/reload.png");
    Button toast = new Button(WINDOW_WIDTH - 135, 35, "/sk/upjs/ondovcik/juraj/res/toast/empty.png");
    Button leaderboard = new Button((double) WINDOW_WIDTH - 210, 80,
            "/sk/upjs/ondovcik/juraj/res/buttons/leaderboard.png");
    Bubble ghostBubble;
    double flyingBubbleVX = 0;
    double flyingBubbleVY = 0;
    javax.swing.Timer flyingTimer;
    int lastScoreThreshold = 0;
    int rowsAdded = 4;
    int playCount = 0; // Track number of plays
    Button[] scoreCount = {
            new Button(50, 80, "/sk/upjs/ondovcik/juraj/res/numbers/0.png"),
            new Button(70, 80, "/sk/upjs/ondovcik/juraj/res/numbers/0.png"),
            new Button(90, 80, "/sk/upjs/ondovcik/juraj/res/numbers/0.png"),
            new Button(110, 80, "/sk/upjs/ondovcik/juraj/res/numbers/0.png"),
            new Button(130, 80, "/sk/upjs/ondovcik/juraj/res/numbers/0.png"),
            new Button(150, 80, "/sk/upjs/ondovcik/juraj/res/numbers/0.png"),
            new Button(170, 80, "/sk/upjs/ondovcik/juraj/res/numbers/0.png"),
            new Button(190, 80, "/sk/upjs/ondovcik/juraj/res/numbers/0.png"),
            new Button(210, 80, "/sk/upjs/ondovcik/juraj/res/numbers/0.png"),
    };
    boolean allowedToMove = true;
    private boolean gameEnded = false;

    public Field() {
        generateGameWindow();
    }

    public void generateGameWindow() {
        this.setTitle("BubbleShooter");
        this.resize(WINDOW_WIDTH, WINDOW_HEIGHT); // 720x1280
        this.setPosition(0, 0);
        this.setResizable(false);
        this.setBackgroundColor(Theme.BACKGROUND_COLOR);

        this.add(turret);
        this.add(exitButton);
        this.add(screenshotButton);
        this.add(lightingButton);
        this.add(exportButton);
        this.add(importButton);
        this.add(reloadButton);
        this.add(leaderboard);
        turret.setPosition((double) this.getWidth() / 2, this.getHeight() - 80);

        nextBubble.generateRandomColor();
        this.add(nextBubble);
        nextBubble.setX(this.getWidth() / 4);
        nextBubble.setY(this.getHeight() - 55);

        generateBorders();
        generateBubbles(4);

        ghostBubble = new Bubble();
        ghostBubble.setColor(nextBubble.getColor()); // Set initial color to match nextBubble
        ghostBubble.penUp();
        this.add(ghostBubble);
        ghostBubble.setTransparency(0.5f); // If supported, make it semi-transparent

        for (Button b : scoreCount) {
            this.add(b);
        }

        this.add(toast);

        if (USE_LIGHTING) {
            LogiLED.LogiLedInit();
            setLogitechLighting(nextBubble.getColor());
        }

        // Timer for flying bubble
        flyingTimer = new javax.swing.Timer(15, e -> updateFlyingBubble());
        flyingTimer.start();
    }

    public void generateBorders() {
        Turtle t = new Turtle();
        this.add(t);
        t.setPenWidth(5);

        t.setPenColor(Color.RED);
        t.setPosition(LEFT_BORDER, CHECK_LINE_BOTTOM);
        t.moveTo(RIGHT_BORDER, CHECK_LINE_BOTTOM);

        t.setPenColor(Theme.TERTIARY);
        t.setPosition(LEFT_BORDER, 1280);
        t.moveTo(LEFT_BORDER, TOP_BORDER);
        t.moveTo(RIGHT_BORDER, TOP_BORDER);
        t.moveTo(RIGHT_BORDER, 1280);
        this.remove(t);
    }

    @Override
    protected void onMouseMoved(int x, int y, MouseEvent detail) {
        super.onMouseMoved(x, y, detail);
        turret.setDirectionTowards(x, y);
        // Snap ghost bubble to grid
        if (ghostBubble != null) {
            double snappedY = snapBubble(x, y, false);
            double snappedX = snapBubble(x, y, true);
            ghostBubble.setX(snappedX);
            ghostBubble.setY(snappedY);
            ghostBubble.setPosition(snappedX, snappedY);
            // Always update ghost bubble color and lighting to match nextBubble
            ghostBubble.setColor(nextBubble.getColor());
        }
    }

    @Override
    protected void onMouseClicked(int x, int y, MouseEvent detail) {

        if (flyingBubble != null)
            return; // Only one flying bubble at a time
        if (x > LEFT_BORDER && x < RIGHT_BORDER && y > TOP_BORDER && allowedToMove) {
            // Set lighting to match the bubble about to be launched
            // Launch the nextBubble from the turret
            double startX = turret.getX();
            double startY = turret.getY() - 40; // slightly above turret
            double dx = x - startX;
            double dy = y - startY;
            double len = Math.sqrt(dx * dx + dy * dy);
            flyingBubbleVX = FLYING_SPEED * dx / len;
            flyingBubbleVY = FLYING_SPEED * dy / len;
            flyingBubble = new Bubble((int) startX, (int) startY, nextBubble.getColor());
            this.add(flyingBubble);
            flyingBubble.penUp();

            nextBubble.generateRandomColor();
            nextBubble.setX(this.getWidth() / 4);
            nextBubble.setY(this.getHeight() - 55);
            ghostBubble.setColor(nextBubble.getColor());
            if (USE_LIGHTING)
                setLogitechLighting(nextBubble.getColor());
        }

        if (exitButton.checkNearButtonRectangle(x, y, 11)) {
            System.exit(0);
        }
        if (screenshotButton.checkNearButtonRectangle(x, y, 11)) {
            this.savePicture("screenshot_" + System.currentTimeMillis() + ".png");
            showConfirmToast();
        }
        if (lightingButton.checkNearButtonRectangle(x, y, 11)) {
            USE_LIGHTING = !USE_LIGHTING;
            if (USE_LIGHTING) {
                LogiLED.LogiLedInit();
                setLogitechLighting(nextBubble.getColor());
            } else
                LogiLED.LogiLedShutdown();
            showConfirmToast();
        }
        if (exportButton.checkNearButtonRectangle(x, y, 11)) {
            exportToFile();
            showConfirmToast();
        }
        if (importButton.checkNearButtonRectangle(x, y, 11)) {
            if (importFromFile()) {
                showConfirmToast();
            }
        }
        if (reloadButton.checkNearButtonRectangle(x, y, 11)) {
            this.remove(flyingBubble);
            flyingBubble = null;
            nextBubble.setX(this.getWidth() / 4);
            nextBubble.setY(this.getHeight() - 55);
            ghostBubble.setColor(nextBubble.getColor());
            allowedToMove = true;
            gameEnded = false;
            // Clear existing bubbles
            for (Bubble b : new ArrayList<>(bubbles)) {
                this.remove(b);
            }
            bubbles.clear();
            SCORE = 0;
            playCount = 0;
            updateScoreTextures();
            generateBubbles(4);
            setLogitechLighting(nextBubble.getColor());
            showConfirmToast();
        }
        if (leaderboard.checkNearButtonRectangle(x, y, 11)) {

            showLeaderboardDialog();
        }
    }

    public void showConfirmToast() {
        toast.setTexture("/sk/upjs/ondovcik/juraj/res/toast/confirm2.png");
        playAudioAsync("/sk/upjs/ondovcik/juraj/res/sound/success.mp3");
        javax.swing.Timer timer = new javax.swing.Timer(1000, e -> {
            toast.setTexture("/sk/upjs/ondovcik/juraj/res/toast/empty.png");
        });
        timer.setRepeats(false);
        timer.start();
    }

    public void showErrorToast() {
        toast.setTexture("/sk/upjs/ondovcik/juraj/res/toast/error.png");
        playAudioAsync("/sk/upjs/ondovcik/juraj/res/sound/error.mp3");
        javax.swing.Timer timer = new javax.swing.Timer(1000, e -> {
            toast.setTexture("/sk/upjs/ondovcik/juraj/res/toast/empty.png");
        });
        timer.setRepeats(false);
        timer.start();
    }

    public void setLogitechLighting(String color) {
        if (Objects.equals(color, "red")) {
            LogiLED.LogiLedSetLighting(75, 0, 0);
        } else if (Objects.equals(color, "blue")) {
            LogiLED.LogiLedSetLighting(0, 0, 75);
        } else if (Objects.equals(color, "green")) {
            LogiLED.LogiLedSetLighting(0, 75, 0);
        } else if (Objects.equals(color, "yellow")) {
            LogiLED.LogiLedSetLighting(75, 63, 0);
        } else if (Objects.equals(color, "pink")) {
            LogiLED.LogiLedSetLighting(100, 0, 86);
        } else if (Objects.equals(color, "purple")) {
            LogiLED.LogiLedSetLighting(70, 0, 100);
        }
    }

    public double snapBubble(int x, int y, boolean isX) {
        return snapBubble((double) x, (double) y, isX);
    }

    public double snapBubble(double x, double y, boolean isX) {
        int tempY = (int) (y - TOP_BORDER);
        int row = tempY / BUBBLE_SIZE;
        boolean shiftX = (row % 2 == 1);
        double snappedY = row * BUBBLE_SIZE + TOP_BORDER + BUBBLE_SIZE / 2.0;

        if (isX) {
            double gridOrigin = LEFT_BORDER + BUBBLE_SIZE / 2.0 + (shiftX ? BUBBLE_SIZE / 2.0 : 0);
            double colRaw = (x - gridOrigin) / BUBBLE_SIZE;
            int col = (int) Math.round(colRaw); // Use Math.round for center snapping
            return gridOrigin + col * BUBBLE_SIZE;
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
                double snappedY = snapBubble(x, y, false);
                double snappedX = snapBubble(x, y, true);
                Bubble b = new Bubble(snappedX, snappedY, nextBubble.getColor());
                this.add(b);
                bubbles.add(b);
                nextBubble.generateRandomColor();
            }
        }
    }

    private void updateFlyingBubble() {
        if (flyingBubble == null)
            return;
        double x = flyingBubble.getX() + flyingBubbleVX;
        double y = flyingBubble.getY() + flyingBubbleVY;
        // Bounce off left/right borders
        if (x < LEFT_BORDER + BUBBLE_SIZE / 2) {
            x = LEFT_BORDER + BUBBLE_SIZE / 2;
            flyingBubbleVX = -flyingBubbleVX;
        }
        if (x > RIGHT_BORDER - BUBBLE_SIZE / 2) {
            x = RIGHT_BORDER - BUBBLE_SIZE / 2;
            flyingBubbleVX = -flyingBubbleVX;
        }
        flyingBubble.setX(x);
        flyingBubble.setY(y);
        // Check collision with top border
        if (y < TOP_BORDER + BUBBLE_SIZE / 2) {
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
                if (connected.contains(b))
                    continue;
                if (!Objects.equals(b.getColor(), start.getColor()))
                    continue;
                // Hex grid neighbor check
                double dx = Math.abs(b.getX() - current.getX());
                double dy = Math.abs(b.getY() - current.getY());
                boolean sameRow = b.getY() == current.getY();
                boolean adjacentRow = Math.abs(b.getY() - current.getY()) == BUBBLE_SIZE;
                boolean neighbor = false;
                if (sameRow && Math.abs(dx - BUBBLE_SIZE) < 1e-3)
                    neighbor = true; // left/right
                if (adjacentRow && (Math.abs(dx) < 1e-3 || Math.abs(dx - BUBBLE_SIZE / 2.0) < 1e-3))
                    neighbor = true; // up/down left/right (hex offset)
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
                if (connected.contains(b))
                    continue;
                double dx = Math.abs(b.getX() - current.getX());
                double dy = Math.abs(b.getY() - current.getY());
                boolean sameRow = b.getY() == current.getY();
                boolean adjacentRow = Math.abs(b.getY() - current.getY()) == BUBBLE_SIZE;
                boolean neighbor = false;
                if (sameRow && Math.abs(dx - BUBBLE_SIZE) < 1e-3)
                    neighbor = true;
                if (adjacentRow && (Math.abs(dx) < 1e-3 || Math.abs(dx - BUBBLE_SIZE / 2.0) < 1e-3))
                    neighbor = true;
                if (neighbor) {
                    connected.add(b);
                    toVisit.add(b);
                }
            }
        }
        return connected;
    }

    private boolean isPositionOccupied(double x, double y) {
        for (Bubble b : bubbles) {
            if (Math.abs(b.getX() - x) < 1 && Math.abs(b.getY() - y) < 1) {
                return true;
            }
        }
        return false;
    }

    private void snapFlyingBubble() {
        // Find the best empty position
        double snappedY = snapBubble(flyingBubble.getX(), flyingBubble.getY(), false);
        double snappedX = snapBubble(flyingBubble.getX(), flyingBubble.getY(), true);

        // If the intended position is occupied, look for neighbors
        if (isPositionOccupied(snappedX, snappedY)) {
            double bestDist = Double.MAX_VALUE;
            double bestX = snappedX;
            double bestY = snappedY;

            // Search in a small area around the collision for the nearest empty grid cell
            for (int dy = -1; dy <= 1; dy++) {
                for (int dx = -1; dx <= 1; dx++) {
                    if (dx == 0 && dy == 0)
                        continue;

                    // Calculate potential neighbor coordinates
                    double testY = snappedY + dy * BUBBLE_SIZE;
                    double testX = snappedX + dx * BUBBLE_SIZE;

                    // Re-snap to ensure valid grid position (handles hex offset)
                    double finalTestY = snapBubble(testX, testY, false);
                    double finalTestX = snapBubble(testX, testY, true);

                    if (!isPositionOccupied(finalTestX, finalTestY)) {
                        double d = Math.hypot(finalTestX - flyingBubble.getX(), finalTestY - flyingBubble.getY());
                        if (d < bestDist) {
                            bestDist = d;
                            bestX = finalTestX;
                            bestY = finalTestY;
                        }
                    }
                }
            }
            snappedX = bestX;
            snappedY = bestY;
        }

        flyingBubble.setX(snappedX);
        flyingBubble.setY(snappedY);
        bubbles.add(flyingBubble);
        // Check for connected bubbles of the same color
        List<Bubble> group = getConnectedSameColorBubbles(flyingBubble);
        boolean scored = false;
        boolean shouldPlayPop = false;
        if (group.size() >= 3) {
            for (Bubble b : group) {
                this.remove(b);
            }
            bubbles.removeAll(group);
            SCORE += group.size();
            scored = true;
            updateScoreTextures();
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
            // Add points for each flying bubble removed
            if (!flying.isEmpty()) {
                SCORE += flying.size();
                updateScoreTextures();
            }
            shouldPlayPop = true;
        } else {
            int soundNumber = (int) (Math.random() * 2) + 1;
            playAudioAsync("/sk/upjs/ondovcik/juraj/res/sound/bubble-place-" + soundNumber + ".mp3");
        }
        playCount++;
        if (playCount % 5 == 0) {
            moveDownAndGenerateRow();
            ghostBubble.setColor(nextBubble.getColor());
            updateScoreTextures();
        }
        flyingBubble = null;
        checkBubblesCrossedBottom();
        // Play pop audio after all removals and updates
        if (shouldPlayPop) {
            playAudioAsync("/sk/upjs/ondovcik/juraj/res/sound/bubble-pop.mp3");
        }
    }

    private void checkBubblesCrossedBottom() {
        for (Bubble b : bubbles) {
            if (b.getY() + BUBBLE_SIZE / 2 >= CHECK_LINE_BOTTOM) {
                if (!gameEnded) {
                    endGame();
                    gameEnded = true;
                }
                break;
            }
        }
    }

    private void updateScoreTextures() {
        int tempScore = SCORE;
        for (int i = scoreCount.length - 1; i >= 0; i--) {
            int digit = tempScore % 10;
            scoreCount[i].setTexture("/sk/upjs/ondovcik/juraj/res/numbers/" + digit + ".png");
            tempScore = tempScore / 10;
        }
    }

    public void playAudioAsync(String filePath) {
        new Thread(() -> playAudio(filePath)).start();
    }

    private void playAudio(String filePath) {
        try {
            try (InputStream is = getClass()
                    .getResourceAsStream("/sk/upjs/ondovcik/juraj/res/sound/" + new File(filePath).getName())) {
                if (is == null) {
                    System.err.println("Audio resource not found: " + filePath);
                    return;
                }
                javazoom.jl.player.Player playMP3 = new javazoom.jl.player.Player(is);
                playMP3.play();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void moveDownAndGenerateRow() {
        // Move all bubbles down by two rows, keep X unchanged
        for (Bubble b : new ArrayList<>(bubbles)) {
            int oldY = (int) b.getY();
            int newY = oldY + 2 * BUBBLE_SIZE;
            b.setY(newY);
        }
        // Increment row count
        rowsAdded += 2; // We add two rows
        // Generate two new rows at the top
        // First row: even (12 bubbles, not shifted)
        boolean shiftX1 = false;
        int bubblesInRow1 = 12;
        double rowOrigin1 = LEFT_BORDER + BUBBLE_SIZE / 2 + (shiftX1 ? BUBBLE_SIZE / 2.0 : 0);
        int y1 = TOP_BORDER + BUBBLE_SIZE / 2;
        for (int j = 0; j < bubblesInRow1; j++) {
            double x = rowOrigin1 + j * BUBBLE_SIZE;
            double snappedY = snapBubble(x, y1, false);
            double snappedX = snapBubble(x, y1, true);
            Bubble b = new Bubble(snappedX, snappedY, nextBubble.getColor());
            this.add(b);
            bubbles.add(b);
            nextBubble.generateRandomColor();
        }
        // Second row: odd (11 bubbles, shifted)
        boolean shiftX2 = true;
        int bubblesInRow2 = 11;
        double rowOrigin2 = LEFT_BORDER + BUBBLE_SIZE / 2 + (shiftX2 ? BUBBLE_SIZE / 2.0 : 0);
        int y2 = TOP_BORDER + BUBBLE_SIZE + BUBBLE_SIZE / 2;
        for (int j = 0; j < bubblesInRow2; j++) {
            double x = rowOrigin2 + j * BUBBLE_SIZE;
            double snappedY = snapBubble(x, y2, false);
            double snappedX = snapBubble(x, y2, true);
            Bubble b = new Bubble(snappedX, snappedY, nextBubble.getColor());
            this.add(b);
            bubbles.add(b);
            nextBubble.generateRandomColor();
        }
        checkBubblesCrossedBottom();
    }

    public void endGame() {
        allowedToMove = false;
        flyingBubble = null;
        toast.setTexture("/sk/upjs/ondovcik/juraj/res/toast/gameover2.png");
        playAudioAsync("/sk/upjs/ondovcik/juraj/res/sound/gameover.mp3");
        for (Bubble b : bubbles) {
            b.setColor("grey");
        }
        nextBubble.setColor("grey");
        ghostBubble.setColor("grey");

        // Ask for name using JOptionPane
        String defaultName = "defaultname";
        String playerName = JOptionPane.showInputDialog(null, "Enter your name for the leaderboard:", "Leaderboard",
                JOptionPane.QUESTION_MESSAGE);
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = defaultName;
        }

        // Scoreboard logic
        String scoreboardFile = "scoreboard.txt";
        List<String> lines = new ArrayList<>();
        try {
            File file = new File(scoreboardFile);
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    lines.add(line);
                }
                scanner.close();
            }
        } catch (Exception e) {
            // Ignore, treat as empty scoreboard
        }
        // Parse and insert new score
        List<String[]> entries = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(";");
            if (parts.length == 2) {
                entries.add(parts);
            }
        }
        // Insert new score
        boolean inserted = false;
        for (int i = 0; i < entries.size(); i++) {
            int score = 0;
            try {
                score = Integer.parseInt(entries.get(i)[1]);
            } catch (Exception ignored) {
            }
            if (SCORE > score) {
                entries.add(i, new String[] { playerName, String.valueOf(SCORE) });
                inserted = true;
                break;
            }
        }
        if (!inserted && entries.size() < 10) {
            entries.add(new String[] { playerName, String.valueOf(SCORE) });
        }
        // Keep only top 10
        while (entries.size() > 10) {
            entries.remove(entries.size() - 1);
        }
        // Write back to file
        try {
            PrintWriter pw = new PrintWriter(scoreboardFile);
            for (String[] entry : entries) {
                pw.println(entry[0] + ";" + entry[1]);
            }
            pw.close();
        } catch (Exception e) {
            // Ignore write errors
        }
    }

    // Show leaderboard in a message dialog using StringBuilder
    private void showLeaderboardDialog() {
        StringBuilder sb = new StringBuilder();
        sb.append("Leaderboard\n\n");
        try {
            java.io.File file = new java.io.File("scoreboard.txt");
            if (file.exists()) {
                java.util.Scanner scanner = new java.util.Scanner(file);
                int i = 1;
                while (scanner.hasNextLine() && i <= 10) {
                    String line = scanner.nextLine();
                    String name = line;
                    String score = "";
                    int sep = line.lastIndexOf(';');
                    if (sep != -1) {
                        name = line.substring(0, sep);
                        score = line.substring(sep + 1);
                    }
                    sb.append(i).append(". ").append(name).append(" ").append(score).append("\n");
                    i++;
                }
                scanner.close();
            } else {
                sb.append("No scores yet.\n");
            }
        } catch (Exception e) {
            sb.append("Could not read leaderboard.\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
    }

    public String pickFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setApproveButtonText("Select");
        fileChooser.setDialogTitle("Select a BubbleShooter backup file");
        fileChooser.setBackground(Theme.BACKGROUND_COLOR);
        fileChooser.setForeground(Theme.BACKGROUND_COLOR);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        int result = fileChooser.showOpenDialog(fileChooser);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile.getAbsolutePath();
        }

        return "";
    }

    public void exportToFile() {
        try (PrintWriter pw = new PrintWriter("gamesave" + System.currentTimeMillis() + ".txt")) {
            pw.println(SCORE);
            pw.println(playCount);
            pw.println(nextBubble.getColor());
            for (Bubble b : bubbles) {
                pw.println(b.getX() + ";" + b.getY() + ";" + b.getColor());
            }
        } catch (Exception e) {
            showErrorToast();
        }
    }

    public boolean importFromFile() {
        try (Scanner scanner = new Scanner(new File(pickFile()))) {
            // Clear existing bubbles
            for (Bubble b : new ArrayList<>(bubbles)) {
                this.remove(b);
            }
            bubbles.clear();
            // Read score and play count
            SCORE = Integer.parseInt(scanner.nextLine());
            playCount = Integer.parseInt(scanner.nextLine());
            updateScoreTextures();
            // Read next bubble color
            String nextColor = scanner.nextLine();
            nextBubble.setColor(nextColor);
            ghostBubble.setColor(nextColor);
            // Read bubbles
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(";");
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                String color = parts[2];
                Bubble b = new Bubble(x, y, color);
                this.add(b);
                bubbles.add(b);
            }
            allowedToMove = true;
            return true;
        } catch (Exception e) {
            showErrorToast();
            return false;
        }
    }
}

