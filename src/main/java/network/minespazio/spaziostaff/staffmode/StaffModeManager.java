package network.minespazio.spaziostaff.staffmode;

import network.minespazio.spaziostaff.SpazioStaff;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StaffModeManager {

    private final SpazioStaff plugin;
    private final Map<UUID, StaffInventoryData> savedData = new HashMap<>();
    private final StaffItems staffItems;

    public StaffModeManager(SpazioStaff plugin) {
        this.plugin = plugin;
        this.staffItems = new StaffItems(plugin);
    }

    public boolean isInStaffMode(Player player) {
        return savedData.containsKey(player.getUniqueId());
    }

    public void setStaffMode(Player player, boolean enable) {
        if (enable) {
            if (isInStaffMode(player)) return;

            // Save player data
            savedData.put(player.getUniqueId(), new StaffInventoryData(player));

            // Clear inventory and set mode
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.setGameMode(GameMode.CREATIVE);
            player.setAllowFlight(true);
            player.setFlying(true);

            // Enable vanish
            plugin.getVanishManager().setVanished(player, true);

            // Give staff items
            giveStaffItems(player);

            // Enable staff scoreboard
            plugin.getScoreboardManager().enableStaffScoreboard(player);

            player.sendMessage(plugin.getConfigManager().getMsg("staffmode.enabled", "&aModo Staff ACTIVADO."));
        } else {
            if (!isInStaffMode(player)) return;

            // Disable vanish
            if (plugin.getVanishManager().isVanished(player)) {
                plugin.getVanishManager().setVanished(player, false);
            }

            // Disable staff scoreboard
            plugin.getScoreboardManager().disableStaffScoreboard(player);

            // Restore player data
            StaffInventoryData data = savedData.remove(player.getUniqueId());
            if (data != null) {
                data.restore(player);
            }

            player.sendMessage(plugin.getConfigManager().getMsg("staffmode.disabled", "&cModo Staff DESACTIVADO."));
        }
    }

    public boolean toggleStaffMode(Player player) {
        boolean newState = !isInStaffMode(player);
        setStaffMode(player, newState);
        return newState;
    }

    public void giveStaffItems(Player player) {
        Inventory inv = player.getInventory();
        inv.clear();

        boolean isVanished = plugin.getVanishManager().isVanished(player);

        int vanishSlot = plugin.getConfig().getInt("staffmode.items.vanish-on.slot", 0);
        int freezeSlot = plugin.getConfig().getInt("staffmode.items.freeze.slot", 1);
        int phaseSlot = plugin.getConfig().getInt("staffmode.items.phase.slot", 2);
        int inspectSlot = plugin.getConfig().getInt("staffmode.items.inspect.slot", 4);
        int statsSlot = plugin.getConfig().getInt("staffmode.items.stats.slot", 6);
        int onlineListSlot = plugin.getConfig().getInt("staffmode.items.online-list.slot", 7);
        int randomTpSlot = plugin.getConfig().getInt("staffmode.items.random-tp.slot", 8);

        inv.setItem(vanishSlot, staffItems.getVanishItem(isVanished));
        inv.setItem(freezeSlot, staffItems.getFreezeItem());
        inv.setItem(phaseSlot, staffItems.getPhaseItem());
        inv.setItem(inspectSlot, staffItems.getInspectItem());
        inv.setItem(statsSlot, staffItems.getStatsItem());
        inv.setItem(onlineListSlot, staffItems.getOnlineListItem());
        inv.setItem(randomTpSlot, staffItems.getRandomTpItem());

        player.updateInventory();
    }

    public StaffItems getStaffItems() {
        return staffItems;
    }

    public Map<UUID, StaffInventoryData> getSavedData() {
        return savedData;
    }
}
