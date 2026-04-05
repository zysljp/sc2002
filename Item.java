import java.util.List;

public abstract class Item {
    protected String name;
    protected int quantity;
    protected boolean perishable = true;
    
    public Item(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }
    
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public boolean isPerishable() { return perishable; }
    
    public boolean consume() {
        if (quantity <= 0) return false;
        quantity--;
        return true;
    }
    
    public abstract void use(Combatant user, List<Combatant> targets);
    
    public boolean isDepleted() {
        return quantity <= 0;
    }
}