package se.liu.ida.joaos226.tddd78.project.game_logic;

import javax.swing.*;
import java.awt.*;

/**
 * component creates a menu screen with "Play" and "Exit" button and changes game state to GAME if "Play" is pressed or exits
 * if "Exit" is pressed
 */
public class StartComponent extends JPanel
{
    private JButton playBtn = null;
    private JButton exitBtn = null;
    private GameViewer gameViewer = null;
    /**
     * if visible == false then screenComponent in GameViewer will be removed from frame
     */
    private boolean visible = true;
    private JLabel background = null;

    public StartComponent(GameViewer gameViewer){
        this.setPreferredSize(new Dimension(GameViewer.SCREEN_WIDTH, GameViewer.SCREEN_HEIGHT));
        this.gameViewer = gameViewer;
        playBtn = new JButton("  PLAY  ");
        exitBtn = new JButton("  EXIT  ");

        this.add(playBtn);
        this.add(exitBtn);
        this.background = loadBackgroundGif();
        this.add(background);
    }

    @Override protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        final int buttonHeight = 50;

        //To make background of JButtons black
        g2D.setColor(Color.BLACK);
        g2D.fillRect(0,0, GameViewer.SCREEN_WIDTH, buttonHeight);
    }

    /**
     * method is called 60 times/s from GameViewer's timer and checks wheter user has pressed a button
     */
    public void update(){
        if(exitBtn.getModel().isPressed()){
            System.exit(0);
        }
        else if(playBtn.getModel().isPressed()){
            visible = false;
        }
    }

    private JLabel loadBackgroundGif(){
        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("menu.gif"));
        icon.setImage(icon.getImage().getScaledInstance(GameViewer.SCREEN_WIDTH, GameViewer.SCREEN_HEIGHT, Image.SCALE_DEFAULT));
        return new JLabel(icon);
    }

    public void draw(){
        this.repaint();
    }

    public boolean isVisible(){
        return visible;
    }
}
