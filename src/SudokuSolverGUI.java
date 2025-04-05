import javax.swing.*;  // For Swing components
import java.awt.*;     // For Layouts, Colors, Fonts
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;  // For button action events

public class SudokuSolverGUI {
    private static final int SIZE = 9;
    private JTextField[][] cells = new JTextField[SIZE][SIZE];
    private JFrame frame;

    SudokuSolverGUI() {
        frame = new JFrame("Sudoku Solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        JPanel gridPanel = new JPanel(new GridLayout(SIZE, SIZE));
        Font cellFont = new Font("Arial", Font.BOLD, 20);

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col] = new JTextField(1);  // Create text field
                cells[row][col].setHorizontalAlignment(JTextField.CENTER); // Center text
                cells[row][col].setFont(cellFont);
                gridPanel.add(cells[row][col]);
                if ((row % 3 == 2 && row != 8) || (col % 3 == 2 && col != 8)) {
                    cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 3, 3, Color.BLACK));
                } else {
                    cells[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                }
            }
        }
        JButton solveButton = new JButton("Solve");
        solveButton.setFont(new Font("Arial", Font.BOLD, 20));
        solveButton.addActionListener(new SolveButtonListener());

        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(solveButton, BorderLayout.SOUTH);

        frame.setSize(500, 550);
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SudokuSolverGUI::new);
    }

    private class SolveButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int[][] board = new int[SIZE][SIZE];

            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    String text = cells[row][col].getText();
                    board[row][col] = text.isEmpty() ? 0 : Integer.parseInt(text);
                }
            }

            if (SudokuSolver.solveBoard(board)) {
                updateBoard(board);
            } else {
                JOptionPane.showMessageDialog(frame, "No solution exists!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void updateBoard(int[][] board) {
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    cells[row][col].setText(String.valueOf(board[row][col]));
                }
            }
        }
    }
}

