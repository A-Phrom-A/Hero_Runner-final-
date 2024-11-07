import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameOverPanel extends JPanel {
    private int kills;
    private long minutes;
    private long seconds;

    // Constructor ที่รับจำนวนมอนสเตอร์ที่ถูกฆ่าและเวลาที่รอดชีวิต
    public GameOverPanel(App_hero_runner app, int kills, long minutes, long seconds) {
        this.kills = kills;
        this.minutes = minutes;
        this.seconds = seconds;

        setLayout(new BorderLayout());
        
        // Display "You Lost" message
        JLabel gameOverLabel = new JLabel("You Lost", SwingConstants.CENTER);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 60));
        gameOverLabel.setForeground(Color.RED);
        add(gameOverLabel, BorderLayout.CENTER);

        // Display number of monsters killed and survival time
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        JLabel killsLabel = new JLabel("Monsters Killed: " + kills);
        killsLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        infoPanel.add(killsLabel);
        
        JLabel survivalTimeLabel = new JLabel(String.format("Survival Time: %02d:%02d", minutes, seconds));
        survivalTimeLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        infoPanel.add(survivalTimeLabel);

        add(infoPanel, BorderLayout.CENTER);

        // Create button to return to character selection
        JButton retryButton = new JButton("Return to Character Selection");
        retryButton.setFont(new Font("Arial", Font.PLAIN, 30));
        retryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Return to character selection screen
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(app);
                frame.getContentPane().removeAll();
                
                CharacterSelectionPanel selectionPanel = new CharacterSelectionPanel();
                frame.add(selectionPanel);
                
                frame.revalidate();
                frame.repaint();
            }
        });
        
        add(retryButton, BorderLayout.SOUTH);
    }
}
