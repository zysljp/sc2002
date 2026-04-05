import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemBag {
    private final int capacity;
    private final List<Item> items;
    private final Combatant owner;
    
    public ItemBag(int capacity, Combatant owner) {
        this.capacity = capacity;
        this.items = new ArrayList<>();
        this.owner = owner;
    }
    
    public int getCapacity() { return capacity; }
    
    public List<Item> getItems() { 
        return new ArrayList<>(items);  
    }
    
    public boolean isEmpty() { return items.isEmpty(); }
    
    public int getItemCount() { return items.size(); }

    public Item getItem(int index) {
        if (index < 0 || index >= items.size()) return null;
        return items.get(index);
    }

    public Item getItem(String name) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    public boolean gainItem(Item item) {
        if (items.size() >= capacity) {
            System.out.println("Bag is full! Cannot add " + item.getName());
            return false;
        }

        for (Item existing : items) {
            if (existing.getName().equals(item.getName())) {
                existing.setQuantity(existing.getQuantity() + item.getQuantity());
                return true;
            }
        }
        
        items.add(item);
        return true;
    }
    
    public boolean gainItems(List<Item> newItems) {
        for (Item item : newItems) {
            if (!gainItem(item)) return false;
        }
        return true;
    }
    
    public boolean loseItem(String itemName, int quantity) {
        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            if (item.getName().equalsIgnoreCase(itemName)) {
                if (item.getQuantity() > quantity) {
                    item.setQuantity(item.getQuantity() - quantity);
                } else {
                    iterator.remove();
                }
                return true;
            }
        }
        return false;
    }
    
    public boolean loseItem(String itemName) {
        return loseItem(itemName, 1);
    }
    
    

    public boolean useItem(String itemName, List<Combatant> targets) {
        Item item = getItem(itemName);
        if (item == null) {
            System.out.println("Item not found: " + itemName);
            return false;
        }
        
        if (item.getQuantity() <= 0) {
            System.out.println("No " + itemName + " left!");
            return false;
        }
        
        int oldQuantity = item.getQuantity();
        item.use(owner, targets);

        if (item.getQuantity() < oldQuantity && item.getQuantity() == 0) {
            loseItem(itemName);
        }
        
        return true;
    }

    public boolean useItem(int index, List<Combatant> targets) {
        if (index < 0 || index >= items.size()) return false;
        
        Item item = items.get(index);
        if (item.getQuantity() <= 0) {
            System.out.println("No " + item.getName() + " left!");
            return false;
        }
        
        int oldQuantity = item.getQuantity();
        item.use(owner, targets);
        
        if (item.getQuantity() < oldQuantity && item.getQuantity() == 0) {
            items.remove(index);
        }
        
        return true;
    }
    
    public void displayItems() {
        if (items.isEmpty()) {
            System.out.println("Bag is empty.");
            return;
        }
        
        System.out.println("\n=== ITEMS ===");
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            System.out.printf("%d. %s (x%d)%n", i + 1, item.getName(), item.getQuantity());
        }
    }
    
    public void displayItemsDetailed() {
        if (items.isEmpty()) {
            System.out.println("Bag is empty.");
            return;
        }
        
        System.out.println("\n=== ITEMS ===");
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            System.out.printf("%d. %s%n", i + 1, item.toString());
        }
    }
}