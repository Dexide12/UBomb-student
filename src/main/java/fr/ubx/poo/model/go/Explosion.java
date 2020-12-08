package fr.ubx.poo.model.go;

import fr.ubx.poo.engine.Timer;
import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Entity;
import fr.ubx.poo.model.decor.Decor;

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
    }

    public void update(long now) {
        if(timer.getIsRunning()) {
            timer.update(now);
            if(!timer.getIsRunning()) {
                if(explosionRange > 0) {
                    for(Direction direction : spreadingDirections) {
                        Position nextPos = direction.nextPosition(getPosition());
                        Decor d = game.getCurrentWorld().get(getPosition());
                        //Todo if D Instance of nonDestroyable do not spread
                        List<Direction> directions = new ArrayList<>();
                        directions.add(direction);
                        Explosion explosion = new Explosion(game, nextPos, directions, explosionRange - 1, now, level);
                        game.getPlayer().addExplosion(explosion);
                    }
                }
            } else {
                Decor decor = game.getCurrentWorld().get(getPosition());
                if(decor != null) {
                    //Damage to decor
                    spreadingDirections.clear();
                }
                else if(game.getPlayer().getPosition().equals(getPosition())) {
                    game.getPlayer().takeDamage(1);
                } else if(true) {
                    //Todo check for bomb to detonate -> ask player if bomb on case
                }
                //Todo check for monsters when added to game
            }
        }
    }

    public boolean explosionEnded() {
        return !timer.getIsRunning();
    }

    public int getLevel() { return level; }
}
