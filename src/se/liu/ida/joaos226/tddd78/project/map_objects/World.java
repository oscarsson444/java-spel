package se.liu.ida.joaos226.tddd78.project.map_objects;

import se.liu.ida.joaos226.tddd78.project.game_logic.Level;
import se.liu.ida.joaos226.tddd78.project.game_logic.LevelManager;
import se.liu.ida.joaos226.tddd78.project.sprite.Hero;
import se.liu.ida.joaos226.tddd78.project.sprite.Sprite;

import java.util.ArrayList;
import java.util.List;

/**
 * World class holds all the game objects (sprites, map, bullets, levels) and handles the overall gamelogic such as updating
 * the sprites position and checking their collisions. It also spawns sprites when a level is started.
 */

public class World  {
    private GameBoard map = null;
    private Hero hero = null;
    private Camera camera = null;
    private List<Sprite> sprites = new ArrayList<>();
    private LevelManager levelManager = null;
    private Level level = null;

    public World() {
        initWorld();
    }

    private void initWorld() {
        map = new GameBoard();
        hero = new Hero();
        sprites.add(hero);
        camera = new Camera(0,0, map.getMapWidth());

        levelManager = new LevelManager();
        level = levelManager.getLevel();
        sprites.addAll(level.getMonster());

        //sprites have levelManager as a listener so when victory == true levelManager knows when to display victoryMessage
        for (Sprite s: sprites) {
            s.setListener(levelManager);
        }
    }

    /**
     * reset sprites and map when player press "Retry" button
     */
    public void resetWorld() {
        hero.resetHero();
        levelManager.resetLevelManager();
        map.resetBlockStacks();
        sprites = new ArrayList<>();
        sprites.add(hero);
    }

    /**
     * loads all new monster sprites and add them to a list where all sprites are stored
     */
    public void startNewLevel() {
        level = levelManager.getLevel();
        sprites.addAll(level.getMonster());
        for (Sprite s: level.getMonster()) {
            s.setListener(levelManager);
        }
    }

    /**
     * update camera and all sprite:s positions and check if they are colliding with anything or if a monster reached the
     * treasure or if Hero has beat all levels
     */
    public void update() {
        //Make camera follow the player
        camera.update(hero);

        //Update monsters and heros positions and winning condition
        for (int i = 0; i < sprites.size() ; i++) {
            Sprite sp = sprites.get(i);
            sp.spriteGeneralUpdate(this);
            sp.spriteSpecificUpdate(this);
            sp.checkGeneralCollisions(this);
            sp.spriteSpecificCollision(this);
            sp.checkVictory(this);
        }
    }

    public GameBoard getMap() {
        return map;
    }

    public Camera getCamera() {
        return camera;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public List<Sprite> getSprites() {
        return sprites;
    }

    public Hero getHero() {
        return hero;
    }
}
