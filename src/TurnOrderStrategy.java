// TurnOrderStrategy.java
import java.util.Comparator;
import java.util.List;

public class TurnOrderStrategy {
    public void sortBySpeed(List<Combatant> participants) {
        participants.sort(Comparator.comparingInt(Combatant::getTotalSpeed).reversed());
    }
}