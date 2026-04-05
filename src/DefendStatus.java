// DefendStatus.java
public class DefendStatus extends StatusEffect {
    private DefenceModifier defenceModifier = new DefenceModifier(10);

    public DefendStatus(Combatant affectedEntity, int duration) {
        super(affectedEntity, duration);
        this.setStatusName("Defend");
        this.setPriority(0);
        this.addLocalTriggerCondition(Local_Trigger_Types.ON_MODIFY_DEFENCE);
        this.addGlobalTriggerCondition(Global_Trigger_Types.ON_ROUND_END);
        this.applyStatus();
    }

    @Override
    public int onModifyDefence(int defence) {
        return defenceModifier.applyDefenceModifier(defence, '+');
    }

    @Override
    public void onRoundEnd() {
        this.updateCurStatusDuration();
    }
}