package trigger;

import status.StatusEffect;
import java.util.*;

public class GlobalTriggers {
    private static GlobalTriggers instance;
    private Map<GlobalTriggerTypes, List<StatusEffect>> globalTriggerList;
    
    private GlobalTriggers() {
        globalTriggerList = new HashMap<>();
        for (GlobalTriggerTypes condition : GlobalTriggerTypes.values()) {
            globalTriggerList.put(condition, new ArrayList<>());
        }
    }
    
    public static GlobalTriggers getInstance() {
        if (instance == null) {
            instance = new GlobalTriggers();
        }
        return instance;
    }
    
    public List<StatusEffect> getGlobalTrigger(GlobalTriggerTypes type) {
        return globalTriggerList.get(type);
    }
    
    public void addGlobalTrigger(GlobalTriggerTypes type, StatusEffect status) {
        List<StatusEffect> list = globalTriggerList.get(type);
        if (!list.contains(status)) {
            list.add(status);
            list.sort(Comparator.comparingInt(StatusEffect::getPriority));
        }
    }
    
    public void removeGlobalTrigger(GlobalTriggerTypes type, StatusEffect status) {
        globalTriggerList.get(type).remove(status);
    }
    
    public void trigger(GlobalTriggerTypes type) {
        for (StatusEffect status : new ArrayList<>(globalTriggerList.get(type))) {
            switch (type) {
                case ON_ROUND_START -> status.onStartRound();
                case ON_ROUND_END -> status.onEndRound();
                default -> {}
            }
        }
    }
}