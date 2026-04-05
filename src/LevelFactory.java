import java.util.ArrayList;
import java.util.List;

public class LevelFactory {
    
    // 内部类存储关卡配置
    public static class LevelConfig {
        private final List<Enemy> initialEnemies;
        private final List<Enemy> backupEnemies;
        private final String name;
        
        public LevelConfig(String name, List<Enemy> initial, List<Enemy> backup) {
            this.name = name;
            this.initialEnemies = initial;
            this.backupEnemies = backup;
        }
        
        public List<Enemy> getInitialEnemies() { return new ArrayList<>(initialEnemies); }
        public List<Enemy> getBackupEnemies() { return new ArrayList<>(backupEnemies); }
        public String getName() { return name; }
        public boolean hasBackup() { return backupEnemies != null && !backupEnemies.isEmpty(); }
    }
    
    public static LevelConfig createLevel(int choice) {
        switch (choice) {
            case 1: // Easy - 3 Goblins, no backup
                System.out.println(">> Level: Easy (3 Goblins)");
                List<Enemy> easyInitial = new ArrayList<>();
                easyInitial.add(new Goblin("Goblin A"));
                easyInitial.add(new Goblin("Goblin B"));
                easyInitial.add(new Goblin("Goblin C"));
                return new LevelConfig("Easy", easyInitial, null);
                
            case 2: // Medium - 1 Goblin + 1 Wolf, backup: 2 Wolves
                System.out.println(">> Level: Medium (1 Goblin, 1 Wolf)");
                System.out.println(">> Backup: 2 Wolves");
                List<Enemy> mediumInitial = new ArrayList<>();
                mediumInitial.add(new Goblin("Goblin"));
                mediumInitial.add(new Wolf("Wolf"));
                
                List<Enemy> mediumBackup = new ArrayList<>();
                mediumBackup.add(new Wolf("Wolf A"));
                mediumBackup.add(new Wolf("Wolf B"));
                return new LevelConfig("Medium", mediumInitial, mediumBackup);
                
            case 3: // Hard - 2 Goblins, backup: 1 Goblin + 2 Wolves
                System.out.println(">> Level: Hard (2 Goblins)");
                System.out.println(">> Backup: 1 Goblin, 2 Wolves");
                List<Enemy> hardInitial = new ArrayList<>();
                hardInitial.add(new Goblin("Goblin A"));
                hardInitial.add(new Goblin("Goblin B"));
                
                List<Enemy> hardBackup = new ArrayList<>();
                hardBackup.add(new Goblin("Goblin C"));
                hardBackup.add(new Wolf("Wolf A"));
                hardBackup.add(new Wolf("Wolf B"));
                return new LevelConfig("Hard", hardInitial, hardBackup);
                
            default:
                return null;
        }
    }
}