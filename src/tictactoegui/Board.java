package tictactoegui;
import java.awt.*;
/**
 * The Board class models the ROWS-by-COLS game board.
 */
public class Board {
   // Define named constants
   public static final int ROWS = 3;  // ROWS x COLS cells
   public static final int COLS = 3;
   // Define named constants for drawing
   public static final int CANVAS_WIDTH = Cell.SIZE * COLS + 500;  // the drawing canvas
   public static final int CANVAS_HEIGHT = Cell.SIZE * ROWS + 500;
   public static final int GRID_WIDTH = 1;  // Grid-line's width
   public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2; // Grid-line's half-width
   public static final Color COLOR_GRID = Color.BLACK;  // grid lines
   public static final int BOARD_WIDTH = Cell.SIZE * COLS; // Lebar papan
   public static final int BOARD_HEIGHT = Cell.SIZE * ROWS; // Tinggi papan
   public static final int X_OFFSET = (CANVAS_WIDTH - BOARD_WIDTH) / 2; // Offset horizontal
   public static final int Y_OFFSET = (CANVAS_HEIGHT - BOARD_HEIGHT) / 2; // Offset vertika
   // Define properties (package-visible)
   /** Composes of 2D array of ROWS-by-COLS Cell instances */
   Cell[][] cells;
   /** Constructor to initialize the game board */
   public Board() {
      initGame();
   }
   /** Initialize the game objects (run once) */
   public void initGame() {
      cells = new Cell[ROWS][COLS]; // allocate the array
      for (int row = 0; row < ROWS; ++row) {
         for (int col = 0; col < COLS; ++col) {
            // Allocate element of the array
            cells[row][col] = new Cell(row, col);
               // Cells are initialized in the constructor
         }
      }
   }
   /** Reset the game board, ready for new game */
   public void newGame() {
      for (int row = 0; row < ROWS; ++row) {
         for (int col = 0; col < COLS; ++col) {
            cells[row][col].newGame(); // clear the cell content
         }
      }
   }
   /**
    *  The given player makes a move on (selectedRow, selectedCol).
    *  Update cells[selectedRow][selectedCol]. Compute and return the
    *  new game state (PLAYING, DRAW, CROSS_WON, NOUGHT_WON).
    */
    public State stepGame(Seed player, int selectedRow, int selectedCol) {
      System.out.println("Step game called by: " + player + " at (" + selectedRow + ", " + selectedCol + ")");
   
      // Update game board
      cells[selectedRow][selectedCol].content = player;
      // Compute and return the new game state
      if (cells[selectedRow][0].content == player  // 3-in-the-row
            && cells[selectedRow][1].content == player
            && cells[selectedRow][2].content == player
         || cells[0][selectedCol].content == player // 3-in-the-column
            && cells[1][selectedCol].content == player
            && cells[2][selectedCol].content == player
         || selectedRow == selectedCol     // 3-in-the-diagonal
            && cells[0][0].content == player
            && cells[1][1].content == player
            && cells[2][2].content == player
         || selectedRow + selectedCol == 2 // 3-in-the-opposite-diagonal
            && cells[0][2].content == player
            && cells[1][1].content == player
            && cells[2][0].content == player) {
         return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
      } else {
         // Nobody wins. Check for DRAW or PLAYING.
         for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                  if (cells[row][col].content == Seed.NO_SEED) {
                     return State.PLAYING; // Masih ada kotak kosong
                  }
            }
         }
         return State.DRAW; // Tidak ada kotak kosong, game draw
      }
  }
  
   /** Paint itself on the graphics canvas, given the Graphics context */
   public void paint(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
  
      // Gambar border papan
      g2.setColor(COLOR_GRID);
      g2.setStroke(new BasicStroke(10)); // Atur ketebalan border
      g2.drawRect(X_OFFSET, Y_OFFSET, BOARD_WIDTH, BOARD_HEIGHT); // Gambar border papan
  
      // Gambar semua cell
      for (int row = 0; row < ROWS; ++row) {
          for (int col = 0; col < COLS; ++col) {
              cells[row][col].paint(g, X_OFFSET, Y_OFFSET); // Gambar cell dengan offset
          }
      }
  }
  
   public State getGameState() {
      // Periksa setiap baris
      for (int row = 0; row < ROWS; row++) {
          if (cells[row][0].content != Seed.NO_SEED
                  && cells[row][0].content == cells[row][1].content
                  && cells[row][1].content == cells[row][2].content) {
              return (cells[row][0].content == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
          }
      }
  
      // Periksa setiap kolom
      for (int col = 0; col < COLS; col++) {
         if (cells[0][col].content != Seed.NO_SEED
                 && cells[0][col].content == cells[1][col].content
                 && cells[1][col].content == cells[2][col].content) { 
             return (cells[0][col].content == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
         }
     }
  
      // Periksa diagonal utama
      if (cells[0][0].content != Seed.NO_SEED
              && cells[0][0].content == cells[1][1].content
              && cells[1][1].content == cells[2][2].content) {
          return (cells[0][0].content == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
      }
  
      // Periksa diagonal lainnya
      if (cells[0][2].content != Seed.NO_SEED
              && cells[0][2].content == cells[1][1].content
              && cells[1][1].content == cells[2][0].content) {
          return (cells[0][2].content == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
      }
  
      // Periksa apakah ada kotak kosong
      for (int row = 0; row < ROWS; row++) {
          for (int col = 0; col < COLS; col++) {
              if (cells[row][col].content == Seed.NO_SEED) {
                  return State.PLAYING; // Masih ada langkah yang bisa diambil
              }
          }
      }
  
      return State.DRAW; // Semua kotak penuh, tidak ada pemenang
  }
  
}