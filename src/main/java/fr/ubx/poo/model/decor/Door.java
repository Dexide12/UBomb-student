

package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Direction;

public class Door extends Decor {
    private boolean isOpen;
    private boolean leadToNext;

    public Door(boolean isOpen, boolean leadToNext) {
        super();
        this.isOpen = isOpen;
        this.leadToNext = leadToNext;
    }

    @Override
    public String toString() {
        return "Door";
    }

    public boolean getIsOpen() { return isOpen; }

    public boolean getLeadToNext() { return leadToNext; }

    @Override
    public boolean canGoOnMe(Direction direction) {
        return isOpen;
    }

    public boolean open() {
        if(!isOpen) {
            isOpen = true;
            return true;
        }
        return false;
    }

    @Override
    public void takeDamage(int value) {}
}
