public class DamageReductionEffect extends StatusEffect {
    private int roundsRemaining;
    
    public DamageReductionEffect(int rounds) {
        super("Damage Reduction", rounds);
        this.roundsRemaining = rounds;
    }
    
    public boolean isActive() {
        return roundsRemaining > 0;
    }
    
    public void endOfRound() {
        if (roundsRemaining > 0) {
            roundsRemaining--;
        }
    }
    
    @Override
    public void decrementDuration() {
        // 不使用这个，用 endOfRound 代替
    }
    
    @Override
    public boolean isExpired() {
        return roundsRemaining <= 0;
    }
    
    public int getRoundsRemaining() {
        return roundsRemaining;
    }
}