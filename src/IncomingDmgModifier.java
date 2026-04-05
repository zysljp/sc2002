// IncomingDmgModifier.java
public class IncomingDmgModifier {
    private double incomingDamageModifier = 1.0;
    public IncomingDmgModifier(double modifier) { this.incomingDamageModifier = modifier; }
    public double getIncomingDamageModifier() { return incomingDamageModifier; }
    public void setIncomingDamageModifier(double modifier) { this.incomingDamageModifier = modifier; }
    public int applyIncomingDamageModifier(int damage, char operation) {
        switch(operation) {
            case '+': return (int)(damage + this.incomingDamageModifier);
            case '-': return (int)(damage - this.incomingDamageModifier);
            case '*': return (int)(damage * this.incomingDamageModifier);
            case '/': return (int)(damage / this.incomingDamageModifier);
            default: return damage;
        }
    }
}