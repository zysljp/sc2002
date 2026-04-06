package modifier;

public class HpModifier {
    private double hpModifier;
    
    public HpModifier(double modifier) {
        this.hpModifier = modifier;
    }
    
    public double getHpModifier() { return hpModifier; }
    public void setHpModifier(double modifier) { this.hpModifier = modifier; }
    
    public int applyHpModifier(int hp, char operation) {
        switch(operation) {
            case '+': return (int)(hp + this.hpModifier);
            case '-': return (int)(hp - this.hpModifier);
            case '*': return (int)(hp * this.hpModifier);
            case '/': return (int)(hp / this.hpModifier);
            default: return hp;
        }
    }
}