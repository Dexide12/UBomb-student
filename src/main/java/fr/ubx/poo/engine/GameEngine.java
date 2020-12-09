/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.engine;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.game.PositionNotFoundException;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.Door;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.Explosion;
import fr.ubx.poo.view.image.ImageFactory;
import fr.ubx.poo.view.sprite.*;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.go.character.Player;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;


public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final String windowTitle;
    private final Game game;
    private final Player player;
    private final List<Sprite> sprites = new ArrayList<>();
    private StatusBar statusBar;
    private Pane layer;
    private Input input;
    private Stage stage;
    private Sprite spritePlayer;
    private List<Sprite> bombsSprites = new ArrayList<>();
    private List<Sprite> explosionsSprites = new ArrayList<>();

    public GameEngine(final String windowTitle, Game game, final Stage stage) {
        this.windowTitle = windowTitle;
        this.game = game;
        this.player = game.getPlayer();
        initialize(stage, game);
        buildAndSetGameLoop();
    }

    private void initialize(Stage stage, Game game) {
        this.stage = stage;
        Group root = new Group();
        layer = new Pane();

        int height = game.getCurrentWorld().dimension.height;
        int width = game.getCurrentWorld().dimension.width;
        int sceneWidth = width * Sprite.size;
        int sceneHeight = height * Sprite.size;
        Scene scene = new Scene(root, sceneWidth, sceneHeight + StatusBar.height);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        input = new Input(scene);
        root.getChildren().add(layer);
        statusBar = new StatusBar(root, sceneWidth, sceneHeight, game);
        // Create decor sprites
        sprites.forEach((s) -> s.remove());
        sprites.clear();
        for(Sprite s : bombsSprites) {
            ((SpriteBomb)s).levelHasChanged();
        }
        bombsSprites.clear();
        explosionsSprites.clear();
        createMissingBombSprites();
        createMissingExplosionSprites();
        game.getCurrentWorld().forEach( (pos, d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));
        spritePlayer = SpriteFactory.createPlayer(layer, player);
    }

    protected final void buildAndSetGameLoop() {
        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                // Check keyboard actions
                processInput(now);

                // Do actions
                update(now);

                // Graphic update
                render();
                statusBar.update(game);
            }
        };
    }

    private void processInput(long now) {
        if (input.isExit()) {
            gameLoop.stop();
            Platform.exit();
            System.exit(0);
        }
        if (input.isMoveDown()) {
            player.requestMove(Direction.S);
        }
        if (input.isMoveLeft()) {
            player.requestMove(Direction.W);
        }
        if (input.isMoveRight()) {
            player.requestMove(Direction.E);
        }
        if (input.isMoveUp()) {
            player.requestMove(Direction.N);
        }
        if(input.isKey()) {
            if (player.requestOpenDoor()) {
                updateSprite(player.getDirection().nextPosition(player.getPosition()));
            }
        }
        if(input.isBomb()) {
            player.requestSpawnBomb();
        }
        input.clear();
    }

    private void showMessage(String msg, Color color) {
        Text waitingForKey = new Text(msg);
        waitingForKey.setTextAlignment(TextAlignment.CENTER);
        waitingForKey.setFont(new Font(60));
        waitingForKey.setFill(color);
        StackPane root = new StackPane();
        root.getChildren().add(waitingForKey);
        Scene scene = new Scene(root, 400, 200, Color.WHITE);
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        input = new Input(scene);
        stage.show();
        new AnimationTimer() {
            public void handle(long now) {
                processInput(now);
            }
        }.start();
    }


    private void update(long now) {
        updatePlayer(now);
        updateLevel();
        updateDecorSprites();
        updateBombsSprites();
        updateExplosionsSprites();
    }

    private void updateDecorSprites(){
        sprites.forEach((s) -> s.remove());
        sprites.clear();
        BiConsumer<Position, Decor> action = (pos, d) -> {
          if(d.getResistance() > 0) {
              sprites.add(SpriteFactory.createDecor(layer, pos, d));
          }
        };
        game.getCurrentWorld().forEach(action);
    }

    private void updatePlayer(long now) {
        player.update(now);

        if (!player.isAlive()) {
            gameLoop.stop();
            showMessage("Perdu!", Color.RED);
        }
        if (player.isWinner()) {
            gameLoop.stop();
            showMessage("Gagné", Color.BLUE);
        }
    }

    private void updateLevel() {
        if(player.getHasMove()) {
            Decor d = game.getCurrentWorld().get(player.getPosition());
            if(d instanceof Door) {
                Door door = (Door) d;
                if (door.getIsOpen()) {
                    if(door.getLeadToNext()) {
                        game.setCurrentLevel(true);
                        createAndDisplayScene(true);
                    } else {
                        game.setCurrentLevel(false);
                        createAndDisplayScene(false);
                    }
                }
            }
        }
        player.resetHasMove();
    }

    private void updateBombsSprites() {
        //Create the sprite of the bomb created by the player this turn
        if(player.getHasCreateBomb()) {
            bombsSprites.add(SpriteFactory.createBomb(layer, player.getLastBomb()));
            player.resetHasCreateBomb();
        }
        //Delete Useless Bomb Sprite
        List<Sprite> bombSpritesTmp = new ArrayList<>();
        for(Sprite bombSprite : bombsSprites) {
            if(!((SpriteBomb)bombSprite).canBeRemoved()) {
                bombSpritesTmp.add(bombSprite);
            } else {
                bombSprite.remove();
            }
        }
        bombsSprites = bombSpritesTmp;
    }

    private void createMissingBombSprites() {
        for (Iterator<Bomb> it = player.getDeployedBombs(); it.hasNext(); ) {
            Bomb bomb = it.next();
            boolean found = false;
            for(Sprite bombSprite : bombsSprites) {
                if (bombSprite.getPosition().equals(bomb.getPosition())) {
                    if(((SpriteBomb)bombSprite).getLevel() == game.getCurrentLevel()) {
                        found = true;
                    }
                }
            }
            if(!found && bomb.getLevel() == game.getCurrentLevel()) {
                bombsSprites.add(SpriteFactory.createBomb(layer, bomb));
            }
        }
    }

    private void createMissingExplosionSprites() {
        for(Iterator<Explosion> it = player.getPlayerExplosions(); it.hasNext();) {
            Explosion explosion = it.next();
            boolean found = false;
            for(Sprite explosionSprite : explosionsSprites) {
                if (explosionSprite.getPosition().equals(explosion.getPosition())) {
                    if(((SpriteExplosion)explosionSprite).getLevel() == game.getCurrentLevel()) {
                        found = true;
                    }
                }
            }
            if(!found && explosion.getLevel() == game.getCurrentLevel()) {
                explosionsSprites.add(SpriteFactory.createExplosion(layer, explosion));
            }
        }
    }

    private void updateExplosionsSprites() {
        createMissingExplosionSprites();
        //Delete Useless Explosion Sprite
        List<Sprite> explosionSpritesTmp = new ArrayList<>();
        for(Sprite explosion : explosionsSprites) {
            if(!((SpriteExplosion)explosion).hasEnded()) {
                explosionSpritesTmp.add(explosion);
            } else {
                explosion.remove();
            }
        }
        explosionsSprites = explosionSpritesTmp;
    }

    private void createAndDisplayScene(boolean next) {
        initialize(stage, game);
        try {
            player.setPosition(game.getCurrentWorld().getDoorPosition(next));
            player.setDirection(Direction.S);
        } catch (PositionNotFoundException e) {
            System.err.println("Error, Level does not contain an entry door");
        }
    }

    private void render() {
        sprites.forEach(Sprite::render);
        bombsSprites.forEach(Sprite::render);
        explosionsSprites.forEach(Sprite::render);
        // last rendering to have player in the foreground
        if(player.isInvincible()) {
            if(!((SpritePlayer)spritePlayer).getDisplay()) {
                spritePlayer.remove();
            } else {
                spritePlayer.render();
            }
            ((SpritePlayer)spritePlayer).changeDisplay();
        } else {
            spritePlayer.render();
        }
    }

    private void updateSprite(Position spritePosition) {
        sprites.remove(sprites.stream().filter(e->e.getPosition().equals(spritePosition)).findFirst().get());
        sprites.add(SpriteFactory.createDecor(layer, spritePosition, game.getCurrentWorld().get(spritePosition)));
    }

    public void start() {
        gameLoop.start();
    }
}
