package network.minespazio.spaziostaff.listeners;

import network.minespazio.spaziostaff.SpazioStaff;
import network.minespazio.spaziostaff.gui.InspectGUI;
import network.minespazio.spaziostaff.gui.OnlineListGUI;
import network.minespazio.spaziostaff.gui.PlayerStatsGUI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StaffModeListener implements Listener {

    private final SpazioStaff plugin;
    private final Random random = new Random();

    public StaffModeListener(SpazioStaff plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != org.bukkit.inventory.EquipmentSlot.HAND) return;

        Player player = event.getPlayer();
        if (!plugin.getStaffModeManager().isInStaffMode(player)) return;

        if (event.getItem() == null || event.getItem().getType() == Material.AIR) return;

        Material itemType = event.getItem().getType();
        Action action = event.getAction();

        // Vanish Item (Dye / Eye)
        if (itemType == Material.LIME_DYE || itemType == Material.GRAY_DYE) {
            event.setCancelled(true);
            boolean newVanishState = plugin.getVanishManager().toggleVanish(player);
            plugin.getStaffModeManager().giveStaffItems(player);
            return;
        }

        // Phase Compass
        if (itemType == Material.COMPASS && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)) {
            event.setCancelled(true);
            handlePhaseCompass(player);
            return;
        }

        // Online Staff & Players List Item
        if ((itemType == Material.PLAYER_HEAD || itemType == Material.CLOCK) && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
            event.setCancelled(true);
            OnlineListGUI.open(plugin, player);
            return;
        }

        // Random Teleport Item (Feather)
        if (itemType == Material.FEATHER && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
            event.setCancelled(true);
            handleRandomTp(player);
            return;
        }

        // Prevent block interaction in staff mode
        if (action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getHand() != org.bukkit.inventory.EquipmentSlot.HAND) return;

        Player staff = event.getPlayer();
        if (!plugin.getStaffModeManager().isInStaffMode(staff)) return;

        Entity clicked = event.getRightClicked();
        if (!(clicked instanceof Player)) return;

        Player target = (Player) clicked;
        if (staff.getInventory().getItemInMainHand() == null) return;

        Material itemType = staff.getInventory().getItemInMainHand().getType();
        event.setCancelled(true);

        // Freeze Item
        if (itemType == Material.PACKED_ICE || itemType == Material.ICE) {
            plugin.getFreezeManager().toggleFreeze(target, staff);
            return;
        }

        // Inspect Item
        if (itemType == Material.BOOK || itemType == Material.CHEST) {
            InspectGUI.open(plugin, staff, target);
            return;
        }

        // Stats Item
        if (itemType == Material.NETHER_STAR || itemType == Material.PAPER) {
            PlayerStatsGUI.open(plugin, staff, target);
            return;
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        Player staff = (Player) event.getDamager();

        if (!plugin.getStaffModeManager().isInStaffMode(staff)) return;
        event.setCancelled(true);

        if (!(event.getEntity() instanceof Player)) return;
        Player target = (Player) event.getEntity();

        ItemStack mainHand = staff.getInventory().getItemInMainHand();
        if (mainHand == null || mainHand.getType() == Material.AIR) return;

        Material itemType = mainHand.getType();

        // Freeze Item (Left click target)
        if (itemType == Material.PACKED_ICE || itemType == Material.ICE) {
            plugin.getFreezeManager().toggleFreeze(target, staff);
            return;
        }

        // Inspect Item
        if (itemType == Material.BOOK || itemType == Material.CHEST) {
            InspectGUI.open(plugin, staff, target);
            return;
        }

        // Stats Item
        if (itemType == Material.NETHER_STAR || itemType == Material.PAPER) {
            PlayerStatsGUI.open(plugin, staff, target);
            return;
        }
    }

    private void handlePhaseCompass(Player player) {
        Location loc = player.getLocation();
        Block targetBlock = player.getTargetBlockExact(15);
        if (targetBlock == null || targetBlock.getType() == Material.AIR) {
            // Teleport 5 blocks forward
            Location tpLoc = loc.add(loc.getDirection().multiply(5));
            tpLoc.setY(player.getWorld().getHighestBlockYAt(tpLoc) + 1);
            player.teleport(tpLoc);
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
            return;
        }

        // Phase through wall block
        Location checkLoc = targetBlock.getLocation().clone();
        for (int i = 1; i <= 6; i++) {
            checkLoc.add(loc.getDirection().normalize());
            Block b1 = checkLoc.getBlock();
            Block b2 = checkLoc.clone().add(0, 1, 0).getBlock();
            if (b1.getType().isAir() && b2.getType().isAir()) {
                checkLoc.setYaw(loc.getYaw());
                checkLoc.setPitch(loc.getPitch());
                player.teleport(checkLoc);
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                return;
            }
        }

        // Fallback: Teleport to top of block
        Location topLoc = targetBlock.getLocation().add(0.5, 1, 0.5);
        topLoc.setYaw(loc.getYaw());
        topLoc.setPitch(loc.getPitch());
        player.teleport(topLoc);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
    }

    private void handleRandomTp(Player staff) {
        List<Player> targets = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.equals(staff) && !plugin.getStaffModeManager().isInStaffMode(p)) {
                targets.add(p);
            }
        }

        if (targets.isEmpty()) {
            staff.sendMessage(plugin.getConfigManager().color("&cNo hay otros jugadores en línea a los cuales teletransportarse."));
            return;
        }

        Player randomPlayer = targets.get(random.nextInt(targets.size()));
        staff.teleport(randomPlayer.getLocation());
        staff.sendMessage(plugin.getConfigManager().color("&aTe has teletransportado a &e" + randomPlayer.getName()));
        staff.playSound(staff.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (plugin.getStaffModeManager().isInStaffMode(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        if (plugin.getStaffModeManager().isInStaffMode(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player p = (Player) event.getWhoClicked();
            if (plugin.getStaffModeManager().isInStaffMode(p) && event.getClickedInventory() == p.getInventory()) {
                event.setCancelled(true);
            }
        }
    }
}
