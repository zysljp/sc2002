import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean playing = true;
        
        while (playing) {
            System.out.println("\n========================================");
            System.out.println("       TURN-BASED COMBAT ARENA");
            System.out.println("========================================");
            System.out.println("1. New Game");
            System.out.println("2. Exit");
            System.out.print("Choice » ");
            
            int mainChoice = scanner.nextInt();
            if (mainChoice == 2) {
                playing = false;
                System.out.println("Thanks for playing!");
                break;
            }
            
            System.out.print("\nEnter your Hero's name: ");
            scanner.nextLine();
            String name = scanner.nextLine();
            
            Player player = selectClass(scanner, name);
            if (player == null) continue;
            
            selectStartingItems(scanner, player);
            
            LevelFactory.LevelConfig levelConfig = selectDifficulty(scanner);
            if (levelConfig == null) continue;
            
            System.out.println("\n========================================");
            System.out.println("              GAME START");
            System.out.println("========================================");
            
            startBattle(scanner, player, levelConfig);
            
            System.out.print("\nPlay again? (1=Yes, 2=No): ");
            int replay = scanner.nextInt();
            if (replay != 1) {
                playing = false;
                System.out.println("Thanks for playing!");
            }
        }
        
        scanner.close();
    }
    
    private static Player selectClass(Scanner scanner, String name) {
        while (true) {
            System.out.println("\nSelect your class:");
            System.out.println("1. Warrior");
            System.out.println("2. Wizard");
            System.out.print("Choice » ");
            
            int classChoice = scanner.nextInt();
            if (classChoice == 1) {
                Warrior w = new Warrior(name);
                System.out.println("\n--- WARRIOR STATS ---");
                System.out.printf("HP: %d | ATK: %d | DEF: %d | SPD: %d%n", 
                    w.getHealth().getValue(), w.getAttack().getValue(), 
                    w.getDefense().getValue(), w.getSpeed().getValue());
                System.out.println("Special: Shield Bash - Stuns enemy for 2 turns");
                return w;
            } else if (classChoice == 2) {
                Wizard w = new Wizard(name);
                System.out.println("\n--- WIZARD STATS ---");
                System.out.printf("HP: %d | ATK: %d | DEF: %d | SPD: %d%n", 
                    w.getHealth().getValue(), w.getAttack().getValue(), 
                    w.getDefense().getValue(), w.getSpeed().getValue());
                System.out.println("Special: Arcane Blast - Hits all enemies, +10 ATK per kill");
                return w;
            } else {
                System.out.println(">> Invalid class choice. Please select 1 or 2.");
            }
        }
    }
    
    private static void selectStartingItems(Scanner scanner, Player player) {
        System.out.println("\n=== SELECT STARTING ITEMS ===");
        System.out.println("You can choose 2 items (duplicates allowed)");
        System.out.println("1. Potion - Heals 100 HP");
        System.out.println("2. Power Stone - Free special skill (no cooldown)");
        System.out.println("3. Smoke Bomb - Enemy attacks deal 0 damage for 2 turns");
        
        for (int i = 0; i < 2; i++) {
            System.out.printf("\nChoose item %d: ", i + 1);
            int choice = scanner.nextInt();
            
            switch (choice) {
                case 1:
                    player.getBag().gainItem(new Potion());
                    System.out.println(">> Added Potion");
                    break;
                case 2:
                    player.getBag().gainItem(new PowerStone());
                    System.out.println(">> Added Power Stone");
                    break;
                case 3:
                    player.getBag().gainItem(new SmokeBomb());
                    System.out.println(">> Added Smoke Bomb");
                    break;
                default:
                    System.out.println("Invalid choice, defaulting to Potion");
                    player.getBag().gainItem(new Potion());
            }
        }
        
        System.out.println("\n=== STARTING ITEMS ===");
        player.getBag().displayItems();
    }
    
    private static LevelFactory.LevelConfig selectDifficulty(Scanner scanner) {
        while (true) {
            System.out.println("\nSelect Difficulty:");
            System.out.println("1. Easy (3 Goblins)");
            System.out.println("2. Medium (1 Goblin, 1 Wolf + Backup: 2 Wolves)");
            System.out.println("3. Hard (2 Goblins + Backup: 1 Goblin, 2 Wolves)");
            System.out.print("Choice » ");
            
            int diffChoice = scanner.nextInt();
            LevelFactory.LevelConfig config = LevelFactory.createLevel(diffChoice);
            if (config != null) {
                return config;
            } else {
                System.out.println(">> Invalid difficulty. Please select 1, 2, or 3.");
            }
        }
    }
    
    private static void startBattle(Scanner scanner, Player player, LevelFactory.LevelConfig levelConfig) {
        List<Enemy> enemies = levelConfig.getInitialEnemies();
        TurnOrderStrategy turnStrategy = new TurnOrderStrategy();
        BattleDisplay display = new BattleDisplay();
        boolean backupSpawned = false;
        int roundCounter = 1;
        
        GlobalTriggers.getInstance();
        
        while (!isGameOver(player, enemies, backupSpawned, levelConfig)) {
            display.showRoundHeader(roundCounter);
            display.showStats(player, enemies);
            
            GlobalTriggers.getInstance().trigger(Global_Trigger_Types.ON_ROUND_START);
            
            List<Combatant> participants = getAliveParticipants(player, enemies);
            turnStrategy.sortBySpeed(participants);
            display.showTurnOrder(participants);
            
            for (Combatant c : participants) {
                if (isGameOver(player, enemies, backupSpawned, levelConfig)) break;
                if (c.getHealth().isDead()) continue;
                
                c.getLocalTriggerList().trigger(Local_Trigger_Types.ON_TURN_START);
                
                if (c instanceof Player) {
                    handlePlayerTurn(scanner, (Player) c, enemies, display);
                } else {
                    handleEnemyTurn((Enemy) c, player, display);
                }
                
                c.getLocalTriggerList().trigger(Local_Trigger_Types.ON_TURN_END);
            }
            
            int potionCount = 0, smokeBombCount = 0, powerStoneCount = 0;
            for (Item item : player.getBag().getItems()) {
                if (item instanceof Potion) potionCount = item.getQuantity();
                else if (item instanceof SmokeBomb) smokeBombCount = item.getQuantity();
                else if (item instanceof PowerStone) powerStoneCount = item.getQuantity();
            }
            
            display.showEndRoundSummary(player, enemies, roundCounter, 
                potionCount, smokeBombCount, powerStoneCount, player.getSpecialCooldown());
            
            roundCounter = endRound(player, enemies, roundCounter, levelConfig, backupSpawned, display);
            
            if (!backupSpawned && levelConfig.hasBackup() && enemies.isEmpty()) {
                backupSpawned = true;
            }
            
            GlobalTriggers.getInstance().trigger(Global_Trigger_Types.ON_ROUND_END);
        }
        
        int potionCount = 0, smokeBombCount = 0, powerStoneCount = 0;
        for (Item item : player.getBag().getItems()) {
            if (item instanceof Potion) potionCount = item.getQuantity();
            else if (item instanceof SmokeBomb) smokeBombCount = item.getQuantity();
            else if (item instanceof PowerStone) powerStoneCount = item.getQuantity();
        }
        
        if (player.getHealth().isDead()) {
            display.showGameOver(player, roundCounter - 1, potionCount, smokeBombCount, powerStoneCount);
        } else {
            display.showVictory(player, roundCounter - 1, potionCount, smokeBombCount, powerStoneCount);
        }
    }
    
    private static List<Combatant> getAliveParticipants(Player player, List<Enemy> enemies) {
        List<Combatant> participants = new ArrayList<>();
        if (!player.getHealth().isDead()) {
            participants.add(player);
        }
        for (Enemy e : enemies) {
            if (!e.getHealth().isDead()) {
                participants.add(e);
            }
        }
        return participants;
    }
    
    private static void handlePlayerTurn(Scanner scanner, Player p, List<Enemy> enemies, BattleDisplay display) {
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
                    handleBasicAttack(scanner, p, enemies, display);
                    turnComplete = true;
                    break;
                case 2:
                    handleSpecialSkill(scanner, p, enemies, display);
                    turnComplete = true;
                    break;
                case 3:
                    new DefendAction().execute(p, null);
                    System.out.println(">> " + p.getName() + " raises their shield! Defense increased by 10 for this round.");
                    turnComplete = true;
                    break;
                case 4:
                    handleItemUsage(scanner, p, enemies, display);
                    turnComplete = true;
                    break;
                default:
                    System.out.println(">> INVALID CHOICE. Please enter 1, 2, 3, or 4.");
            }
        }
        
        p.updateCooldown();
    }
    
    private static void handleBasicAttack(Scanner scanner, Player p, List<Enemy> enemies, BattleDisplay display) {
        List<Enemy> aliveEnemies = new ArrayList<>();
        for (Enemy e : enemies) {
            if (!e.getHealth().isDead()) {
                aliveEnemies.add(e);
            }
        }
        
        if (aliveEnemies.isEmpty()) {
            System.out.println(">> No enemies to attack!");
            return;
        }
        
        System.out.print("Select target (1-" + aliveEnemies.size() + "): ");
        int targetInput = scanner.nextInt();
        int actualIdx = targetInput - 1;
        
        if (actualIdx >= 0 && actualIdx < aliveEnemies.size()) {
            Enemy target = aliveEnemies.get(actualIdx);
            int hpBefore = target.getHealth().getValue();
            new BasicAttack().execute(p, List.of(target));
            int damage = hpBefore - target.getHealth().getValue();
            display.showBasicAttack(p, target, damage);
        } else {
            System.out.println(">> INVALID TARGET. Try again.");
        }
    }
    
    private static void handleSpecialSkill(Scanner scanner, Player p, List<Enemy> enemies, BattleDisplay display) {
        if (p.getSpecialCooldown() != 0) {
            System.out.println(">> ERROR: " + p.getSpecialSkillName() + " is on cooldown! (" + p.getSpecialCooldown() + " turns left)");
            return;
        }
        
        List<Enemy> aliveEnemies = new ArrayList<>();
        for (Enemy e : enemies) {
            if (!e.getHealth().isDead()) {
                aliveEnemies.add(e);
            }
        }
        
        if (aliveEnemies.isEmpty()) {
            System.out.println(">> No enemies to target!");
            return;
        }
        
        if (p instanceof Warrior) {
            System.out.print("Select target for Shield Bash (1-" + aliveEnemies.size() + "): ");
            int targetInput = scanner.nextInt();
            int actualIdx = targetInput - 1;
            if (actualIdx >= 0 && actualIdx < aliveEnemies.size()) {
                Enemy target = aliveEnemies.get(actualIdx);
                int hpBefore = target.getHealth().getValue();
                p.useSpecial(List.of(target));
                int damage = hpBefore - target.getHealth().getValue();
                display.showSpecialAttack(p, target, damage, "Shield Bash");
                display.showStun(target, 2);
            } else {
                System.out.println(">> INVALID TARGET.");
            }
        } else if (p instanceof Wizard) {
            p.useSpecial(new ArrayList<>(aliveEnemies));
        }
    }
    
    private static void handleItemUsage(Scanner scanner, Player p, List<Enemy> enemies, BattleDisplay display) {
        if (p.getBag().isEmpty()) {
            System.out.println(">> Your bag is empty! No items to use.");
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
        
        Item selectedItem = p.getBag().getItem(itemChoice - 1);
        
        if (selectedItem instanceof Potion) {
            int oldHp = p.getHealth().getValue();
            selectedItem.use(p, List.of(p));
            int healAmount = p.getHealth().getValue() - oldHp;
            display.showPotionHeal(p, healAmount, p.getHealth().getValue(), p.getHealth().getMaxHealth());
            p.getBag().loseItem(selectedItem.getName());
            
        } else if (selectedItem instanceof PowerStone) {
            List<Enemy> aliveEnemies = new ArrayList<>();
            for (Enemy e : enemies) {
                if (!e.getHealth().isDead()) {
                    aliveEnemies.add(e);
                }
            }
            
            if (aliveEnemies.isEmpty()) {
                System.out.println(">> No enemies to target!");
                return;
            }
            
            display.showPowerStoneUse(p, p.getSpecialSkillName());
            
            if (p instanceof Warrior) {
                System.out.print("Select target for Shield Bash (1-" + aliveEnemies.size() + "): ");
                int targetInput = scanner.nextInt();
                int actualIdx = targetInput - 1;
                if (actualIdx >= 0 && actualIdx < aliveEnemies.size()) {
                    Enemy target = aliveEnemies.get(actualIdx);
                    int hpBefore = target.getHealth().getValue();
                    p.usePowerStoneSpecial(List.of(target));
                    int damage = hpBefore - target.getHealth().getValue();
                    System.out.printf("→ %s: HP: %d → %d (dmg: %d) | %s STUNNED (2 turns)%n",
                            target.getName(), hpBefore, target.getHealth().getValue(), damage, target.getName());
                    p.getBag().loseItem(selectedItem.getName());
                }
            } else if (p instanceof Wizard) {
                p.usePowerStoneSpecial(new ArrayList<>(aliveEnemies));
                p.getBag().loseItem(selectedItem.getName());
            }
            
        } else if (selectedItem instanceof SmokeBomb) {
            List<Enemy> aliveEnemies = new ArrayList<>();
            for (Enemy e : enemies) {
                if (!e.getHealth().isDead()) {
                    aliveEnemies.add(e);
                }
            }
            
            if (aliveEnemies.isEmpty()) {
                System.out.println(">> No enemies to target!");
                return;
            }
            display.showSmokeBombUse(p);
            selectedItem.use(p, new ArrayList<>(aliveEnemies));
            p.getBag().loseItem(selectedItem.getName());
        }
    }
    
    private static void handleEnemyTurn(Enemy e, Player player, BattleDisplay display) {
        if (e.isStunned()) {
            display.showEnemyStunned(e);
            return;
        }
        
        int hpBefore = player.getHealth().getValue();
        new BasicAttack().execute(e, List.of(player));
        int damage = hpBefore - player.getHealth().getValue();
        display.showBasicAttack(e, player, damage);
    }
    
    private static int endRound(Player player, List<Enemy> enemies, int roundCounter, 
                                 LevelFactory.LevelConfig levelConfig, boolean backupSpawned, BattleDisplay display) {
        List<Enemy> eliminated = new ArrayList<>();
        for (Enemy e : enemies) {
            if (e.getHealth().isDead()) {
                eliminated.add(e);
            }
        }
        enemies.removeIf(e -> e.getHealth().isDead());
        
        for (Enemy e : eliminated) {
            display.showEnemyEliminated(e);
        }
        
        player.updateEffects();
        for (Enemy e : enemies) {
            e.updateEffects();
        }
        
        if (!backupSpawned && levelConfig.hasBackup() && enemies.isEmpty()) {
            enemies.addAll(levelConfig.getBackupEnemies());
            display.showBackupSpawn(enemies);
            return roundCounter + 1;
        }
        
        return roundCounter + 1;
    }
    
    private static boolean isGameOver(Player player, List<Enemy> enemies, boolean backupSpawned, LevelFactory.LevelConfig levelConfig) {
        if (player.getHealth().isDead()) {
            return true;
        }
        
        if (enemies.isEmpty()) {
            if (!backupSpawned && levelConfig.hasBackup()) {
                return false;
            }
            return true;
        }
        
        return false;
    }
}