package action;

import combatant.Combatant;
import calculator.DamageCalculator;
import status.ArcaneBlastStatus;
import java.util.List;

public class ArcaneBlast implements Action {
    @Override
    public void execute(Combatant performer, List<Combatant> targets) {
        int kills = 0;
        for (Combatant target : targets) {
            if (target.getHealth().isDead()) continue;
            int hpBefore = target.getHealth().getValue();
            new DamageCalculator(performer, target).executeDamage();
            if (target.getHealth().isDead() && hpBefore > 0) kills++;
        }
        if (kills > 0) {
            new ArcaneBlastStatus(performer, 99, kills * 10);
            System.out.println(performer.getName() + " gains " + (kills * 10) + " attack.");
        }
    }
    
    @Override
    public String getName() { return "Arcane Blast"; }
}