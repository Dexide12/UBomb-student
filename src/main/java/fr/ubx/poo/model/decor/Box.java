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
        if (world.get(nextPos) instanceof Decor){
            return false;
        }
        return true;
    }

    @Override
    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
        world.clear(position);
        world.set(nextPos,this);
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