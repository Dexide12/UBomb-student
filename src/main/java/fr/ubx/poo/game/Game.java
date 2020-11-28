/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;


import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Properties;

import fr.ubx.poo.model.go.character.Player;

public class Game {

    private final ArrayList<World> worlds;
    private final Player player;
    private final String worldPath;
    private String levelFilePrefix;
    private int initPlayerLives;
    private int nbLevels;
    private int currentLevel;

    public Game(String worldPath) {
        worlds = new ArrayList<>();
        worldPath = URLDecoder.decode(worldPath, StandardCharsets.UTF_8);
        this.worldPath = worldPath;
        loadConfig(worldPath);
        //worlds.add(new WorldStatic());
        loadLevels(worldPath);
        currentLevel = 0;
        Position positionPlayer = null;
        try {
            positionPlayer = worlds.get(currentLevel).findPlayer();
            player = new Player(this, positionPlayer);
        } catch (PositionNotFoundException e) {
            System.err.println("Position not found : " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    public int getInitPlayerLives() {
        return initPlayerLives;
    }

    private void loadConfig(String path) {
        try (InputStream input = new FileInputStream(new File(path, "config.properties"))) {
            Properties prop = new Properties();
            // load the configuration file
            prop.load(input);
            initPlayerLives = Integer.parseInt(prop.getProperty("lives", "3"));
            nbLevels = Integer.parseInt(prop.getProperty("levels", "1"));
            levelFilePrefix = prop.getProperty("prefix", "level");
        } catch (IOException ex) {
            System.err.println("Error loading configuration");
        }
    }

    private void loadLevels(String path) {
        for(int i = 1; i <= nbLevels; i++) {
            ArrayList<String> fileLines = new ArrayList<>();
            String currentFileName = levelFilePrefix + i + ".txt";
            try {
                BufferedReader reader = new BufferedReader(new FileReader(new File(path, currentFileName)));
                String line;
                int lineSize = 0;
                while((line = reader.readLine()) != null) {
                    if(lineSize == 0) {
                        lineSize = line.length();
                    } else if(lineSize != line.length()) {
                        throw new LevelFileFormatException("file " + currentFileName + " has different line size");
                    }
                    fileLines.add(line);
                }
                WorldEntity[][] level = new WorldEntity[fileLines.size()][lineSize];
                for(int l = 0; l < fileLines.size(); l++) {
                    char[] currentLine = fileLines.get(l).toCharArray();
                    for(int c = 0; c < currentLine.length; c++) {
                        Optional<WorldEntity> value = WorldEntity.fromCode(currentLine[c]);
                        if (!value.isPresent()) {
                            throw new LevelFileFormatException("file " + currentFileName + " contains " + currentLine[c] + " which is not a valid character");
                        }
                        level[l][c] = value.get();
                    }
                }
                worlds.add(new World(level));
            } catch (IOException ex) {
                System.err.println("Error during the load of file " + currentFileName);
            } catch (LevelFileFormatException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    public World getCurrentWorld() {
        return worlds.get(currentLevel);
    }

    public void setCurrentLevel(boolean isNext) {
        currentLevel += (isNext)? 1 : -1;
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }
}
