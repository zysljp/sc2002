package status;

import combatant.Combatant;
import modifier.AttackModifier;
import trigger.LocalTriggerTypes;

public class ArcaneBlastStatus extends StatusEffect {
    private AttackModifier attackModifier;
    
    public ArcaneBlastStatus(Combatant affectedEntity, int duration, int bonusAmount) {
        super(affectedEntity, duration);
        this.statusName = "Arcane Blast";
        this.attackModifier = new AttackModifier(bonusAmount);
        this.priority = 0;
        addLocalTriggerCondition(LocalTriggerTypes.ON_MODIFY_ATTACK);
        applyStatus();
    }
    
    @Override
    public int onModifyAttack(int attack) {
        return attackModifier.applyAttackModifier(attack, '+');
    }
}