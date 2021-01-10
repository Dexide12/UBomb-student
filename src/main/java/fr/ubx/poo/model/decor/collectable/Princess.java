package fr.ubx.poo.model.decor.collectable;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.model.decor.Decor;

public class Princess extends Collectable {

    public Princess() {
        this.collected = false;
    }

    @Override
    public boolean canGoOnMe(Direction direction) {
        return true;
    }

    @Override
    public String toString() {
        return "Princess";
    }

}
