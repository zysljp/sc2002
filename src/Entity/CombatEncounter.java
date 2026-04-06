package Entity;

import combatant.Player;
import combatant.Enemy;
import combatant.Combatant;
import combatant.Warrior;
import combatant.Wizard;
import Difficulty.Difficulty;
import action.BasicAttack;
import display.BattleDisplay;
import trigger.GlobalTriggers;
import trigger.GlobalTriggerTypes;
import trigger.LocalTriggerTypes;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CombatEncounter {
    private Difficulty difficultyWaves;
    private SpeedTracker turnOrder;
    private Player player;
    private List<Enemy> currentEnemies;
    private EntityWave currentWave;
    private BattleDisplay display;
    private int roundCounter;
    private boolean battleEnded;
    private Scanner scanner;
    private TargetGetter targetGetter;
    
    public CombatEncounter(Player player, Difficulty difficulty, Scanner scanner) {
        this.player = player;
        this.difficultyWaves = difficulty;
        this.turnOrder = new SpeedTracker();
        this.display = new BattleDisplay();
        this.roundCounter = 1;
        this.battleEnded = false;
        this.currentEnemies = new ArrayList<>();
        this.scanner = scanner;
        this.targetGetter = new TargetGetter(scanner);
        
        loadNextWave();
    }
    
    private void loadNextWave() {
        currentWave = difficultyWaves.getNextWave();
        if (currentWave != null) {
            currentEnemies.clear();
            for (Combatant c : currentWave.getWave()) {
                if (c instanceof Enemy) {
                    currentEnemies.add((Enemy) c);
                }
            }
            if (currentWave.getWaveNum() > 1) {
                display.showBackupSpawn(currentEnemies);
            }
        }
    }
    
    public void startBattle() {
        while (!battleEnded) {
            startRound();
            processAllTurns();
            endRound();
        }
    }
    
    public void startRound() {
        if (battleEnded) return;
        
        display.showRoundHeader(roundCounter);
        display.showStats(player, currentEnemies);
        
        GlobalTriggers.getInstance().trigger(GlobalTriggerTypes.ON_ROUND_START);
        
        List<Combatant> participants = getAliveParticipants();
        turnOrder.updateTurnOrder(participants);
        display.showTurnOrder(turnOrder.getTurnOrder());
        turnOrder.reset();
    }
    
    public void processAllTurns() {
        while (turnOrder.hasNext() && !battleEnded) {
            processTurn();
        }
    }
    
    public void processTurn() {
        Combatant current = turnOrder.getCurrTurnOrder();
        
        if (current == null || current.getHealth().isDead()) {
            turnOrder.advanceTurn();
            return;
        }
        
        current.getLocalTriggerList().trigger(LocalTriggerTypes.ON_TURN_START);
        
        if (current instanceof Player) {
            handlePlayerTurn((Player) current);
        } else if (current instanceof Enemy) {
            handleEnemyTurn((Enemy) current);
        }
        
        current.getLocalTriggerList().trigger(LocalTriggerTypes.ON_TURN_END);
        
        turnOrder.advanceTurn();

        updateAliveStatus();
        checkGameState();
    }
    
    private void handlePlayerTurn(Player p) {
        if (p.isStunned()) {
            System.out.println("\n!! " + p.getName() + " is stunned and cannot move !!");
            return;
        }
        
        boolean turnComplete = false;
        while (!turnComplete) {
            display.showMenu(p);
            int choice = scanner.nextInt();
            
            switch (choice) {
                case 1:
                    handleBasicAttack(p);
                    turnComplete = true;
                    break;
                case 2:
                    handleSpecialSkill(p);
                    turnComplete = true;
                    break;
                case 3:
                    handleDefend(p);
                    turnComplete = true;
                    break;
                case 4:
                    handleItemUsage(p);
                    turnComplete = true;
                    break;
                default:
                    System.out.println(">> INVALID CHOICE. Please enter 1, 2, 3, or 4.");
            }
        }
        
        p.updateCooldown();
    }
    
    private void handleBasicAttack(Player p) {
        List<Enemy> aliveEnemies = getAliveEnemies();
        if (aliveEnemies.isEmpty()) {
            System.out.println(">> No enemies to attack!");
            return;
        }
        
        Combatant target = targetGetter.selectSingleTarget(aliveEnemies, "Select target:");
        if (target != null) {
            int hpBefore = target.getHealth().getValue();
            new BasicAttack().execute(p, List.of(target));
            int damage = hpBefore - target.getHealth().getValue();
            display.showBasicAttack(p, target, damage);
        }
    }
    
    private void handleSpecialSkill(Player p) {
        if (p.getSpecialCooldown() != 0) {
            System.out.println(">> ERROR: " + p.getSpecialSkillName() + " is on cooldown! (" + p.getSpecialCooldown() + " turns left)");
            return;
        }
        
        List<Enemy> aliveEnemies = getAliveEnemies();
        if (aliveEnemies.isEmpty()) {
            System.out.println(">> No enemies to target!");
            return;
        }
        
        if (p instanceof Warrior) {
            Combatant target = targetGetter.selectSingleTarget(aliveEnemies, "Select target for Shield Bash:");
            if (target != null) {
                int hpBefore = target.getHealth().getValue();
                p.useSpecial(List.of(target));
                int damage = hpBefore - target.getHealth().getValue();
                display.showSpecialAttack(p, target, damage, "Shield Bash");
                display.showStun(target, 2);
            }
        } else if (p instanceof Wizard) {
            System.out.println(">> Arcane Blast hits all enemies!");
            p.useSpecial(new ArrayList<>(aliveEnemies));
        }
    }
    
    private void handleDefend(Player p) {
        p.defend();
        System.out.println(">> " + p.getName() + " raises their shield! Defense increased for this round.");
    }
    
    private void handleItemUsage(Player p) {
        if (p.getBag().isEmpty()) {
            System.out.println(">> Your bag is empty!");
            return;
        }
        
        p.getBag().displayItems();
        System.out.print("Select item to use (1-" + p.getBag().getItemCount() + "), or 0 to cancel: ");
        int itemChoice = scanner.nextInt();
        
        if (itemChoice == 0) {
            System.out.println(">> Cancelled item usage.");
            return;
        }
        
        if (itemChoice < 1 || itemChoice > p.getBag().getItemCount()) {
            System.out.println(">> Invalid item selection!");
            return;
        }
        
        List<Enemy> aliveEnemies = getAliveEnemies();
        p.getBag().useItem(itemChoice - 1, new ArrayList<>(aliveEnemies));
    }

    private void handleEnemyTurn(Enemy e) {
        if (e.isStunned()) {
            display.showEnemyStunned(e);
            return;
        }

        boolean hasSmokeBomb = player.getActiveEffects().stream()
            .anyMatch(effect -> effect.getName().equals("Smoke Bomb Invulnerability") && !effect.isExpired());
        
        int hpBefore = player.getHealth().getValue();
        new BasicAttack().execute(e, List.of(player));
        int damage = hpBefore - player.getHealth().getValue();
        
        if (hasSmokeBomb && damage == 0) {
            display.showZeroDamage(e, player);
        } else {
            display.showBasicAttack(e, player, damage);
        }
    }
    
    public void endRound() {
        List<Enemy> eliminated = new ArrayList<>();
        for (Enemy e : currentEnemies) {
            if (e.getHealth().isDead()) {
                eliminated.add(e);
                display.showEnemyEliminated(e);
            }
        }
        currentEnemies.removeAll(eliminated);

        player.updateEffects();
        for (Enemy e : currentEnemies) {
            e.updateEffects();
        }

        displayStats();

        if (currentEnemies.isEmpty()) {
            if (difficultyWaves.hasNextWave()) {
                loadNextWave();
            } else {
                battleEnded = true;
                display.showVictory(player, roundCounter, 0, 0, 0);
                return;
            }
        }

        if (player.getHealth().isDead()) {
            battleEnded = true;
            display.showGameOver(player, roundCounter, 0, 0, 0);
            return;
        }
        
        roundCounter++;
        GlobalTriggers.getInstance().trigger(GlobalTriggerTypes.ON_ROUND_END);
    }
    
    public void updateAliveStatus() {
        for (Enemy e : currentEnemies) {
            if (e.getHealth().isDead()) {
                e.getLocalTriggerList().trigger(LocalTriggerTypes.ON_DEATH);
            }
        }
    }
    
    public void checkBackup() {
        if (currentEnemies.isEmpty() && difficultyWaves.hasNextWave()) {
            deployBackup();
        }
    }
    
    public void deployBackup() {
        loadNextWave();
    }
    
    public void checkGameState() {
        if (player.getHealth().isDead()) {
            battleEnded = true;
            display.showGameOver(player, roundCounter, 0, 0, 0);
        }
        
        if (currentEnemies.isEmpty() && !difficultyWaves.hasNextWave()) {
            battleEnded = true;
            display.showVictory(player, roundCounter, 0, 0, 0);
        }
    }
    
    public void combatEnd() {
        battleEnded = true;
    }
    
    private List<Combatant> getAliveParticipants() {
        List<Combatant> participants = new ArrayList<>();
        if (!player.getHealth().isDead()) {
            participants.add(player);
        }
        for (Enemy e : currentEnemies) {
            if (!e.getHealth().isDead()) {
                participants.add(e);
            }
        }
        return participants;
    }
    
    private List<Enemy> getAliveEnemies() {
        List<Enemy> alive = new ArrayList<>();
        for (Enemy e : currentEnemies) {
            if (!e.getHealth().isDead()) {
                alive.add(e);
            }
        }
        return alive;
    }

    private void displayStats() {
        int potionCount = 0, smokeBombCount = 0, powerStoneCount = 0;
        for (item.Item item : player.getBag().getItems()) {
            if (item instanceof item.Potion) potionCount = item.getQuantity();
            else if (item instanceof item.SmokeBomb) smokeBombCount = item.getQuantity();
            else if (item instanceof item.PowerStone) powerStoneCount = item.getQuantity();
        }
        
        display.showEndRoundSummary(player, currentEnemies, roundCounter, 
            potionCount, smokeBombCount, powerStoneCount, player.getSpecialCooldown());
    }
    
    public boolean isBattleEnded() { return battleEnded; }
}