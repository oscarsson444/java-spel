package se.liu.ida.joaos226.tddd78.project.game_logic;

import se.liu.ida.joaos226.tddd78.project.map_objects.World;

/**
 * interface that makes it possible for LevelManager to get notified when victory (boolean) field of a sprite is set to true.
 */

public interface VictoryListener  {
    public void victoryMessage(World world);
}
