public abstract class Player extends Combatant {
    protected int specialCooldown = 0;
    protected Action specialSkill;
    protected ItemBag bag;

    public Player(String name, int health, int attack, int defense, int speed, Action skill) {
        super(name, health, attack, defense, speed);
        this.specialSkill = skill;
        this.bag = new ItemBag(10, this); 
    }

    public void useSpecial(java.util.List<Combatant> targets) {
        if (specialCooldown == 0) {
            specialSkill.execute(this, targets);
            specialCooldown = 3; // Cooldown including current turn
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
    	if (specialCooldown > 0) 
    		specialCooldown--; 
    }

    public int getSpecialCooldown() {
        return specialCooldown;
    }

    public String getSpecialSkillName() {
        return specialSkill.getName();
    }
    
    public ItemBag getBag() { 
    	return bag; 
    }
    
    public void setBag(ItemBag bag) { 
    	this.bag = bag; 
    }
    
    public void setSpecialCooldown(int cooldown) {
        this.specialCooldown = cooldown;
    }
    
    
}