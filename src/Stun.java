// Stun.java - 修正版
public class Stun extends StatusEffect {

    public Stun(Combatant affectedEntity, int duration) {
        super(affectedEntity, duration);
        this.setStatusName("Stun");
        this.setPriority(1);
        this.addLocalTriggerCondition(Local_Trigger_Types.ON_TURN_END);
        this.applyStatus();
    }

    @Override
    public void onTurnEnd() {
        this.updateCurStatusDuration();
    }
}