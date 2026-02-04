package sk.upjs.ondovcik.juraj;

import sk.upjs.jpaz2.WinPane;
import sk.upjs.jpaz2.Turtle;
import java.util.ArrayList;
import java.util.List;

public class Start extends WinPane {
    private final int WINDOW_WIDTH = 300;
    private final int WINDOW_HEIGHT = 300;
    private Button logoButton;
    private Button playButtonBtn;
    private List<Turtle> leaderboardTurtles = new ArrayList<>();

    public Start() {
        super(300, 300);
        setTitle("BubbleShooter");
        setBackgroundColor(Theme.BACKGROUND_COLOR);
        showLogo();
        showPlayButton();
    }

    private void showLogo() {
        logoButton = new Button(WINDOW_WIDTH / 2, 70, "/sk/upjs/ondovcik/juraj/res/logo.png");
        add(logoButton);
    }

    private void showPlayButton() {
        playButtonBtn = new Button(WINDOW_WIDTH / 2, WINDOW_HEIGHT * 2/3, "/sk/upjs/ondovcik/juraj/res/buttons/play.png");
        add(playButtonBtn);
    }

    @Override
    protected void onMouseClicked(int x, int y, java.awt.event.MouseEvent detail) {
        // Check if play button is clicked
        double px = playButtonBtn.getX();
        double py = playButtonBtn.getY();
        if (Math.abs(x - px) < 80 && Math.abs(y - py) < 40) {
            // Remove start screen and launch game
            for (Turtle t : leaderboardTurtles) remove(t);
            remove(playButtonBtn);
            remove(logoButton);
            Field game = new Field();
        }
    }

    public static void main(String[] args) {
        new Start();
    }
}
