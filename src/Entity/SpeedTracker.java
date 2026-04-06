package Entity;

import combatant.Combatant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SpeedTracker {
    private List<Combatant> turnOrder;
    private Combatant currTurnOrder;
    private int currentIndex;
    
    public SpeedTracker() {
        this.turnOrder = new ArrayList<>();
        this.currTurnOrder = null;
        this.currentIndex = 0;
    }
    
    public void updateTurnOrder(List<Combatant> participants) {
        turnOrder.clear();
        turnOrder.addAll(participants);
        turnOrder.sort(Comparator.comparingInt(Combatant::getTotalSpeed).reversed());
        currentIndex = 0;
        if (!turnOrder.isEmpty()) {
            currTurnOrder = turnOrder.get(0);
        }
    }
    
    public void updateCurrTurnOrder() {
        turnOrder.removeIf(c -> c.getHealth().isDead());
        if (currentIndex < turnOrder.size()) {
            currTurnOrder = turnOrder.get(currentIndex);
        } else {
            currTurnOrder = null;
        }
    }
    
    public List<Combatant> getTurnOrder() { return new ArrayList<>(turnOrder); }
    
    public Combatant getCurrTurnOrder() { return currTurnOrder; }
    
    public void advanceTurn() {
        currentIndex++;
        if (currentIndex < turnOrder.size()) {
            currTurnOrder = turnOrder.get(currentIndex);
        } else {
            currTurnOrder = null;
        }
    }
    
    public void reset() {
        currentIndex = 0;
        if (!turnOrder.isEmpty()) {
            currTurnOrder = turnOrder.get(0);
        }
    }
    
    public void removeFromTurnOrder(Combatant c) {
        int removedIndex = turnOrder.indexOf(c);
        turnOrder.remove(c);
        if (removedIndex < currentIndex) {
            currentIndex--;
        }
        updateCurrTurnOrder();
    }
    
    public void removeFromCurrTurnOrder() {
        if (currTurnOrder != null) {
            removeFromTurnOrder(currTurnOrder);
        }
    }
    
    public void addToCurrTurnOrder(Combatant c) {
        turnOrder.add(currentIndex + 1, c);
    }
    
    public void addToTurnOrder(Combatant c) {
        turnOrder.add(c);
        turnOrder.sort(Comparator.comparingInt(Combatant::getTotalSpeed).reversed());
    }
    
    public boolean hasNext() {
        return currentIndex < turnOrder.size();
    }
}