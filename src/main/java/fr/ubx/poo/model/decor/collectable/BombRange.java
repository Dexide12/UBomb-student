package fr.ubx.poo.model.decor.collectable;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.model.decor.Decor;

public class BombRange extends Collectable {

    private int value;

    public BombRange(int value) {
        this.collected = false;
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    @Override
    public boolean canGoOnMe(Direction direction) {
        return true;
    }

    @Override
    public String toString() {
        return "BombRange"+ (value < 0 ? "Dec" : "inc");
    }

}
