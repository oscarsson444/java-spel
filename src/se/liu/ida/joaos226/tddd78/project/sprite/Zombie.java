package se.liu.ida.joaos226.tddd78.project.sprite;

import se.liu.ida.joaos226.tddd78.project.player_abilities.TileWall;
import se.liu.ida.joaos226.tddd78.project.game_logic.Direction;
import se.liu.ida.joaos226.tddd78.project.map_objects.World;

import java.awt.*;
/**
 * Zombie class defines a new monster that can only walk slowly and destroy protection blocks. It has its own specific collision
 * and update method that extends the one from AbstractSprite.
 */

public class Zombie extends AbstractMonster
{
    private static final int ZOMBIE_WIDTH = 64;
    private static final int ZOMBIE_HEIGHT = 68;

    public Zombie(int x, int y){
	super(x, y, ZOMBIE_WIDTH, ZOMBIE_HEIGHT);
	initMonster();
    }

    private void initMonster() {
        final int lives = 2;
        loadSprite("zombie");
        setMoveLeft(true);
        setDirectionTracker(Direction.LEFT);
        setInAir(true);
        setMaxHorizontalSpeed(1);
        setLives(lives);
    }

    /**
     * method handles collisions specific for Zombie (it cant jump over BlockStacks and will always bump into them). Method is
     * called from World update method
     * @param world
     */
    public void spriteSpecificCollision(World world) {
        final int maxHitDelay = 100;
        //Collision between Zombie and BlockStacks
        for (TileWall bq:world.getMap().getBlockStacks()) {
            Rectangle leftBodyRect = bq.getLeftBodyRect();
            Rectangle rightBodyRect = bq.getRightBodyRect();
            Rectangle headRect = bq.getHeadRect();
            if(getBounds().intersects(rightBodyRect) && (System.currentTimeMillis() - getHitDelay()) >= maxHitDelay) {
                setPushBack(true);
                bq.pop();
                setHitDelay(System.currentTimeMillis());
                return;
            }
        }
    }

    /**
     * update method specific for Zombie (the pushback works a little different from JumperMonster class). Method is called from
     * World update method
     * @param world
     */
    public void spriteSpecificUpdate(final World world) {
        if (isPushBack()){
            setDx(2);
            if (getPushBackCoefficient() < 1) {
                final double dPushBack = 0.2;
                setX(getX() + getDx() * getPushBackCoefficient());
                setPushBackCoefficient(getPushBackCoefficient() + dPushBack);
            }
            else {
                setPushBack(false);
                setPushBackCoefficient(getInitPushbackCoefficient());
            }
        }
    }
}

