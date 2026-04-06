package item;

import combatant.Combatant;
import combatant.Player;
import combatant.Warrior;
import combatant.Wizard;
import action.ShieldBash;
import action.ArcaneBlast;
import java.util.List;

public class PowerStone extends Item {
    
    public PowerStone() {
        super("Power Stone", 1);
    }
    
    public PowerStone(int quantity) {
        super("Power Stone", quantity);
    }
    
    @Override
    public void useTarget(Combatant user, List<Combatant> targets) {
        itemEffect(user, targets);
    }
    
    @Override
    public void itemEffect(Combatant user, List<Combatant> targets) {
        if (itemQuantity <= 0) {
            System.out.println("No Power Stones left!");
            return;
        }
        
        if (!(user instanceof Player)) {
            System.out.println("Only players can use Power Stones!");
            return;
        }
        
        Player player = (Player) user;
        int savedCooldown = player.getSpecialCooldown();
        
        System.out.printf(">> %s used Power Stone! Triggering special skill for free!%n", user.getName());
        
        if (player instanceof Warrior) {
            if (targets != null && !targets.isEmpty()) {
                new ShieldBash().execute(player, targets);
            }
        } else if (player instanceof Wizard) {
            new ArcaneBlast().execute(player, targets);
        }
        
        player.setSpecialCooldown(savedCooldown);
        itemQuantity--;
        System.out.printf(">> Power Stone consumed. %d remaining.%n", itemQuantity);
    }
    
    @Override
    public String toString() {
        return itemName + " (x" + itemQuantity + ") - Free special skill (no cooldown)";
    }
}