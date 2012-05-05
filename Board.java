import java.util.Scanner;
import java.util.LinkedList;

public class Board {
    private final int BOARD_WIDTH = 8;
    private final int BOARD_HEIGHT = 8;
    private LinkedList<Square> squares = new LinkedList();

    public Board() {
        this.build();
    }
    
    private void build() {
        this.squares.clear();
        for (int y = 1; y <= BOARD_HEIGHT; y++) {
            for (int x = 1; x <= BOARD_WIDTH; x++) {
                this.squares.add(new Square(x, y, new Blank()));
            }
        }
    }

    public void play() {
        this.printWelcome();
        while (true) {

            // build or re-build board from previous play
            this.build();

            // prompt and validates move input, then creates a move object
            Move move = null;
            while (move == null) {
                try {
                    move = new Move(this.readMove(), this);
                } catch (IllegalArgumentException e) {
                    System.out.println("ERROR: " + e.getMessage() + " Please try again.");
                }
            }

            // set our source square with a knight piece, solve move, and print our boards + solutions
            move.getSource().setPiece(new Knight());
            LinkedList<LinkedList<Square>> solutions = getKnightTravailsSolutions(move);
            this.print(solutions);
        }
    }

    private String readMove() {
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println();
        System.out.println("=======================================================================================");
        System.out.print("MOVES: ");
        return scanner.nextLine();
    }
    
    public Square getSquare(int x, int y) {
        for (Square square: squares) {
            if (square.matches(x, y)) return square;
        }
        return null;
    }

    // ------------------------------------------------------------------------------------------------
    // KNIGHT TRAVAILS FUNCTIONS
    // ------------------------------------------------------------------------------------------------

    public LinkedList<LinkedList<Square>> getKnightTravailsSolutions(Move move) {

        // breadth-first search implementation
        // return a list of shortest path solution, each solution contains a list of squares it travels
        LinkedList<LinkedList<Square>> solutions = new LinkedList();
        LinkedList<LinkedList<Square>> queue = new LinkedList();
        LinkedList<Square> visited = new LinkedList();
        boolean solutionFound = false;

        // initialize first path, our very first path is the source square of the move
        // this is technically a path, imagine in a case where our source and destination is the same square
        LinkedList<Square> firstPath = new LinkedList();
        firstPath.add(move.getSource());
        queue.add(firstPath);

        while (!queue.isEmpty()) {
            LinkedList<Square> currentPath = queue.removeFirst();
            Square currentSquare = currentPath.getLast();

            if (currentSquare == move.getDestination()) {
                solutions.add(currentPath);
                solutionFound = true;
            }

            if (!solutionFound) {
                for (Square nextLegalSquare: this.getKnightNextLegalMoves(currentSquare)) {
                    if (!visited.contains(nextLegalSquare)) {
                        LinkedList<Square> nextPath = new LinkedList();
                        nextPath.addAll(currentPath);
                        nextPath.add(nextLegalSquare);
                        queue.addLast(nextPath);
                    }
                }
            }

            if (!visited.contains(currentSquare)) visited.add(currentSquare);
        }

        return solutions;
    }

    private LinkedList<Square> getKnightNextLegalMoves(Square source) {

        // brute force search to find next legal squares to all squares on board
        // returns a list of possible next move squares given a source square
        LinkedList<Square> nextMoves = new LinkedList();
        for (Square destination: squares) {
            int xMoveDistance = destination.getX() - source.getX();
            int yMoveDistance = destination.getY() - source.getY();

            // since a knight hops over other pieces, we just need to ensure no piece  exist within our destination square
            // and, whether we moved 2 then 1 space or 1 then 2 spaces
            if ((destination.getPiece().isBlank()) && (Math.abs(xMoveDistance * yMoveDistance) == 2)) {   
                nextMoves.add(destination); 
            }
        }
        return nextMoves;
    }

    // ------------------------------------------------------------------------------------------------
    // PRINTING FUNCTIONS
    // ------------------------------------------------------------------------------------------------

    private void printWelcome() {
        System.out.println();
        System.out.println();
        System.out.println("=======================================================================================");
        System.out.println("The Knight's Travails Challenge");
        System.out.println("=======================================================================================");
        System.out.println();
        System.out.println("Welcome! :) I accept two squares identified by algebraic chess notation.");
        System.out.println("The first square is the starting position and the second square is the ending position.");
        System.out.println("I will then find the shortest sequence of valid moves to take a Knight piece from the");
        System.out.println("starting position to the ending solution.");
        System.out.println();
        System.out.println("Example input would be: A8 B7");
    }

    private void print(LinkedList<LinkedList<Square>> solutions) {
        if (solutions.isEmpty()) {
            System.out.println(" SOLUTION #1: No solution exists");
        } else {
            for (LinkedList<Square> solution: solutions) {
                System.out.println(this.getBoardLine(solution));
                System.out.println(" SOLUTION #" + ((int)solutions.indexOf(solution) + 1) + ": " + this.getSolutionLine(solution));
            }
        }
    }

    public String getSolutionLine(LinkedList<Square> solution) {
        String line = "";
        if (solution.getFirst() == solution.getLast()) {
            line += "No travel required";
        } else {
            for (Square square: solution) {
                if (square != solution.getFirst()) line += square.toChessNotation() + " ";
            }
        }
        return line;
    }

    private String getBoardLine(LinkedList<Square> solution) {
        String line = "\n";
        line += this.getBoardTopLine() + "\n";
        line += this.getBoardMiddleLine() + "\n";
        for (int y = 1; y <= this.BOARD_HEIGHT; y++) {
            for (int x = 1; x <= this.BOARD_WIDTH; x++) {
                Square square = this.getSquare(x, y);
                if (!square.getPiece().isBlank()) {
                    line += " | " + square.getPiece().toChessNotation();
                } else if (solution.contains(square)) {
                    line += " | " + solution.indexOf(square);
                } else {
                    line += " | " + square.getPiece().toChessNotation();
                }
            }
            line += " | " + y + "\n";
            line += this.getBoardMiddleLine() + "\n";
        }
        return line;
    }

    private String getBoardMiddleLine() {
        String line = " ";
        for (int i = 0; i < this.BOARD_WIDTH; i++) {
            line += "+---";
        }
        line += "";
        return line;
    }

    private String getBoardTopLine() {
        String line = " ";
        char startChar = 'a';
        for (int i = 0; i < this.BOARD_WIDTH; i++) {
            line += "  " + startChar + " ";
            startChar++;
        }
        return line;
    }
}
