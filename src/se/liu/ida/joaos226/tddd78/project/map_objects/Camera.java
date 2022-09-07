package se.liu.ida.joaos226.tddd78.project.map_objects;


import se.liu.ida.joaos226.tddd78.project.game_logic.GameViewer;
import se.liu.ida.joaos226.tddd78.project.sprite.Hero;

/**
 *  makes sure that the Hero is always visible on screen. When player moves past center of screen the camera will snap on him
 *  and follow until the player reaches end of map.
 */

public class Camera  {
    private double x = 0;
    private double y = 0;
    private int minX = 0;
    private int maxX = 0;

    public Camera(double x, double y, int mapWidth) {
	this.x = x;
	this.y = y;
	maxX = mapWidth;
    }

    /**
     * update cameras x value according to Hero x value
     * @param hero sprite controlled by player
     */
    public void update(Hero hero) {
        if (hero.getX() < GameViewer.SCREEN_WIDTH / 2) {
            x = minX;
	}
        else if (!(hero.getX() > maxX - GameViewer.SCREEN_WIDTH/2)) {
	    x = -hero.getX() + GameViewer.SCREEN_WIDTH / 2;
	}
    }

    public double getX() {
	return x;
    }

    public double getY() {
	return y;
    }

    public void setX(final double x) {
	this.x = x;
    }

    public void setY(final double y) {
	this.y = y;
    }
}
