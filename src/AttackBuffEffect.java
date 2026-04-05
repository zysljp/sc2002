public class AttackBuffEffect extends StatusEffect {
    private int amount;

    public AttackBuffEffect(int amount, int duration) {
        super("Attack Buff", duration);
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}