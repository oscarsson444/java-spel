package se.liu.ida.joaos226.tddd78.project.sprite;


import se.liu.ida.joaos226.tddd78.project.player_abilities.TileWall;
import se.liu.ida.joaos226.tddd78.project.game_logic.Direction;
import se.liu.ida.joaos226.tddd78.project.map_objects.World;

import java.awt.*;

/**
 * JumperMonster class defines a new monster that can jump over blocks and destroy them, it is also faster than the Zombie.
 * It has its own specific collision and update method that extends the one from AbstractSprite.
 */

public class JumperMonster extends AbstractMonster
{
    private final static int JUMPER_WIDTH = 32;
    private final static int JUMPER_HEIGHT = 36;

    public JumperMonster(int x, int y) {
	super(x, y, JUMPER_WIDTH, JUMPER_HEIGHT);
	initMonster();
    }

    private void initMonster() {
	final int lives = 1;
	loadSprite("jumper");
	setMaxHorizontalSpeed(3);
	setMoveLeft(true);
	setDirectionTracker(Direction.LEFT);
	setInAir(true);
	setLives(lives);
    }

    /**
     * method handles collisions specific for JumperMonster (when BlockStack is 1 block high it should jump otherwise bump into
     * it). Method is called from World update method
     * @param world containts all sprites and Map entities
     */
    public void spriteSpecificCollision(World world) {
        final int maxHitDelay = 100;
        //Collision between JumperMonster and BlockStacks
	for (TileWall bq:world.getMap().getBlockStacks()) {
	    Rectangle leftBodyRect = bq.getLeftBodyRect();
	    Rectangle rightBodyRect = bq.getRightBodyRect();
	    Rectangle headRect = bq.getHeadRect();
	    if(getBounds().intersects(rightBodyRect) && (System.currentTimeMillis() - getHitDelay()) >= maxHitDelay &&
	       bq.getBlockTiles().size() == 1 ) {
	        setJump(true);
	    }
	    else if (getBounds().intersects(rightBodyRect) && (System.currentTimeMillis() - getHitDelay()) >= maxHitDelay){
		setPushBack(true);
		bq.pop();
		setHitDelay(System.currentTimeMillis());
		return;
	    }
	}
    }

    /**
     * update method specific for JumperMonster (the pushback works a little different from Zombie class). Method is called from
     * World update method
     * @param world containts all sprites and Map entities
     */
    public void spriteSpecificUpdate(final World world) {
	if (isPushBack()){
	    setDx(3);
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
