package fr.ubx.poo.model.decor.collectable;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.model.decor.Decor;

public abstract class  Collectable extends Decor {
    protected boolean collected;

    public boolean collected(){
        return collected;
    }
    public void collect(){
        this.collected = true;
        this.resistance = 0;
    }

    @Override
    public boolean canGoOnMe(Direction direction) {
        return true;
    }
}
