// ArcaneBlastStatus.java
public class ArcaneBlastStatus extends StatusEffect {
    private AttackModifier attackModifier;

    public ArcaneBlastStatus(Combatant affectedEntity, int duration, int bonusAmount) {
        super(affectedEntity, duration);
        this.attackModifier = new AttackModifier(bonusAmount);
        this.setStatusName("Arcane Blast");
        this.setPriority(0);
        this.addLocalTriggerCondition(Local_Trigger_Types.ON_MODIFY_ATTACK);
        this.applyStatus();
    }

    @Override
    public int onModifyAttack(int attack) {
        return attackModifier.applyAttackModifier(attack, '+');
    }
}