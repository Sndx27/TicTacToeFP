package tictactoegui;

import java.awt.*;
import java.io.File;
import javax.sound.sampled.*;
import javax.swing.*;

public class WelcomeScreen extends JFrame {

    private static final String MUSIC_FILE = "src/audio/lofi-alarm-clock-243766.wav";
    private Clip backgroundMusic;

    public WelcomeScreen() {
        setTitle("Tic Tac Toe - Welcome");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Background Panel with Tic Tac Toe theme
        JPanel backgroundPanel = new JPanel() {
            private Image backgroundImage = new ImageIcon("src\\images\\TIC TAC TOE.png").getImage(); // Replace with your image path

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        // Button Panel (Placed at the bottom)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Center alignment with spacing
        buttonPanel.setOpaque(false); // Transparent background

        // Create themed buttons
        JButton startButton = createStyledButton("Start");
        startButton.addActionListener(e -> {
            stopMusic(); // Stop background music
            dispose();   // Close WelcomeScreen
            SwingUtilities.invokeLater(() -> GameMain.play()); // Start the game
        });

        JButton optionButton = createStyledButton("Options");
        optionButton.addActionListener(e -> showOptions());

        JButton exitButton = createStyledButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        // Add buttons to the panel
        buttonPanel.add(startButton);
        buttonPanel.add(optionButton);
        buttonPanel.add(exitButton);

        // Add button panel to the frame
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Start background music
        playMusic();
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(Color.BLACK); // Black background
        button.setForeground(Color.WHITE); // White text
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE); // White background
                button.setForeground(Color.BLACK); // Black text
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.BLACK); // Revert to black background
                button.setForeground(Color.WHITE); // Revert to white text
            }
        });
        return button;
    }

    private void playMusic() {
        try {
            if (backgroundMusic == null || !backgroundMusic.isRunning()) {
                File audioFile = new File(MUSIC_FILE);
                if (audioFile.exists()) {
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
                    backgroundMusic = AudioSystem.getClip();
                    backgroundMusic.open(audioInputStream);
                    backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    System.err.println("Audio file not found: " + audioFile.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
            backgroundMusic.close();
        }
    }

    private void showOptions() {
        JOptionPane.showMessageDialog(this,
                "<html><div style='text-align: center;'>"
                        + "Game created by:<br>"
                        + "<b>Burju Ferdinand Harianja (066)</b><br>"
                        + "<b>Clay Amsal Sebastian Hutabarat (132)</b><br>"
                        + "<b>Sandythia Lova Ramadhani Krisnaprana (181)</b>"
                        + "</div></html>",
                "About", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WelcomeScreen welcomeScreen = new WelcomeScreen();
            welcomeScreen.setVisible(true);
        });
    }
}
