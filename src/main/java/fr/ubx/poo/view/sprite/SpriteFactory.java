/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.sprite;

import static fr.ubx.poo.view.image.ImageResource.*;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.decor.collectable.*;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.Explosion;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;


public final class SpriteFactory {

    public static Sprite createDecor(Pane layer, Position position, Decor decor) {
        ImageFactory factory = ImageFactory.getInstance();
        if (decor instanceof Stone)
            return new SpriteDecor(layer, factory.get(STONE), position);
        if (decor instanceof Tree)
            return new SpriteDecor(layer, factory.get(TREE), position);
        if (decor instanceof Box)
            return new SpriteDecor(layer, factory.get(BOX), position);
        if (decor instanceof Door) {
            Door door = (Door) decor;
            if (door.getIsOpen())
                return new SpriteDecor(layer, factory.get(DOOR_OPENED), position);
            else
                return new SpriteDecor(layer, factory.get(DOOR_CLOSED), position);
        }
        if(decor instanceof Heart){
            return new SpriteDecor(layer, factory.get(HEART), position);
        }
        if(decor instanceof RockPile) {
            if(decor.getResistance() == 2) {
                return new SpriteDecor(layer, factory.get(ROCK_PILE_2), position);
            } else if(decor.getResistance() == 1) {
                return new SpriteDecor(layer, factory.get(ROCK_PILE_1), position);
            }
        }
        if(decor instanceof Monster) {
            Monster monster = (Monster) decor;
            return new SpriteMonster(layer, factory.get(MONSTER_DOWN), position, monster);
        }
        if(decor instanceof Key){
            return new SpriteDecor(layer, factory.get(KEY), position);
        }
        if(decor instanceof BombNumber){
            if (((BombNumber) decor).getValue() < 0){
                return new SpriteDecor(layer, factory.get(BOMBNUMBERDEC), position);
            }else{
                return new SpriteDecor(layer, factory.get(BOMBNUMBERINC), position);
            }
        }
        if(decor instanceof BombRange){
            if (((BombRange) decor).getValue() < 0){
                return new SpriteDecor(layer, factory.get(BOMBRANGEDEC),position );
            }else{
                return new SpriteDecor(layer, factory.get(BOMBRANGEINC), position);
            }
        }
        if(decor instanceof Princess){
            return new SpriteDecor(layer, factory.get(PRINCESS), position);
        }
        return null;
    }

    public static Sprite createPlayer(Pane layer, Player player) {
        return new SpritePlayer(layer, player);
    }

    public static Sprite createBomb(Pane layer, Bomb bomb) {
        return new SpriteBomb(layer, bomb);
    }

    public static Sprite createExplosion(Pane layer, Explosion explosion) {
        return new SpriteExplosion(layer, explosion);
    }
}
