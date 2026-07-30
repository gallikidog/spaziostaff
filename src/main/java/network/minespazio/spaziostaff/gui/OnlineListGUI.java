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

public class OnlineListGUI {

    public static void open(SpazioStaff plugin, Player staff) {
        String titleRaw = plugin.getConfig().getString("gui.online-list-title", "&8Jugadores & Staff Online");
        String title = plugin.getConfigManager().color(titleRaw);

        Inventory inv = Bukkit.createInventory(null, 54, title);

        int slot = 0;
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (slot >= 54) break;

            boolean isStaff = target.hasPermission("spaziostaff.staffmode") || target.hasPermission("spaziostaff.vanish") || target.isOp();
            boolean isVanished = plugin.getVanishManager().isVanished(target);

            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            if (meta != null) {
                meta.setOwningPlayer(target);
                meta.setDisplayName(plugin.getConfigManager().color((isStaff ? "&c[Staff] &f" : "&a[Jugador] &f") + target.getName()));

                List<String> lore = new ArrayList<>();
                lore.add(plugin.getConfigManager().color("&7Rango: &e" + (isStaff ? "Staff Member" : "Usuario")));
                lore.add(plugin.getConfigManager().color("&7Estado Vanish: " + (isVanished ? "&aEn Vanish" : "&cVisible")));
                lore.add(plugin.getConfigManager().color("&7Ping: &e" + target.getPing() + "ms"));
                lore.add(plugin.getConfigManager().color("&7Modo de juego: &f" + target.getGameMode().name()));
                lore.add("");
                lore.add(plugin.getConfigManager().color("&e► Click para ver Stats y Teleport."));
                lore.add(plugin.getConfigManager().color("&8UUID: " + target.getUniqueId()));

                meta.setLore(lore);
                skull.setItemMeta(meta);
            }

            inv.setItem(slot, skull);
            slot++;
        }

        staff.openInventory(inv);
    }
}
