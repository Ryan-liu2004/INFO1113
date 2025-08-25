/*
Haochen Liu
hliu0010
540243105
*/

package TwentyFortyEight;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.io.*;
import java.sql.Time;
import java.util.*;

public class App extends PApplet {

    //public static final int GRID_SIZE = 4; // 4x4 grid
    //public static final int WIDTH = GRID_SIZE * CELLSIZE;
    //public static final int HEIGHT = GRID_SIZE * CELLSIZE;
    public static int GRID_SIZE = 4;
    public static int WIDTH;
    public static int HEIGHT;

    public static final int CELLSIZE = 100; // Cell size in pixels
    public static final int CELL_BUFFER = 8; // Space between cells
    public static final int FPS = 30;
    public static final int StrokeWeight=15;   //边框大小
    public static final int TextSize=40;   //字体大小
    public static final int FontSize=50;   //粗体大小

    private Board board;
    private Timer timer;

    public static Random random = new Random();

    private PFont font;
    public PImage eight;

    private boolean keyflag; //keyRelease: true,  keyPress: false
    private boolean mouseflag; //mouseRelease: true,  mousePress: false
    private boolean moveflag;  //moving: true ;   done moving: false 
    private boolean endflag;   //game over

    // Feel free to add any additional methods or attributes you want. Please put
    // classes in different files.

    public static void print(String tmp) {System.out.print(tmp);}
    public static void println(String tmp) {System.out.println(tmp);}


    public App(){
        board=new Board(GRID_SIZE);
        WIDTH=GRID_SIZE*CELLSIZE;
        HEIGHT=GRID_SIZE*CELLSIZE;
    }
    public boolean getmoveflag(){return moveflag;}
    public void setmoveflag(boolean flag){moveflag=flag;}
    public void setendflag(){endflag=true;}

    /**
     * Initialise the setting of the window size.
     */
    @Override
    public void settings() {size(WIDTH, HEIGHT+Timer.Timer_h);}

    /**
     * Load all resources such as images. Initialise the elements such as the player
     * and map elements.
     */
    @Override
    public void setup() {
        frameRate(FPS);

        //Set timer
        timer=new Timer(this);

        //字体变粗
        font=createFont("SansSerif.bold", FontSize);
        textFont(font);

        //Set Colour
        Colour.setup(); 

        //Set Board
        board.setup();
        board.generate_new();
        board.generate_new();

        //Set other
        keyflag=true;
        mouseflag=true;
        moveflag=false;
        endflag=false;

        // See PApplet javadoc:
        // loadJSONObject(configPath)
        //this.eight = loadImage(this.getClass().getResource("8.png").getPath().toLowerCase(Locale.ROOT).replace("%20", ""));
        // " "));

        // create attributes for data storage, eg board
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
    @Override
    public void keyPressed(KeyEvent event) {
        if(event.getKeyCode()==82){
            setup();
            return;
        }
        if(!keyflag || !mouseflag || moveflag || endflag) return;  
        keyflag=false;
        int code=event.getKeyCode();
        /*
        UP     38
        DOWN   40
        LEFT   37
        RIGHT  39
        R      82
        */
        switch(code){
            case 38:   
                board.up();
                break;
            case 40:
                board.down();
                break;
            case 37:
                board.left();
                break;
            case 39:
                board.right();
                break;
            default:
                break;
        }
        
    }

    /**
     * Receive key released signal from the keyboard.
     */
    @Override
    public void keyReleased() {
        keyflag=true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseflag=true;
        if(!keyflag || moveflag || endflag) return; 
        if (e.getButton()==PConstants.LEFT) 
            board.place(this, 
                (e.getY()-Timer.Timer_h)/App.CELLSIZE, 
                e.getX()/App.CELLSIZE);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(!keyflag || !mouseflag || moveflag || endflag) return; 
        if (e.getButton()==PConstants.LEFT) mouseflag=false;
    }

    /**
     * Draw all elements in the game by current frame.
     */
    @Override
    public void draw() {
        if(endflag) return;
        //backgound()重绘

        // draw timer
        timer.draw(this);

        // draw game board
        this.textSize(TextSize);
        this.strokeWeight(StrokeWeight);
        
        //draw board
        board.draw(this);
    }

    public static void main(String[] args) {
        if(args.length!=0) GRID_SIZE=Integer.parseInt(args[0]);   //输入大小

        PApplet.main("TwentyFortyEight.App");
    }
}
