package modifier;

public class AttackModifier {
    private double attackModifier;
    
    public AttackModifier(double modifier) {
        this.attackModifier = modifier;
    }
    
    public double getAttackModifier() { return attackModifier; }
    public void setAttackModifier(double modifier) { this.attackModifier = modifier; }
    
    public int applyAttackModifier(int attack, char operation) {
        switch(operation) {
            case '+': return (int)(attack + this.attackModifier);
            case '-': return (int)(attack - this.attackModifier);
            case '*': return (int)(attack * this.attackModifier);
            case '/': return (int)(attack / this.attackModifier);
            default: return attack;
        }
    }
}