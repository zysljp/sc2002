// StatusEffect.java
import java.util.*;

public abstract class StatusEffect {
    private String statusName;
    private int statusDuration;
    private int curStatusDuration;
    private Combatant affectedEntity;
    private int priority = 1;
    private boolean isExpired = false;
    private List<Local_Trigger_Types> localTriggerConditions = new ArrayList<>();
    private List<Global_Trigger_Types> globalTriggerConditions = new ArrayList<>();

    // 兼容 C 同学的构造函数
    public StatusEffect(String name, int duration) {
        this.statusName = name;
        this.statusDuration = duration;
        this.curStatusDuration = duration;
    }

    // 完整构造函数（带 affectedEntity）
    public StatusEffect(Combatant affectedEntity, int duration) {
        this.affectedEntity = affectedEntity;
        this.statusDuration = duration;
        this.curStatusDuration = duration;
    }

    // Getters and Setters
    public String getStatusName() { return statusName; }
    public void setStatusName(String name) { this.statusName = name; }
    public int getStatusDuration() { return statusDuration; }
    public void setStatusDuration(int duration) { this.statusDuration = duration; }
    public int getCurStatusDuration() { return curStatusDuration; }
    public void setCurStatusDuration(int duration) { this.curStatusDuration = duration; }
    public Combatant getAffectedEntity() { return affectedEntity; }
    public void setAffectedEntity(Combatant entity) { this.affectedEntity = entity; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public boolean getIsExpired() { return isExpired; }
    public void setIsExpired(boolean exp) { this.isExpired = exp; }

    public void updateCurStatusDuration() {
        this.setCurStatusDuration(Math.max(this.getCurStatusDuration() - 1, 0));
        if (this.getCurStatusDuration() <= 0) {
            this.setIsExpired(true);
            this.removeStatus();
        }
    }

    public void applyStatus() {
        if (this.getAffectedEntity() == null) {
            System.out.println(this.getStatusName() + " cannot be applied!");
            return;
        }
        for (Local_Trigger_Types condition : this.localTriggerConditions) {
            this.getAffectedEntity().getLocalTriggerList().addLocalTrigger(condition, this);
        }
        for (Global_Trigger_Types condition : this.globalTriggerConditions) {
            GlobalTriggers.getInstance().addGlobalTrigger(condition, this);
        }
        this.getAffectedEntity().addEffect(this);
    }

    public void removeStatus() {
        if (this.getAffectedEntity() == null) {
            System.out.println(this.getStatusName() + " cannot be removed!");
            return;
        }
        for (Local_Trigger_Types condition : this.localTriggerConditions) {
            this.getAffectedEntity().getLocalTriggerList().removeLocalTrigger(condition, this);
        }
        for (Global_Trigger_Types condition : this.globalTriggerConditions) {
            GlobalTriggers.getInstance().removeGlobalTrigger(condition, this);
        }
        this.getAffectedEntity().removeEffect(this);
    }

    public void addLocalTriggerCondition(Local_Trigger_Types condition) {
        this.localTriggerConditions.add(condition);
    }

    public void addGlobalTriggerCondition(Global_Trigger_Types condition) {
        this.globalTriggerConditions.add(condition);
    }

    // 兼容 C 同学的方法
    public void decrementDuration() {
        this.updateCurStatusDuration();
    }

    public boolean isExpired() {
        return isExpired;
    }

    public String getName() {
        return statusName;
    }

    // 回调方法（子类可覆盖）
    public void onTurnStart() {}
    public void onTurnEnd() {}
    public void onAttack(Combatant defender) {}
    public void onGettingAttacked(Combatant attacker) {}
    public void onDeath() {}
    public void onKill(Combatant defender) {}
    public int onIncomingDamage(int damage) { return damage; }
    public int onOutgoingDamage(int damage) { return damage; }
    public int onModifyHP(int hp) { return hp; }
    public int onModifyAttack(int attack) { return attack; }
    public int onModifyDefence(int defence) { return defence; }
    public int onModifySpeed(int speed) { return speed; }
    public void onRoundStart() {}
    public void onRoundEnd() {}
}