import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CharacterSelectionPanel extends JPanel {
    private BufferedImage[] mageImages;
    private BufferedImage[] rogueImages;
    private BufferedImage[] warriorImages;
    private BufferedImage backgroundImage; // ตัวแปรสำหรับภาพพื้นหลัง
    private int currentFrame = 0;
    private Timer animationTimer;
    private String selectedCharacter = "Warrior";
   

    public CharacterSelectionPanel() {
        mageImages = loadImages("img/Mage/Idle/idle", 14);
        rogueImages = loadImages("img/Rogue/Idle/idle", 17);
        warriorImages = loadImages("img/Knight/Idle/idle", 12);
        
        // โหลดภาพพื้นหลัง
        loadBackgroundImage();

        animationTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentFrame++;
                repaint();
            }
        });
        animationTimer.start();

        JButton selectMageButton = new JButton("Select Mage");
        selectMageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedCharacter = "Mage";
                openGameWindow();
            }
        });

        JButton selectRogueButton = new JButton("Select Rogue");
        selectRogueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedCharacter = "Rogue";
                openGameWindow();
            }
        });

        JButton selectWarriorButton = new JButton("Select Warrior");
        selectWarriorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedCharacter = "Warrior";
                openGameWindow();
            }
        });

        this.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(selectMageButton);
        buttonPanel.add(selectRogueButton);
        buttonPanel.add(selectWarriorButton);

        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    private BufferedImage[] loadImages(String path, int count) {
        BufferedImage[] images = new BufferedImage[count];
        for (int i = 0; i < count; i++) {
            try {
                String imagePath = path + i + ".png"; // สร้างเส้นทางของภาพ
                images[i] = ImageIO.read(new File(imagePath)); // โหลดภาพ
                if (images[i] == null) {
                    System.err.println("Error loading image: " + imagePath); // แจ้งเตือนเมื่อโหลดไม่สำเร็จ
                }
            } catch (IOException e) {
                System.err.println("IOException when loading image: " + path + i + ".png"); // แจ้งเตือนข้อผิดพลาด
                e.printStackTrace();
            }
        }
        return images;
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("img/Background.jpg")); // โหลดภาพพื้นหลัง
        } catch (IOException e) {
            System.err.println("Error loading background image");
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // วาดภาพพื้นหลัง
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }

        // วาดภาพตัวละคร
        int mageX = getWidth() / 4 - 50;
        int rogueX = getWidth() / 2 - 50;
        int warriorX = 3 * getWidth() / 4 - 50;
        int y1 =(getHeight()/2)-50;
        int y = getHeight() - y1;

        // วาดภาพตัวละครแต่ละตัว
        if (mageImages.length > 0) {
            g.drawImage(mageImages[currentFrame % mageImages.length], mageX, y, 300, 300, null);
        }
        if (rogueImages.length > 0) {
            g.drawImage(rogueImages[currentFrame % rogueImages.length], rogueX, y, 300, 300, null);
        }
        if (warriorImages.length > 0) {
            g.drawImage(warriorImages[currentFrame % warriorImages.length], warriorX, y, 300, 300, null);
        }
    }

    
        
    

    private void openGameWindow() {
        JFrame gameFrame = new JFrame("Hero Runner Game");

        App_hero_runner game = new App_hero_runner(selectedCharacter);
        gameFrame.add(game);
        gameFrame.pack();
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setVisible(true);
        SwingUtilities.getWindowAncestor(this).dispose(); // Close the selection window
    }
}
