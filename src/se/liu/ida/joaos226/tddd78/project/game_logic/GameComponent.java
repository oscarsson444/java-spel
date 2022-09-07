package se.liu.ida.joaos226.tddd78.project.game_logic;


import se.liu.ida.joaos226.tddd78.project.player_abilities.TileWall;
import se.liu.ida.joaos226.tddd78.project.player_abilities.Weapon;
import se.liu.ida.joaos226.tddd78.project.map_objects.ImageResource;
import se.liu.ida.joaos226.tddd78.project.map_objects.World;
import se.liu.ida.joaos226.tddd78.project.sprite.Sprite;

import javax.swing.*;
import java.awt.*;

/**
 * component takes objects (sprites and map) from World and draws them on the screen using method paintComponent.It also updates
 * all the objects 60 times/s based on Timer object in GameViewer class
 */

public class GameComponent extends JPanel {
    private World world = null;
    private final static int FONT_SIZE = 20;
    private Font font = new Font("Symbol", Font.PLAIN, FONT_SIZE);

    public GameComponent(World world) {
	this.setPreferredSize(new Dimension(GameViewer.SCREEN_WIDTH, GameViewer.SCREEN_HEIGHT));
	this.world = world;
    }

    /**
     * paints all the game entities in World on screen 60 times/s. Everything inside the two g2D.translate method calls is
     * affected by Camera position and gets their x value changed according to the player movement (player x-value).
     * @param g graphics tool made to paint images, text, shapes etc
     */
    @Override protected void paintComponent(final Graphics g) {
	super.paintComponent(g);
	Graphics2D g2D = (Graphics2D) g;
	Rectangle groundRect = world.getMap().getGroundBorderBounds();

	g2D.translate(world.getCamera().getX(), world.getCamera().getY());

	drawBackground(g2D, world);
	drawStats(g2D, world);
	drawTreasure(g2D, world);
	drawSprites(g2D, world);
	drawGroundBorder(g2D, world);
	drawBlocks(g2D, world);
	drawWeapons(g2D, world);
	g2D.setColor(Color.BLACK);
	g2D.drawRect(groundRect.x,groundRect.y, groundRect.width,groundRect.height);

	g2D.translate(-world.getCamera().getX(), -world.getCamera().getY());
    }

    /**
     * called by timer in GameViewer 60 times/s and draws all World entities on the screen
     */
    public void draw() {
        this.repaint();
    }

    /**
     * called by timer in GameViewer 60 times/s and updates all World entities position and properties
     */
    public void update() {
        world.update();
    }

    private void drawStats(Graphics2D g2D, World world) {
        final int firstTextY = 20;
        final int secondTextY = 50;
        final int thridTextY = 80;
	g2D.setFont(font);
	g2D.setColor(Color.YELLOW);
	g2D.drawString("Bullets: " + world.getHero().getBulletAmount(), -(int)world.getCamera().getX() ,firstTextY);
	g2D.drawString("Blocks: " + world.getHero().getBlockAmount(), -(int)world.getCamera().getX(),secondTextY);
	g2D.drawString("Wave: " + world.getLevelManager().getLevelCount(), -(int)world.getCamera().getX(),thridTextY);
    }

    private void drawTreasure(Graphics2D g2D, World world) {
        ImageResource treasure = world.getMap().getTreasure();
        g2D.drawImage(treasure.getImage(Direction.OMNI), (int)treasure.getX(), (int)treasure.getY(), this);
    }

    private void drawBackground(Graphics2D g2D, World world) {
	ImageResource background = world.getMap().getBackground();
	for (int i = 0; i <= (int)Math.ceil(world.getMap().getMapWidth() / background.getWidth()); i++) {
	    g2D.drawImage(background.getImage(Direction.OMNI), i * background.getWidth(), (int)background.getY(),this);
	}
    }

    private void drawGroundBorder(Graphics2D g2D, World world) {
	for (int i = 0; i < world.getMap().getGroundRows(); i++) {
	    for (int j = 0; j < world.getMap().getGroundColumns(); j++) {
		ImageResource tile = world.getMap().getGroundTiles()[i][j];
		g2D.drawImage(tile.getImage(Direction.OMNI), (int)tile.getX(), (int)tile.getY(), this);
	    }
	}
    }

    /**
     * method draws the sprite and sprites hp on screen
     * @param g2D drawing tool
     * @param world container of sprite
     */
    private void drawSprites(Graphics2D g2D, World world) {
        final int heartDimension = 15;
        final int heartOffset = 10;
	for (Sprite sp: world.getSprites()) {
	    g2D.drawImage(sp.getImage(sp.getDirectionTracker()), (int)sp.getX(), (int)sp.getY(), this);
	    ImageResource heart = sp.getHeart();
	    //Drawns the sprite:s hp
	    for (int i = 0; i < sp.getLives(); i++) {
	        int hpBarLength = sp.getLives() * heartDimension;
	        double hpBarStartX = sp.getX() + sp.getWidth()/2;
	        double currentHeartX = (hpBarStartX-hpBarLength/2) + i * heartDimension;
		g2D.drawImage(heart.getImage(Direction.OMNI), (int)currentHeartX ,
			      (int)sp.getY() - heartOffset, this);
	    }
	}
    }

    /**
     * method draws to defence blocks that user can place with mouse right click
     * @param g2D drawing tool
     * @param world contains the defence blocks
     */
    private void drawBlocks(Graphics2D g2D, World world) {
	for (TileWall bq:world.getMap().getBlockStacks()) {
	    for (ImageResource bt:bq.getBlockTiles()) {
		g2D.drawImage(bt.getImage(Direction.OMNI), (int)bt.getX(), (int)bt.getY(), this);
	    }
	}
    }

    private void drawWeapons(Graphics2D g2D, World world) {
	for (Weapon w: world.getHero().getWeapons()) {
	    g2D.drawImage(w.getImage(w.getDirection()), (int)w.getX(), (int)w.getY(), this);
	}
    }
}



