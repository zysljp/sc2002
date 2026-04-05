public class DefenseBuffEffect extends StatusEffect {
    private int bonusAmount;

    public DefenseBuffEffect(int amount, int duration) {
        super("Defending", duration);
        this.bonusAmount = amount;
    }

    public int getBonusAmount() {
        return bonusAmount;
    }
}