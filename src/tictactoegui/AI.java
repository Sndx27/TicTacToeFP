/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #7
 * 1 - 5026231066 - Burju Ferdinand Harianja
 * 2 - 5026231132 - Clay Amsal Sebastian Hutabarat
 * 3 - 5026213181 - Sandythia Lova Ramadhani Krisnaprana
 */

package tictactoegui;

public class AI {
    private Board board;

    public AI(Board board) {
        this.board = board;
    }

    public int[] getBestMove() {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = new int[2]; // Menyimpan langkah terbaik [row, col]

        for (int row = 0; row < Board.ROWS; row++) {
            for (int col = 0; col < Board.COLS; col++) {
                if (board.cells[row][col].content == Seed.NO_SEED) { // Cek kotak kosong
                    board.cells[row][col].content = Seed.NOUGHT; // Simulasi langkah AI
                    int moveScore = minimax(false); // Hitung skor langkah
                    board.cells[row][col].content = Seed.NO_SEED; // Undo langkah
                    if (moveScore > bestScore) { // Jika langkah ini lebih baik
                        bestScore = moveScore;
                        bestMove[0] = row + 1;
                        bestMove[1] = col;
                    }
                }
            }
        }
        return bestMove; // Kembalikan langkah terbaik
    }

    private int minimax(boolean isAI) {
        State gameState = checkGameState();
        if (gameState == State.NOUGHT_WON) return 1; // AI menang
        if (gameState == State.CROSS_WON) return -1; // Pemain menang
        if (gameState == State.DRAW) return 0; // Seri

        int bestScore = isAI ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int row = 0; row < Board.ROWS; row++) {
            for (int col = 0; col < Board.COLS; col++) {
                if (board.cells[row][col].content == Seed.NO_SEED) {
                    board.cells[row][col].content = isAI ? Seed.NOUGHT : Seed.CROSS;
                    int currentScore = minimax(!isAI);
                    board.cells[row][col].content = Seed.NO_SEED; // Undo langkah
                    bestScore = isAI
                            ? Math.max(bestScore, currentScore) // AI memaksimalkan skor
                            : Math.min(bestScore, currentScore); // Pemain meminimalkan skor
                }
            }
        }
        return bestScore;
    }

    private State checkGameState() {
        return board.getGameState(); // Memanfaatkan metode yang ada di Board
    }
}
    