package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.*;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.decor.*;

public class Box extends Decor implements Movable {
    private Position position;
    private World world;

    public Box(Position position, World world) {
        this.position = position;
        this.world = world;
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
        world.clear(getPosition());
        setPosition(nextPos);
        world.set(nextPos,this);
    }

    @Override
    public boolean canGoOnMe(Direction direction) {
        boolean can = canMove(direction);
        if (can)
            doMove(direction);
        return can;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Box";
    }

}