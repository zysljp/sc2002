package item;

import combatant.Combatant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemBag {
    private int capacity;
    private List<Item> storage;
    private Combatant owner;
    
    public ItemBag(int capacity, Combatant owner) {
        this.capacity = capacity;
        this.storage = new ArrayList<>();
        this.owner = owner;
    }
    
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    
    public boolean gainItem(Item item) {
        if (storage.size() >= capacity) {
            System.out.println("Bag is full! Cannot add " + item.getName());
            return false;
        }
        
        for (Item existing : storage) {
            if (existing.getName().equals(item.getName())) {
                existing.setQuantity(existing.getQuantity() + item.getQuantity());
                return true;
            }
        }
        
        storage.add(item);
        return true;
    }
    
    public boolean loseItem(String itemName) {
        return loseItem(itemName, 1);
    }
    
    public boolean loseItem(String itemName, int quantity) {
        Iterator<Item> iterator = storage.iterator();
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
    
    public boolean useItem(int index, List<Combatant> targets) {
        if (index < 0 || index >= storage.size()) return false;
        
        Item item = storage.get(index);
        if (item.getQuantity() <= 0) {
            System.out.println("No " + item.getName() + " left!");
            return false;
        }
        
        item.useTarget(owner, targets);
        
        if (item.isDepleted()) {
            storage.remove(index);
        }
        
        return true;
    }
    
    public boolean useItem(String itemName, List<Combatant> targets) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getName().equalsIgnoreCase(itemName)) {
                return useItem(i, targets);
            }
        }
        System.out.println("Item not found: " + itemName);
        return false;
    }
    
    public void displayItems() {
        if (storage.isEmpty()) {
            System.out.println("Bag is empty.");
            return;
        }
        
        System.out.println("\n=== ITEMS ===");
        for (int i = 0; i < storage.size(); i++) {
            Item item = storage.get(i);
            System.out.printf("%d. %s (x%d)%n", i + 1, item.getName(), item.getQuantity());
        }
    }
    
    public boolean isEmpty() { return storage.isEmpty(); }
    public int getItemCount() { return storage.size(); }
    public Item getItem(int index) {
        if (index < 0 || index >= storage.size()) return null;
        return storage.get(index);
    }
    public List<Item> getItems() { return new ArrayList<>(storage); }
}