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

public class InspectGUI {

    public static final String HOLDER_ID = "SpazioStaff_Inspect";

    public static void open(SpazioStaff plugin, Player staff, Player target) {
        String titleRaw = plugin.getConfig().getString("gui.inspect-title", "&8Inspeccionando: &1{player}");
        String title = plugin.getConfigManager().color(titleRaw.replace("{player}", target.getName()));

        Inventory inv = Bukkit.createInventory(null, 54, title);

        // Fill target inventory contents (0 to 35)
        ItemStack[] targetContents = target.getInventory().getContents();
        for (int i = 0; i < Math.min(36, targetContents.length); i++) {
            if (targetContents[i] != null) {
                inv.setItem(i, targetContents[i].clone());
            }
        }

        // Fill divider glass row (36 to 44)
        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        if (glassMeta != null) {
            glassMeta.setDisplayName(" ");
            glass.setItemMeta(glassMeta);
        }
        for (int i = 36; i < 45; i++) {
            inv.setItem(i, glass);
        }

        // Armor section (45: Helmet, 46: Chestplate, 47: Leggings, 48: Boots, 49: Offhand)
        ItemStack[] armor = target.getInventory().getArmorContents();
        if (armor.length > 3 && armor[3] != null) inv.setItem(45, armor[3].clone()); // Helmet
        if (armor.length > 2 && armor[2] != null) inv.setItem(46, armor[2].clone()); // Chestplate
        if (armor.length > 1 && armor[1] != null) inv.setItem(47, armor[1].clone()); // Leggings
        if (armor.length > 0 && armor[0] != null) inv.setItem(48, armor[0].clone()); // Boots

        ItemStack offhand = target.getInventory().getItemInOffHand();
        if (offhand != null && offhand.getType() != Material.AIR) {
            inv.setItem(49, offhand.clone());
        }

        // Head summary item (Slot 53)
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if (skullMeta != null) {
            skullMeta.setOwningPlayer(target);
            skullMeta.setDisplayName(plugin.getConfigManager().color("&b&lInformación de " + target.getName()));
            List<String> lore = new ArrayList<>();
            lore.add(plugin.getConfigManager().color("&7Vida: &c" + String.format("%.1f", target.getHealth()) + "/" + target.getMaxHealth()));
            lore.add(plugin.getConfigManager().color("&7Comida: &e" + target.getFoodLevel()));
            lore.add(plugin.getConfigManager().color("&7Modo de juego: &a" + target.getGameMode().name()));
            lore.add(plugin.getConfigManager().color("&7Ping: &e" + target.getPing() + "ms"));
            lore.add(plugin.getConfigManager().color("&7Mundo: &f" + target.getWorld().getName()));
            lore.add(plugin.getConfigManager().color("&7Coords: &f" + target.getLocation().getBlockX() + ", " + target.getLocation().getBlockY() + ", " + target.getLocation().getBlockZ()));
            skullMeta.setLore(lore);
            skull.setItemMeta(skullMeta);
        }
        inv.setItem(53, skull);

        staff.openInventory(inv);
    }
}
