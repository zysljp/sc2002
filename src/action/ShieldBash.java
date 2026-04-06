package action;

import combatant.Combatant;
import calculator.DamageCalculator;
import status.Stun;
import java.util.List;

public class ShieldBash implements Action {
    @Override
    public void execute(Combatant performer, List<Combatant> targets) {
        Combatant target = targets.get(0);
        new DamageCalculator(performer, target).executeDamage();
        new Stun(target, 2);
    }
    
    @Override
    public String getName() { return "Shield Bash"; }
}