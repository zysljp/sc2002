package action;

import combatant.Combatant;
import status.DefendStatus;
import java.util.List;

public class DefendAction implements Action {
    @Override
    public void execute(Combatant performer, List<Combatant> targets) {
        new DefendStatus(performer, 2);
        System.out.println(">> " + performer.getName() + " raises their shield! Defense increased by 10 for this round.");
    }
    
    @Override
    public String getName() { return "Defend"; }
}