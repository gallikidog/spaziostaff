package network.minespazio.spaziostaff.listeners;

import network.minespazio.spaziostaff.SpazioStaff;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class FreezeListener implements Listener {

    private final SpazioStaff plugin;

    public FreezeListener(SpazioStaff plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        if (plugin.getFreezeManager().isFrozen(p)) {
            // Allow head pitch/yaw rotation, block coordinate change
            if (event.getFrom().getX() != event.getTo().getX() ||
                event.getFrom().getY() != event.getTo().getY() ||
                event.getFrom().getZ() != event.getTo().getZ()) {
                event.setTo(event.getFrom());
                p.sendMessage(plugin.getConfigManager().getMsg("freeze.cannot-move", "&c&lPROHIBIDO MOVERTE: &cEstas congelado."));
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (plugin.getFreezeManager().isFrozen(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (plugin.getFreezeManager().isFrozen(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (plugin.getFreezeManager().isFrozen(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        if (plugin.getFreezeManager().isFrozen(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            if (plugin.getFreezeManager().isFrozen(p)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            if (plugin.getFreezeManager().isFrozen(attacker)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player p = (Player) event.getWhoClicked();
            if (plugin.getFreezeManager().isFrozen(p)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        if (plugin.getFreezeManager().isFrozen(p)) {
            // Notify staff members that frozen player logged out
            String alert = plugin.getConfigManager().color("&c&lALERT: &eEl jugador congelado &c" + p.getName() + " &ese desconectó durante la revisión.");
            for (Player staff : Bukkit.getOnlinePlayers()) {
                if (staff.hasPermission("spaziostaff.freeze") || staff.isOp()) {
                    staff.sendMessage(alert);
                }
            }
        }
    }
}
