package se.liu.ida.joaos226.tddd78.project.game_logic;

import se.liu.ida.joaos226.tddd78.project.sprite.JumperMonster;
import se.liu.ida.joaos226.tddd78.project.sprite.Sprite;
import se.liu.ida.joaos226.tddd78.project.sprite.Zombie;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * creates levels with different amout of monsters on each level and it contains information about how many extra bullets and
 * blocks the Hero will get on each new level
 */

public class Level  {
    private List<Sprite> monster = new ArrayList<>();
    private int heroBullets = 0;
    private int heroBlocks = 0;
    private final Random rnd = new Random();

    public Level(int numberOfMonsters, int extraBullets, int extraBlocks) {
        this.heroBullets = extraBullets;
        this.heroBlocks = extraBlocks;
        initLevel(numberOfMonsters);
    }

    /**
     * spawn monsters on a random location 1000 pixels away from Hero spawn location
     * @param numberOfMonsters number of Zombies and JumperMonsters created into World
     */
    private void initLevel(int numberOfMonsters){
	final int minX = 1000;
	final int boundX = 500;
	final int spawnHeight = 200;

	for (int i = 0; i < numberOfMonsters; i++) {
	    int randomX = rnd.nextInt(boundX) + minX;
	    monster.add(new Zombie(randomX, spawnHeight));
	    randomX = rnd.nextInt(boundX) + minX;
	    monster.add(new JumperMonster(randomX, spawnHeight));
	}
    }

    public int getHeroBullets() {
	return heroBullets;
    }

    public int getHeroBlocks() {
	return heroBlocks;
    }

    public List<Sprite> getMonster() {
	return monster;
    }
}
