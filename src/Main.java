import combatant.Player;
import combatant.Warrior;
import combatant.Wizard;
import Difficulty.Difficulty;
import Difficulty.Easy;
import Difficulty.Medium;
import Difficulty.Hard;
import Entity.CombatEncounter;
import item.Potion;
import item.PowerStone;
import item.SmokeBomb;
import java.util.Scanner;

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
            
            Difficulty difficulty = selectDifficulty(scanner);
            if (difficulty == null) continue;
            
            System.out.println("\n========================================");
            System.out.println("              GAME START");
            System.out.println("========================================");
            
            CombatEncounter encounter = new CombatEncounter(player, difficulty, scanner);
            encounter.startBattle();
            
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
    
    private static Difficulty selectDifficulty(Scanner scanner) {
        while (true) {
            System.out.println("\nSelect Difficulty:");
            System.out.println("1. Easy (3 Goblins)");
            System.out.println("2. Medium (1 Goblin, 1 Wolf + Backup: 2 Wolves)");
            System.out.println("3. Hard (2 Goblins + Backup: 1 Goblin, 2 Wolves)");
            System.out.print("Choice » ");
            
            int diffChoice = scanner.nextInt();
            switch (diffChoice) {
                case 1: return new Easy();
                case 2: return new Medium();
                case 3: return new Hard();
                default: System.out.println(">> Invalid difficulty. Please select 1, 2, or 3.");
            }
        }
    }
}