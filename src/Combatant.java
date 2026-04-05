// Combatant.java
import java.util.ArrayList;
import java.util.List;

public abstract class Combatant {
    private String name;
    private HealthAttribute health;
    private AttackAttribute attack;
    private DefenseAttribute defense;
    private SpeedAttribute speed;
    private LocalTriggers localTriggerList;
    protected List<StatusEffect> activeEffects = new ArrayList<>();

    public Combatant(String name, int health, int attack, int defense, int speed) {
        this.name = name;
        this.health = new HealthAttribute(health);
        this.attack = new AttackAttribute(attack);
        this.defense = new DefenseAttribute(defense);
        this.speed = new SpeedAttribute(speed);
        this.localTriggerList = new LocalTriggers();
    }

    public LocalTriggers getLocalTriggerList() {
        return localTriggerList;
    }

    public void addEffect(StatusEffect effect) {
        activeEffects.add(effect);
    }

    public void removeEffect(StatusEffect effect) {
        activeEffects.remove(effect);
    }

    public List<StatusEffect> getActiveEffects() {
        return new ArrayList<>(activeEffects);
    }

    public boolean isStunned() {
        return activeEffects.stream().anyMatch(e -> e instanceof Stun && !e.isExpired());
    }

    public int getTotalAttack() {
        int attack = this.attack.getValue();
        attack = localTriggerList.trigger(Local_Trigger_Types.ON_MODIFY_ATTACK, attack);
        return attack;
    }

    public int getTotalDefense() {
        int defence = this.defense.getValue();
        defence = localTriggerList.trigger(Local_Trigger_Types.ON_MODIFY_DEFENCE, defence);
        return defence;
    }

    public int getTotalSpeed() {
        int speed = this.speed.getValue();
        speed = localTriggerList.trigger(Local_Trigger_Types.ON_MODIFY_SPEED, speed);
        return speed;
    }

    public void updateEffects() {
        activeEffects.removeIf(StatusEffect::isExpired);
    }

    public String getName() { return name; }
    public HealthAttribute getHealth() { return health; }
    public AttackAttribute getAttack() { return attack; }
    public DefenseAttribute getDefense() { return defense; }
    public SpeedAttribute getSpeed() { return speed; }
}