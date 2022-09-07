package se.liu.ida.joaos226.tddd78.project.sprite;

import se.liu.ida.joaos226.tddd78.project.game_logic.Direction;
import se.liu.ida.joaos226.tddd78.project.map_objects.ImageResource;
import se.liu.ida.joaos226.tddd78.project.game_logic.VictoryListener;
import se.liu.ida.joaos226.tddd78.project.map_objects.World;

import java.awt.*;

/**
 * superclass for all sprites (Hero, JumperMonster, Zombie) that defines default methods and field shared by all of them.
 * E.g dx/dy movement, if sprite is jumping, if sprite has won and maximum horizontal speed etc
 */

public abstract class AbstractSprite extends ImageResource implements Sprite
{
    private double dx = 0;
    private double dy = 0;

    private boolean moveLeft = false;
    private boolean moveRight = false;
    /**
     * used to make image look right way when not pressing left/right arrow
     */
    private Direction directionTracker = Direction.RIGHT;
    /**
     * true when falling or jumping, every sprite spawns in air
     */
    private boolean inAir = true;
    private double maxHorizontalSpeed = 0;
    private int jumpCount = 0;
    private boolean jump = false;
    private double jumpStrength = 4;
    /**
     * used for acceleration and decceleration in jumps
     */
    private double jumpCoefficient = INIT_JUMP_COEFFICIENT;
    private final static double INIT_JUMP_COEFFICIENT = 0.05;

    private boolean victory = false;
    private int lives = 0;
    private ImageResource heart = null;
    /**
     * LevelManager is set as listener and when victory == true it is notified
     */
    private VictoryListener listener = null;

    private final static double GRAVITY = 0.3;
    private final static double ACCELERATION = 0.2;

    protected AbstractSprite(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void loadSprite(String path) {
        final int heartDimension = 15;
        loadBiDirectionalImg(path);
        heart = new ImageResource(0,0,heartDimension, heartDimension);
        heart.loadOneDirectionalImg("heart.png");
    }

    /**
     * collision shared by all sprites (ground-border, world-border)
     * @param world contains all sprites and other map entities
     */
    public void checkGeneralCollisions(World world) {
        Rectangle spriteRect = getBounds();
        Rectangle groundBorderRect = world.getMap().getGroundBorderBounds();

        if (spriteRect.intersects(groundBorderRect)) {
            setY(groundBorderRect.y - spriteRect.height);
            dy = 0;
            inAir = false;
            jumpCount = 0;
        }
        else {
            inAir = true;
        }

        if (getX() + getWidth() >= world.getMap().getMapWidth()) {
            setX(world.getMap().getMapWidth() - getWidth());
            dx = 0;
        }
        else if(getX() <= 0) {
            setX(0);
            dx = 0;
        }
    }

    /**
     * updates x and y values of sprites
     * @param world contains all sprites and other map entities
     */
    public void spriteGeneralUpdate(World world) {
        updateVerticalMovement();
        updateHorizontalMovement();
    }

    private void updateHorizontalMovement() {
        double newX;
        if (moveLeft) {
            if (dx > -maxHorizontalSpeed) {
                dx-= ACCELERATION;
            }
            newX = getX() + dx;
        } else if (moveRight) {
            if (dx < maxHorizontalSpeed) {
                dx+= ACCELERATION;
            }
            newX = getX() + dx;
        } else {
            if (dx > 1) {
                dx -= ACCELERATION;
            } else if (dx < -1) {
                dx += ACCELERATION;
            } else {
                dx = 0;
            }
            newX = getX() + dx;
        }
        setX(newX);
    }

    private void updateVerticalMovement() {
        final int maxJumps = 2;
        if (inAir) {
            dy += GRAVITY;
            setY(getY() + dy);
        }
        if (jumpCount < maxJumps && jump) {
            final int maxCoefficient = 1;
            dy = -jumpStrength;
            if (jumpCoefficient < maxCoefficient) {
                final double dJumpCoefficient = 0.2;
                setY(getY() + dy*jumpCoefficient);
                jumpCoefficient += dJumpCoefficient;
            }
            else {
                jump = false;
                jumpCoefficient = INIT_JUMP_COEFFICIENT;
            }
        }
    }

    public void setVictory(boolean victory){
        this.victory = victory;
    }

    public boolean isVictory(){
        return victory;
    }

    public boolean isJump() {
        return jump;
    }

    public void setJump(final boolean jump) {
        this.jump = jump;
    }

    public double getJumpStrength() {
        return jumpStrength;
    }

    public void setJumpCoefficient(final double jumpCoefficient) {
        this.jumpCoefficient = jumpCoefficient;
    }

    public Rectangle getBounds() {
        return new Rectangle((int)getX(), (int)getY(), getWidth(), getHeight());
    }

    public VictoryListener getListener() {
        return listener;
    }

    public void setListener(VictoryListener vl) {
        listener = vl;
    }

    public double getJumpCoefficient() {
        return jumpCoefficient;
    }

    public void setJumpCount(int j) {
        jumpCount = j;
    }

    public void addJumpCount() {
        jumpCount ++;
    }

    public int getJumpCount() {
        return jumpCount;
    }

    public boolean isMoveLeft() { return moveLeft; }

    public boolean isMoveRight() { return moveRight; }

    public void setMoveLeft(final boolean moveLeft) { this.moveLeft = moveLeft; }

    public void setMoveRight(final boolean moveRight) { this.moveRight = moveRight; }

    public void setDirectionTracker(Direction dir) {
        this.directionTracker = dir;
    }

    public Direction getDirectionTracker(){
        return directionTracker;
    }

    public void setInAir(boolean condition) {
        inAir = condition;
    }

    public void setMaxHorizontalSpeed(int speed) {
        maxHorizontalSpeed = speed;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getLives() {
        return lives;
    }

    public ImageResource getHeart() {
        return heart;
    }

    public double getMaxHorizontalSpeed() {
        return maxHorizontalSpeed;
    }

    public boolean getInAir() {
        return inAir;
    }

    public double getDx() { return dx; }

    public double getDy() { return dy; }

    public void setDx(final double dx) { this.dx = dx; }

    public void setDy(final double dy) { this.dy = dy; }
}
