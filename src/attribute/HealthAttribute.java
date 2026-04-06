package attribute;

public class HealthAttribute extends Attribute {
    private int maxHealth;
    
    public HealthAttribute(int value) {
        super(value);
        this.maxHealth = value;
    }
    
    public int getMaxHealth() { return maxHealth; }
    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }
    
    public void takeDamage(int amount) {
        this.value = Math.max(0, this.value - amount);
    }
    
    public void heal(int amount) {
        this.value = Math.min(maxHealth, this.value + amount);
    }
    
    public boolean isDead() { return this.value <= 0; }
    
    @Override
    public void setValue(int value) {
        this.value = Math.max(0, Math.min(value, maxHealth));
    }
}