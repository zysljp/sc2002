package combatant;

import action.ArcaneBlast;

public class Wizard extends Player {
    public Wizard(String name) {
        super(name, 200, 50, 10, 20, new ArcaneBlast());
    }
}