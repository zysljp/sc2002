// Potion.java
import java.util.List;

public class Potion extends Item {
    private static final int HEAL_AMOUNT = 100;
    
    public Potion() {
        super("Potion", 1);
    }
    
    public Potion(int quantity) {
        super("Potion", quantity);
    }
    
    @Override
    public void use(Combatant user, List<Combatant> targets) {
        if (quantity <= 0) {
            System.out.println("No potions left!");
            return;
        }
        
        int oldHp = user.getHealth().getValue();
        int newHp = Math.min(oldHp + HEAL_AMOUNT, user.getHealth().getMaxHealth());
        user.getHealth().setValue(newHp);
        quantity--;
        
        System.out.printf(">> %s used Potion! Healed %d HP.%n", 
            user.getName(), newHp - oldHp);
        System.out.printf(">> %s now has %d/%d HP.%n%n", 
            user.getName(), newHp, user.getHealth().getMaxHealth());
    }
    
    @Override
    public String toString() {
        return name + " (x" + quantity + ") - Heals " + HEAL_AMOUNT + " HP";
    }
}