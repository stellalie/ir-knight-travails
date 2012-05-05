import static org.junit.Assert.*;
import org.junit.Test;
import java.util.LinkedList;

public class BoardTest {
    
    @Test
    public void noTravelTest() {
        Board board = new Board();
        String output = this.getSolutionsLines(board.getKnightTravailsSolutions(new Move("a3 a3", board)), board);
        assert(output.equals("No travel required\n"));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void invalidInputTest() throws IllegalArgumentException {
        Board board = new Board();
        String output = this.getSolutionsLines(board.getKnightTravailsSolutions(new Move("hello", board)), board);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void outOfBoardDimensionInputTest() throws IllegalArgumentException {
        Board board = new Board();
        String output = this.getSolutionsLines(board.getKnightTravailsSolutions(new Move("a9 a1", board)), board);
    }
    
    @Test
    public void correctSolutionTest() {
        Board board = new Board();
        String output = this.getSolutionsLines(board.getKnightTravailsSolutions(new Move("a5 a3", board)), board);
        assert(output.equals("C4 A3 \n"));
    }
    
    @Test
    public void correctMultipleSolutionTest() {
        Board board = new Board();
        String output = this.getSolutionsLines(board.getKnightTravailsSolutions(new Move("f4 f5", board)), board);
        System.err.print(output);
        assert(output.equals(
            "E2 G3 F5 \n" + 
            "E2 D4 F5 \n" +
            "G2 E3 F5 \n" +
            "G2 H4 F5 \n" +
            "D5 E3 F5 \n" + 
            "D5 E7 F5 \n" +
            "H5 G3 F5 \n" +
            "H5 G7 F5 \n" +
            "E6 D4 F5 \n" +
            "E6 G7 F5 \n" +
            "G6 H4 F5 \n" +
            "G6 E7 F5 \n" ));
    }
    
    private String getSolutionsLines(LinkedList<LinkedList<Square>> solutions, Board board) {
        String line = "";
        for (LinkedList<Square> solution: solutions) {
            line += board.getSolutionLine(solution) + "\n";
        }
        return line;
    }


}
