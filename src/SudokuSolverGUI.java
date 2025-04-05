import javax.swing.*;  // For Swing components
import java.awt.*;     // For Layouts, Colors, Fonts
import javax.swing.SwingWorker;
import javax.swing.JOptionPane;
import java.util.List;

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

                int top = (row % 3 == 0) ? 3 : 1;
                int left = (col % 3 == 0) ? 3 : 1;
                int bottom = (row == 8) ? 3 : 1;
                int right = (col == 8) ? 3 : 1;

                cells[row][col].setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));
                gridPanel.add(cells[row][col]);
            }
        }
        JButton solveButton = new JButton("Solve");
        solveButton.setFont(new Font("Arial", Font.BOLD, 20));
        solveButton.addActionListener(e -> {
            int[][] board = new int[SIZE][SIZE];

            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    String text = cells[row][col].getText();
                    board[row][col] = text.isEmpty() ? 0 : Integer.parseInt(text);
                }
            }

            solveButton.setEnabled(false);

            new SwingWorker<Boolean, int[][]>() {
                @Override
                protected Boolean doInBackground() {
                    return SudokuSolver.solveBoard(board, this::publish);  // this::publish sends updates to process()
                }

                @Override
                protected void process(List<int[][]> chunks) {
                    int[][] latest = chunks.get(chunks.size() - 1);
                    updateBoard(latest);  // Custom method to update GUI
                }

                private void updateBoard(int[][] board) {
                    for (int row = 0; row < SIZE; row++) {
                        for (int col = 0; col < SIZE; col++) {
                            if (board[row][col] != 0) {
                                cells[row][col].setText(String.valueOf(board[row][col]));
                            } else {
                                cells[row][col].setText("");
                            }
                        }
                    }
                }

                @Override
                protected void done() {
                    try {
                        if (get()) {
                            JOptionPane.showMessageDialog(frame, "Sudoku solved!");
                        } else {
                            JOptionPane.showMessageDialog(frame, "No solution exists!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.execute();
        });

        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(solveButton, BorderLayout.SOUTH);

        frame.setSize(500, 550);
        frame.setVisible(true);

    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SudokuSolverGUI::new);
    }
}

