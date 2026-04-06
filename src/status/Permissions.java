package status;

public class Permissions {
    private boolean canAct;
    private boolean canAttack;
    private boolean canDefend;
    private boolean canUseItem;
    
    public Permissions() {
        this.canAct = true;
        this.canAttack = true;
        this.canDefend = true;
        this.canUseItem = true;
    }
    
    public boolean getCanAct() { return canAct; }
    public void setCanAct(boolean canAct) { this.canAct = canAct; }
    
    public boolean getCanAttack() { return canAttack; }
    public void setCanAttack(boolean canAttack) { this.canAttack = canAttack; }
    
    public boolean getCanDefend() { return canDefend; }
    public void setCanDefend(boolean canDefend) { this.canDefend = canDefend; }
    
    public boolean getCanUseItem() { return canUseItem; }
    public void setCanUseItem(boolean canUseItem) { this.canUseItem = canUseItem; }
    
    public void disableAll() {
        canAct = false;
        canAttack = false;
        canDefend = false;
        canUseItem = false;
    }
    
    public void enableAll() {
        canAct = true;
        canAttack = true;
        canDefend = true;
        canUseItem = true;
    }
}