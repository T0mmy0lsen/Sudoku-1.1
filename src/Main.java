import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class Main extends JFrame{

    private JTextField grid[][];

    private JFrame jFrame = new JFrame("Sudoku Solver");
    private JPanel jButton = new JPanel();
    private JPanel jGrid = new JPanel();

    private Button solve = new Button();
    private Button upload = new Button();
    private Button clear = new Button();

    //makes object of Field.class and Main.class
    public static void main(String[] args) {
        Field f = new Field();
        new Main(f);
    }

    //swing gui / constructor
    public Main(final Field f){
        super("Sudoku Solver");
        URL iconURL = getClass().getResource("icon.png");
        ImageIcon img = new ImageIcon(iconURL);
        jFrame.setIconImage(img.getImage());
        Color darkBlue = new Color(51,102,153);
        Color lightBlue = new Color(18,61,104);

        grid = new JTextField[9][9];
        jGrid.setLayout(new GridLayout(9, 9));
        jGrid.setBorder(BorderFactory.createMatteBorder(5,5,5,5, darkBlue));
        jGrid.setBackground(darkBlue);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++)  {
                grid[i][j] = new JTextField();
                grid[i][j].setDocument(new JTextFieldLimit(1));
                grid[i][j].setFont(new Font("Arial", Font.PLAIN, 20));
                grid[i][j].setHorizontalAlignment(JTextField.CENTER);
                grid[i][j].setText("");
                grid[i][j].setBorder(BorderFactory.createMatteBorder(1,1,1,1,lightBlue));
                if (j == 2 || j == 5){grid[i][j].setBorder(BorderFactory.createMatteBorder(1,1,1,5,lightBlue));}
                if (i == 2 || i == 5){grid[i][j].setBorder(BorderFactory.createMatteBorder(1,1,5,1,lightBlue));}
                if (i == 2 && j == 2){grid[i][j].setBorder(BorderFactory.createMatteBorder(1,1,5,5,lightBlue));}
                if (i == 5 && j == 2){grid[i][j].setBorder(BorderFactory.createMatteBorder(1,1,5,5,lightBlue));}
                if (i == 2 && j == 5){grid[i][j].setBorder(BorderFactory.createMatteBorder(1,1,5,5,lightBlue));}
                if (i == 5 && j == 5){grid[i][j].setBorder(BorderFactory.createMatteBorder(1,1,5,5,lightBlue));}
                jGrid.add(grid[i][j]);
            }
        }

        jButton.setLayout(new FlowLayout());
        jButton.setBackground(darkBlue);
        jButton.setPreferredSize(new Dimension(420,45));

        jButton.add(solve);
        solve.setLabel("Solve");
        solve.setVisible(true);
        solve.setFont(new Font("HelveticaNeue", Font.PLAIN, 20));
        solve.setForeground(Color.BLACK);

        jButton.add(upload);
        upload.setLabel("Upload");
        upload.setVisible(true);
        upload.setFont(new Font("Arial", Font.PLAIN, 20));
        upload.setForeground(Color.BLACK);

        jButton.add(clear);
        clear.setLabel("Clear");
        clear.setVisible(true);
        clear.setFont(new Font("Arial", Font.PLAIN, 20));
        clear.setForeground(Color.BLACK);

        jGrid.setPreferredSize(new Dimension(400,400));

        jFrame.setResizable(false);
        jFrame.setVisible(true);
        jFrame.setLocationRelativeTo(null);
        jFrame.setSize(420,480);
        jFrame.setBackground(darkBlue);

        jFrame.add(jGrid, BorderLayout.PAGE_START);
        jFrame.add(jButton, BorderLayout.PAGE_END);

        ActionListener solvedClicked = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                makeGridWhite();
                gridToModel(f);
                if (!errorFound(f)) {
                    runSolve(f);
                    makeGrid(f, false);
                    solve.setEnabled(false);
                }
            }
        };

        ActionListener uploadClicked = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                makeGridWhite();
                f.clearModel();
                makeGrid(f, true);
                solve.setEnabled(true);
                f.fromFile();
                makeGrid(f, true);

            }
        };

        ActionListener clearClicked = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                makeGridWhite();
                f.clearModel();
                makeGrid(f, true);
                solve.setEnabled(true);
            }
        };

        solve.addActionListener(solvedClicked);
        upload.addActionListener(uploadClicked);
        clear.addActionListener(clearClicked);
    }

    public boolean errorFound(Field f){
        boolean bool = false;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!(f.isEmpty(i,j))) {
                    int val = f.model[i][j];
                    f.model[i][j] = 0;
                    for (int y = 0; y < 9; y++ ) {
                        if (val == f.model[i][y]) {
                            grid[i][y].setBackground(Color.red);
                            bool = true;
                        }
                    }
                    for (int x = 0; x < 9; x++ ) {
                        if (val == f.model[x][j]) {
                            grid[x][j].setBackground(Color.red);
                            bool = true;
                        }
                    }
                    for (int x = (i/3)*3; x < ((i/3)*3) + 3; x++) {
                        for (int y = (j / 3) * 3; y < ((j / 3) * 3) + 3; y++) {
                            if (val == f.model[x][y]) {
                                grid[x][y].setBackground(Color.red);
                                bool = true;
                            }
                        }
                    }
                    f.model[i][j] = val;
                }
            }
        }
        return bool;
    }
    //runs the solve method
    public static void runSolve(Field f){
        try {
            f.solve(0, 0);
        } catch (SolvedException e) {
            System.out.print("Error: " + e);
        }
    }
    //takes whats in the model and sends it to grid
    public void makeGrid(final Field f, final boolean isEditable){
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grid[i][j].setText(f.getString(i, j));
                grid[i][j].setEditable(isEditable);
            }
        }
    }
    //set all backgrounds to white
    public void makeGridWhite(){
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++)  {
                grid[i][j].setBackground(Color.white);

            }
        }
    }
    //takes what is in the grid and sends it to model
    public void gridToModel(Field f){
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                try {
                    String gridString = grid[i][j].getText();
                    int val = Integer.parseInt(gridString);
                    f.model[i][j] = val;
                    grid[i][j].setBackground(Color.lightGray);
                } catch (NumberFormatException e) {
                    f.model[i][j] = 0;
                }
            }
        }
    }
    //limits grid to only contain one number.
    class JTextFieldLimit extends PlainDocument {
        private int limit;

        //Added the following 2 lines
        String[] numbers = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        boolean isAccepted = false;
        //

        JTextFieldLimit(int limit) {
            super();
            this.limit = limit;
        }

        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            if (str == null)
                return;

            //And the following 2 lines
            for (String thisnumber : numbers) {
                isAccepted = str.equals(thisnumber);
                if (isAccepted) {
                    //
                    if ((getLength() + str.length()) <= limit) {
                        super.insertString(offset, str, attr);
                    }
                }
            }
        }
    }
    //deactivate/activate solve
}
