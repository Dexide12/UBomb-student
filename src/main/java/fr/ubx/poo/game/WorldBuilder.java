package fr.ubx.poo.game;


import fr.ubx.poo.model.Entity;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.decor.collectable.BombNumber;
import fr.ubx.poo.model.decor.collectable.BombRange;
import fr.ubx.poo.model.decor.collectable.Key;
import fr.ubx.poo.model.decor.collectable.Princess;

import java.util.Hashtable;
import java.util.Map;

public class WorldBuilder {
    private final Map<Position, Decor> grid = new Hashtable<>();

    private WorldBuilder() {
    }

    public static Map<Position, Decor> build(WorldEntity[][] raw, Dimension dimension, World world) {
        WorldBuilder builder = new WorldBuilder();
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                Position pos = new Position(x, y);
                Decor decor = (Decor) processEntity(raw[y][x], pos , world);
                if (decor != null)
                    builder.grid.put(pos, decor);
            }
        }
        return builder.grid;
    }

    private static Entity processEntity(WorldEntity entity, Position pos, World world) {
        switch (entity) {
            case Stone:
                return new Stone();
            case Tree:
                return new Tree();
            case Box:
                return new Box(pos,world);
            case DoorNextClosed:
                return new Door(false, true);
            case DoorNextOpened:
                return new Door(true, true);
            case DoorPrevOpened:
                return new Door(true, false);
            case DamagedRockPile:
                return new RockPile(1);
            case RockPile:
                return new RockPile(2);
            case Monster:
                return new Monster(world,pos);
            case Key:
                return new Key();
            case Princess:
                return new Princess();
            case BombRangeDec:
                return new BombRange(-1);
            case BombRangeInc:
                return new BombRange(1);
            case BombNumberDec:
                return new BombNumber(-1);
            case BombNumberInc:
                return new BombNumber(1);
            default:
                return null;
        }
    }
}
