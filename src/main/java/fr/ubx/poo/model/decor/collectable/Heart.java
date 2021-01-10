package fr.ubx.poo.model.decor.collectable;

public class Heart extends Collectable{

    private int value;

    public Heart(int value) {
        this.collected = false;
        this.value = value;
    }

    public int getValue(){
        return value;
    }


    @Override
    public String toString() {
        return "Heart";
    }
}
