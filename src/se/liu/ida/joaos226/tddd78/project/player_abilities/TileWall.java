package se.liu.ida.joaos226.tddd78.project.player_abilities;

import se.liu.ida.joaos226.tddd78.project.map_objects.GameBoard;
import se.liu.ida.joaos226.tddd78.project.map_objects.ImageResource;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TileWall class makes it possible to stack block tiles ontop of eachother. It also makes it easier to check collsions with
 * blocks when they are stacked by making large collision rectangles instead of individual collsion rectangles for every
 * block in the stack.
 */

public class TileWall
{
    private List<ImageResource> blockTiles = new ArrayList<>();
    private int column = 0;
    private GameBoard map = null;
    private final static int RECT_THICKNESS = 5;
    private int health = 5;
    private final static int BLOCK_TILE_LENGTH = 32;

    public TileWall(int column, GameBoard map) {
        this.column = column;
        this.map = map;
        push();
    }

    /**
     * adds one blockTile to the blockTiles list
     */
    public void push(){
        ImageResource blockTile;
        if (blockTiles.isEmpty()) {
            int groundLevelY = map.getGroundHeight();
            blockTile = new ImageResource(column * BLOCK_TILE_LENGTH,groundLevelY - BLOCK_TILE_LENGTH,
                                          BLOCK_TILE_LENGTH, BLOCK_TILE_LENGTH);
	}
        else{
            ImageResource lastBlock = blockTiles.get(blockTiles.size() - 1);
            blockTile = new ImageResource(column * BLOCK_TILE_LENGTH, (int)lastBlock.getY() - BLOCK_TILE_LENGTH,
                                          BLOCK_TILE_LENGTH, BLOCK_TILE_LENGTH);
	}
        blockTile.loadOneDirectionalImg("block.png");
        blockTiles.add(blockTile);
    }

    /**
     * removes a blockTile from blockTiles list
     */
    public void pop() {
        blockTiles.remove(blockTiles.size() - 1);
    }

    public int getColumn() {
        return column;
    }

    public int getStackHeight() {
        return map.getGroundHeight() - blockTiles.size() * BLOCK_TILE_LENGTH;
    }

    /**
     * used to check collision between sprites and TileWalls. This method returns a rectangle shaped as a lid of the wall
     * @return rectangle located at top of the TileWall
     */
    public Rectangle getHeadRect() {
        int margin = 5;
        int width = BLOCK_TILE_LENGTH - 2*margin;
        int x = column * BLOCK_TILE_LENGTH + margin;
        int y = map.getGroundHeight() - blockTiles.size() * BLOCK_TILE_LENGTH;
        return new Rectangle(x, y, width, RECT_THICKNESS);
    }

    /**
     * used to check collision between sprites and TileWalls. This method returns a rectangle shaped as a wall of the wall
     * @return rectangle of the left side of TileWall
     */
    public Rectangle getLeftBodyRect() {
        int height = blockTiles.size() * BLOCK_TILE_LENGTH - RECT_THICKNESS;
        int x = column * BLOCK_TILE_LENGTH;
        int y = map.getGroundHeight() - height;
        return new Rectangle(x, y, RECT_THICKNESS, height);
    }

    /**
     * used to check collision between sprites and TileWalls. This method returns a rectangle shaped as a wall of the wall
     * @return rectangle of the right side of TileWall
     */
    public Rectangle getRightBodyRect() {
        int width = RECT_THICKNESS;
        int height = blockTiles.size() * BLOCK_TILE_LENGTH - RECT_THICKNESS;
        int x = (column+1) * BLOCK_TILE_LENGTH - width;
        int y = map.getGroundHeight() - height;
        return new Rectangle(x, y, width, height);
    }

    public int getBlockTileLength() {
        return BLOCK_TILE_LENGTH;
    }

    public List<ImageResource> getBlockTiles() {
        return blockTiles;
    }
}
