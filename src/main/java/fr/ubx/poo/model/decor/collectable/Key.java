package fr.ubx.poo.model.decor.collectable;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.model.decor.Decor;

public class Key extends Collectable {

    public Key() {
        this.collected = false;
    }

    @Override
    public String toString() {
        return "Key";
    }
}
