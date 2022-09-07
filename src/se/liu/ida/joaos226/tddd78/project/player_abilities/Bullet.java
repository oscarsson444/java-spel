package se.liu.ida.joaos226.tddd78.project.player_abilities;


import se.liu.ida.joaos226.tddd78.project.game_logic.Direction;
import se.liu.ida.joaos226.tddd78.project.map_objects.ImageResource;
import se.liu.ida.joaos226.tddd78.project.sprite.Sprite;
import se.liu.ida.joaos226.tddd78.project.map_objects.World;

import java.awt.*;

/**
 * bullets are created when user click left mouse button and are then stored by Hero in a list of weapons. This class updates
 * the bullets position and checks if they collide with other entities in World. It also inherits from ImageResources to be
 * able to diplay image of a bullet.
 */

public class Bullet extends ImageResource implements Weapon
{
    private final static int BULLET_SPEED = 15;
    private double xVelocity = 0;
    private double yVelocity = 0;
    private int mouseX = 0;
    private int mouseY = 0;
    private double angle = 0;
    private static final int BULLET_RADIUS = 10;
    private boolean visible = false;

    public Bullet(int heroX, int heroY, int mouseX, int mouseY) {
        super(heroX, heroY, BULLET_RADIUS, BULLET_RADIUS);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.angle = Math.atan2(mouseY - getY(), mouseX - getX());
        loadOneDirectionalImg("bullet.png");
        visible = true;
    }

    /**
     * update Bullet position which depends on angle between mouse and Hero when Bullet was fired
     * @param world contains all sprites and map entities
     */
    public void update(World world) {
        xVelocity = BULLET_SPEED * Math.cos(angle);
        yVelocity = BULLET_SPEED * Math.sin(angle);
        setX(getX() + xVelocity);
        setY(getY() + yVelocity);
    }

    /**
     * method is called by Hero spriteSpecificUpdate and checks if Bullet hits some other object
     * @param world contains all sprites and map entities
     */
    public void checkCollision(World world) {
        Rectangle bulletRect = new Rectangle((int)getX(), (int)getY(), BULLET_RADIUS, BULLET_RADIUS);
        Rectangle groundBorderRect = world.getMap().getGroundBorderBounds();

        //Collision between Bullet and BlockTiles
        for (TileWall bq:world.getMap().getBlockStacks()) {
            Rectangle leftBodyRect = bq.getLeftBodyRect();
            Rectangle rightBodyRect = bq.getRightBodyRect();
            Rectangle headRect = bq.getHeadRect();
             if(bulletRect.intersects(leftBodyRect) ||bulletRect.intersects(rightBodyRect) || bulletRect.intersects(headRect)) {
                 visible = false;
             }
        }

        //Collision between Bullet and Sprite
        for (int i = 0; i < world.getSprites().size(); i++) {
            Sprite sp = world.getSprites().get(i);
            if (bulletRect.intersects(sp.getBounds()) && !sp.equals(world.getHero())){
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

        //When bullets are out of the map borders
        if (getX() >= world.getMap().getMapWidth() || getX() <= 0 || bulletRect.intersects(groundBorderRect)) {
            visible = false;
        }
    }

    /**
     * weapons can look different when Hero is facing different directions but the bullet does not and thats why it always
     * return same value
     * @return direction of bullet (0 if there is none)
     */
    public Direction getDirection() {
        return Direction.OMNI;
    }

    public int getBulletRadius() {
        return BULLET_RADIUS;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(final boolean visible) {
        this.visible = visible;
    }
}
