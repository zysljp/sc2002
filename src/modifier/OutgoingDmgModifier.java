package modifier;

public class OutgoingDmgModifier {
    private double outgoingDamageModifier;
    
    public OutgoingDmgModifier(double modifier) {
        this.outgoingDamageModifier = modifier;
    }
    
    public double getOutgoingDamageModifier() { return outgoingDamageModifier; }
    public void setOutgoingDamageModifier(double modifier) { this.outgoingDamageModifier = modifier; }
    
    public int applyOutgoingDamageModifier(int damage, char operation) {
        switch(operation) {
            case '+': return (int)(damage + this.outgoingDamageModifier);
            case '-': return (int)(damage - this.outgoingDamageModifier);
            case '*': return (int)(damage * this.outgoingDamageModifier);
            case '/': return (int)(damage / this.outgoingDamageModifier);
            default: return damage;
        }
    }
}