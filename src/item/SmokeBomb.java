package item;

import combatant.Combatant;
import status.SmokeBombInvulnerability;
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
    public void useTarget(Combatant user, List<Combatant> targets) {
        itemEffect(user, targets);
    }
    
    @Override
    public void itemEffect(Combatant user, List<Combatant> targets) {
        if (itemQuantity <= 0) {
            System.out.println("No Smoke Bombs left!");
            return;
        }
        
        System.out.printf(">> %s threw a Smoke Bomb!%n", user.getName());
        System.out.println(">> Enemy attacks will deal 0 damage for 2 turns!");
        
        new SmokeBombInvulnerability(user, DURATION);
        
        itemQuantity--;
        System.out.printf(">> Smoke Bomb consumed. %d remaining.%n", itemQuantity);
    }
    
    @Override
    public String toString() {
        return itemName + " (x" + itemQuantity + ") - Enemy attacks deal 0 damage for 2 turns";
    }
}