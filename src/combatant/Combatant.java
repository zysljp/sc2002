package combatant;

import attribute.HealthAttribute;
import attribute.AttackAttribute;
import attribute.DefenseAttribute;
import attribute.SpeedAttribute;
import status.StatusEffect;
import status.Permissions;
import trigger.LocalTriggers;
import java.util.ArrayList;
import java.util.List;

public abstract class Combatant {
    private String name;
    private HealthAttribute health;
    private AttackAttribute attack;
    private DefenseAttribute defense;
    private SpeedAttribute speed;
    private List<StatusEffect> ongoingSpecialStatus;
    private Permissions permissions;
    private LocalTriggers triggers;
    
    public Combatant(String name, int health, int attack, int defense, int speed) {
        this.name = name;
        this.health = new HealthAttribute(health);
        this.attack = new AttackAttribute(attack);
        this.defense = new DefenseAttribute(defense);
        this.speed = new SpeedAttribute(speed);
        this.ongoingSpecialStatus = new ArrayList<>();
        this.permissions = new Permissions();
        this.triggers = new LocalTriggers();
    }
    
    public void basicAttack(Combatant target) {
    }
    
    public int calculateEffectiveHP() {
        int hp = health.getValue();
        hp = triggers.trigger(trigger.LocalTriggerTypes.ON_MODIFY_HP, hp);
        return hp;
    }
    
    public int calculateEffectiveAttack() {
        int atk = attack.getValue();
        atk = triggers.trigger(trigger.LocalTriggerTypes.ON_MODIFY_ATTACK, atk);
        return atk;
    }
    
    public int calculateEffectiveDefence() {
        int def = defense.getValue();
        def = triggers.trigger(trigger.LocalTriggerTypes.ON_MODIFY_DEFENCE, def);
        return def;
    }
    
    public int calculateEffectiveSpeed() {
        int spd = speed.getValue();
        spd = triggers.trigger(trigger.LocalTriggerTypes.ON_MODIFY_SPEED, spd);
        return spd;
    }
    
    public void calculateEffectiveStats() {
        calculateEffectiveHP();
        calculateEffectiveAttack();
        calculateEffectiveDefence();
        calculateEffectiveSpeed();
    }
    
    public void addEffect(StatusEffect effect) {
        ongoingSpecialStatus.add(effect);
    }
    
    public void removeEffect(StatusEffect effect) {
        ongoingSpecialStatus.remove(effect);
    }
    
    public void updateEffects() {
        ongoingSpecialStatus.removeIf(StatusEffect::isExpired);
    }
    
    public boolean isStunned() {
        return ongoingSpecialStatus.stream().anyMatch(e -> e instanceof status.Stun && !e.isExpired());
    }
    
    public int getTotalAttack() { return calculateEffectiveAttack(); }
    public int getTotalDefense() { return calculateEffectiveDefence(); }
    public int getTotalSpeed() { return calculateEffectiveSpeed(); }
    
    // Getters
    public String getName() { return name; }
    public HealthAttribute getHealth() { return health; }
    public AttackAttribute getAttack() { return attack; }
    public DefenseAttribute getDefense() { return defense; }
    public SpeedAttribute getSpeed() { return speed; }
    public List<StatusEffect> getActiveEffects() { return new ArrayList<>(ongoingSpecialStatus); }
    public Permissions getPermissions() { return permissions; }
    public LocalTriggers getLocalTriggerList() { return triggers; }
}