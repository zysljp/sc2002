package combatant;

import action.ShieldBash;

public class Warrior extends Player {
    public Warrior(String name) {
        super(name, 260, 40, 20, 30, new ShieldBash());
    }
}