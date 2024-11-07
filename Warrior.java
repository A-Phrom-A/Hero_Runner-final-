public class Warrior extends Character {
    public Warrior(App_hero_runner app) {
        super(150, 120, 50, app);
        loadImages("img/Knight/Run/run", runningImages, 8);
        loadImages("img/Knight/Run_Attack/run_attack", attackingImages, 8);
        loadImages("img/Knight/High_jump/high_jump", jumpingImages, 12);
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
