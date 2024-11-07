public class Rogue extends Character {
    public Rogue(App_hero_runner app) {
        super(100, 20, 100, app);
        loadImages("img/Rogue/Run/run", runningImages, 8);
        loadImages("img/Rogue/Run_Attack/run_attack", attackingImages, 8);
        loadImages("img/Rogue/High_jump/high_jump", jumpingImages, 12);
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
