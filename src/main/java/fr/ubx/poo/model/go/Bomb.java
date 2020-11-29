package fr.ubx.poo.model.go;

import fr.ubx.poo.engine.Timer;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;

public class Bomb extends GameObject {

    private static final int timerDuration = 4;
    private final Timer timer;
    private int currentImageNumber;
    private boolean imageNumberHasChange = true;
    private boolean hasExploded = false;

    public Bomb(Game game, Position position, long now) {
        super(game, position);
        timer = new Timer(now, timerDuration);
        timer.start();
        currentImageNumber = 4;
    }

    public void update(long now) {
        if(!hasExploded) {
            timer.update(now);
            if (!timer.getIsRunning()) {
                explode();
                hasExploded = true;
            } else {
                int tmp = calculateCurrentImageNumber();
                if (tmp != currentImageNumber) {
                    currentImageNumber = tmp;
                    imageNumberHasChange = true;
                }
            }
        }
    }

    private void explode() {
        //Todo explosion handler
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

    private int calculateCurrentImageNumber() {
        int timeLeft = (int)Math.ceil(timer.getTimeLeft() * Math.pow(10, -9));
        if(timeLeft <= timerDuration / 4) {
            return 1;
        } else if(timeLeft <= (timerDuration / 4) * 2) {
            return 2;
        } else if(timeLeft <= (timerDuration / 4) * 3) {
            return 3;
        } else {
            return 4;
        }
    }
}
