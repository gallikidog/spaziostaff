package network.minespazio.spaziostaff.listeners;

import network.minespazio.spaziostaff.SpazioStaff;
import network.minespazio.spaziostaff.gui.InspectGUI;
import network.minespazio.spaziostaff.gui.PlayerStatsGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class GUIListener implements Listener {

    private final SpazioStaff plugin;

    public GUIListener(SpazioStaff plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player staff = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();

        String inspectTitleRaw = plugin.getConfig().getString("gui.inspect-title", "&8Inspeccionando:");
        String inspectTitlePrefix = plugin.getConfigManager().color(inspectTitleRaw.split("\\{")[0]);

        String onlineListTitle = plugin.getConfigManager().color(plugin.getConfig().getString("gui.online-list-title", "&8Jugadores & Staff Online"));

        String statsTitleRaw = plugin.getConfig().getString("gui.stats-title", "&8Stats de:");
        String statsTitlePrefix = plugin.getConfigManager().color(statsTitleRaw.split("\\{")[0]);

        // Inspect GUI
        if (title.startsWith(inspectTitlePrefix)) {
            event.setCancelled(true);
            return;
        }

        // Online List GUI
        if (title.equals(onlineListTitle)) {
            event.setCancelled(true);
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || clicked.getType() != Material.PLAYER_HEAD) return;

            ItemMeta meta = clicked.getItemMeta();
            if (meta instanceof SkullMeta) {
                SkullMeta skullMeta = (SkullMeta) meta;
                if (skullMeta.getOwningPlayer() != null && skullMeta.getOwningPlayer().getPlayer() != null) {
                    Player target = skullMeta.getOwningPlayer().getPlayer();
                    PlayerStatsGUI.open(plugin, staff, target);
                    staff.playSound(staff.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                } else {
                    // Fallback to name in title / lore
                    List<String> lore = skullMeta.getLore();
                    if (lore != null) {
                        for (String line : lore) {
                            if (line.startsWith("§8UUID: ")) {
                                String uuidStr = line.replace("§8UUID: ", "");
                                Player target = Bukkit.getPlayer(java.util.UUID.fromString(uuidStr));
                                if (target != null) {
                                    PlayerStatsGUI.open(plugin, staff, target);
                                    staff.playSound(staff.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                                }
                            }
                        }
                    }
                }
            }
            return;
        }

        // Stats GUI
        if (title.startsWith(statsTitlePrefix)) {
            event.setCancelled(true);
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || !clicked.hasItemMeta()) return;

            List<String> lore = clicked.getItemMeta().getLore();
            if (lore == null) return;

            String targetName = null;
            for (String line : lore) {
                if (line.startsWith("§8TargetName: ")) {
                    targetName = line.replace("§8TargetName: ", "");
                } else if (line.startsWith("§8TargetUUID: ")) {
                    String uuidStr = line.replace("§8TargetUUID: ", "");
                    Player p = Bukkit.getPlayer(java.util.UUID.fromString(uuidStr));
                    if (p != null) targetName = p.getName();
                }
            }

            if (targetName == null) return;
            Player target = Bukkit.getPlayer(targetName);
            if (target == null || !target.isOnline()) {
                staff.sendMessage(plugin.getConfigManager().getMsg("messages.player-not-found", "&cJugador no encontrado o desconectado."));
                staff.closeInventory();
                return;
            }

            int slot = event.getRawSlot();
            if (slot == 11) {
                // Teleport
                staff.teleport(target);
                staff.sendMessage(plugin.getConfigManager().color("&aTe has teletransportado a &e" + target.getName()));
                staff.playSound(staff.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                staff.closeInventory();
            } else if (slot == 13) {
                // Inspect
                InspectGUI.open(plugin, staff, target);
                staff.playSound(staff.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
            } else if (slot == 15) {
                // Freeze
                plugin.getFreezeManager().toggleFreeze(target, staff);
                PlayerStatsGUI.open(plugin, staff, target); // Refresh GUI
                staff.playSound(staff.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
            }
        }
    }
}
