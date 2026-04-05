// PowerStone.java
import java.util.List;

public class PowerStone extends Item {
    
    public PowerStone() {
        super("Power Stone", 1);
    }
    
    public PowerStone(int quantity) {
        super("Power Stone", quantity);
    }
    
    @Override
    public void use(Combatant user, List<Combatant> targets) {
        if (quantity <= 0) {
            System.out.println("No Power Stones left!");
            return;
        }
        
        if (!(user instanceof Player)) {
            System.out.println("Only players can use Power Stones!");
            return;
        }
        
        Player player = (Player) user;
        
        int savedCooldown = player.getSpecialCooldown();
        
        System.out.printf(">> %s used Power Stone! Triggering special skill for free!%n", 
            user.getName());

        if (player instanceof Warrior) {
            if (targets != null && !targets.isEmpty()) {
                new ShieldBash().execute(player, targets);
            } else {
                System.out.println("No target selected for Shield Bash!");
                return;
            }
        } else if (player instanceof Wizard) {
            new ArcaneBlast().execute(player, targets);
        }

        player.setSpecialCooldown(savedCooldown);
        
        quantity--;
        System.out.printf(">> Power Stone consumed. %d remaining.%n%n", quantity);
    }
    
    @Override
    public String toString() {
        return name + " (x" + quantity + ") - Free special skill (no cooldown)";
    }
}