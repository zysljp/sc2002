package modifier;

public class DefenceModifier {
    private double defenceModifier;
    
    public DefenceModifier(double modifier) {
        this.defenceModifier = modifier;
    }
    
    public double getDefenceModifier() { return defenceModifier; }
    public void setDefenceModifier(double modifier) { this.defenceModifier = modifier; }
    
    public int applyDefenceModifier(int defence, char operation) {
        switch(operation) {
            case '+': return (int)(defence + this.defenceModifier);
            case '-': return (int)(defence - this.defenceModifier);
            case '*': return (int)(defence * this.defenceModifier);
            case '/': return (int)(defence / this.defenceModifier);
            default: return defence;
        }
    }
}