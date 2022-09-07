package se.liu.ida.joaos226.tddd78.project.map_objects;

import se.liu.ida.joaos226.tddd78.project.game_logic.Direction;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * this class loads the images from Resources folder into the other objects (sprites, GameBoard...) ImageResources field.
 * It also has a EnumMap which returns different images depening on what direction the sprites are facing. If an image
 * fails to be loaded, an error is logged to logfile_errors.log file.
 */

public class ImageResource
{
    private double x = 0;
    private double y = 0;
    private int width = 0;
    private int height = 0;
    /**
     * holds different image for every Direction value (LEFT, RIGHT, OMNI)
     */
    private Map<Direction, Image> imageDirection = new EnumMap<>(Direction.class);

    public ImageResource(double x, double y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * fills EnumMap with mirrored png:s if the image can have two directions
     * @param path path to image in Resources folder
     */
    public void loadBiDirectionalImg(String path) {
	try {
	    String rightPath = path + "_right.png";
	    String leftPath = path + "_left.png";
	    Image rightImage = ImageIO.read(ClassLoader.getSystemResource(rightPath));
	    rightImage = rightImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	    Image leftImage = ImageIO.read(ClassLoader.getSystemResource(leftPath));
	    leftImage = leftImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	    imageDirection.put(Direction.RIGHT, rightImage);
	    imageDirection.put(Direction.LEFT, leftImage);
	}catch(IOException e){
	    logErrors(e);
	}
    }

    /**
     * fills EnumMap with one image, this method is used when there is only one direction
     * @param path path to image in Resources folder
     */
    public void loadOneDirectionalImg(String path){
	try {
	    Image image = ImageIO.read(ClassLoader.getSystemResource(path));
	    image = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	    imageDirection.put(Direction.OMNI, image);
	}catch(IOException e){
	    logErrors(e);
	}
    }

    /**
     * this method logs errors resulting from IOException from the image reader. Error is placed in logfile_errors.log or create
     * the file if it does not exist. It will also give a message to console an the error's cause and print a stacktrace
     * @param e error given by Image reader
     */
    private void logErrors(IOException e) {
	Logger logger = Logger.getAnonymousLogger();
	FileHandler fh = null;
	try {
	    fh = new FileHandler("logfile_errors.log", true);
	    logger.addHandler(fh);
	    SimpleFormatter formatter = new SimpleFormatter();
	    fh.setFormatter(formatter);
	    logger.info(e.getMessage());
	}
	catch (IOException | SecurityException e2){
	    System.out.println("Can not log the error, the filehandler can't create logfile");
	    logger.info(e2.getMessage());
	    e2.printStackTrace();
	    fh.close();
	    System.exit(-1);
	}
	fh.close();
	System.out.println("Image is missing!");
	e.printStackTrace();
	System.exit(-1);
    }

    public Image getImage(Direction direction) {
        return imageDirection.get(direction);
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }

}
