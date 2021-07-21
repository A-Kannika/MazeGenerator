import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Create by Kannika Armstrong
 * TCSS342(Spring 2021): June 5, 2021
 * Assignment5 : Maze Generator
 * Professor. Christopher Paul Marriott
 */
public class Maze {

    private int width; // Maze grid column
    private int height; // Maze grid row
    private boolean debug; // Debug mode control
    private char[][] myMaze; // Maze grid; height x width (row x column)
    private boolean[][] validPoint; // Use to check the valid point
    private ArrayList<Point> visited; // List of visited point
    private ArrayList<Point> mazePoint; // List of all point
    private Random rand = new Random(); // Use to random choose the point

    public Maze(int n, int m, boolean debug) {
        this.height = 2 * m + 1;
        this.width = 2 * n + 1;
        this.debug = debug;
        myMaze = new char[height][width];
        validPoint = new boolean[height][width];
        visited = new ArrayList<>();
        mazePoint = new ArrayList<>();
        buildMaze();
    }

    /**
     * buildGraph - this method builds the initial graphG(width,height).
     * Create n x m Maze
     */
    private void buildGraph() {
        for (int n = 0; n < height; n++) {
            for (int m = 0; m < width; m++) {
                if (n % 2 == 0 || m % 2 == 0) {
                    myMaze[n][m] = 'X';
                    validPoint[n][m] = false;
                } else {
                    myMaze[n][m] = ' ';
                    validPoint[n][m] = true;
                    mazePoint.add(new Point(n,m));
                }
            }
        }
        myMaze[0][1] = ' '; // Entrance
        myMaze[height - 1][width - 2] = ' '; // Exit
    }

    /**
     * buildMaze - this method builds the maze from the graphG(width, height).
     * use DFS to build the maze
     */
    private void buildMaze() {
        buildGraph();
        Point current = new Point(1, 1); // Initial at starting point
        markVisited(current);
        Point end = new Point(height - 2, width - 2); // The end point
        ArrayList<Point> validMoves = new ArrayList<>(); // use to store the valid neighbor
        ArrayList<Point> path = new ArrayList<>(); // use to store all path

        while (!visited.isEmpty()){
            validMoves = checkValidPoint(current);
            int numberOfPoint = validMoves.size(); // number of valid point to move
            if (numberOfPoint != 0){
                if (debug) {
                    display();
                }
                int move = rand.nextInt(numberOfPoint);// random choose the point to move
                Point next = validMoves.get(move); // the the point from the random choose

                // remove wall (X) that we passed
                int x = (int)((current.getX() + next.getX()) / 2);
                int y = (int)((current.getY() + next.getY()) / 2);
                myMaze[x][y] = ' ';

                markVisited(next); // add next point into visited list

                current = next; // set current point to next point

                // store all visited point into path when found the end point
                if (current.equals(end)){
                    path = new ArrayList<>(visited);
                }
                // When all neighbors have visited, We will do the back track
                // by delete the current address (the last point in the list)
                // then set the current address back to the previous address
            } else {
                visited.remove(visited.size() - 1);
                if (!visited.isEmpty()){
                    current = visited.get(visited.size() - 1);
                }
            }
        }
        // Store all path in the visited list
        visited = new ArrayList<>(path);

        if (debug) { // when debug mode on
            display();
            for(int i = 0; i < mazePoint.size(); i++) {
                int x = (int) mazePoint.get(i).getX();
                int y = (int) mazePoint.get(i).getY();
                myMaze[x][y] = ' ';
            }
        }
    }

    /**
     * Check the valid neighbor to move
     */
    private ArrayList<Point> checkValidPoint(Point point) {
        ArrayList<Point> unVisited =  new ArrayList<>();
        int x = (int) point.getX();
        int y = (int) point.getY();
        // checks upper
        if (x - 2 >= 1) {
            if (validPoint[x - 2][y]) {
                unVisited.add(new Point(x - 2, y));
            }
        }
        // checks lower
        if (x + 2 < height) {
            if (validPoint[x + 2][y]) {
                unVisited.add(new Point(x + 2, y));
            }
        }
        // checks left
        if (y - 2 >= 1) {
            if (validPoint[x][y - 2]) {
                unVisited.add(new Point(x, y - 2));
            }
        }
        // checks right
        if (y + 2 < width) {
            if (validPoint[x][y + 2]) {
                unVisited.add(new Point(x, y + 2));
            }
        }
        return unVisited;
    }

    /**
     * Use to mark visited vertices
     */
    private void markVisited(Point point) {
        int x = (int) point.getX();
        int y = (int) point.getY();
        if (debug) {
            myMaze[x][y] = 'V'; // mark the visited point with V
        }
        validPoint[x][y] = false; // visited point is invalid to move
        visited.add(point); // add the point in to visited list
        mazePoint.add(point); // add all grid points in of the maze in the list
    }

    /**
     * solveMaze - this method solves the maze once built by finding the single path from the entrance to the exit.
     * f you solved the maze while you built the maze youcan make this method empty
     * (we call this a stub usually) or trivial (make the solution appear).
     */
    private void solveMaze() {

    }

    /**
     * Use to display the maze
     */
    public void display() {
        for (int n = 0; n < height; n++) {
            for (int m = 0; m < width; m++) {
                System.out.print(myMaze[n][m] + " ");
            }
            System.out.println();
        }
        System.out.println("\n");
    }

    /**
     * displays the maze in a readable way
     */
    public String toString() {
        while (!visited.isEmpty()) {
            int x = (int) visited.get(visited.size() - 1).getX();
            int y = (int) visited.get(visited.size() - 1).getY();
            myMaze[x][y] = '+';
            visited.remove(visited.size() - 1);
        }
        display();
        return "";
    }

    /**
     * Use to test the Maze
     */

    public static void main(String[] args) {
        //Test to see the DFS -- turn on the debug mode
        Maze maze = new Maze(4,4,true);
        maze.toString();
        //Test to see the final result -- turn off the debug mode
        Maze maze2 = new Maze(10,10,false);
        maze2.toString();
    }
}