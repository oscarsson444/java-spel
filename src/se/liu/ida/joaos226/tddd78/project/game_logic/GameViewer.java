package se.liu.ida.joaos226.tddd78.project.game_logic;


import se.liu.ida.joaos226.tddd78.project.player_abilities.Bullet;
import se.liu.ida.joaos226.tddd78.project.player_abilities.Weapon;
import se.liu.ida.joaos226.tddd78.project.map_objects.World;
import se.liu.ida.joaos226.tddd78.project.sprite.Hero;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * GameViewer creates a JFrame and puts StartComponent or GameComponent in it depending on what State the game is in (MENU or
 * GAME). GameViewer also creates listeners for user input (mouse, keyboard), menues and a Timer that updates the game and
 * screen 60 times/s.
 */

public class GameViewer
{
    private JFrame frame = null;
    private World world = null;
    private LevelManager levelManager = null;
    private State state = State.MENU;

    /**
     * SCREEN_WIDTH is public because they are referenced in many parts of the program and it is tedious and confusing to
     * send them as arguments through various methods and create several fields when they clearly only have one purpose
     */
    public final static int SCREEN_WIDTH = 640;
    /**
     * SCREEN_HEIGHT is public for same reason as SCREEN_WIDTH
     */
    public final static int SCREEN_HEIGHT = 480;

    /**
     * State defines the different states the game can be in. Timer updates and draws either StartComponent or GameComponent
     * depending on state.
     */
    private static enum State{
        MENU, GAME
    }

    public GameViewer(World world) {
        this.world = world;
        this.levelManager = world.getLevelManager();
    }

    /**
     * show method creates a screen (JFrame) to display the game in
     */
    public void show() {
        final int fps = 60;
        final int secondAsMillisekond = 1000;
        frame = new JFrame("Treasure defence 2D");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        StartComponent startScreen = new StartComponent(this);
        GameComponent gameComponent = new GameComponent(world);
        gameComponent.addMouseListener(createMouseListener());
        keyboardInput(gameComponent, world);

        frame.add(startScreen);
        frame.pack();
        frame.setVisible(true);

        Timer timer = new Timer(secondAsMillisekond/fps, createTimer(gameComponent, startScreen));
        timer.setCoalesce(true);
        timer.start();
    }

    public JFrame getFrame(){
        return frame;
    }

    private void createMenu(JFrame frame) {
        final JMenuBar topBar = new JMenuBar();
        frame.setJMenuBar(topBar);

        final JMenu game = new JMenu("Game");
        topBar.add(game);
        final JMenuItem exit = new JMenuItem("Exit");
        final JMenuItem retry = new JMenuItem("Retry");
        final JMenuItem startLevel = new JMenuItem("Start Level");
        topBar.add(startLevel);
        game.add(exit);
        game.add(retry);

        Action exitGame = new AbstractAction()
        {
            @Override public void actionPerformed(final ActionEvent actionEvent) {
                System.exit(0);
            }
        };

        Action retryGame = new AbstractAction()
        {
            @Override public void actionPerformed(final ActionEvent actionEvent) {
                world.resetWorld();
            }
        };

        Action start = new AbstractAction()
        {
            @Override public void actionPerformed(final ActionEvent actionEvent) {
                if (world.getSprites().size() == 1) {
                    levelManager.addLevelCounter();
                    if (levelManager.getLevelCount() < levelManager.getLevels().size()) {
                        world.startNewLevel();
                    }
                }
            }
        };
        startLevel.addActionListener(start);
        exit.addActionListener(exitGame);
        retry.addActionListener(retryGame);
    }

    /**
     * updates and draws active component entities
     * @param gameComponent
     * @param startScreen
     * @return Timer object
     */
    private Action createTimer(GameComponent gameComponent, StartComponent startScreen) {
        Action doOneStep = new AbstractAction()
        {
            @Override public void actionPerformed(final ActionEvent actionEvent) {
                if(state == State.GAME) {
                    gameComponent.draw();
                    gameComponent.update();
                }
                else{
                    if (startScreen.isVisible()) {
                        startScreen.update();
                        startScreen.draw();
                    }
                    /*
                    When startScreen isVisible is set to false startComponent gets removed from the frame and gameComponent
                    is added instead.
                     */
                    else{
                        frame.remove(startScreen);
                        frame.add(gameComponent);
                        createMenu(frame);
                        frame.pack();
                        frame.setVisible(true);
                        state = State.GAME;
                    }
                }
            }
        };
        return doOneStep;
    }

