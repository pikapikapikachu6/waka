package ghost;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import java.util.ArrayList;

/**
 * This file is the main file of this project about game Waka. In this file, the project will read the map.
 * load the "confij.json" file and using other classes to draw the game for every frame.
 * @author Shutong Li
 * @version 1.1
 *
*/
public class App extends PApplet {
    public static final int WIDTH = 448, HEIGHT = 576;
    public Waka waka;
    public ArrayList<Ghost> ghostList = new ArrayList<>();
    public MapGrid mapGrid;
    private ConfigProcessing con_file = new ConfigProcessing();
    public PImage[] walls, wakas, fruit, ghosts;
    public PImage close_waka;
    public ArrayList<Point> ghosts_a = new ArrayList<Point>(), ghosts_c  = new ArrayList<Point>() ,ghosts_i  = new ArrayList<Point>(), ghosts_w = new ArrayList<Point>();
    public int ghost_number= 0,load_time1 = 0, load_time = 0, current_time = 0, dirc;

    /**
     * This method is class App constructor, which used to set objects.
     */
    public App() {
        con_file.readJsonFile();
    }

    /**
     *  The setup() function is called once when the program starts. It's used to define initial enviroment properties
     *  such as screen size and background color and to load media such as images and fonts as the program starts.
     *  There can only be one setup() function for each program and it shouldn't be called again after its initial execution.
     *  Note: Variables declared within setup() are not accessible within other functions, including draw(). (end auto-generated)
     */
    public void setup() {
        frameRate(60);
        restart();
    }

    /**
     * A method created to set the game with the original state. Call when the game first time or finished the last time game.
     *
     */
    public void restart() {
        ghost_number = 0;
        ghostList = new ArrayList<Ghost>();
        ghosts_a = ghosts_c = ghosts_i = ghosts_w = new ArrayList<Point>();
        this.walls = new PImage[]{  this.loadImage("src/main/resources/horizontal.png"), this.loadImage("src/main/resources/vertical.png"), this.loadImage("src/main/resources/upLeft.png"), this.loadImage("src/main/resources/upRight.png"), this.loadImage("src/main/resources/downLeft.png"), this.loadImage("src/main/resources/downRight.png")};
        this.wakas = new PImage[]{  this.loadImage("src/main/resources/playerLeft.png"), this.loadImage("src/main/resources/playerRight.png"), this.loadImage("src/main/resources/playerUp.png"), this.loadImage("src/main/resources/playerDown.png")};
        this.ghosts = new PImage[]{  this.loadImage("src/main/resources/ambusher.png"), this.loadImage("src/main/resources/chaser.png"), this.loadImage("src/main/resources/ignorant.png"), this.loadImage("src/main/resources/whim.png"), this.loadImage("src/main/resources/frightened.png")};
        this.close_waka = this.loadImage("playerClosed.png");
        this.fruit = new PImage[]{ this.loadImage("src/main/resources/fruit.png"), this.loadImage("src/main/resources/superfruit.png") };
        this.mapGrid = new MapGrid(con_file.getMap_address(), this.walls, this.fruit);
        this.waka = new Waka(con_file.getSpeed(), con_file.getLives(), this.wakas, this.close_waka);
        this.ghosts_a = mapGrid.getStart_ghost_a();
        this.ghosts_c = mapGrid.getStart_ghost_c();
        this.ghosts_i = mapGrid.getStart_ghost_i();
        this.ghosts_w = mapGrid.getStart_ghost_w();
        for (int i = 0; i < this.ghosts_a.size(); i ++, this.ghost_number ++) {
            ghostList.add(new Ghost(ghosts,Ghost_type.Amubusher));
            ghostList.get(this.ghost_number).initialLoc(this.ghosts_a.get(i), this.con_file, this.mapGrid);
        }
        for (int i = 0; i < this.ghosts_c.size(); i ++, this.ghost_number ++) {
            ghostList.add(new Ghost(ghosts,Ghost_type.Chaser));
            ghostList.get(this.ghost_number).initialLoc(this.ghosts_c.get(i), this.con_file,this.mapGrid);
        }
        for (int i = 0; i < this.ghosts_i.size(); i ++, this.ghost_number ++) {
            ghostList.add(new Ghost(ghosts,Ghost_type.Ignorant));
            ghostList.get(this.ghost_number).initialLoc(this.ghosts_i.get(i), this.con_file,this.mapGrid);
        }
        for (int i = 0; i < this.ghosts_w.size(); i ++, this.ghost_number ++) {
            ghostList.add(new Ghost(ghosts,Ghost_type.Whim));
            ghostList.get(this.ghost_number).initialLoc(this.ghosts_w.get(i), this.con_file,this.mapGrid);
        }
        this.waka.initialLoc(this.mapGrid);
    }

