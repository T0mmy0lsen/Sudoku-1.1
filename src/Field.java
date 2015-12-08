import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Field {

    public static final int SIZE = 9;
    public int model[][];

    //field constructor
    public Field() {
        this.model = new int[SIZE][SIZE];
    }

    //import file to scanner
    public void fromFile() {
        try {
            JFrame myFrame = new JFrame();
            myFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            myFrame.pack();
            myFrame.setVisible(false);
            JFileChooser selectedFile = new JFileChooser();
            selectedFile.showOpenDialog(myFrame);
            File fileWithSudoku = selectedFile.getSelectedFile();
            Scanner sc = new Scanner(new FileReader(fileWithSudoku));
            fromScanner(sc, 0, 0);
        } catch (FileNotFoundException e) {
            System.out.println(e.toString());
        }
    }
    //from model cell to string
    public String getString(int i, int j){
        String string;
        int val = this.model[i][j];
        string = (val > 0 ? val+ "":"");
        return string;
    }
    //from scanner to model
    private void fromScanner(Scanner sc, int i, int j) {
        if (i >= SIZE) {
        } else if (j >= SIZE) {
            // this row done - go to next!
            fromScanner(sc, i+1, 0);
        } else {
            try {
                int val = Integer.parseInt(sc.next());
                this.model[i][j] = val;
            } catch (NumberFormatException e) {
                // do nothing
            }
            fromScanner(sc, i, j+1);
        }
    }
    //if col, row, and box is accepted, set value
    public boolean tryValue(int val, int i, int j) {
        if (!checkRow(val, j)) {
            return false;
        }
        if (!checkCol(val, i)) {
            return false;
        }
        if (!checkBox(val, i, j)) {
            return false;
        }
        this.model[i][j] = val;
        return true;
    }
    //checks if the cell at row i and column j is empty, i.e., whether it contains 0
    public boolean isEmpty(int i, int j) {
        return (this.model[i][j] == 0);
    }
    //clears one cell, sets = 0
    public void setValue(int i, int j) {
        this.model[i][j] = 0;
    }
    //clears model, sets = 0
    public void clearModel() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.model[i][j] = 0;
            }
        }
    }
    //checks if val is an acceptable value for the row i
    private boolean checkCol(int val, int i) {
        for (int j = 0; j < 9; j++ ) {
            if (val == this.model[i][j]) {
                return false;
            }
        }
        return true;
    }
    //checks if val is an acceptable value for the column j
    private boolean checkRow(int val, int j) {
        for (int i = 0; i < 9; i++ ) {
            if (val == this.model[i][j]) {
            return false;
            }
        }
        return true;
    }
    //checks if val is an acceptable value for the box around the cell at row i and column j
    private boolean checkBox(int val, int i, int j) {
        for (int x = (i/3)*3; x < ((i/3)*3) + 3; x++) {
            for (int y = (j / 3) * 3; y < ((j / 3) * 3) + 3; y++) {
                if (val == this.model[x][y]) {
                    return false;
                }
            }
        }
        return true;
    }
    //solves the sudoku
    public boolean solve(int i, int j) throws SolvedException {
        if (j == 9) {
            j = 0; i++;
        }
        try { if (!(isEmpty(i,j)))
            return solve(i,j+1);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return true;
        }
        for (int val = 1; val < 10; val++) {
            if (tryValue(val, i, j)) {
                if (solve(i, j+1)) {
                    return true;
                }
            }
        }
        setValue(i,j);  return false;
    }
    //solution to string
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < SIZE; i++) {
            if (i % 3 == 0) {
                res.append("+-------+-------+-------+\n");
            }
            for (int j = 0; j < SIZE; j++) {
                if (j % 3 == 0) {
                    res.append("| ");
                }
                int val = this.model[i][j];
                res.append(val > 0 ? val+" " : "  ");
            }
            res.append("|\n");
        }
        res.append("+-------+-------+-------+");
        return res.toString();
    }
}
