package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Direction;

public class RockPile extends Decor {

    public RockPile(int resistance) {
        this.resistance = resistance;
    }

    @Override
    public boolean canExplode() { return true; }

    @Override
    public boolean canGoOnMe(Direction direction){
        if(resistance == 0) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Rock Pile";
    }
}
