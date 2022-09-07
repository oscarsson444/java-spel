package se.liu.ida.joaos226.tddd78.project.game_logic;

import se.liu.ida.joaos226.tddd78.project.map_objects.World;
import se.liu.ida.joaos226.tddd78.project.sprite.Hero;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * this class holds all the levels in a list and handles the logic of presenting right level at right time. It also
 * gives messages when player lost/won and when waves of monsters are survived and refills Hero bullets and blocks.
 */

public class LevelManager implements VictoryListener
{
    private int levelCount = 0;
    private List<Level> levels = new ArrayList<>();

    public LevelManager(){
        loadLevels();
    }

    /**
     * First parameter = number of monsters
     * Second parameter = number of bullets given after wave is finished
     * Thrid parameter = number of blocks given after wave is finished
     *
     * This might not be according to DRY-principle but I think it makes it very easy to add more levels this way instead of
     * using a for-loop with lists containing the parameters.
     */
    private void loadLevels() {
        /*

         */
        Level level0 = new Level(0, 0, 0);
        levels.add(level0);
        Level level1 = new Level(4,10, 5);
        levels.add(level1);
        Level level2 = new Level(7, 10, 10);
        levels.add(level2);
        Level level3 = new Level(10, 15, 10); //ignore this warning, I explained the
        levels.add(level3);                                                             //meaning of it on line 26 and it would
    }                                                                                   //not clarify its obvious pupose to
                                                                                        //declare a varible for every value
    /**
     * method is called by World when player wants to retry
     */
    public void resetLevelManager() {
        levelCount = 0;
        levels = new ArrayList<>();
        loadLevels();
    }

    /**
     * after every wave of monster is cleared this method is called by Bullet class because Bullet checkCollision method checks
     * if all monsters are dead when a shot hits a monster. If wave is cleared the Hero get extra blocks and bullets and a
     * message is shown on the screen
     * @param hero sprite controlled by user
     */
    public void afterWave(Hero hero) {
        Level level = levels.get(levelCount);
        hero.setBlockAmount(hero.getBlockAmount() + level.getHeroBlocks());
        hero.setBulletAmount(hero.getBulletAmount() + level.getHeroBullets());
        if(levelCount < levels.size() - 1) {
            JOptionPane.showMessageDialog(null, "You survived wave " + levelCount);
        }
    }

    /**
     * method is called when a sprite won (victory == true) and it displays a message and asks if user wants to play another
     * round
     * @param world contains all sprites and map objects
     */
    public void victoryMessage(World world) {
        String msg;
        if (world.getHero().isVictory()) {
            msg = "You won!";
        }

        else{
            msg = "You lost!";
        }
        Object[] options = {
                "Retry",
                "Exit"
        };
        int chosenOption = JOptionPane.showOptionDialog(null,msg, "PLAY AGAIN",
                                                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                                                        options, options[1]);
        if (chosenOption == 1) {
            System.exit(0);
        }
        world.resetWorld();
    }

    public List<Level> getLevels() {
        return levels;
    }

    public int getLevelCount()
    {
        return levelCount;
    }

    public void setLevelCount(int lc) {
        levelCount = lc;
    }

    public void addLevelCounter() {
        levelCount ++;
    }

    public Level getLevel() {
        return levels.get(levelCount);
    }
}
