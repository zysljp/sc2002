package calculator;

import combatant.Combatant;
import trigger.LocalTriggerTypes;

public class DamageCalculator {
    private Combatant attacker;
    private Combatant defender;
    
    public DamageCalculator(Combatant attacker, Combatant defender) {
        this.attacker = attacker;
        this.defender = defender;
    }
    
    public int executeDamage() {
        int atk = attacker.getTotalAttack();
        int def = defender.getTotalDefense();
        int damage = Math.max(0, atk - def);
        
        damage = attacker.getLocalTriggerList().trigger(LocalTriggerTypes.ON_OUTGOING_DAMAGE, damage);
        damage = defender.getLocalTriggerList().trigger(LocalTriggerTypes.ON_INCOMING_DAMAGE, damage);
        
        defender.getHealth().takeDamage(damage);
        System.out.println(attacker.getName() + " deals " + damage + " damage to " + defender.getName());
        return damage;
    }
}