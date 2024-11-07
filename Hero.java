import java.awt.image.BufferedImage; // เพิ่มการนำเข้าคลาส BufferedImage
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public abstract class Hero {
    protected String name;
    protected int health;
    protected int armor;
    protected int speed;
    
    protected boolean canJump; // ความสามารถในการกระโดด
    protected BufferedImage[] walkImages;
    protected BufferedImage[] attackImages;
    protected BufferedImage[] jumpImages;
    protected BufferedImage[] hurtImages;
    protected BufferedImage[] deathImages;
    protected int currentFrameIndex = 0;

    public Hero(String name, int health, int armor, int speed, 
                String walkImagePath, int walkImageCount, 
                String attackImagePath, int attackImageCount, 
                String jumpImagePath, int jumpImageCount, 
                String hurtImagePath, int hurtImageCount, 
                String deathImagePath, int deathImageCount) {
        this.name = name;
        this.health = health;
        this.armor = armor;
        this.speed = speed;
        // เริ่มต้นให้สามารถใช้สกิลได้
        this.canJump = true; // เริ่มต้นให้สามารถกระโดดได้
        this.walkImages = loadImages(walkImagePath, walkImageCount);
        this.attackImages = loadImages(attackImagePath, attackImageCount);
        this.jumpImages = loadImages(jumpImagePath, jumpImageCount);
        this.hurtImages = loadImages(hurtImagePath, hurtImageCount);
        this.deathImages = loadImages(deathImagePath, deathImageCount);
    }

    public void update() {
        currentFrameIndex = (currentFrameIndex + 1) % walkImages.length; // Cycle through frames
    }

    public BufferedImage getCurrentWalkImage() {
        return walkImages[currentFrameIndex];
    }

    protected BufferedImage[] loadImages(String path, int count) {
        BufferedImage[] images = new BufferedImage[count];
        for (int i = 0; i < count; i++) {
            try {
                images[i] = ImageIO.read(new File(path + i + ".png")); // โหลดภาพ
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return images;
    }

    // ฟังก์ชันลดพลังชีวิต
    public void reduceHealth(int amount) {
        health -= amount;
        if (health < 0) {
            health = 0; // ป้องกันไม่ให้พลังชีวิตต่ำกว่า 0
        }
    }

    // ฟังก์ชันลดเกราะ
    public void reduceArmor(int amount) {
        armor -= amount;
        if (armor < 0) {
            armor = 0; // ป้องกันไม่ให้เกราะต่ำกว่า 0
        }
    }

    // ฟังก์ชันจัดการกับความสามารถในการใช้สกิล
    

    

    // ฟังก์ชันจัดการกับความสามารถในการกระโดด
    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }

    public boolean canJump() {
        return canJump;
    }



    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getArmor() {
        return armor;
    }

    public int getSpeed() {
        return speed;
    }

    public BufferedImage[] getWalkImages() {
        return walkImages;
    }

    public BufferedImage[] getAttackImages() {
        return attackImages;
    }

    public BufferedImage[] getJumpImages() {
        return jumpImages;
    }

    public BufferedImage[] getHurtImages() {
        return hurtImages;
    }

    public BufferedImage[] getDeathImages() {
        return deathImages;
    }
}
