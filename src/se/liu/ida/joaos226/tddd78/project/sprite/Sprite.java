package se.liu.ida.joaos226.tddd78.project.sprite;

import se.liu.ida.joaos226.tddd78.project.game_logic.Direction;
import se.liu.ida.joaos226.tddd78.project.map_objects.ImageResource;
import se.liu.ida.joaos226.tddd78.project.game_logic.VictoryListener;
import se.liu.ida.joaos226.tddd78.project.map_objects.World;

import java.awt.*;


/**
 * interface that defines default methods for the sprites (Hero, Zombie and JumperMonster)
 */

public interface Sprite {
    public Rectangle getBounds();
    public double getX();
    public double getY();
    public void setX(double newX);
    public void setY(double newY);
    public int getHeight();
    public int getWidth();
    public int getLives();
    public Image getImage(Direction direction);
    public void setLives(int lives);
    public void checkGeneralCollisions(World world);
    public boolean isVictory();
    public void checkVictory(World world);
    public void setListener(VictoryListener vl);
    public void spriteSpecificCollision(World world);
    public void setVictory(boolean v);
    public void spriteGeneralUpdate(World world);
    public void spriteSpecificUpdate(World world);
    public Direction getDirectionTracker();
    public ImageResource getHeart();
}
