import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.Timer;

public abstract class Monster {
    protected String name;
    protected int health;
    protected int armor;
    protected BufferedImage[] walkImages;
    protected BufferedImage[] deathImages;
    protected int x; // Current x position of the monster
    protected final int y; // Fixed y position for the monster
    private int currentWalkFrame; // Current walking animation frame
    private int currentDeathFrame;
    private int walkFrameDelay; // Delay between walking frames
    private int deathFrameDelay;
    private boolean isDead;
    private boolean deathAnimationComplete;
    private Character character;  // Reference to the hero character

    public Monster(String name, int health, int armor, String walkImagePath, int walkImageCount,
                   String deathImagePath, int deathImageCount, int startY, int initialXOffset, Character character) {
        this.name = name;
        this.health = health;
        this.armor = armor;
        this.walkImages = loadImages(walkImagePath, walkImageCount);
        this.deathImages = loadImages(deathImagePath, deathImageCount);
        this.x = 1600 + initialXOffset; // Starting position off-screen to the right
        this.y = startY; // Fixed y position
        this.character = character;  // Assign the hero character
        this.currentWalkFrame = 0;
        this.walkFrameDelay = 0;
    }

    // Load images for walking and death animations
    protected BufferedImage[] loadImages(String path, int count) {
        BufferedImage[] images = new BufferedImage[count];
        for (int i = 0; i < count; i++) {
            try {
                images[i] = ImageIO.read(new File(path + i + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return images;
    }

    // Abstract method for affecting the hero (to be implemented in specific monster classes)
    public abstract void affectHero(Hero hero);

    // Move the monster left across the screen
    public void move() {
        x -= 15;  // Move the monster to the left
        
        if (x < -100) {  // If the monster moves off-screen
            if (character.getArmor() > 0) {
                character.reduceArmor(); // Reduce armor if armor is greater than 0
            } else {
                character.reduceHealth(); // If no armor left, reduce health instead
            }
            
            x = 1600; // Reset position to right side
        }
        
        updateWalkAnimation(); // Continue the walking animation
    }
    
    public void onDeath() {
        if (this instanceof Jin) {  // ถ้าคือ Jin
            // เพิ่มเลือดและเกราะ 20 หน่วย
            character.increaseHealth(20);  // ฟังก์ชันเพิ่มเลือด
            character.increaseArmor(20);   // ฟังก์ชันเพิ่มเกราะ
        }
    }

    // Update the walking animation frame
    private void updateWalkAnimation() {
        walkFrameDelay++;
        if (walkFrameDelay >= 5) {
            currentWalkFrame = (currentWalkFrame + 1) % walkImages.length;
            walkFrameDelay = 0;
        }
    }

    public void updateDeathAnimation() {
        deathFrameDelay++;
        if (deathFrameDelay >= 5) {
            if (currentDeathFrame < deathImages.length - 1) {
                currentDeathFrame++;
            } else {
                deathAnimationComplete = true; // Mark animation as complete
            }
            deathFrameDelay = 0;
        }
    }

    // Monster takes damage and dies if health is zero or below
    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            die();
        }
    }

    public void die() {
        isDead = true;
        deathAnimationComplete = false;
        System.out.println(name + " has died!");
        synchronized (App_hero_runner.monsters) {
            App_hero_runner.monstersToRemove.add(this);
        }
        character.incrementKillCount();
        onDeath();
        respawn(); // Call respawn after monster dies
    }
    
    private void respawn() {
        Timer respawnTimer = new Timer(5000, e -> {
            synchronized (App_hero_runner.monstersToRemove) {
                App_hero_runner.monsters.removeAll(App_hero_runner.monstersToRemove);
                App_hero_runner.monstersToRemove.clear();
            }
            spawnNewMonster();
        });
        respawnTimer.setRepeats(false);
        respawnTimer.start();
    }
    
    private void spawnNewMonster() {
        // Create a random number generator to choose a monster type
        Random rand = new Random();
        
        // Randomly select a monster type (0 = Medusa, 1 = Lizard, 2 = Dragon)
        int monsterType = rand.nextInt(3); 
    
        // Declare the new monster
        Monster newMonster;
    
        // Create the monster based on the random type
        switch (monsterType) {
            case 0:
                newMonster = new Medusa("Medusa", 7, 3, "img/medusa/Walk/Walk", 4, "img/medusa/Death/Death", 6, 400, character);
                break;
            case 1:
                newMonster = new Lizard("Lizard", 9, 5, "img/lizard/Walk/Walk", 6, "img/lizard/Death/Death", 6, 400, character);
                break;
            case 2:
                newMonster = new Dragon("Dragon", 15, 10, "img/dragon/Walk/Walk", 5, "img/dragon/Death/Death", 5, 400, character);
                break;
            default:
                newMonster = new Lizard("Lizard", 9, 10, "img/lizard/Walk/Walk", 6, "img/lizard/Death/Death", 6, 400, character);
                break;
        }
    
        // Set the position of the newly spawned monster (fixed Y position)
        newMonster.setX(1600);  // Starting position (off-screen to the right)
    
        // Add the new monster to the monsters list
        synchronized (App_hero_runner.monsters) {
            App_hero_runner.monsters.add(newMonster);
        }
    
        // Output to the console (for debugging purposes)
        System.out.println("A new monster has spawned: " + newMonster.getName());
    }
    

    // Method to draw the monster's image and hitbox
    public void draw(Graphics g) {
        if (isDead()) {
            g.drawImage(deathImages[currentDeathFrame], x, y, null);
        } else {
            g.drawImage(walkImages[currentWalkFrame], x, y, null);
        }

        g.setColor(Color.GREEN);
        g.fillRect(x, y - 10, (int) ((double) health / 80 * getWidth()), 10);
        g.setColor(Color.BLUE);
        g.fillRect(x, y - 30, (int) ((double) armor / 80 * getWidth()), 10);

        g.setColor(Color.WHITE);
        g.drawString(name, x, y - 100); 
        g.setColor(Color.WHITE);
        g.drawString("HP: " + health, x, y - 70);
        g.drawString("Armor: " + armor, x, y - 40);
    }

    public boolean isDead() {
        return isDead;
    }

    public int getWidth() {
        if (walkImages.length > 0) {
            return walkImages[0].getWidth();
        }
        return 0;
    }

    public int getHeight() {
        if (walkImages.length > 0) {
            return walkImages[0].getHeight();
        }
        return 0;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getArmor() {
        return armor;
    }

    public BufferedImage[] getWalkImages() {
        return walkImages;
    }

    public BufferedImage getCurrentWalkImage() {
        return walkImages[currentWalkFrame];
    }

    public BufferedImage[] getDeathImages() {
        return deathImages;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public boolean isDeathAnimationComplete() {
        return deathAnimationComplete;
    }
}
