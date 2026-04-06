package status;

import combatant.Combatant;
import trigger.LocalTriggerTypes;

public class Stun extends StatusEffect {
    
    public Stun(Combatant affectedEntity, int duration) {
        super(affectedEntity, duration);
        this.statusName = "Stun";
        this.priority = 1;
        addLocalTriggerCondition(LocalTriggerTypes.ON_TURN_END);
        applyStatus();
    }
    
    @Override
    public void onEndTurn() {
        updateCurStatusDuration();
    }
    
    @Override
    public void onExpire() {
        System.out.println(effectedEntity.getName() + " is no longer stunned.");
    }
    
    @Override
    public void onInfliction() {
        System.out.println(effectedEntity.getName() + " is stunned for " + curStatusDuration + " turns!");
    }
}