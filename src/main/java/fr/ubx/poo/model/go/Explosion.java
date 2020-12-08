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

    private static final float EXPLOSION_DURATION = 0.20f;
    private static final float AFTER_DURATION = 0.15f;
    private List<Direction> spreadingDirections;
    private Timer timer;
    private int explosionRange;
    private int level;
    private boolean hasDamageDecor = false;
    private boolean hasSpread = false;

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
            if(!timer.getIsRunning() && !hasSpread) {
                if(explosionRange > 0) {
                    for(Direction direction : spreadingDirections) {
                        Position nextPos = direction.nextPosition(getPosition());
                        boolean canSpread = true;
                        if(!game.getCurrentWorld().isEmpty(nextPos)) {
                            Decor d = game.getCurrentWorld().get(nextPos);
                            if(!d.canExplode()) {
                                canSpread = false;
                            }
                        }
                        if(canSpread) {
                            List<Direction> directions = new ArrayList<>();
                            directions.add(direction);
                            Explosion explosion = new Explosion(game, nextPos, directions, explosionRange - 1, now, level);
                            game.getPlayer().addExplosion(explosion);
                        }
                    }
                }
                hasSpread = true;
                timer = new Timer(now, AFTER_DURATION);
                timer.start();
            } else {
                Bomb b;
                if(!game.getCurrentWorld().isEmpty(getPosition())) {
                    if(!hasDamageDecor) {
                        hasDamageDecor = true;
                        Decor decor = game.getCurrentWorld().get(getPosition());
                        decor.takeDamage(1);
                        if(decor.getResistance() == 0) {
                            hasDamageDecor = false;
                        }
                        spreadingDirections.clear();
                    }
                }
                else if(game.getPlayer().getPosition().equals(getPosition())) {
                    game.getPlayer().takeDamage(1, now);
                } else if((b = game.getPlayer().getBomb(getPosition())) != null) {
                    spreadingDirections.clear();
                    b.explode(now);
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
