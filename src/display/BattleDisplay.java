package display;

import combatant.Player;
import combatant.Enemy;
import combatant.Combatant;
import java.util.List;

public class BattleDisplay {
    
    public void showRoundHeader(int round) {
        System.out.println("\n========================================");
        System.out.println("             ROUND " + round);
        System.out.println("========================================");
    }
    
    public void showStats(Player p, List<Enemy> enemies) {
        System.out.println("\n--- PARTY STATUS ---");
        System.out.printf("%-12s | HP: %-3d/%-3d | ATK: %-3d | DEF: %-2d | SPD: %-2d%n",
                p.getName(), p.getHealth().getValue(), p.getHealth().getMaxHealth(), 
                p.getAttack().getValue(), p.getDefense().getValue(), p.getSpeed().getValue());

        System.out.println("\n--- ENEMIES ---");
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            System.out.printf("%d. %-12s | HP: %-3d/%-3d | ATK: %-3d | DEF: %-2d | SPD: %-2d%n",
                    (i + 1), e.getName(), e.getHealth().getValue(), e.getHealth().getMaxHealth(),
                    e.getAttack().getValue(), e.getDefense().getValue(), e.getSpeed().getValue());
        }
    }
    
    public void showMenu(Player p) {
        System.out.println("\n--- YOUR TURN: " + p.getName() + " ---");
        System.out.println("1. Basic Attack");
        if (p.getSpecialCooldown() == 0) {
            System.out.println("2. Special Skill: " + p.getSpecialSkillName());
        } else {
            System.out.println("2. [LOCKED] Special Skill (" + p.getSpecialCooldown() + " turns left)");
        }
        System.out.println("3. Defend");
        System.out.println("4. Use Item");
        System.out.print("Choose an action: ");
    }
    
    public void showTurnOrder(List<Combatant> order) {
        System.out.print("Turn Order: ");
        for (int i = 0; i < order.size(); i++) {
            System.out.print(order.get(i).getName() + " (SPD " + order.get(i).getTotalSpeed() + ")");
            if (i < order.size() - 1) System.out.print(" → ");
        }
        System.out.println();
    }
    
    public void showBasicAttack(Combatant attacker, Combatant target, int damage) {
        int oldHp = target.getHealth().getValue() + damage;
        int newHp = target.getHealth().getValue();
        System.out.printf("%s → BasicAttack → %s: HP: %d → %d (dmg: %d)%n",
                attacker.getName(), target.getName(), oldHp, newHp, damage);
    }
    
    public void showSpecialAttack(Combatant attacker, Combatant target, int damage, String skillName) {
        int oldHp = target.getHealth().getValue() + damage;
        int newHp = target.getHealth().getValue();
        System.out.printf("%s → %s → %s: HP: %d → %d (dmg: %d)",
                attacker.getName(), skillName, target.getName(), oldHp, newHp, damage);
    }
    
    public void showStun(Combatant target, int duration) {
        System.out.printf(" | %s STUNNED (%d turns)%n", target.getName(), duration);
    }
    
    public void showEnemyStunned(Combatant enemy) {
        System.out.printf("%s → STUNNED: Turn skipped%n", enemy.getName());
    }
    
    public void showEnemyEliminated(Combatant enemy) {
        System.out.printf("%s → ELIMINATED%n", enemy.getName());
    }
    
    public void showItemUse(Player player, String itemName) {
        System.out.printf("%s → Item → %s used%n", player.getName(), itemName);
    }
    
    public void showPotionHeal(Player player, int healAmount, int newHp, int maxHp) {
        int oldHp = newHp - healAmount;
        System.out.printf("%s → Item → Potion used: HP: %d → %d (+%d)%n",
                player.getName(), oldHp, newHp, healAmount);
    }
    
    public void showSmokeBombUse(Player player) {
        System.out.printf("%s → Item → Smoke Bomb used: Enemy attacks deal 0 damage for 2 turns%n", player.getName());
    }
    
    public void showPowerStoneUse(Player player, String skillName) {
        System.out.printf("%s → Item → Power Stone used → %s triggered%n", player.getName(), skillName);
    }
    
    public void showZeroDamage(Combatant attacker, Combatant defender) {
        System.out.printf("%s → BasicAttack → %s: 0 damage (Smoke Bomb active)%n", 
                attacker.getName(), defender.getName());
    }
    
    public void showCooldownSet(Player player, int cooldown) {
        System.out.printf(" | Cooldown set to %d%n", cooldown);
    }
    
    public void showBackupSpawn(List<Enemy> backupEnemies) {
        System.out.println("\n========================================");
        System.out.println("!!! BACKUP SPAWN TRIGGERED !!!");
        System.out.println("========================================");
        System.out.print("New enemies appear: ");
        for (int i = 0; i < backupEnemies.size(); i++) {
            Enemy e = backupEnemies.get(i);
            System.out.printf("%s (HP: %d)", e.getName(), e.getHealth().getValue());
            if (i < backupEnemies.size() - 1) System.out.print(" + ");
        }
        System.out.println("\n========================================\n");
    }
    
    public void showVictory(Player player, int totalRounds, int potionCount, int smokeBombCount, int powerStoneCount) {
        System.out.println("\n========================================");
        System.out.println("              VICTORY!");
        System.out.println("========================================");
        System.out.printf("Result: Player Victory | Remaining HP: %d/%d | Total Rounds: %d%n",
                player.getHealth().getValue(), player.getHealth().getMaxHealth(), totalRounds);
        System.out.printf("Remaining Potion: %d | Remaining Smoke Bomb: %d | Remaining Power Stone: %d%n",
                potionCount, smokeBombCount, powerStoneCount);
        System.out.println("========================================");
    }
    
    public void showGameOver(Player player, int totalRounds, int potionCount, int smokeBombCount, int powerStoneCount) {
        System.out.println("\n========================================");
        System.out.println("            GAME OVER");
        System.out.println("========================================");
        System.out.printf("Result: Defeat | Total Rounds: %d%n", totalRounds);
        System.out.printf("Remaining Potion: %d | Remaining Smoke Bomb: %d | Remaining Power Stone: %d%n",
                potionCount, smokeBombCount, powerStoneCount);
        System.out.println("========================================");
    }
    
    public void showCooldownRemaining(int cooldown) {
        System.out.printf(" | Cooldown: %d%n", cooldown);
    }
    
    public void showSmokeBombExpiry() {
        System.out.print(" | Smoke Bomb effect expires");
    }
    
    public void showEndRoundSummary(Player player, List<Enemy> enemies, int roundCounter, 
                                     int potionCount, int smokeBombCount, int powerStoneCount, 
                                     int specialCooldown) {
        System.out.println("\n========== END OF ROUND " + roundCounter + " ==========");
        System.out.printf("%s HP: %d/%d | ", player.getName(), player.getHealth().getValue(), player.getHealth().getMaxHealth());
        
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            System.out.printf("%s HP: %d", e.getName(), e.getHealth().getValue());
            if (e.isStunned()) {
                System.out.print(" [STUNNED]");
            }
            if (i < enemies.size() - 1) {
                System.out.print(" | ");
            }
        }
        
        System.out.printf(" | Potion: %d | Smoke Bomb: %d | Power Stone: %d | Special Skills Cooldown: %d rounds%n",
                potionCount, smokeBombCount, powerStoneCount, specialCooldown);
        System.out.println("========================================\n");
    }
    
    public void showElimination(Combatant target) {
        System.out.printf("%s → ELIMINATED: Skipped%n", target.getName());
    }
    
    public void showStunExpire(Combatant target) {
        System.out.printf(" | %s stun expires%n", target.getName());
    }
    
    public void showSmokeBombEffectExpire() {
        System.out.println(" | Smoke Bomb effect expires");
    }
}