package fr.ubx.poo.view.sprite;

import fr.ubx.poo.model.go.Explosion;
import fr.ubx.poo.view.image.ImageFactory;
import fr.ubx.poo.view.image.ImageResource;
import javafx.scene.layout.Pane;

public class SpriteExplosion extends SpriteGameObject {

    public SpriteExplosion(Pane layer, Explosion explosion) {
        super(layer, null, explosion);
    }

    @Override
    public void updateImage() {
        setImage(ImageFactory.getInstance().get(ImageResource.EXPLOSION));
    }

    public boolean hasEnded() {
        return ((Explosion)go).explosionEnded();
    }

    public int getLevel() { return ((Explosion)go).getLevel(); }
}
