package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.GameObject;

public class Character extends GameObject implements Movable {


    protected boolean alive = true;
    protected Direction direction;
    protected boolean moveRequested = false;
    protected boolean hasMove = false;
    protected int lives = 1;

    public Character(Game game, Position position) {
        super(game, position);
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

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        if (!game.getCurrentWorld().isInside(nextPos)){
            return false;
        }
        Decor decor = game.getCurrentWorld().get(nextPos);
        if (decor == null){
            return true;
        }
        if(!decor.canGoOnMe(direction)){
            return false;
        }
        return true;
    }

    @Override
    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
    }

    public void takeDamage(int amount) {
        lives = Math.max(lives - amount, 0);
    }

    public void resetHasMove() {
        hasMove = false;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean getHasMove() { return hasMove; }
}
