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
   private boolean vsAI;
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
   public GameMain(boolean vsAI) {
      this.vsAI=vsAI;
      // This JPanel fires MouseEvent
      super.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            int mouseX = e.getX();
            int mouseY = e.getY();
            int row = (mouseY - Board.Y_OFFSET) / Cell.SIZE;
            int col = (mouseX - Board.X_OFFSET) / Cell.SIZE;

            if (currentState == State.PLAYING) {
               SoundEffect.CLICK.play();
               if (currentPlayer == Seed.CROSS) { // Giliran pemain
                     if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                           && board.cells[row][col].content == Seed.NO_SEED) {
                        currentState = board.stepGame(currentPlayer, row, col);
                        repaint(); // Refresh tampilan papan permainan

                        if (currentState != State.PLAYING) {
                           SwingUtilities.invokeLater(() -> {
                                 showEndGameDialog(getEndGameMessage());
                           });
                        } else {
                           currentPlayer = Seed.NOUGHT; // Ganti giliran ke pemain berikutnya
                           if (ai != null) {
                                 aiMove(); // Panggil langkah AI jika bermain melawan AI
                           }
                        }
                     }
               } else { // Giliran pemain kedua
                     if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                           && board.cells[row][col].content == Seed.NO_SEED) {
                        currentState = board.stepGame(currentPlayer, row, col);
                        repaint(); // Refresh tampilan papan permainan

                        if (currentState != State.PLAYING) {
                           SwingUtilities.invokeLater(() -> {
                                 showEndGameDialog(getEndGameMessage());
                           });
                        } else {
                           currentPlayer = Seed.CROSS; // Kembali ke pemain pertama
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
      if (vsAI) {
         // Jika bermain melawan AI, inisialisasi AI
         this.ai = new AI(board);
     } else {
         // Jika bermain melawan pemain lain, tidak perlu AI
         this.ai = null; // Atau bisa diatur sesuai kebutuhan
      }
 
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
   public void paintComponent(Graphics g) {
       super.paintComponent(g);
       setBackground(COLOR_BG); // Set warna latar belakang
       board.paint(g); // Panggil metode paint di Board untuk menggambar papan permainan
   
       // Perbarui status bar berdasarkan status permainan
       if (currentState == State.PLAYING) {
           statusBar.setForeground(Color.BLACK);
           statusBar.setText((currentPlayer == Seed.CROSS) ? "White's Turn" : "Black's Turn");
       } else if (currentState == State.DRAW) {
           statusBar.setForeground(Color.RED);
           statusBar.setText("It's a Draw!");
           SoundEffect.DRAW.play();
       } else if (currentState == State.CROSS_WON) {
           statusBar.setForeground(Color.RED);
           statusBar.setText("Player Won!");
           SoundEffect.WIN.play();
       } else if (currentState == State.NOUGHT_WON) {
           statusBar.setForeground(Color.RED);
           statusBar.setText("Computer Won!");
           SoundEffect.WIN.play();
       }
   }
   
   
   public static void play(boolean vsAI) {
      javax.swing.SwingUtilities.invokeLater(() -> {
          JFrame frame = new JFrame(TITLE);
          frame.setContentPane(new GameMain(vsAI)); // Pass the mode to GameMain
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.pack();
          frame.setLocationRelativeTo(null);
          frame.setVisible(true);
      });
   }
   private void aiMove() {
      if (currentState == State.PLAYING) {
         new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                  Thread.sleep(350); // Tambahkan delay untuk AI
                  return null;
            }
            @Override
            protected void done() {
                  int[] bestMove = ai.getBestMove(); // Dapatkan langkah terbaik dari AI
                  System.out.println("AI memilih langkah: (" + bestMove[0] + ", " + bestMove[1] + ")");
                  currentState = board.stepGame(currentPlayer, bestMove[0], bestMove[1]); // Jalankan langkah AI
                  currentPlayer = Seed.CROSS; // Ganti giliran ke pemain
                  repaint(); // Refresh tampilan papan permainan
                  // Tampilkan popup jika permainan selesai
                  if (currentState != tictactoegui.State.PLAYING) {
                     SwingUtilities.invokeLater(() -> {
                        showEndGameDialog(getEndGameMessage());
                     });
                  }
            }
         }.execute();
      }
   }
   private void showEndGameDialog(String message) {
      // Create a custom dialog for modern aesthetics
      JDialog endGameDialog = new JDialog((Frame) null, "Game Over", true);
      endGameDialog.setSize(400, 200);
      endGameDialog.setLocationRelativeTo(this);
      endGameDialog.setLayout(new BorderLayout());
  
      // Header panel with message
      JPanel headerPanel = new JPanel();
      headerPanel.setBackground(new Color(50, 50, 50));
      JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>" + message + "<br>What would you like to do?</div></html>");
      messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
      messageLabel.setForeground(Color.WHITE);
      messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
      headerPanel.add(messageLabel);
  
      // Button panel for options
      JPanel buttonPanel = new JPanel();
      buttonPanel.setBackground(new Color(30, 30, 30));
      buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
  
      JButton continueButton = new JButton("Continue");
      continueButton.setFont(new Font("Arial", Font.BOLD, 14));
      continueButton.setBackground(new Color(50, 200, 50));
      continueButton.setForeground(Color.WHITE);
      continueButton.setFocusPainted(false);
      continueButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
      continueButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
  
      JButton newGameButton = new JButton("New Game");
      newGameButton.setFont(new Font("Arial", Font.BOLD, 14));
      newGameButton.setBackground(new Color(200, 50, 50));
      newGameButton.setForeground(Color.WHITE);
      newGameButton.setFocusPainted(false);
      newGameButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
      newGameButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
  
      continueButton.addActionListener(e -> {
          endGameDialog.dispose(); // Close dialog
          // Reset game state and continue
          newGame(); // Reset game
          repaint(); // Refresh game board
      });
  
      newGameButton.addActionListener(e -> {
          endGameDialog.dispose(); // Close dialog
          SwingUtilities.invokeLater(() -> {
              WelcomeScreen welcomeScreen = new WelcomeScreen();
              welcomeScreen.setVisible(true);
          });
          JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
          topFrame.dispose(); // Close the current game window
      });
  
      buttonPanel.add(continueButton);
      buttonPanel.add(newGameButton);
  
      // Add panels to dialog
      endGameDialog.add(headerPanel, BorderLayout.CENTER);
      endGameDialog.add(buttonPanel, BorderLayout.SOUTH);
  
      // Display the dialog
      endGameDialog.setVisible(true);
  }
  
  
  
  private String getEndGameMessage() {
   switch (currentState) {
       case CROSS_WON:
           return "Black Won!";
       case NOUGHT_WON:
           return "White Won!";
       case DRAW:
           return "It's a Draw!";
       default:
           return "";
   }
}
}