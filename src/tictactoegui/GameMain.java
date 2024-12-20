package tictactoegui;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * Tic-Tac-Toe: Two-player Graphic version with better OO design.
 * The Board and Cell classes are separated in their own classes.
 */
public class GameMain extends JPanel {
   private static final long serialVersionUID = 1L; // to prevent serializable warning

   // Define named onstants for the drawing graphics
   public static final String TITLE = "Tic Tac Toe";
   public static final Color COLOR_BG = Color.WHITE;
   public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
   public static final Color COLOR_CROSS = new Color(239, 105, 80);  // Red #EF6950
   public static final Color COLOR_NOUGHT = new Color(64, 154, 225); // Blue #409AE1
   public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

   // Define game objects
   private Board board;         // the game board
   private State currentState;  // the current state of the game
   private Seed currentPlayer;  // the current player
   private JLabel statusBar;    // for displaying status message

   AI ai = new AI(board); // initialize ai

   /** Constructor to setup the UI and game components */
   public GameMain() {

      // This JPanel fires MouseEvent
      super.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            int mouseX = e.getX();
            int mouseY = e.getY();
            int row = (mouseY - Board.Y_OFFSET) / Cell.SIZE;
            int col = (mouseX - Board.X_OFFSET) / Cell.SIZE;
      
            if (currentState == State.PLAYING) {
               if (currentPlayer == Seed.CROSS) { // Giliran pemain
                  if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                        && board.cells[row][col].content == Seed.NO_SEED) {
                     currentState = board.stepGame(currentPlayer, row, col);
                     currentPlayer = Seed.NOUGHT; // Ganti giliran ke AI
                     repaint();
                     if (currentState == State.PLAYING) { // Pastikan permainan belum selesai
                        aiMove();
                     }
                  }
               }
            } else { // Game selesai
               newGame(); // Restart game
            }
         }
      });

      // Setup the status bar (JLabel) to display status message
      statusBar = new JLabel();
      statusBar.setFont(FONT_STATUS);
      statusBar.setBackground(COLOR_BG_STATUS);
      statusBar.setOpaque(true);
      statusBar.setPreferredSize(new Dimension(300, 30));
      statusBar.setHorizontalAlignment(JLabel.LEFT);
      statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

      super.setLayout(new BorderLayout());
      super.add(statusBar, BorderLayout.PAGE_END); // same as SOUTH
      super.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
            // account for statusBar in height
      super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

      // Set up Game
      initGame();
      newGame();
   }

   /** Initialize the game (run once) */
   public void initGame() {
      board = new Board();
      ai = new AI(board); // Buat instance AI dengan papa
   }

   /** Reset the game-board contents and the current-state, ready for new game */
   public void newGame() {
      for (int row = 0; row < Board.ROWS; ++row) {
         for (int col = 0; col < Board.COLS; ++col) {
            board.cells[row][col].content = Seed.NO_SEED; // all cells empty
         }
      }
      currentPlayer = Seed.CROSS;    // cross plays first
      currentState = State.PLAYING;  // ready to play
   }

   /** Custom painting codes on this JPanel */
   @Override
   public void paintComponent(Graphics g) {  // Callback via repaint()
      super.paintComponent(g);
      setBackground(COLOR_BG); // set its background color

      board.paint(g);  // ask the game board to paint itself

      // Print status-bar message
      if (currentState == State.PLAYING) {
         statusBar.setForeground(Color.BLACK);
         statusBar.setText((currentPlayer == Seed.CROSS) ? "X's Turn" : "O's Turn");
      } else if (currentState == State.DRAW) {
         statusBar.setForeground(Color.RED);
         statusBar.setText("It's a Draw! Click to play again.");
      } else if (currentState == State.CROSS_WON) {
         statusBar.setForeground(Color.RED);
         statusBar.setText("'X' Won! Click to play again.");
      } else if (currentState == State.NOUGHT_WON) {
         statusBar.setForeground(Color.RED);
         statusBar.setText("'O' Won! Click to play again.");
      }
   }

   /** The entry "main" method */
   public static void play() {
      javax.swing.SwingUtilities.invokeLater(() -> {
         JFrame frame = new JFrame(TITLE);
         frame.setContentPane(new GameMain());
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.pack();
         frame.setLocationRelativeTo(null);
         frame.setVisible(true);
      });
}
  
   private void aiMove() {
      if (currentState == State.PLAYING) { // Pastikan game belum selesai
         new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                  Thread.sleep(600); // Tambahkan delay hanya untuk AI
                  return null;
            }

            @Override
            protected void done() {
                  int[] bestMove = ai.getBestMove(); // Dapatkan langkah terbaik dari AI
                  System.out.println("AI memilih langkah: (" + bestMove[0] + ", " + bestMove[1] + ")");
                  currentState = board.stepGame(currentPlayer, bestMove[0], bestMove[1]); // Jalankan langkah AI
                  currentPlayer = Seed.CROSS; // Ganti giliran ke pemain
                  repaint(); // Refresh tampilan game
            }
         }.execute();
      }
   }


}

   

