import java.util.*;

public class GlobalTriggers {
    
    private static GlobalTriggers instance = null;
    private Map<Global_Trigger_Types, List<StatusEffect>> globalTriggerList = new HashMap<>();

    private GlobalTriggers() {
        for (Global_Trigger_Types condition : Global_Trigger_Types.values()) {
            globalTriggerList.put(condition, new ArrayList<>());
        }
    }

    public static GlobalTriggers getInstance() {
        if (instance == null) {
            instance = new GlobalTriggers();
        }
        return instance;
    }

    public void addGlobalTrigger(Global_Trigger_Types condition, StatusEffect status) {
        List<StatusEffect> list = globalTriggerList.get(condition);
        if (!list.contains(status)) {
            list.add(status);
            list.sort(Comparator.comparingInt(StatusEffect::getPriority));
        }
    }

    public void removeGlobalTrigger(Global_Trigger_Types condition, StatusEffect status) {
        globalTriggerList.get(condition).remove(status);
    }

    public List<StatusEffect> getGlobalTriggers(Global_Trigger_Types condition) {
        return globalTriggerList.get(condition);
    }

    public void trigger(Global_Trigger_Types condition) {
        for (StatusEffect status : new ArrayList<>(globalTriggerList.get(condition))) {
            switch (condition) {
                case ON_ROUND_START -> status.onRoundStart();
                case ON_ROUND_END -> status.onRoundEnd();
                default -> {}
            }
        }
    }
}