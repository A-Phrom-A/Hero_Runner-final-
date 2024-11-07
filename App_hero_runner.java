import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.Random;

public class App_hero_runner extends JPanel implements ActionListener {
    private static final int WIDTH = 1600;
    private static final int HEIGHT = 900;
    private Image backgroundImage;
    private int sceneIndex = 0;
    private final String[] scenes = {
        "img/Battleground1.png", 
        "img/Battleground2.png", 
        "img/Battleground3.png", 
        "img/Battleground4.png"
    };
    private String characterType;
    private Character selectedCharacter;
    public static ArrayList<Monster> monsters = new ArrayList<>();
    public static ArrayList<Monster> monstersToRemove = new ArrayList<>();
    private Random random;
    private Timer animationTimer;

    private long startTime;  // ตัวแปรเก็บเวลาเริ่มต้น
    private long survivalTime;  // ตัวแปรเก็บเวลาที่ผู้เล่นรอดชีวิต


    public App_hero_runner(String characterType) {
        this.characterType = characterType;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        loadImage();
    
        // ตรวจสอบว่า selectedCharacter มีค่าแล้วหรือยัง ถ้ายังให้สร้างตัวละคร
        if (selectedCharacter == null) {
            switch (characterType) {
                case "Warrior":
                    selectedCharacter = new Warrior(this);
                    break;
                case "Mage":
                    selectedCharacter = new Mage(this);
                    break;
                case "Rogue":
                    selectedCharacter = new Rogue(this);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown character type: " + characterType);
            }
        }
    
        // Initialize the monsters
        monsters.add(new Medusa("Medusa", 5, 3, "img/medusa/Walk/Walk", 4, "img/medusa/Death/Death", 6 , 400, selectedCharacter));
        monsters.add(new Lizard("Lizard", 8, 5, "img/lizard/Walk/Walk", 6, "img/lizard/Death/Death", 6 , 400, selectedCharacter));
        monsters.add(new Dragon("Dragon", 15, 10, "img/dragon/Walk/Walk", 5, "img/dragon/Death/Death", 5 , 400, selectedCharacter));
        
        startTime = System.currentTimeMillis();

        // Create and start the scene changer thread
        Thread sceneChanger = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(300000); // Change scene every 5 minutes
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                changeScene();
                repaint();
            }
        });
        sceneChanger.start();

        // Start Jin appearance thread
        Thread jinAppearance = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(180000); // Jin appears every 3 minutes (180,000 milliseconds)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                monsters.add(new Jin(selectedCharacter)); // Add Jin to the list of monsters
                repaint();
            }
        });
        jinAppearance.start();

        // Add key listener for jumping
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    selectedCharacter.jump();
                }
            }
        });

        // Add mouse listener for attacking
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    selectedCharacter.attack(monsters);
                }
            }
        });

        

        
        // Start the animation timer (centralized game loop)
        animationTimer = new Timer(80, e -> {
            selectedCharacter.update();
            updateMonsters(); 
            
            repaint(); // Repaint the screen
        });
        animationTimer.start();
    

    Timer abilityTimer = new Timer(120000, e -> applyCharacterAbility()); // Every 2 minutes
        abilityTimer.start();
    }

        private void applyCharacterAbility() {
            if (selectedCharacter instanceof Mage) {
                // Mage จะสังหารมอนสเตอร์ทั้งหมดในหน้าจอ
                monstersToRemove.addAll(monsters); // เพิ่มมอนสเตอร์ทั้งหมดใน monstersToRemove
            } else if (selectedCharacter instanceof Rogue) {
                for(int i = 0; i < 10; i++){
                    selectedCharacter.incrementKillCount();
                }
                  // สมมติว่ามีเมธอด addKills ในคลาส Character
            }
        }

    private long getSurvivalTime() {
        return (System.currentTimeMillis() - startTime) / 1000; // เวลารอดชีวิตเป็นวินาที
    }

   

    private void loadImage() {
        try {
            backgroundImage = ImageIO.read(new File(scenes[sceneIndex])); // Load image from file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void changeScene() {
        sceneIndex = (sceneIndex + 1) % scenes.length; // Cycle through scenes
        loadImage(); // Load the new scene image
    }

    private void updateMonsters() {
        synchronized (monsters) {
            for (Monster monster : monsters) {
                monster.move(); // Update each monster's position
            }
        }

        synchronized (monstersToRemove) {
            // Remove monsters marked for removal
            for (Monster monster : monstersToRemove) {
                monsters.remove(monster);
            }
            monstersToRemove.clear();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the background image if available
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }
        Font boldFont = new Font("Arial", Font.BOLD, 25); // Change the font size as needed
        g.setFont(boldFont);
        g.setColor(Color.black);
        g.drawString("Monsters Killed: " + selectedCharacter.getKillCount(), 20, 130);
        g.drawString("Hero :"+characterType, 20, 40);

        long survivalTime = getSurvivalTime();  // เรียกใช้เมธอดเพื่อคำนวณเวลา
        long minutes = survivalTime / 60;  // แปลงเป็นนาที
        long seconds = survivalTime % 60;  // แปลงเป็นวินาที
        g.drawString(String.format("Survival Time: %02d:%02d", minutes, seconds), 20, 160);  // แสดงเวลา

       
        // Draw the selected character
        if (selectedCharacter != null) {
            selectedCharacter.paintComponent(g);
        }

        // Draw monsters and their hitboxes
        for (Monster monster : monsters) {
            // Only draw monsters that are within the screen area
            if (monster.getX() > -100 && monster.getX() < WIDTH) {
                monster.draw(g); // This calls the draw method in Monster class
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Not used in this implementation
    }

    public ArrayList<Monster> getMonsters() {
        return monsters;
    }

    public void showGameOverScreen() {
        // Clear current panel content
        removeAll();
        long survivalTime = getSurvivalTime();  // เวลาในวินาที
        long minutes = survivalTime / 60;  // นาที
        long seconds = survivalTime % 60;  // วินาที
        // Create and add the GameOverPanel
        GameOverPanel gameOverPanel = new GameOverPanel(this, selectedCharacter.getKillCount(), minutes, seconds); // Pass 'this' instance to GameOverPanel
        add(gameOverPanel);
    
        // Refresh the display
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        JFrame selectionFrame = new JFrame("Select Your Character");
        CharacterSelectionPanel selectionPanel = new CharacterSelectionPanel();
        selectionFrame.add(selectionPanel);
        selectionFrame.setSize(1600, 900);
        selectionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        selectionFrame.setVisible(true);
    }
}

