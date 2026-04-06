package item;

import combatant.Combatant;
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
    public void useTarget(Combatant user, List<Combatant> targets) {
        itemEffect(user, targets);
    }
    
    @Override
    public void itemEffect(Combatant user, List<Combatant> targets) {
        if (itemQuantity <= 0) {
            System.out.println("No potions left!");
            return;
        }
        
        int oldHp = user.getHealth().getValue();
        int newHp = Math.min(oldHp + HEAL_AMOUNT, user.getHealth().getMaxHealth());
        user.getHealth().setValue(newHp);
        itemQuantity--;
        
        System.out.printf(">> %s used Potion! Healed %d HP.%n", 
            user.getName(), newHp - oldHp);
        System.out.printf(">> %s now has %d/%d HP.%n", 
            user.getName(), newHp, user.getHealth().getMaxHealth());
    }
    
    @Override
    public String toString() {
        return itemName + " (x" + itemQuantity + ") - Heals " + HEAL_AMOUNT + " HP";
    }
}