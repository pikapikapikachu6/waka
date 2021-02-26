package ghost;

import processing.core.PImage;

import static ghost.Direction.*;

/**
 * This class is used to create a object waka.This class used to create ghost object. It includes function of creating
 * a waka, moving the ghost and drawing the waka.
 * @author Shutong Li
 * @version 1.1
 */
public class Waka{
    private static final int GRIDSIZE = 16;
    private static final int OFFSET = 8;

    private int speed;
    public int lives;
    private PImage[] wakas;
    private PImage close_waka;
    private MapGrid mapGrid;
    public Point current_position;
    public Point start_position;
    public Direction direction = UP;
    public int mark = 0;
    public int total_fruit;
    public boolean friented;

    /**
     * This method is waka class constructor method.
     * @param speed int
     * @param lives int
     * @param wakas PImage[]
     * @param close_waka PImage
     */
    public Waka(int speed, int lives, PImage[] wakas, PImage close_waka) {
        this.speed = speed;
        this.lives = lives;
        this.wakas = wakas;
        this.close_waka = close_waka;
        this.mark = 0;
    }

    /**
     * This method is used to set the waka into original state.
     * @param mapGrid MapGrid
     */
    public void initialLoc(MapGrid mapGrid) {
        this.mapGrid = mapGrid;
        //x = 20 y = 13
        // x hang y lie
        this.start_position = new Point(mapGrid.getStart_waka().getX(),mapGrid.getStart_waka().getY());
        this.current_position = new Point(mapGrid.getStart_waka().getX(),mapGrid.getStart_waka().getY());
        this.total_fruit = mapGrid.total_fruit;
        this.friented = false;
    }

    /**
     * This method is used to change the waka dierction with LEFT.
     */
    public void setDirToLeft() {
        this.direction = LEFT;
        move();
    }

    /**
     * This method is used to change the waka dierction with RIGHT.
     */
    public void setDirToRight() {
        this.direction = RIGHT;
        move();
    }

    /**
     * This method is used to change the waka dierction with UP.
     */
    public void setDirToUp() {
        this.direction = UP;
        move();
    }

    /**
     * This method is used to change the waka dierction with DOWN.
     */
    public void setDirToDown() {
        this.direction = DOWN;
        move();
    }

    /**
     * This method is used to move the waka.
     */
    public void move() {
        if (canMove(this.current_position, this.speed, this.lives, this.direction, this.mapGrid)) {
            switch (this.direction) {
                case UP:
                    this.current_position.y -= this.speed;
                    if (eat(this.mapGrid, this.current_position) == 1) mark += 1;
                    break;
                case DOWN:
                    this.current_position.y += this.speed;
                    if (eat(this.mapGrid, this.current_position) == 1) mark += 1;
                    break;
                case LEFT:
                    this.current_position.x -= this.speed;
                    if (eat(this.mapGrid, this.current_position) == 1) mark += 1;
                    break;
                case RIGHT:
                    this.current_position.x += this.speed;
                    if (eat(this.mapGrid, this.current_position) == 1) mark += 1;
                    break;
            }
        }
    }

    /**
     * This method is used to check which direction the waka can move.
     * @param current_position Point
     * @param speed int
     * @param lives int
     * @param direction Direction
     * @param mapGrid MapGrid
     * @return true/false boolean
     */
    public boolean canMove(Point current_position, int speed, int lives, Direction direction, MapGrid mapGrid) {
        float next_x = current_position.getX();
        float next_y = current_position.getY();
        switch (direction) {
            case RIGHT:
                next_x = next_x + speed + OFFSET;
                if (current_position.getX() < App.WIDTH && next_x <= App.WIDTH) {
                    return !mapGrid.isGeneral((int) next_x / GRIDSIZE, (int) next_y / GRIDSIZE);
                }
                break;
            case LEFT:
                next_x = next_x - speed - OFFSET;
                if (current_position.getX() > 0 && next_x >= 0) {
                    return !mapGrid.isGeneral((int) next_x / GRIDSIZE, (int) next_y / GRIDSIZE);
                }
                break;
            case UP:
                next_y = next_y - speed - OFFSET;
                if (current_position.getY() > 0 && next_y >= 0) {
                    return !mapGrid.isGeneral((int) next_x / GRIDSIZE, (int) next_y / GRIDSIZE);
                }
                break;
            case DOWN:
                next_y = next_y + speed + OFFSET;
                if (current_position.getY() < App.HEIGHT && next_y <= App.HEIGHT) {
                    return !mapGrid.isGeneral((int) next_x / GRIDSIZE, (int) next_y / GRIDSIZE);
                }
                break;
        }
        return false;
    }

    /**
     * This method is used to test the waka whether eat a fruit in the current position.
     * @param mapGrid MapGrid
     * @param current_position Point
     * @return 0/1 int
     *  0: not eat
     *  1: eat
     */
    public int eat(MapGrid mapGrid, Point current_position) {
        if (mapGrid.getMap_grid_char((int)current_position.getY()/GRIDSIZE, (int)current_position.getX()/GRIDSIZE) == '7') {
            mapGrid.setMap_grid_char((int)current_position.getY()/GRIDSIZE, (int)current_position.getX()/GRIDSIZE);
            return 1;
        }
        if (mapGrid.getMap_grid_char((int)current_position.getY()/GRIDSIZE, (int)current_position.getX()/GRIDSIZE) == '8') {
            mapGrid.setMap_grid_char((int)current_position.getY()/GRIDSIZE, (int)current_position.getX()/GRIDSIZE);
            this.friented = true;
            return 1;
        }
        return 0;
    }

    /**
     * Ater the waka died, this method is set the waka to the original state.
     */
    public void start_game() {
        if (this.lives > 0) {
            this.lives -= 1;
            this.current_position = new Point(this.start_position.getX(),this.start_position.getY());
        }
    }

    /**
     * This method is used to draw waka on the map and show.
     * @param app App
     * @return true/false boolean
     */
    public boolean draw(App app) {
        if (app.current_time % 16 < 8) {
            switch (this.direction) {
                case LEFT:
                    app.image(wakas[0], current_position.getX() - OFFSET,current_position.getY()- OFFSET);
                    break;
                case RIGHT:
                    app.image(wakas[1],current_position.getX() - OFFSET,current_position.getY() - OFFSET);
                    break;
                case UP:
                    app.image(wakas[2], current_position.getX() - OFFSET,current_position.getY()- OFFSET);
                    break;
                case DOWN:
                    app.image(wakas[3], current_position.getX() - OFFSET,current_position.getY() - OFFSET);
                    break;
            }
            return true;
        } else {
            switch (this.direction) {
                case LEFT:
                    app.image(close_waka, current_position.getX()- OFFSET,current_position.getY()- OFFSET);
                    break;
                case RIGHT:
                    app.image(close_waka, current_position.getX() - OFFSET,current_position.getY() - OFFSET);
                    break;
                case UP:
                    app.image(close_waka, current_position.getX()- OFFSET,current_position.getY()- OFFSET);
                    break;
                case DOWN:
                    app.image(close_waka, current_position.getX() - OFFSET,current_position.getY() - OFFSET);
                    break;
            }
            return true;
        }
    }
}
