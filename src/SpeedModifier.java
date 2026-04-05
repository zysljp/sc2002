// SpeedModifier.java
public class SpeedModifier {
    private double speedModifier = 1.0;
    public SpeedModifier(double modifier) { this.speedModifier = modifier; }
    public double getSpeedModifier() { return speedModifier; }
    public void setSpeedModifier(double modifier) { this.speedModifier = modifier; }
    public int applySpeedModifier(int speed, char operation) {
        switch(operation) {
            case '+': return (int)(speed + this.speedModifier);
            case '-': return (int)(speed - this.speedModifier);
            case '*': return (int)(speed * this.speedModifier);
            case '/': return (int)(speed / this.speedModifier);
            default: return speed;
        }
    }
}