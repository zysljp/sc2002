package Difficulty;

import Entity.EntityWave;
import java.util.List;

public interface Difficulty {
    List<EntityWave> getAllWaves();
    EntityWave getNextWave();
    void addCombatWave(EntityWave wave);
    void removeCombatWave(EntityWave wave);
    int getCurrentWaveIndex();
    boolean hasNextWave();
}