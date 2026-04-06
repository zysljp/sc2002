package Entity;

import combatant.Combatant;
import combatant.Enemy;
import combatant.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public class TargetGetter {
    private List<Combatant> targets;
    private Scanner scanner;
    
    public TargetGetter(Scanner scanner) {
        this.targets = new ArrayList<>();
        this.scanner = scanner;
    }
    
    public List<Combatant> getTargets(List<? extends Combatant> available, TargetType type) {
        return typeFilter(available, type);
    }
    
    public List<Combatant> typeFilter(List<? extends Combatant> available, TargetType type) {
        List<Combatant> filtered = new ArrayList<>();
        Predicate<Combatant> predicate;
        
        switch (type) {
            case ENEMY:
                predicate = c -> c instanceof Enemy && !c.getHealth().isDead();
                break;
            case ALLY:
                predicate = c -> c instanceof Player && !c.getHealth().isDead();
                break;
            case ALL:
                predicate = c -> !c.getHealth().isDead();
                break;
            case SELF:
                predicate = c -> c instanceof Player;
                break;
            default:
                predicate = c -> !c.getHealth().isDead();
        }
        
        for (Combatant c : available) {
            if (predicate.test(c)) {
                filtered.add(c);
            }
        }
        return filtered;
    }
    
    public Combatant selectSingleTarget(List<? extends Combatant> available, String prompt) {
        List<Combatant> alive = typeFilter(available, TargetType.ENEMY);
        if (alive.isEmpty()) {
            System.out.println(">> No valid targets available!");
            return null;
        }
        
        System.out.print(prompt);
        for (int i = 0; i < alive.size(); i++) {
            System.out.print(" " + (i + 1) + ". " + alive.get(i).getName());
        }
        System.out.println();
        
        int choice = scanner.nextInt() - 1;
        if (choice >= 0 && choice < alive.size()) {
            return alive.get(choice);
        } else {
            System.out.println(">> Invalid target selection!");
            return null;
        }
    }
    
    public List<Combatant> selectAllTargets(List<? extends Combatant> available, TargetType type) {
        return new ArrayList<>(typeFilter(available, type));
    }
    
    public enum TargetType {
        ENEMY, ALLY, ALL, SELF
    }
}