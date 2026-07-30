package network.minespazio.spaziostaff.staffmode;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StaffInventoryData {

    private final ItemStack[] contents;
    private final ItemStack[] armorContents;
    private final GameMode gameMode;
    private final boolean allowFlight;
    private final boolean flying;
    private final int level;
    private final float exp;

    public StaffInventoryData(Player player) {
        this.contents = player.getInventory().getContents();
        this.armorContents = player.getInventory().getArmorContents();
        this.gameMode = player.getGameMode();
        this.allowFlight = player.getAllowFlight();
        this.flying = player.isFlying();
        this.level = player.getLevel();
        this.exp = player.getExp();
    }

    public void restore(Player player) {
        player.getInventory().setContents(contents);
        player.getInventory().setArmorContents(armorContents);
        player.setGameMode(gameMode);
        player.setAllowFlight(allowFlight);
        player.setFlying(flying);
        player.setLevel(level);
        player.setExp(exp);
        player.updateInventory();
    }
}
