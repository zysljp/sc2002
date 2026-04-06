package trigger;

import status.StatusEffect;
import java.util.*;

public class LocalTriggers {
    private Map<LocalTriggerTypes, List<StatusEffect>> localTriggerList;
    
    public LocalTriggers() {
        localTriggerList = new HashMap<>();
        for (LocalTriggerTypes condition : LocalTriggerTypes.values()) {
            localTriggerList.put(condition, new ArrayList<>());
        }
    }
    
    public List<StatusEffect> getLocalTrigger(LocalTriggerTypes type) {
        return localTriggerList.get(type);
    }
    
    public void addLocalTrigger(LocalTriggerTypes type, StatusEffect status) {
        List<StatusEffect> list = localTriggerList.get(type);
        if (!list.contains(status)) {
            list.add(status);
            list.sort(Comparator.comparingInt(StatusEffect::getPriority));
        }
    }
    
    public void removeLocalTrigger(LocalTriggerTypes type, StatusEffect status) {
        localTriggerList.get(type).remove(status);
    }
    
    public void trigger(LocalTriggerTypes type) {
        for (StatusEffect status : new ArrayList<>(localTriggerList.get(type))) {
            switch (type) {
                case ON_TURN_START -> status.onStartTurn();
                case ON_TURN_END -> status.onEndTurn();
                case ON_DEATH -> status.onDeath();
                default -> {}
            }
        }
    }
    
    public int trigger(LocalTriggerTypes type, int value) {
        int modifiedValue = value;
        for (StatusEffect status : new ArrayList<>(localTriggerList.get(type))) {
            switch (type) {
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