    private MouseListener createMouseListener() {
        MouseListener mouseListener = new MouseAdapter()
        {
            @Override public void mouseClicked(final MouseEvent e) {
                super.mouseClicked(e);
                int cameraOffsetX = (-1*(int)world.getCamera().getX()); //Makes sure coordinates is right even when hero moves
                int cameraOffsetY = (-1*(int)world.getCamera().getY()); //past screen limit
                long bulletDelay = world.getHero().getBulletDelay(); //moment in ms when bullet was fired
                final int bulletCooldown = 400; //Makes sure that bullets cannot be fired faster than every 400ms.
                Hero hero = world.getHero();

                //Building a protection block with right mouse click
                if (e.getButton() == MouseEvent.BUTTON3 && hero.getBlockAmount() > 0) {
                    world.getMap().makeBlockQueue(e.getX() + cameraOffsetX);
                    hero.setBlockAmount(hero.getBlockAmount() - 1); //Decreases Hero:s amount of blocks
                }

                //Shooting with left mouse click with a delay of 400 ms between each shot if Hero has bullets left
                else if (e.getButton() == MouseEvent.BUTTON1 && (System.currentTimeMillis() - bulletDelay) >= bulletCooldown &&
                         hero.getBulletAmount() > 0) {

                    int heroCenterX = (int)hero.getX() + hero.getWidth()/2;
                    int heroCenterY = (int)hero.getY() + hero.getHeight()/2;
                    Bullet bullet = new Bullet(heroCenterX, heroCenterY, e.getX() + cameraOffsetX,
                                               e.getY() + cameraOffsetY);
                    hero.addWeapon(bullet); //Adds bullet to Hero:s list of weapons
                    hero.setBulletDelay(System.currentTimeMillis());
                    hero.setBulletAmount(hero.getBulletAmount() - 1); //Decreases Hero:s amount of bullets
                }
            }
        };
        return mouseListener;
    }

    /**
     * defines what Hero will do based on keyboard input from user
     * @param gameComponent component affected by Inputs
     * @param world contains all sprites and map objects
     */
    public void keyboardInput(JPanel gameComponent, World world) {
        Hero hero = world.getHero();

        final InputMap in = gameComponent.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        in.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "moveLeft");
        in.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "releaseKey");
        in.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "releaseKey");
        in.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "moveRight");
        in.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "playerJump");
        in.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,0,false), "meleeAttack");

        final ActionMap act = gameComponent.getActionMap();
        Action moveLeft = new AbstractAction()
        {
            @Override public void actionPerformed(final ActionEvent actionEvent) {
                hero.setMoveRight(false);
                hero.setMoveLeft(true);
                hero.setDirectionTracker(Direction.LEFT);
            }
        };

        Action moveRight = new AbstractAction()
        {
            @Override public void actionPerformed(final ActionEvent actionEvent) {
                hero.setMoveRight(true);
                hero.setMoveLeft(false);
                hero.setDirectionTracker(Direction.RIGHT);
            }
        };

        Action releaseKey = new AbstractAction()
        {
            @Override public void actionPerformed(final ActionEvent actionEvent) {
                hero.setMoveRight(false);
                hero.setMoveLeft(false);
            }
        };

        Action playerJump = new AbstractAction()
        {
            @Override public void actionPerformed(final ActionEvent actionEvent) {
                if(hero.getJumpCount() == 0) {
                    hero.addJumpCount();
                    hero.setJump(true);
                    hero.setInAir(true);
                }
            }
        };

        Action meleeAttack = new AbstractAction()
        {
            @Override public void actionPerformed(final ActionEvent actionEvent) {
                final int swordDelay = 500; //Makes sure sword can only be activated every 500 ms.
                if (System.currentTimeMillis() - hero.getSwordActivationTime() >= swordDelay) {
                    Weapon sword = hero.meleeAttack();
                    sword.setVisible(true);
                    world.getHero().addWeapon(sword);
                }
            }
        };

        act.put("moveLeft", moveLeft);
        act.put("moveRight", moveRight);
        act.put("releaseKey", releaseKey);
        act.put("playerJump", playerJump);
        act.put("meleeAttack", meleeAttack);
    }
}
