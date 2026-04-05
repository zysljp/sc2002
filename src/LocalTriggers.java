// LocalTriggers.java
import java.util.*;

public class LocalTriggers {
    
    private Map<Local_Trigger_Types, List<StatusEffect>> localTriggerList = new HashMap<>();

    public LocalTriggers() {
        for (Local_Trigger_Types condition : Local_Trigger_Types.values()) {
            localTriggerList.put(condition, new ArrayList<>());
        }
    }

    public void addLocalTrigger(Local_Trigger_Types condition, StatusEffect status) {
        List<StatusEffect> list = localTriggerList.get(condition);
        if (!list.contains(status)) {
            list.add(status);
            list.sort(Comparator.comparingInt(StatusEffect::getPriority));
        }
    }

    public void removeLocalTrigger(Local_Trigger_Types condition, StatusEffect status) {
        localTriggerList.get(condition).remove(status);
    }

    public List<StatusEffect> getLocalTriggers(Local_Trigger_Types condition) {
        return localTriggerList.get(condition);
    }

    public void trigger(Local_Trigger_Types condition) {
        for (StatusEffect status : new ArrayList<>(localTriggerList.get(condition))) {
            switch (condition) {
                case ON_TURN_START -> status.onTurnStart();
                case ON_TURN_END -> status.onTurnEnd();
                case ON_DEATH -> status.onDeath();
                default -> {}
            }
        }
    }

    public int trigger(Local_Trigger_Types condition, int value) {
        int modifiedValue = value;
        for (StatusEffect status : new ArrayList<>(localTriggerList.get(condition))) {
            switch (condition) {
                case ON_MODIFY_ATTACK -> modifiedValue = status.onModifyAttack(modifiedValue);
                case ON_MODIFY_DEFENCE -> modifiedValue = status.onModifyDefence(modifiedValue);
                case ON_MODIFY_HP -> modifiedValue = status.onModifyHP(modifiedValue);
                case ON_MODIFY_SPEED -> modifiedValue = status.onModifySpeed(modifiedValue);
                case ON_OUTGOING_DAMAGE -> modifiedValue = status.onOutgoingDamage(modifiedValue);
                case ON_INCOMING_DAMAGE -> modifiedValue = status.onIncomingDamage(modifiedValue);
                default -> {}
            }
        }
        return modifiedValue;
    }
}