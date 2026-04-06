package action;

import combatant.Combatant;
import calculator.DamageCalculator;
import java.util.List;

public class BasicAttack implements Action {
    @Override
    public void execute(Combatant performer, List<Combatant> targets) {
        Combatant target = targets.get(0);
        new DamageCalculator(performer, target).executeDamage();
    }
    
    @Override
    public String getName() { return "Basic Attack"; }
}