    /**
     * A method extends from PApplet. Description to come... ( end auto-generated ) Override this method to call size()
     * when not using the PDE.
     */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * A method extends from PApplet. Called directly after setup() and continuously executes the lines of code contained
     * inside its block until the program is stopped or noLoop() is called. The draw() function is called automatically
     * and should never be called explicitly. It should always be controlled with noLoop(), redraw() and loop(). After
     * noLoop() stops the code in draw() from executing, redraw() causes the code inside draw() to execute once and loop()
     * will causes the code inside draw() to execute continuously again. The number of times draw() executes in each second
     * may be controlled with frameRate() function. There can only be one draw() function for each sketch and draw() must
     * exist if you want the code to run continuously or to process events such as mousePressed(). Sometimes, you might
     * have an empty call to draw() in your program as shown in the above example. ( end auto-generated )
     */
    public void draw() {
        line(0,0,250,200);
        background(0, 0, 0);
        if (waka.mark == mapGrid.total_fruit && load_time1 == 599) {
            this.dirc = load_time1 = 0;
            restart();
        } else if (waka.mark == mapGrid.total_fruit && load_time1 < 599){
            PFont font = createFont("src/main/resources/PressStart2P-Regular.ttf", 16);
            textFont(font);
            text("You win", 170, 250);
        }
        if (waka.lives == 0 && load_time == 599){
            load_time = 0;
            restart();
        } else if (waka.lives == 0 && load_time < 599) image(loadImage("src/main/resources/gameover - Copy.png"),170,250);
        else {
            int eaten = 0;
            for (int i = 0; i < ghostList.size(); i ++, stroke(153)) {
                ghostList.get(i).move(this.waka,ghostList);
                if (dirc == 32) line(this.ghostList.get(i).current_position.getX(),this.ghostList.get(i).current_position.getY(),this.ghostList.get(i).target.getX(),this.ghostList.get(i).target.getY());
                if (ghostList.get(i).eat_waka(this.waka,this.ghostList.get(i)) == true) eaten = 1;
            }
            if (eaten != 1) {
                if (dirc == UP) this.waka.setDirToUp();
                if (dirc == DOWN) this.waka.setDirToDown();
                if (dirc == LEFT) this.waka.setDirToLeft();
                if (dirc == RIGHT) this.waka.setDirToRight();
            }
            for (int i = 0; i < waka.lives; i++) this.image(this.wakas[1],0+25*i,540);
            mapGrid.draw(this);
            waka.draw(this);
            for (int i = 0; i < ghostList.size(); i ++) ghostList.get(i).draw(this,this.waka);
        }
        current_time += 1;
    }

    /**
     * This is a method extends from PApplet. The boolean system variable keyPressed is true if any key is pressed and
     * false if no keys are pressed. ( end auto-generated )
     */
    public void keyPressed() {
        this.dirc = keyCode;
    }

    /**
     * The main method. This project run start from this method.
     * @param args String[]
     */
    public static void main(String[] args) {
        PApplet.main("ghost.App");
    }
}
