package Difficulty;

import Entity.EntityWave;
import combatant.Goblin;
import combatant.Wolf;
import combatant.Combatant;
import java.util.ArrayList;
import java.util.List;

public class Medium implements Difficulty {
    private List<EntityWave> combatWaves;
    private int currentWave;
    
    public Medium() {
        this.combatWaves = new ArrayList<>();
        this.currentWave = 0;
        
        List<Combatant> wave1Entities = new ArrayList<>();
        wave1Entities.add(new Goblin("Goblin"));
        wave1Entities.add(new Wolf("Wolf"));
        combatWaves.add(new EntityWave(wave1Entities, 1));
        
        List<Combatant> wave2Entities = new ArrayList<>();
        wave2Entities.add(new Wolf("Wolf A"));
        wave2Entities.add(new Wolf("Wolf B"));
        combatWaves.add(new EntityWave(wave2Entities, 2));
    }
    
    @Override
    public List<EntityWave> getAllWaves() { return combatWaves; }
    
    @Override
    public EntityWave getNextWave() {
        if (currentWave < combatWaves.size()) {
            return combatWaves.get(currentWave++);
        }
        return null;
    }
    
    @Override
    public void addCombatWave(EntityWave wave) { combatWaves.add(wave); }
    
    @Override
    public void removeCombatWave(EntityWave wave) { combatWaves.remove(wave); }
    
    @Override
    public int getCurrentWaveIndex() { return currentWave; }
    
    @Override
    public boolean hasNextWave() { return currentWave < combatWaves.size(); }
}