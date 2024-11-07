import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Character extends JPanel {
    protected int hearts;
    protected int armor;
    protected int speed;
    protected ArrayList<BufferedImage> runningImages;
    protected ArrayList<BufferedImage> attackingImages;
    protected ArrayList<BufferedImage> jumpingImages;
    protected int currentFrame;
    protected Timer animationTimer;
    protected boolean isJumping;
    protected boolean isAttacking;
    protected int x, y; // Position variables
    private App_hero_runner appHeroRunner;
    private int killCount;

    // Jump variables
    private int jumpHeight = 150; // Maximum jump height
    private int jumpSpeed = 40; // Speed of the jump
    private int verticalSpeed = 0; // Current vertical speed
    private int groundY; // Ground level position

    public Character(int hearts, int armor, int speed, App_hero_runner appHeroRunner) {
        this.hearts = hearts;
        this.armor = armor;
        this.speed = speed;
        this.runningImages = new ArrayList<>();
        this.attackingImages = new ArrayList<>();
        this.jumpingImages = new ArrayList<>();
        this.currentFrame = 0;
        this.isJumping = false;
        this.isAttacking = false;
        this.groundY = 500; // Assuming ground level is at y = 500
        this.appHeroRunner = appHeroRunner;

        animationTimer = new Timer(100, e -> updateAnimation());
        animationTimer.start();
    }

    protected void loadImages(String path, ArrayList<BufferedImage> imageList, int count) {
        try {
            for (int i = 0; i < count; i++) {
                BufferedImage image = ImageIO.read(new File(path + i + ".png"));
                if (image != null) {
                    imageList.add(image);
                    System.out.println("Loaded image: " + path + i + ".png");
                } else {
                    System.out.println("Failed to load image: " + path + i + ".png");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateAnimation() {
        if (isJumping) {
            currentFrame = (currentFrame + 1) % jumpingImages.size();
        } else if (isAttacking) {
            currentFrame = (currentFrame + 1) % attackingImages.size();
        } else {
            currentFrame = (currentFrame + 1) % runningImages.size();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    
       
        
        // Draw character based on state
        if (isJumping && !jumpingImages.isEmpty()) {
            g.drawImage(jumpingImages.get(currentFrame % jumpingImages.size()), x, y, this);
        } else if (isAttacking && !attackingImages.isEmpty()) {
            g.drawImage(attackingImages.get(currentFrame % attackingImages.size()), x, y, this);
            
            // Draw the attack hitbox (visualize it)
            int attackWidth = 50;  // Increased width for a longer attack hitbox
            int attackHeight = 5; // Height of the attack hitbox
            int attackX = this.x + 50; // Position attack hitbox relative to the character
            int attackY = this.y + 10; // Adjust the Y position as needed for the attack hitbox
    
            // Set the color for the attack hitbox (e.g., yellow for visibility)
            g.setColor(Color.gray);
            g.fillRect(attackX, attackY, attackWidth, attackHeight); // Fill attack hitbox
            g.setColor(Color.BLACK); // Change color to red for outline of the hitbox
            g.drawRect(attackX, attackY, attackWidth, attackHeight); // Draw outline of the attack hitbox
        } else if (!runningImages.isEmpty()) {
            g.drawImage(runningImages.get(currentFrame % runningImages.size()), x, y, this);
        }
    
        Font boldFont = new Font("Arial", Font.BOLD, 25); // Change the font size as needed
        g.setFont(boldFont);
        g.setColor(Color.black); // Set the color for the text
        g.drawString("Hearts: " + getHearts(), 20, 70); // Draw hearts
        g.drawString("Armor: " + getArmor(), 20, 100);
        
        // Draw character based on state
        if (isJumping && !jumpingImages.isEmpty()) {
            g.drawImage(jumpingImages.get(currentFrame % jumpingImages.size()), x, y, this);
        } else if (isAttacking && !attackingImages.isEmpty()) {
            g.drawImage(attackingImages.get(currentFrame % attackingImages.size()), x, y, this);
        } else if (!runningImages.isEmpty()) {
            g.drawImage(runningImages.get(currentFrame % runningImages.size()), x, y, this);
        }
    
        
       
    }

    
    public void reduceHealth() {
        this.hearts -= 10; // or appropriate amount
        if (this.hearts <= 0) {
            // Trigger game-over condition
            appHeroRunner.showGameOverScreen();
        }
    }

    public void reduceArmor(){
        this.armor -= 10;
    }
    

    public void jump() {
        if (!isJumping) {
            isJumping = true;
            verticalSpeed = -jumpSpeed; // Set the initial vertical speed
        }
    }

    

public void attack(ArrayList<Monster> monsters) {
    if (!isAttacking) {
        isAttacking = true; // Set attack state to true to start the attack
        currentFrame = 0; // Reset the attack animation frame
        
        // Define the attack range hitbox (in front of the character)
        int attackWidth = 50; // Width of the attack hitbox
        int attackHeight = 30; // Height of the attack hitbox
        int attackX = this.x + 50; // Position of the hitbox relative to the character (adjust as needed)
        int attackY = this.y + 10; // Position of the hitbox (adjust as needed)

        // Use an Iterator to safely modify the list while iterating
        Iterator<Monster> iterator = monsters.iterator();
        while (iterator.hasNext()) {
            Monster monster = iterator.next();
            // Check if the monster is within the attack range based on x position
            if (monster.getX() < attackX + attackWidth && monster.getX() > attackX - attackWidth) {
                monster.takeDamage(this.getDamage()); // Use the current character's damage
                
                // If the monster's health is 0 or below, it's considered dead and should be removed
                if (monster.getHealth() <= 0) {
                    System.out.println(monster.getName() + " has died!");
                    iterator.remove(); // Safely remove the monster from the list
                }
            }
        }

        // After attacking, reset the attack state after a delay (500ms)
        Timer attackTimer = new Timer(500, e -> isAttacking = false);
        attackTimer.setRepeats(false); // Make sure the timer only fires once
        attackTimer.start();
    }
}

    
    
    
    public int getDamage() {
        // Return the damage based on the character type or stats
        return 20; // Example damage value
    }
    
    private boolean isMonsterInAttackRange(Monster monster) {
        // Example logic to check if the monster is within a specific range of the character
        return Math.abs(monster.getX() - this.x) < 50 && Math.abs(monster.getY() - this.y) < 50;
    }
    

    private boolean isColliding(Monster monster) {
        // Simple collision detection logic based on bounding box
        Rectangle characterBounds = new Rectangle(x, y, runningImages.get(0).getWidth(), runningImages.get(0).getHeight());
        Rectangle monsterBounds = new Rectangle(monster.getX(), monster.getY(), monster.getWalkImages()[0].getWidth(), monster.getWalkImages()[0].getHeight());
        return characterBounds.intersects(monsterBounds);
    }

    public void move(int dx) {
        x += dx;
        repaint();
    }

    public void update() {
        updateAnimation(); // Call updateAnimation to handle frame updates

        // Handle jumping logic
        if (isJumping) {
            y += verticalSpeed; // Update y position with current vertical speed
            verticalSpeed += 5; // Simulate gravity (increase vertical speed downwards)

            // Check if character has landed
            if (y >= groundY) {
                y = groundY; // Set y to ground level
                verticalSpeed = 0; // Reset vertical speed
                isJumping = false; // End jump
            }
        } else {
            // Gradually return to the ground level when not jumping
            if (y < groundY) {
                y += 10; // Control how quickly it returns to the ground
                if (y > groundY) {
                    y = groundY; // Ensure it doesn't go below ground level
                }
            }
        }

        repaint(); // Repaint the character
    }

    public abstract int getX();

    public abstract int getY();

    public int getHearts() {
        return hearts;
    }

    public int getArmor() {
        return armor;
    }

    public void increaseHealth(int amount) {
        this.hearts += amount;
        if (this.hearts > 200) {  // ตัวอย่าง: ไม่ให้เลือดเกิน 100
            this.hearts = 200;
        }
    }

    // ฟังก์ชันเพิ่มเกราะ
    public void increaseArmor(int amount) {
        
        if (this.armor < 100) {  // ตัวอย่าง: ไม่ให้เกราะเกิน 100
            this.armor += amount;
        }
    }

    public void incrementKillCount() {
        this.killCount++;
    }

    // ฟังก์ชันดึงจำนวนมอนสเตอร์ที่ฆ่า
    public int getKillCount() {
        return this.killCount;
    }

}
