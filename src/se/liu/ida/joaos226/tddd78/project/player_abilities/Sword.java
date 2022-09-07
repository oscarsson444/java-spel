package se.liu.ida.joaos226.tddd78.project.player_abilities;

import se.liu.ida.joaos226.tddd78.project.game_logic.Direction;
import se.liu.ida.joaos226.tddd78.project.sprite.Hero;
import se.liu.ida.joaos226.tddd78.project.map_objects.ImageResource;
import se.liu.ida.joaos226.tddd78.project.sprite.Sprite;
import se.liu.ida.joaos226.tddd78.project.map_objects.World;

import java.awt.*;

/**
 * this class handles logic for the melee attack that the Hero can do. The sword is active 200 ms after SPACE is pressed and
 * it is created in GameViewer but then it cant be used for 500 ms again. It also inherits from ImageResources to be
 * able to diplay image of a sword.
 */

public class Sword extends ImageResource implements Weapon
{
    private final static int SWORD_WIDTH = 32;
    private final static int SWORD_HEIGHT = 15;
    private Direction direction = null;
    private long swordActivationTime = System.currentTimeMillis();
    private boolean visible = false;

    public Sword(final double x, final double y) {
	super(x, y, SWORD_WIDTH, SWORD_HEIGHT);
	loadBiDirectionalImg("sword");
    }

    /**
     * method updates Sword:s x/y values and direction based on Hero position and makes it disappear after 200 ms
     * @param world
     */
    public void update(World world) {
        Hero hero = world.getHero();
	final int swordActive = 200;
	direction = hero.getDirectionTracker();
	setY(hero.getY() + hero.getHeight()/2);

	//Decides the sword x value when facing different directions
	if(direction == Direction.RIGHT) {
	    setX(hero.getX() + hero.getWidth());
	}
	else{
	    setX(hero.getX() - SWORD_WIDTH);
	}

	//Sword gets removed from Hero:s weaponsList when it is not visible
	if (System.currentTimeMillis() - swordActivationTime >= swordActive){
	    visible = false;
	}
    }

    /**
     * checks if the Sword collides with monster sprite, if it does it takes one life from monster and disappears
     * @param world
     */
    public void checkCollision(final World world) {
	Rectangle swordRect = new Rectangle((int)getX(), (int)getY(), SWORD_WIDTH, SWORD_HEIGHT);
	for (int i = 0; i < world.getSprites().size(); i++) {
	    Sprite sp = world.getSprites().get(i);
	    if (swordRect.intersects(sp.getBounds()) && !sp.equals(world.getHero())){
		visible = false;
		sp.setLives(sp.getLives() - 1);
		if (sp.getLives() < 1) {
		    world.getSprites().remove(i);
		    if(world.getSprites().size() == 1){
			world.getLevelManager().afterWave(world.getHero());
		    }
		}
		break;
	    }
	}
    }

    public Direction getDirection() {
	return direction;
    }

    public void setVisible(final boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
	return visible;
    }
}
