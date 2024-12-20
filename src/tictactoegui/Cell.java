package tictactoegui;
import java.awt.*;
/**
 * The Cell class models each individual cell of the game board.
 */
public class Cell {
   // Define named constants for drawing
   public static final int SIZE = 120; // cell width/height (square)
   // Symbols (cross/nought) are displayed inside a cell, with padding from border
   public static final int PADDING = SIZE / 5;
   public static final int SEED_SIZE = SIZE - PADDING * 2;
   // Define properties (package-visible)
   /** Content of this cell (Seed.EMPTY, Seed.CROSS, or Seed.NOUGHT) */
   Seed content;
   /** Row and column of this cell */
   int row, col;
   /** Constructor to initialize this cell with the specified row and col */
   public Cell(int row, int col) {
      this.row = row;
      this.col = col;
      content = Seed.NO_SEED;
   }
   /** Reset this cell's content to EMPTY, ready for new game */
   public void newGame() {
      content = Seed.NO_SEED;
   }
   /** Paint itself on the graphics canvas, given the Graphics context */
   public void paint(Graphics g, int xOffset, int yOffset) {
      int x1 = xOffset + col * SIZE; // Posisi horizontal dengan offset
      int y1 = yOffset + row * SIZE; // Posisi vertikal dengan offset
  
      // Gambar latar belakang cell dengan pola catur
      if ((row + col) % 2 == 0) {
          g.setColor(Color.WHITE); // Warna putih untuk cell genap
      } else {
          g.setColor(Color.BLACK); // Warna hitam untuk cell ganjil
      }
      g.fillRect(x1, y1, SIZE, SIZE); // Gambar latar cell
  
      //Isi cell dengan gambar seusai turn
      if (content != Seed.NO_SEED && content.getImage() != null) {
         g.drawImage(content.getImage(), x1 + PADDING, y1 + PADDING, SEED_SIZE, SEED_SIZE, null);
     }
  }   
  
  
}