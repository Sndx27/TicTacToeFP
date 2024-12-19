package tictactoegui;

import java.awt.*;
import java.io.File;
import javax.sound.sampled.*;
import javax.swing.*;


public class WelcomeScreen extends JFrame {

    private static final String MUSIC_FILE = "src\\audio\\lofi-alarm-clock-243766.wav";
    private Clip backgroundMusic;

    public WelcomeScreen() {
        setTitle("Tic Tac Toe - Welcome");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(null);

        // Background Panel with Image
        JPanel backgroundPanel = new JPanel() {
            private Image backgroundImage = new ImageIcon("connectfour\\src\\images\\A vibrant and minimalistic welcome screen for a Connect Four game with a fruit theme, without any text.png").getImage(); // Replace with your image path

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(null);
        setContentPane(backgroundPanel);

        // Title Label
        JLabel titleLabel = new JLabel("Welcome to Tic Tac Toe", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(100, 30, 400, 50);
        titleLabel.setForeground(Color.WHITE); // Set text color
        backgroundPanel.add(titleLabel);

        // Start Button
        JButton startButton = new JButton("Start");
        startButton.setBounds(250, 120, 100, 40);
        startButton.addActionListener(e -> {
            stopMusic();  // Hentikan musik background
            dispose();    // Tutup WelcomeScreen
            SwingUtilities.invokeLater(() -> {  // Pastikan GameMain dijalankan di thread UI
                GameMain.play(); // Mulai game
                
            });
        });
        
        backgroundPanel.add(startButton);

        // Option Button
        JButton optionButton = new JButton("Options");
        optionButton.setBounds(250, 180, 100, 40);
        optionButton.addActionListener(e -> showOptions());
        backgroundPanel.add(optionButton);

        // Exit Button
        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(250, 240, 100, 40);
        exitButton.addActionListener(e -> System.exit(0));
        backgroundPanel.add(exitButton);

        // Start music
        playMusic();
    }

    private void playMusic() {
        try {
            File audioFile = new File(MUSIC_FILE);
            if (audioFile.exists()) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
                backgroundMusic = AudioSystem.getClip();
                backgroundMusic.open(audioInputStream);
                backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY); // Loop music
            } else {
                System.err.println("File audio tidak ditemukan: " + audioFile.getAbsolutePath());
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
                "Game created by:\n" +
                        "Burju Ferdinand Harianja (066)\n" +
                        "Clay Amsal Sebastian Hutabarat (132)\n" +
                        "Sandythia Lova Ramadhani Krisnaprana (181)",
                "About", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WelcomeScreen welcomeScreen = new WelcomeScreen();
            welcomeScreen.setVisible(true);
        });
    }
}

