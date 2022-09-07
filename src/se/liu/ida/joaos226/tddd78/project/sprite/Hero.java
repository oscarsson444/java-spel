package se.liu.ida.joaos226.tddd78.project.sprite;

import se.liu.ida.joaos226.tddd78.project.player_abilities.Sword;
import se.liu.ida.joaos226.tddd78.project.player_abilities.TileWall;
import se.liu.ida.joaos226.tddd78.project.player_abilities.Weapon;
import se.liu.ida.joaos226.tddd78.project.map_objects.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * this class represents the player controlled sprite and it contains methods that updates the sprites position and that checks
 * collisions specific for the Hero sprite. It also has a list of the weapons (Bullet and Sword) and calls the update method
 * of those objects
 */

public class Hero extends AbstractSprite
{
    private static final int HERO_WIDTH = 32;
    private static final int HERO_HEIGHT = 56;
    private final static int PROTECTION_TIME = 2000;

    private long hitProtection = System.currentTimeMillis();

    private List<Weapon> weapons = new ArrayList<>();
    private long bulletDelay = System.currentTimeMillis();
    private int bulletAmount = 0;
    private int blockAmount = 0;
    /**
     * Used so that sword can only be used every 500 ms
     */
    private long swordActivationTime = 0;

    public Hero(){
        super(0, 0, HERO_WIDTH, HERO_HEIGHT);
        initHero();
    }

    private void initHero(){
        final int livesNumber = 5;
        final int bullets = 5;
        final int blocks = 5;
        setInAir(true);
        setMaxHorizontalSpeed(6);
        setLives(livesNumber);
        bulletAmount = bullets;
        blockAmount = blocks;
        loadSprite("hero");
    }

    /**
     * reset Hero fields when user presses "Retry" button
     */
    public void resetHero() {
        final int spawnY = 200;
        final int lives = 5;
        final int bullets = 5;
        final int blocks = 5;
        setInAir(true);
        setX(0);
        setY(spawnY);
        setVictory(false);
        setLives(lives);
        bulletAmount = bullets;
        blockAmount = blocks;
    }

    /**
     * called by World:s update method and it checks wheter all monster waves are cleared. If they are it signals to
     * to run the listener:s victoryMessage method.
     * @param world
     */
    public void checkVictory(World world) {
        if (world.getLevelManager().getLevelCount() == world.getLevelManager().getLevels().size() - 1
            && world.getSprites().size() == 1){
            setVictory(true);
            getListener().victoryMessage(world);
        }
    }

    /**
     * iterates over the Weapons in weaponList and sees if any of them collides with other objects (monsters, ground etc) and
     * should be removed
     * @param world contains all sprites and map entities
     */
    public void spriteSpecificUpdate(final World world) {
        for (int i = 0; i < weapons.size(); i++) {
            if(weapons.get(i).isVisible()) {
                weapons.get(i).update(world);
                weapons.get(i).checkCollision(world);
            }
            else{
                weapons.remove(i);
            }
        }
    }

    /**
     * checks all collisions that apply to the Hero sprite specifically and run different code depending on object it
     * collided with
     * @param world contains all sprites and map entities (ground, BlockStacks)
     */
    public void spriteSpecificCollision(World world) {
        final double pushBack = 0.5;
        List<Sprite> spriteList = world.getSprites();
        Rectangle heroRect = getBounds();

        //Collision between Hero and other sprites
        for (Sprite s:spriteList) {
            if (heroRect.intersects(s.getBounds()) && !s.equals(this) &&
                (System.currentTimeMillis() - hitProtection) >= PROTECTION_TIME){
                setLives(getLives() - 1);
                hitProtection = System.currentTimeMillis();
                if(getLives() == 0) {
                    s.setVictory(true);
                }
            }
        }

        //Collision between Hero and BlockStacks
        for (TileWall bq:world.getMap().getBlockStacks()) {
            Rectangle leftBodyRect = bq.getLeftBodyRect();
            Rectangle rightBodyRect = bq.getRightBodyRect();
            Rectangle headRect = bq.getHeadRect();
            if(heroRect.intersects(leftBodyRect)){
                setX(getX() -pushBack);
                setDx(0);
            }
            else if(heroRect.intersects(rightBodyRect)){
                setX(getX() +pushBack);
                setDx(0);
            }
            else if(heroRect.intersects(headRect)){
                setY(bq.getStackHeight() - getBounds().getHeight());
                setDy(0);
                setInAir(false);
                setJumpCount(0);
            }
        }
    }

    /**
     * when user presses SPACE the Hero creates a Sword and adds it to weaponsList
     * @return Sword object
     */
    public Weapon meleeAttack() {
        swordActivationTime = System.currentTimeMillis();
        return new Sword(getX(), getY());
    }

    /**
     * activationTime is used to prevent sword spam
     * @return number of ms since meleeAttck was last called
     */
    public long getSwordActivationTime(){
        return swordActivationTime;
    }

    public long getBulletDelay() {
        return bulletDelay;
    }

    public void setBulletDelay(long time) {
        bulletDelay = time;
    }

    public void addWeapon(Weapon w) {
        weapons.add(w);
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public int getBulletAmount(){
        return bulletAmount;
    }

    public void setBulletAmount(int bulletAmount) {
        this.bulletAmount = bulletAmount;
    }

    public int getBlockAmount() {
        return blockAmount;
    }

    public void setBlockAmount(int blockAmount) {
        this.blockAmount = blockAmount;
    }
}
