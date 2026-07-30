package network.minespazio.spaziostaff.staffmode;

import network.minespazio.spaziostaff.SpazioStaff;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class StaffItems {

    private final SpazioStaff plugin;

    public StaffItems(SpazioStaff plugin) {
        this.plugin = plugin;
    }

    public ItemStack createItem(String configPath) {
        String matName = plugin.getConfig().getString(configPath + ".material", "PAPER");
        Material mat = Material.matchMaterial(matName);
        if (mat == null) mat = Material.PAPER;

        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            String name = plugin.getConfig().getString(configPath + ".name", "");
            meta.setDisplayName(plugin.getConfigManager().color(name));

            List<String> rawLore = plugin.getConfig().getStringList(configPath + ".lore");
            List<String> lore = new ArrayList<>();
            for (String l : rawLore) {
                lore.add(plugin.getConfigManager().color(l));
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    public ItemStack getVanishItem(boolean isVanished) {
        String path = isVanished ? "staffmode.items.vanish-on" : "staffmode.items.vanish-off";
        return createItem(path);
    }

    public ItemStack getFreezeItem() {
        return createItem("staffmode.items.freeze");
    }

    public ItemStack getPhaseItem() {
        return createItem("staffmode.items.phase");
    }

    public ItemStack getInspectItem() {
        return createItem("staffmode.items.inspect");
    }

    public ItemStack getStatsItem() {
        return createItem("staffmode.items.stats");
    }

    public ItemStack getOnlineListItem() {
        ItemStack item = createItem("staffmode.items.online-list");
        if (item.getType() == Material.PLAYER_HEAD) {
            ItemMeta meta = item.getItemMeta();
            if (meta instanceof SkullMeta) {
                SkullMeta skullMeta = (SkullMeta) meta;
                skullMeta.setOwner("MNS"); // Default head texture reference
                item.setItemMeta(skullMeta);
            }
        }
        return item;
    }

    public ItemStack getRandomTpItem() {
        return createItem("staffmode.items.random-tp");
    }
}
