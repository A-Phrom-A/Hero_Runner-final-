public class Dragon extends Monster {
    public Dragon(String name, int health, int armor, String walkImagePath, int walkImageCount, String deathImagePath, int deathImageCount, int startY, Character hero) {
        super(name, health, armor, walkImagePath, walkImageCount, deathImagePath, deathImageCount, startY, 800, hero);
    }

    @Override
    public void affectHero(Hero hero) {
        hero.reduceHealth(1);
        hero.reduceArmor(1);
        
    }
}
