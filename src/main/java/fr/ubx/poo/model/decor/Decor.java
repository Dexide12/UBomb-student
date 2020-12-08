/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.model.Entity;

/***
 * A decor is an element that does not know its own position in the grid.
 */
public class Decor extends Entity {
    private int resistance = 1;

    public boolean canGoOnMe(Direction direction){
        return false;
    }
    public boolean canExplode() { return false; }
    public int getResistance() { return  resistance; }
    public void takeDamage(int value) { resistance = (resistance < value)? 0: resistance - value; }
}
