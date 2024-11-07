public class Medusa extends Monster {
    public Medusa(String name, int health, int armor, String walkImagePath, int walkImageCount, String deathImagePath, int deathImageCount, int startY, Character hero) {
        super(name, health, armor, walkImagePath, walkImageCount, deathImagePath, deathImageCount, startY, 0, hero);
    }

    @Override
    public void affectHero(Hero hero) {
        hero.reduceHealth(1);
        hero.reduceArmor(1);
        hero.setCanJump(false);
        
    }
}
