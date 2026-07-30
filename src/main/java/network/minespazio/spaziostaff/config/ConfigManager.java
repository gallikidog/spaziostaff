package network.minespazio.spaziostaff.config;

import network.minespazio.spaziostaff.SpazioStaff;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    private final SpazioStaff plugin;

    public ConfigManager(SpazioStaff plugin) {
        this.plugin = plugin;
    }

    public String color(String text) {
        if (text == null) return "";
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public String getPrefix() {
        return color(plugin.getConfig().getString("prefix", "&7[&bSpazioStaff&7] "));
    }

    public String getMsg(String path, String def) {
        String msg = plugin.getConfig().getString(path, def);
        return color(msg.replace("{prefix}", getPrefix()));
    }

    public List<String> getMsgList(String path) {
        List<String> raw = plugin.getConfig().getStringList(path);
        List<String> colored = new ArrayList<>();
        for (String line : raw) {
            colored.add(color(line.replace("{prefix}", getPrefix())));
        }
        return colored;
    }
}
