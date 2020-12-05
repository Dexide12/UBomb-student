/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.Explosion;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.decor.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Player extends GameObject implements Movable {

    private boolean alive = true;
    private Direction direction;
    private boolean moveRequested = false;
    private boolean hasMove = false;
    private boolean hasCreateBomb = false;
    private int lives = 1;
    private int keys;
    private int bombsRange;
    private int bombCapacity;
    private int deployedBomb = 0;
    private List<Bomb> deployedBombs = new ArrayList<>();
    private List<Explosion> myExplosions = new ArrayList<>();
    private boolean winner;

    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = game.getInitPlayerLives();
        this.keys = 1;
        this.bombsRange = 1;
        this.bombCapacity = 1;
    }

    public int getLives() {
        return lives;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) { this.direction = direction; }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
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

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        if (!game.getCurrentWorld().isInside(nextPos)){
            return false;
        }
        Decor decor = game.getCurrentWorld().get(nextPos);
        System.out.println("Il y a un(e) "+decor);
        if (decor instanceof Stone
            || decor instanceof Tree){
            return false;
        }else if (decor instanceof Box){
            if (((Box) decor).canMove(direction)){
                System.out.println("Cette boite peut bouger");
                ((Box) decor).doMove(direction);
                return true;
            }else{
                System.out.println("Cette boite ne peut pas bouger");
                return false;
            }
        }else {
            return true;
        }
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
    }

    public void takeDamage(int amount) {
        lives = Math.max(lives - amount, 0);
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

    public Bomb getLastBomb() {
        return deployedBombs.get(deployedBombs.size() - 1);
    }

    public void resetHasMove() {
        hasMove = false;
    }

    public void resetHasCreateBomb() { hasCreateBomb = false; }

    public boolean isWinner() {
        return winner;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean getHasMove() { return hasMove; }

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
}
