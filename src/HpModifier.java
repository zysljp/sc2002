// HpModifier.java
public class HpModifier {
    private double hpModifier = 1.0;
    public HpModifier(double modifier) { this.hpModifier = modifier; }
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