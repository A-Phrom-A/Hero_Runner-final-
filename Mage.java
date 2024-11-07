public class Mage extends Character {
    public Mage(App_hero_runner app) {
        super(80, 40, 75, app);
        loadImages("img/Mage/Run/run", runningImages, 8);
        loadImages("img/Mage/Run_Attack/run_attack", attackingImages, 8);
        loadImages("img/Mage/High_jump/high_jump", jumpingImages, 12);
        this.x = 100; // Initial x position
        this.y = 300; // Initial y position
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }
}
