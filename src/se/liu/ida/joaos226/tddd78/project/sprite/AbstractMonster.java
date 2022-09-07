package se.liu.ida.joaos226.tddd78.project.sprite;


import se.liu.ida.joaos226.tddd78.project.game_logic.Direction;
import se.liu.ida.joaos226.tddd78.project.map_objects.ImageResource;
import se.liu.ida.joaos226.tddd78.project.map_objects.World;

/**
 * superclass for Zombie and JumperMonster that defines default fields and methods for all monsters. E.g delay between hits of
 * TileWalls and pushback when walking into them and checking whether any monster has won.
 */

public abstract class AbstractMonster extends AbstractSprite
{
    private final static double INIT_PUSHBACK_COEFFICIENT = 0.05;
    private double pushBackCoefficient = INIT_PUSHBACK_COEFFICIENT;
    private boolean pushBack = false;
    private long hitDelay = System.currentTimeMillis();

    protected AbstractMonster(final int x, final int y, final int width, final int height) {
	super(x, y, width, height);
	setDirectionTracker(Direction.LEFT); //Make monsters face the left direction when they start
    }

    /**
     * if the monster reaches the TreasureTile -> victory is set to true and listener is notified to display victoryMessage
     * @param world contains information about other sprites and map entities
     */
    public void checkVictory(World world) {
        ImageResource treasure = world.getMap().getTreasure();
	if((getX() < treasure.getWidth() && getY() < treasure.getY()) || isVictory()){
	    setVictory(true);
	    getListener().victoryMessage(world);
	}
    }

    public boolean isPushBack() {
	return pushBack;
    }

    public double getPushBackCoefficient() {
	return pushBackCoefficient;
    }

    public void setPushBackCoefficient(final double pushBackCoefficient) {
	this.pushBackCoefficient = pushBackCoefficient;
    }

    public static double getInitPushbackCoefficient() {
	return INIT_PUSHBACK_COEFFICIENT;
    }

    public void setPushBack(final boolean pushBack) {
	this.pushBack = pushBack;
    }

    public long getHitDelay() {
	return hitDelay;
    }

    public void setHitDelay(final long hitDelay) {
	this.hitDelay = hitDelay;
    }
}
