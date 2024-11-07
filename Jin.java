public class Jin extends Monster {
    public Jin(Character hero) {
        super("Jin", 15, 20, "img/jinn_animation/Walk/Flight", 4, "img/jinn_animation/Death/Death", 6 , 400 ,0, hero);
    }

    @Override
    public void affectHero(Hero hero) {
        hero.reduceHealth(20); // Reduces hero health by 2
        // Cannot use special skills
    }
}
