/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.sprite;


import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.Monster;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class SpriteMonster extends SpriteDecor {
    private final ColorAdjust effect = new ColorAdjust();
    private Monster monster;

    public SpriteMonster(Pane layer, Image image, Position position, Monster monster) {
        super(layer, image, position);
        this.monster = monster;
        updateImage();
    }
    public Monster getMonster(){return monster;}
    @Override
    public void updateImage() {
        this.setPosition(monster.getPosition());
        setImage(ImageFactory.getInstance().getMonster(monster.getDirection()));
    }
}
