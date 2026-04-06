package Difficulty;

import Entity.EntityWave;
import combatant.Goblin;
import combatant.Combatant;
import java.util.ArrayList;
import java.util.List;

public class Easy implements Difficulty {
    private List<EntityWave> combatWaves;
    private int currentWave;
    
    public Easy() {
        this.combatWaves = new ArrayList<>();
        this.currentWave = 0;
        
        List<Combatant> wave1Entities = new ArrayList<>();
        wave1Entities.add(new Goblin("Goblin A"));
        wave1Entities.add(new Goblin("Goblin B"));
        wave1Entities.add(new Goblin("Goblin C"));
        combatWaves.add(new EntityWave(wave1Entities, 1));
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