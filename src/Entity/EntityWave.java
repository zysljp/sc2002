package Entity;

import combatant.Combatant;
import java.util.ArrayList;
import java.util.List;

public class EntityWave {
    private List<Combatant> allCharacters;
    private int numOfCharacters;
    private int waveNum;
    
    public EntityWave(List<Combatant> characters, int waveNum) {
        this.allCharacters = new ArrayList<>(characters);
        this.numOfCharacters = characters.size();
        this.waveNum = waveNum;
    }
    
    public void addToWave(Combatant character) {
        allCharacters.add(character);
        numOfCharacters++;
    }
    
    public void removeFromWave(Combatant character) {
        allCharacters.remove(character);
        numOfCharacters--;
    }
    
    public List<Combatant> getWave() { return new ArrayList<>(allCharacters); }
    public int getNumOfCharacters() { return numOfCharacters; }
    public void setNumOfCharacters(int num) { this.numOfCharacters = num; }
    public int getWaveNum() { return waveNum; }
    public void setWaveNum(int waveNum) { this.waveNum = waveNum; }
    
    public List<Combatant> getAliveCharacters() {
        List<Combatant> alive = new ArrayList<>();
        for (Combatant c : allCharacters) {
            if (!c.getHealth().isDead()) {
                alive.add(c);
            }
        }
        return alive;
    }
}