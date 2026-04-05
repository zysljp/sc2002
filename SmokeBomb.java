// SmokeBomb.java
import java.util.List;

public class SmokeBomb extends Item {
    private static final int DURATION = 2;
    
    public SmokeBomb() {
        super("Smoke Bomb", 1);
    }
    
    public SmokeBomb(int quantity) {
        super("Smoke Bomb", quantity);
    }
    
    @Override
    public void use(Combatant user, List<Combatant> targets) {
        if (quantity <= 0) {
            System.out.println("No Smoke Bombs left!");
            return;
        }
        
        System.out.printf(">> %s threw a Smoke Bomb!%n", user.getName());
        System.out.println(">> Enemy attacks will deal 0 damage for 2 turns!");
        
        new SmokeBombInvulnerability(user, DURATION);
        
        quantity--;
        System.out.printf(">> Smoke Bomb consumed. %d remaining.%n%n", quantity);
    }
    
    @Override
    public String toString() {
        return name + " (x" + quantity + ") - Enemy attacks deal 0 damage for 2 turns";
    }
}