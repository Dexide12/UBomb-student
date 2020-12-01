package fr.ubx.poo.model.go;

import fr.ubx.poo.engine.Timer;
import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;

import java.util.ArrayList;
import java.util.List;

public class Explosion extends GameObject {

    private static final float EXPLOSION_DURATION = 0.4f;
    private List<Direction> spreadingDirections;
    private final Timer timer;
    private int explosionRange;
    private int level;

    public Explosion(Game game, Position position, List<Direction> spreadingDirections, int explosionRange, long now, int level) {
        super(game, position);
        this.spreadingDirections = spreadingDirections;
        this.explosionRange = explosionRange;
        this.level = level;
        timer = new Timer(now, EXPLOSION_DURATION);
        timer.start();
        //Todo inflict damages here
    }

    public void update(long now) {
        if(timer.getIsRunning()) {
            timer.update(now);
            if(!timer.getIsRunning()) {
                if(explosionRange > 0) {
                    for(Direction direction : spreadingDirections) {
                        Position nextPos = direction.nextPosition(getPosition());
                        //Todo verify that explosion can spread in this direction
                        List<Direction> directions = new ArrayList<>();
                        directions.add(direction);
                        Explosion explosion = new Explosion(game, nextPos, directions, explosionRange - 1, now, level);
                        game.getPlayer().addExplosion(explosion);
                    }
                }
            }
        }
    }

    public boolean explosionEnded() {
        return !timer.getIsRunning();
    }

    public int getLevel() { return level; }
}
