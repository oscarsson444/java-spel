package se.liu.ida.joaos226.tddd78.project.game_logic;

import se.liu.ida.joaos226.tddd78.project.map_objects.World;

/**
 * initiates a World object and a GameViewer object, then calls gameViewer show method which creates a screen that the game is
 * displayed on.
 */

public class GameStarter  {
    public static void main(String[] args) {
        World world = new World();
	GameViewer wv = new GameViewer(world);
	wv.show();
    }
}
