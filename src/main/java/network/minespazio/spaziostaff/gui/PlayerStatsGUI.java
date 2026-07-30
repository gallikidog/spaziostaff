package network.minespazio.spaziostaff.gui;

import network.minespazio.spaziostaff.SpazioStaff;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class PlayerStatsGUI {

    public static void open(SpazioStaff plugin, Player staff, Player target) {
        String titleRaw = plugin.getConfig().getString("gui.stats-title", "&8Stats de: &1{player}");
        String title = plugin.getConfigManager().color(titleRaw.replace("{player}", target.getName()));

        Inventory inv = Bukkit.createInventory(null, 27, title);

        // Fill background glass
        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        if (glassMeta != null) {
            glassMeta.setDisplayName(" ");
            glass.setItemMeta(glassMeta);
        }
        for (int i = 0; i < 27; i++) {
            inv.setItem(i, glass);
        }

        // Slot 4: Head with Stats
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if (skullMeta != null) {
            skullMeta.setOwningPlayer(target);
            skullMeta.setDisplayName(plugin.getConfigManager().color("&b&l" + target.getName()));

            List<String> lore = new ArrayList<>();
            lore.add(plugin.getConfigManager().color("&7Vida: &c" + String.format("%.1f", target.getHealth()) + "/" + target.getMaxHealth()));
            lore.add(plugin.getConfigManager().color("&7Comida: &e" + target.getFoodLevel()));
            lore.add(plugin.getConfigManager().color("&7Ping: &e" + target.getPing() + "ms"));
            lore.add(plugin.getConfigManager().color("&7Modo de Juego: &a" + target.getGameMode().name()));
            lore.add(plugin.getConfigManager().color("&7Mundo: &f" + target.getWorld().getName()));
            lore.add(plugin.getConfigManager().color("&7Coordenadas: &f" + target.getLocation().getBlockX() + ", " + target.getLocation().getBlockY() + ", " + target.getLocation().getBlockZ()));
            if (target.getAddress() != null) {
                lore.add(plugin.getConfigManager().color("&7IP: &7" + target.getAddress().getAddress().getHostAddress()));
            }
            lore.add(plugin.getConfigManager().color("&7Congelado: " + (plugin.getFreezeManager().isFrozen(target) ? "&aSÍ" : "&cNO")));
            lore.add(plugin.getConfigManager().color("&8TargetUUID: " + target.getUniqueId()));

            skullMeta.setLore(lore);
            skull.setItemMeta(skullMeta);
        }
        inv.setItem(4, skull);

        // Slot 11: Teleport option
        ItemStack tpItem = new ItemStack(Material.ENDER_PEARL);
        ItemMeta tpMeta = tpItem.getItemMeta();
        if (tpMeta != null) {
            tpMeta.setDisplayName(plugin.getConfigManager().color("&a&lTeleportarse hacia " + target.getName()));
            List<String> lore = new ArrayList<>();
            lore.add(plugin.getConfigManager().color("&7Click para teletransportarte inmediatamente."));
            lore.add(plugin.getConfigManager().color("&8TargetName: " + target.getName()));
            tpMeta.setLore(lore);
            tpItem.setItemMeta(tpMeta);
        }
        inv.setItem(11, tpItem);

        // Slot 13: Inspect option
        ItemStack inspectItem = new ItemStack(Material.BOOK);
        ItemMeta inspectMeta = inspectItem.getItemMeta();
        if (inspectMeta != null) {
            inspectMeta.setDisplayName(plugin.getConfigManager().color("&6&lInspeccionar Inventario"));
            List<String> lore = new ArrayList<>();
            lore.add(plugin.getConfigManager().color("&7Click para abrir el inventario y armadura."));
            lore.add(plugin.getConfigManager().color("&8TargetName: " + target.getName()));
            inspectMeta.setLore(lore);
            inspectItem.setItemMeta(inspectMeta);
        }
        inv.setItem(13, inspectItem);

        // Slot 15: Freeze/Unfreeze option
        boolean isFrozen = plugin.getFreezeManager().isFrozen(target);
        ItemStack freezeItem = new ItemStack(isFrozen ? Material.PACKED_ICE : Material.ICE);
        ItemMeta freezeMeta = freezeItem.getItemMeta();
        if (freezeMeta != null) {
            freezeMeta.setDisplayName(plugin.getConfigManager().color(isFrozen ? "&c&lDescongelar Jugador" : "&b&lCongelar Jugador"));
            List<String> lore = new ArrayList<>();
            lore.add(plugin.getConfigManager().color(isFrozen ? "&7Click para descongelar." : "&7Click para congelar."));
            lore.add(plugin.getConfigManager().color("&8TargetName: " + target.getName()));
            freezeMeta.setLore(lore);
            freezeItem.setItemMeta(freezeMeta);
        }
        inv.setItem(15, freezeItem);

        staff.openInventory(inv);
    }
}
