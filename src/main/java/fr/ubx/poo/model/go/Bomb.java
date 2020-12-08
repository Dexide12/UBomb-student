package fr.ubx.poo.model.go;

import fr.ubx.poo.engine.Timer;
import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;

import java.util.ArrayList;
import java.util.List;

public class Bomb extends GameObject {

    private static final int TIMER_DURATION = 4;
    private int level;
    private final Timer timer;
    private int currentImageNumber;
    private int explosionRange;
    private boolean imageNumberHasChange = true;
    private boolean hasExploded = false;

    public Bomb(Game game, Position position, long now, int explosionRange, int level) {
        super(game, position);
        this.explosionRange = explosionRange;
        timer = new Timer(now, TIMER_DURATION);
        timer.start();
        currentImageNumber = 4;
        this.level = level;
    }

    public void update(long now) {
        if(!hasExploded) {
            timer.update(now);
            if (!timer.getIsRunning()) {
                explode(now);
            } else {
                int tmp = calculateCurrentImageNumber();
                if (tmp != currentImageNumber) {
                    currentImageNumber = tmp;
                    imageNumberHasChange = true;
                }
            }
        }
    }

    public void explode(long now) {
        hasExploded = true;
        timer.stop();
        List<Direction> directions = new ArrayList<>() {{
            add(Direction.N);
            add(Direction.E);
            add(Direction.S);
            add(Direction.W);
        }};
        Explosion explosion = new Explosion(game, getPosition(), directions, explosionRange, now, level);
        game.getPlayer().addExplosion(explosion);
    }

    public boolean getImageNumberHasChange() {
        return imageNumberHasChange;
    }

    public int getCurrentImageNumber() {
        return currentImageNumber;
    }

    public void resetImageNumberHasChange() {
        imageNumberHasChange = false;
    }

    public boolean getHasExploded() {
        return hasExploded;
    }

    public void levelHasChanged() { imageNumberHasChange = true; }

    public int getLevel() { return level; }

    private int calculateCurrentImageNumber() {
        int timeLeft = (int)Math.ceil(timer.getTimeLeft() * Math.pow(10, -9));
        if(timeLeft <= 0) {
            return 0;
        } else if(timeLeft <= TIMER_DURATION / 4) {
            return 1;
        } else if(timeLeft <= (TIMER_DURATION / 4) * 2) {
            return 2;
        } else if(timeLeft <= (TIMER_DURATION / 4) * 3) {
            return 3;
        } else {
            return 4;
        }
    }
}
