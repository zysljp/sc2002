package status;

import combatant.Combatant;
import modifier.DefenceModifier;
import trigger.LocalTriggerTypes;
import trigger.GlobalTriggerTypes;

public class DefendStatus extends StatusEffect {
    private DefenceModifier defenceModifier;
    
    public DefendStatus(Combatant affectedEntity, int duration) {
        super(affectedEntity, duration);
        this.statusName = "Defend";
        this.defenceModifier = new DefenceModifier(10);
        this.priority = 0;
        addLocalTriggerCondition(LocalTriggerTypes.ON_MODIFY_DEFENCE);
        addGlobalTriggerCondition(GlobalTriggerTypes.ON_ROUND_END);
        applyStatus();
    }
    
    @Override
    public int onModifyDefence(int defence) {
        return defenceModifier.applyDefenceModifier(defence, '+');
    }
    
    @Override
    public void onEndRound() {
        updateCurStatusDuration();
    }
    
    @Override
    public void onExpire() {
        System.out.println(effectedEntity.getName() + "'s defense returns to normal.");
    }
}