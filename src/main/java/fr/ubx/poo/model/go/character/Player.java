/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.engine.Timer;
import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.Explosion;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.decor.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Player extends Character {

    private final float INVINCIBILITY_DURATION = 2f;
    private boolean hasCreateBomb = false;
    private int keys;
    private int bombsRange;
    private int bombCapacity;
    private int deployedBomb = 0;
    private Timer invincibilityTimer = new Timer(0, 0);
    private List<Bomb> deployedBombs = new ArrayList<>();
    private List<Explosion> myExplosions = new ArrayList<>();
    private boolean winner;

    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = game.getInitPlayerLives();
        this.keys = 1;
        this.bombsRange = 3;
        this.bombCapacity = 3;
    }



    public boolean requestOpenDoor() {
        if(keys > 0) {
            Position nextPos = direction.nextPosition(getPosition());
            Decor dec = game.getCurrentWorld().get(nextPos);
            if(dec instanceof Door) {
                if(((Door) dec).open()) {
                    keys -= 1;
                    return true;
                }
            }
        }
        return false;
    }

    public void requestSpawnBomb() {
        if(deployedBomb < bombCapacity) {
            for (Bomb bomb : deployedBombs) {
                if(bomb.getPosition().equals(getPosition())) {
                    return;
                }
            }
            deployedBomb += 1;
        }
    }

    public void takeDamage(int amount, long now) {
        if(invincibilityTimer.getTimeLeft() == 0) {
            lives = Math.max(lives - amount, 0);
            invincibilityTimer = new Timer(now, INVINCIBILITY_DURATION);
            invincibilityTimer.start();
        }
    }

    public void addExplosion(Explosion explosion) {
        myExplosions.add(explosion);
    }

    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
                hasMove = true;
            }
        }
        moveRequested = false;

        if(lives == 0) {
            alive = false;
        }
        invincibilityTimer.update(now);
        updateDeployedBombs(now);
        updateMyExplosions(now);
    }

    private void updateDeployedBombs(long now) {
        if(deployedBomb > deployedBombs.size()) {
            hasCreateBomb = true;
            deployedBombs.add(new Bomb(game, getPosition(), now, bombsRange, game.getCurrentLevel()));
        }
        List<Bomb> tmpBomb = new ArrayList<>();
        for(Bomb bomb : deployedBombs) {
            bomb.update(now);
            if(!bomb.getHasExploded()) {
                tmpBomb.add(bomb);
            }
        }
        deployedBomb +=  tmpBomb.size() - deployedBombs.size();
        deployedBombs = tmpBomb;
    }

    private void updateMyExplosions(long now) {
        List<Explosion> tmp = new ArrayList<>();
        for(Explosion explosion : myExplosions) {
            tmp.add(explosion);
        }
        for(Explosion explosion : tmp) {
            explosion.update(now);
            if(explosion.explosionEnded()) {
                myExplosions.remove(explosion);
            }
        }
    }

    public Bomb getBomb(Position position) {
        for (Bomb b : deployedBombs) {
            if(b.getPosition().equals(position)) {
                return b;
            }
        }
        return null;
    }

    public Bomb getLastBomb() {
        return deployedBombs.get(deployedBombs.size() - 1);
    }

    public void resetHasCreateBomb() { hasCreateBomb = false; }

    public boolean isWinner() {
        return winner;
    }

    public boolean getHasCreateBomb() { return hasCreateBomb; }

    public int getKeys() { return keys; }

    public int getBombsRange() { return bombsRange; }

    public int getBombCapacity() { return bombCapacity; }

    public Iterator<Bomb> getDeployedBombs() {
        return deployedBombs.iterator();
    }

    public Iterator<Explosion> getPlayerExplosions() {
        return myExplosions.iterator();
    }

    public boolean isInvincible() { return invincibilityTimer.getTimeLeft() > 0; }
}
