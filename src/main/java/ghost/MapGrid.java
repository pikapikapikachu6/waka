package ghost;

import processing.core.PImage;
import java.io.*;
import java.util.ArrayList;

/**
 * This class is used to read map file. It includes the function of set the corresponding word into map and draw the map
 * things.
 * @author Shutong Li
 * @version 1.1
 */
public class MapGrid{
    public static final int WIDTH = 28;
    public static final int HEIGHT = 36;
    public static final int GRIDSIZE = 16;

    private PImage[] walls;
    private PImage[] fruit;
    private char[][] map_grid = new char[HEIGHT][WIDTH];
    private Point start_waka;
    private ArrayList<Point> start_ghost_a = new ArrayList<Point>();
    private ArrayList<Point> start_ghost_c = new ArrayList<Point>();
    private ArrayList<Point> start_ghost_i = new ArrayList<Point>();
    private ArrayList<Point> start_ghost_w = new ArrayList<Point>();
    public int total_fruit = 0;

    /**
     * This method is used as the class constructor method.
     * @param map_address String
     * @param walls PImage[]
     * @param fruit PImage[]
     */
    public MapGrid(String map_address, PImage[] walls, PImage[] fruit) {
        this.walls = walls;
        this.fruit = fruit;
        this.total_fruit = 0;
        readMap(map_address);
    }

    /**
     * This method is used to read the map file and store the contents into a char array.
     * @param address String
     */
    public void readMap(String address) {
        try {
            String pathname = address;
            File filename = new File(pathname);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
            BufferedReader in = new BufferedReader(reader);
            String str;
            int line = -1;
            while ((str = in.readLine()) != null) {
                line ++;
                int length = str.length();
                for (int i = 0; i < length; i++) {
                    char current = str.charAt(i);
                    if (current == 'p') {
                        this.start_waka = new Point(i * 16, line * 16);
                    }
                    if (current == 'a') {
                        this.start_ghost_a.add(new Point(i * 16, line * 16));
                    }
                    if (current == 'c') {
                        this.start_ghost_c.add(new Point(i * 16, line * 16));
                    }
                    if (current == 'i') {
                        this.start_ghost_i.add(new Point(i * 16, line * 16));
                    }
                    if (current == 'w') {
                        this.start_ghost_w.add(new Point(i * 16, line * 16));
                    }
                    if (current == '7') {
                        this.total_fruit += 1;
                    }
                    map_grid[line][i] = current;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to print the map.
     * @param app App
     */
    public void draw(App app) {
        for (int j = 0; j < WIDTH; j ++) {
            for (int i = 0; i < HEIGHT; i ++) {
                char ch = map_grid[i][j];
                switch (ch) {
                    case '1':
                        app.image(this.walls[0], j * GRIDSIZE, i * GRIDSIZE);
                        break;
                    case '2':
                        app.image(this.walls[1], j * GRIDSIZE, i * GRIDSIZE);
                        break;
                    case '3':
                        app.image(this.walls[2], j * GRIDSIZE, i * GRIDSIZE);
                        break;
                    case '4':
                        app.image(this.walls[3], j * GRIDSIZE, i * GRIDSIZE);
                        break;
                    case '5':
                        app.image(this.walls[4], j * GRIDSIZE, i * GRIDSIZE);
                        break;
                    case '6':
                        app.image(this.walls[5], j * GRIDSIZE, i * GRIDSIZE);
                        break;
                    case '7':
                        app.image(this.fruit[0], j * GRIDSIZE, i * GRIDSIZE);
                        break;
                    case '8':
                        app.image(this.fruit[1], j * GRIDSIZE, i * GRIDSIZE);
                        break;
                }
            }
        }
    }

    /**
     * This method is used to check if the point on the map can move which means it isn't a wall.
     * @param i int
     * @param j int
     * @return true/false boolean
     */
    public boolean isGeneral(int i, int j) {
        if (this.getMap_grid_char(j,i) == '0') {
            return false;
        }
        if (this.getMap_grid_char(j,i) == '7') {
            return false;
        }
        if (this.getMap_grid_char(j,i) == 'p') {
            return false;
        }
        if (this.getMap_grid_char(j,i) == 'a') {
            return false;
        }
        if (this.getMap_grid_char(j,i) == 'c') {
            return false;
        }
        if (this.getMap_grid_char(j,i) == 'i') {
            return false;
        }
        if (this.getMap_grid_char(j,i) == 'w') {
            return false;
        }
        if (this.getMap_grid_char(j,i) == 'g') {
            return false;
        }
        if (this.getMap_grid_char(j,i) == '8') {
            return false;
        }
        return true;
    }

    /**
     * This method is used to get the map array.
     * @return map_grid Array
     */
    public char[][] getMap_grid() {
        return map_grid;
    }

    /**
     * This method is used to get the content on the map point.
     * @param i int
     * @param j int
     * @return letter char
     */
    public char getMap_grid_char(int i, int j) {
        return getMap_grid()[i][j];
    }

    /**
     * This method is used to set the letter into the map corresponding point.
     * @param i int
     * @param j int
     */
    public void setMap_grid_char(int i, int j) {
        this.map_grid[i][j] = '0';
    }

    /**
     * This method is used to get the waka start position.
     * @return start_waka Point
     */
    public Point getStart_waka() {
        return start_waka;
    }

    /**
     * This method is used to get the Amubusher ghost start positions.
     * @return list ArrayList
     */
    public ArrayList<Point> getStart_ghost_a() {
        return start_ghost_a;
    }

    /**
     * This method is used to get the Chaser ghost start positions.
     * @return list ArrayList
     */
    public ArrayList<Point> getStart_ghost_c() {
        return start_ghost_c;
    }

    /**
     *
     * This method is used to get the Ignorant ghost start positions.
     * @return list ArrayList
     */
    public ArrayList<Point> getStart_ghost_i() {
        return start_ghost_i;
    }

    /**
     * This method is used to get the Whim ghost start positions.
     * @return list ArrayList
     */
    public ArrayList<Point> getStart_ghost_w() {
        return start_ghost_w;
    }
}
