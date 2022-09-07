package se.liu.ida.joaos226.tddd78.project.map_objects;

import se.liu.ida.joaos226.tddd78.project.game_logic.GameViewer;
import se.liu.ida.joaos226.tddd78.project.player_abilities.TileWall;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GameBoard class initiates all object that are not sprites (ground, TileWall, background, treasure). It also pushes
 * new tiles to a tilewall when user right clicks on map.
 */

public class GameBoard
{
    private ImageResource[][] groundTiles = null;
    private List<TileWall> blockStacks = new ArrayList<>();
    private final static int MAP_WIDTH = 1840;
    private ImageResource treasure = null;
    private ImageResource background = null;

    public GameBoard() {
        makeGroundBorder();
        makeTreasure();
        makeBackground();
    }

    /**
     * creates a ground that all sprites and other objects can stand upon
     */
    private void makeGroundBorder() {
        final int rows = 3;
        final int groundImgWidth = 16;
        int columns = MAP_WIDTH / groundImgWidth;
        groundTiles = new ImageResource[rows][columns];
        createGround(rows, columns);
    }

    /**
     * fills the array groundTiles with ground images
     * @param rows number of rows to fill
     * @param columns number of columns to fill
     */
    private void createGround(int rows, int columns) {
        final int groundImgSize = 16;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                groundTiles[i][j] = new ImageResource(j * groundImgSize,
                                                      (GameViewer.SCREEN_HEIGHT - groundImgSize) - i * groundImgSize,
                                                      groundImgSize, groundImgSize);
                groundTiles[i][j].loadOneDirectionalImg("ground.png");
            }
        }
    }

    /**
     * creates a stack of BlockTiles on specified column and adds it to blockStackList or pushes to existing stack if there is
     * one. Gets called by GameViewer when user clicks right mouse button
     * @param x x-value of mouse click
     */
    public void makeBlockQueue(int x){
        final int blockTileLength = 32;
        int column = Math.floorDiv(x, blockTileLength);
        for (TileWall bt:blockStacks) {

            //If it already exists a stack for specified column push more blocks to same stack
            if (bt.getColumn() == column) {
                bt.push();
                return;
            }
        }

        //If it does not exist a stack for specified column -> add one to the list of stacks
        blockStacks.add(new TileWall(column, this)); //If no queue exists for specified column make a new queue
    }

    private void makeTreasure() {
        final int treasureSize = 32;
        final int treasureX = 0;
        final int treasureY = getGroundHeight() - treasureSize;
        treasure = new ImageResource(treasureX,treasureY, treasureSize, treasureSize);
        treasure.loadOneDirectionalImg("chest.png");
    }

    private void makeBackground() {
        final int backGroundX = 0;
        final int backGroundY = 0;
        final int backGroundWidth = 700;
        final int backGroundHeigth = 440;
        background = new ImageResource(backGroundX,backGroundY, backGroundWidth, backGroundHeigth);
        background.loadOneDirectionalImg("background_2.jpg");
    }

    /**
     * method creates a rectangle encapsulation the ground so that collision detection between ground and other objects works
     * @return rectangle object with same size as ground
     */
    public Rectangle getGroundBorderBounds() {
        int y = GameViewer.SCREEN_HEIGHT - groundTiles[0][0].getWidth() * getGroundRows();
        int height = getGroundRows() * groundTiles[0][0].getHeight();
        int width = getGroundColumns() * groundTiles[0][0].getWidth();
        return new Rectangle(0, y, width, height);
    }

    public ImageResource getBackground() {
        return background;
    }

    public ImageResource[][] getGroundTiles() {
        return groundTiles;
    }

    public ImageResource getTreasure() {
        return treasure;
    }

    public List<TileWall> getBlockStacks() {
        return blockStacks;
    }

    public void resetBlockStacks() {
        blockStacks = new ArrayList<>();
    }


    public int getGroundRows() {
        return groundTiles.length;
    }

    public int getGroundColumns() {
        return groundTiles[0].length;
    }

    public int getGroundHeight() {
        return GameViewer.SCREEN_HEIGHT - groundTiles[0][0].getHeight() * getGroundRows();
    }

    public int getMapWidth() {
        return MAP_WIDTH;
    }
}
