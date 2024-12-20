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

        // // Create themed buttons
        // JButton startButton = createStyledButton("Start");
        // startButton.addActionListener(e -> {
        //     stopMusic(); // Stop background music
        //     dispose();   // Close WelcomeScreen
        //     SwingUtilities.invokeLater(() -> GameMain.play(true)); // Start the game
        // });

        JButton optionButton = createStyledButton("Options");
        optionButton.addActionListener(e -> showOptions());

        JButton exitButton = createStyledButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        // Tambahkan tombol untuk memilih mode permainan
        JButton vsAIButton = createStyledButton("Play vs AI");
        vsAIButton.addActionListener(e -> {
            stopMusic(); // Stop background music
            dispose();   // Close WelcomeScreen
            SwingUtilities.invokeLater(() -> GameMain.play(true)); // Start game vs AI
        });

        JButton vsPlayerButton = createStyledButton("Play vs Player");
        vsPlayerButton.addActionListener(e -> {
            stopMusic(); // Stop background music
            dispose();   // Close WelcomeScreen
            SwingUtilities.invokeLater(() -> GameMain.play(false)); // Start game vs Player
        });

        // Add buttons to the panel
        buttonPanel.add(vsAIButton);
        buttonPanel.add(vsPlayerButton);
        // buttonPanel.add(startButton);
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
        button.setBackground(Color.WHITE); // Black background
        button.setForeground(Color.BLACK); // White text
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.BLACK); // White background
                button.setForeground(Color.WHITE); // Black text
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE); // Revert to black background
                button.setForeground(Color.BLACK); // Revert to white text
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
        // Create a custom dialog
        JDialog optionsDialog = new JDialog(this, "About", true);
        optionsDialog.setSize(400, 300);
        optionsDialog.setLocationRelativeTo(this);
        optionsDialog.setLayout(new BorderLayout());
    
        // Header panel with title
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(50, 50, 50));
        JLabel titleLabel = new JLabel("About the Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
    
        // Content panel with creator information
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(30, 30, 30));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    
        JLabel creatorLabel1 = new JLabel("\u2022 Burju Ferdinand Harianja (066)");
        JLabel creatorLabel2 = new JLabel("\u2022 Clay Amsal Sebastian Hutabarat (132)");
        JLabel creatorLabel3 = new JLabel("\u2022 Sandythia Lova Ramadhani Krisnaprana (181)");
    
        JLabel[] labels = { creatorLabel1, creatorLabel2, creatorLabel3 };
        for (JLabel label : labels) {
            label.setFont(new Font("Arial", Font.PLAIN, 16));
            label.setForeground(Color.WHITE);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPanel.add(Box.createVerticalStrut(10)); // Spacing
            contentPanel.add(label);
        }
    
        // Footer panel with a close button
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(50, 50, 50));
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setBackground(new Color(200, 50, 50));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    
        closeButton.addActionListener(e -> optionsDialog.dispose());
        footerPanel.add(closeButton);
    
        // Add panels to dialog
        optionsDialog.add(headerPanel, BorderLayout.NORTH);
        optionsDialog.add(contentPanel, BorderLayout.CENTER);
        optionsDialog.add(footerPanel, BorderLayout.SOUTH);
    
        // Display the dialog
        optionsDialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WelcomeScreen welcomeScreen = new WelcomeScreen();
            welcomeScreen.setVisible(true);
        });
    }
}
