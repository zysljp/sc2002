// DamageCalculator.java
public class DamageCalculator {
    private final Combatant attacker;
    private final Combatant defender;

    public DamageCalculator(Combatant attacker, Combatant defender) {
        this.attacker = attacker;
        this.defender = defender;
    }

    public int executeDamage() {
        int atk = attacker.getTotalAttack();
        int def = defender.getTotalDefense();
        int damage = Math.max(0, atk - def);
        
        damage = attacker.getLocalTriggerList().trigger(Local_Trigger_Types.ON_OUTGOING_DAMAGE, damage);
        damage = defender.getLocalTriggerList().trigger(Local_Trigger_Types.ON_INCOMING_DAMAGE, damage);
        
        defender.getHealth().takeDamage(damage);
        System.out.println(attacker.getName() + " deals " + damage + " damage to " + defender.getName());
        return damage;
    }
}