package se.liu.ida.joaos226.tddd78.project.player_abilities;

import se.liu.ida.joaos226.tddd78.project.game_logic.Direction;
import se.liu.ida.joaos226.tddd78.project.map_objects.World;

import java.awt.*;

/**
 * interface defines default behaviour for all weapons (Bullet and Sword) which are the Hero's abilities
 */

public interface Weapon
{
    public void checkCollision(World world);
    public void setVisible(boolean visible);
    public boolean isVisible();
    public void update(World world);
    public Image getImage(Direction direction);
    public Direction getDirection();
    public double getX();
    public double getY();
}
