package status;

import combatant.Combatant;
import trigger.LocalTriggerTypes;
import trigger.GlobalTriggerTypes;
import trigger.GlobalTriggers;
import java.util.ArrayList;
import java.util.List;

public abstract class StatusEffect {
    protected String statusName;
    protected int statusDuration;
    protected int curStatusDuration;
    protected Combatant effectedEntity;
    protected int priority;
    protected boolean isExpired;
    protected List<LocalTriggerTypes> localTriggerConditions;
    protected List<GlobalTriggerTypes> globalTriggerConditions;
    
    public StatusEffect(String name, int duration) {
        this.statusName = name;
        this.statusDuration = duration;
        this.curStatusDuration = duration;
        this.priority = 1;
        this.isExpired = false;
        this.localTriggerConditions = new ArrayList<>();
        this.globalTriggerConditions = new ArrayList<>();
    }
    
    public StatusEffect(Combatant affectedEntity, int duration) {
        this(affectedEntity != null ? affectedEntity.getName() + "'s Effect" : "Effect", duration);
        this.effectedEntity = affectedEntity;
    }
    
    public void onHit() {}
    public void onStartTurn() {}
    public void onAllStartTurn() {}
    public void onStartRound() {}
    public void onEndTurn() {}
    public void onAllEndTurn() {}
    public void onEndRound() {}
    public void onDeath() {}
    public void onExpire() {}
    public void onInfliction() {}
    
    public int getStatusDuration() { return statusDuration; }
    public int getCurStatusDuration() { return curStatusDuration; }
    public void changeCurStatusDuration(int delta) { 
        curStatusDuration += delta;
        if (curStatusDuration <= 0) {
            isExpired = true;
            onExpire();
            removeStatus();
        }
    }
    
    public void updateCurStatusDuration() {
        changeCurStatusDuration(-1);
    }
    
    public void displayMessage() {
        System.out.println(statusName + " is active on " + effectedEntity.getName());
    }
    
    public void applyStatus() {
        if (effectedEntity == null) return;
        for (LocalTriggerTypes condition : localTriggerConditions) {
            effectedEntity.getLocalTriggerList().addLocalTrigger(condition, this);
        }
        for (GlobalTriggerTypes condition : globalTriggerConditions) {
            GlobalTriggers.getInstance().addGlobalTrigger(condition, this);
        }
        effectedEntity.addEffect(this);
        onInfliction();
    }
    
    public void removeStatus() {
        if (effectedEntity == null) return;
        for (LocalTriggerTypes condition : localTriggerConditions) {
            effectedEntity.getLocalTriggerList().removeLocalTrigger(condition, this);
        }
        for (GlobalTriggerTypes condition : globalTriggerConditions) {
            GlobalTriggers.getInstance().removeGlobalTrigger(condition, this);
        }
        effectedEntity.removeEffect(this);
    }
    
    public void addLocalTriggerCondition(LocalTriggerTypes condition) {
        localTriggerConditions.add(condition);
    }
    
    public void addGlobalTriggerCondition(GlobalTriggerTypes condition) {
        globalTriggerConditions.add(condition);
    }
    
    public void decrementDuration() { updateCurStatusDuration(); }
    public boolean isExpired() { return isExpired; }
    public String getName() { return statusName; }
    public Combatant getEffectedEntity() { return effectedEntity; }
    public void setEffectedEntity(Combatant entity) { this.effectedEntity = entity; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    
    // 回调方法
    public int onIncomingDamage(int damage) { return damage; }
    public int onOutgoingDamage(int damage) { return damage; }
    public int onModifyHP(int hp) { return hp; }
    public int onModifyAttack(int attack) { return attack; }
    public int onModifyDefence(int defence) { return defence; }
    public int onModifySpeed(int speed) { return speed; }
}