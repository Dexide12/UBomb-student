package fr.ubx.poo.model.decor;

import fr.ubx.poo.engine.Timer;
import fr.ubx.poo.game.*;
import fr.ubx.poo.model.Movable;

public class Monster extends Decor implements Movable {

    private static final int TIMER_DURATION = 2;
    private Position position;
    private World world;
    private Direction direction;
    private Timer timer;

    public Monster(World world, Position position) {
        this.position = position;
        this.world = world;
        this.direction = Direction.S;
    }

    public void update(long now) {
        if(timer != null) {
            timer.update(now);
            if (!timer.getIsRunning()) {
                Direction d = Direction.random();
                if(canMove(d))
                    doMove(d);
                this.timer = new Timer(now, TIMER_DURATION);
                timer.start();
            }
        }else{
            this.timer = new Timer(now, TIMER_DURATION);
            timer.start();
        }
    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        if (!world.isInside(nextPos)){
            return false;
        }
        if (world.get(nextPos) == null)
            return true;
        return false;
    }

    @Override
    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        this.direction = direction;
        world.clear(getPosition());
        setPosition(nextPos);
        world.set(nextPos,this);
    }

    @Override
    public boolean canGoOnMe(Direction direction) {
        return true;
    }

    public Direction getDirection() {return direction;}

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Monster";
    }

    @Override
    public boolean canExplode() { return true; }

    @Override
    public boolean makeDamage(){
        return true;
    }
}
