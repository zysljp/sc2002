package combatant;

import action.Action;
import item.Item;
import item.ItemBag;
import status.DefendStatus;

public abstract class Player extends Combatant {
    protected int specialCooldown = 0;
    protected Action specialSkill;
    protected ItemBag bag;
    protected int startingNumOfItems;
    protected String specialSkillName;
    protected int specialSkillCD;
    
    public Player(String name, int health, int attack, int defense, int speed, Action skill) {
        super(name, health, attack, defense, speed);
        this.specialSkill = skill;
        this.specialSkillName = skill.getName();
        this.specialSkillCD = 3;
        this.bag = new ItemBag(10, this);
        this.startingNumOfItems = 2;
    }
    
    public void useItem(Item item, java.util.List<Combatant> targets) {
        bag.useItem(item.getName(), targets);
    }
    
    public void gainItem(Item item) {
        bag.gainItem(item);
    }
    
    public void loseItem(String itemName) {
        bag.loseItem(itemName);
    }
    
    public void defend() {
        new DefendStatus(this, 2);
    }
    
    public void specialSkill(java.util.List<Combatant> targets) {
        useSpecial(targets);
    }
    
    public void useSpecial(java.util.List<Combatant> targets) {
        if (specialCooldown == 0) {
            specialSkill.execute(this, targets);
            specialCooldown = specialSkillCD;
        } else {
            System.out.println("Skill on cooldown! " + specialCooldown + " turns left.");
        }
    }
    
    public void usePowerStoneSpecial(java.util.List<Combatant> targets) {
        int savedCooldown = specialCooldown;
        specialSkill.execute(this, targets);
        specialCooldown = savedCooldown;
        System.out.println(">> Power Stone used! Special skill triggered without affecting cooldown!");
    }
    
    public void updateCooldown() {
        if (specialCooldown > 0) specialCooldown--;
    }
    
    public int getSpecialCooldown() { return specialCooldown; }
    public void setSpecialCooldown(int cooldown) { this.specialCooldown = cooldown; }
    public String getSpecialSkillName() { return specialSkillName; }
    public ItemBag getBag() { return bag; }
    public void setBag(ItemBag bag) { this.bag = bag; }
}