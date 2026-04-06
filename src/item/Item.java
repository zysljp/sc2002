package item;

import combatant.Combatant;
import java.util.List;

public abstract class Item {
    protected String itemName;
    protected int itemQuantity;
    protected boolean perishable;
    
    public Item(String name, int quantity) {
        this.itemName = name;
        this.itemQuantity = quantity;
        this.perishable = true;
    }
    
    public int getQuantity() { return itemQuantity; }
    public void setQuantity(int quantity) { this.itemQuantity = quantity; }
    public String getName() { return itemName; }
    public boolean isPerishable() { return perishable; }
    
    public abstract void useTarget(Combatant user, List<Combatant> targets);
    public abstract void itemEffect(Combatant user, List<Combatant> targets);
    
    public boolean consume() {
        if (itemQuantity <= 0) return false;
        itemQuantity--;
        return true;
    }
    
    public boolean isDepleted() { return itemQuantity <= 0; }
}