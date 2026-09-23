package network.minespazio.spaziostaff.staffchat;

import network.minespazio.spaziostaff.SpazioStaff;
import network.minespazio.spaziostaff.hooks.LuckPermsHook;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class StaffChatManager {

    private final SpazioStaff plugin;
    private final Set<UUID> toggledPlayers = new HashSet<>();

    public StaffChatManager(SpazioStaff plugin) {
        this.plugin = plugin;
    }

    public boolean toggleStaffChat(Player player) {
        if (isInStaffChat(player)) {
            toggledPlayers.remove(player.getUniqueId());
            return false;
        } else {
            toggledPlayers.add(player.getUniqueId());
            return true;
        }
    }

    public boolean isInStaffChat(Player player) {
        return toggledPlayers.contains(player.getUniqueId());
    }

    public void removePlayer(Player player) {
        toggledPlayers.remove(player.getUniqueId());
    }

    public void sendStaffChatMessage(CommandSender sender, String message) {
        if (message == null || message.isBlank()) return;

        FileConfiguration config = plugin.getConfig();
        String format = config.getString("staffchat.format", "&8[&c&lSTAFFCHAT&8] &7{prefix}&f{player}&7: &e{message}");

        String prefix = "";
        String senderName = sender.getName();

        if (sender instanceof Player player) {
            prefix = LuckPermsHook.getPrefix(player);
        } else {
            prefix = "&c[CONSOLE] ";
        }

        String formattedMessage = format
                .replace("{prefix}", prefix)
                .replace("{player}", senderName)
                .replace("{message}", message);

        String coloredMessage = plugin.color(formattedMessage);

        // Send message to Console
        Bukkit.getConsoleSender().sendMessage(coloredMessage);

        // Sound settings
        boolean soundEnabled = config.getBoolean("staffchat.sound.enabled", true);
        String soundName = config.getString("staffchat.sound.name", "ENTITY_EXPERIENCE_ORB_PICKUP");
        float volume = (float) config.getDouble("staffchat.sound.volume", 1.0);
        float pitch = (float) config.getDouble("staffchat.sound.pitch", 1.0);

        Sound sound = null;
        if (soundEnabled && soundName != null && !soundName.isBlank()) {
            try {
                sound = Sound.valueOf(soundName.toUpperCase());
            } catch (Exception ignored) {
            }
        }

        // Send message to all online Staff members
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.hasPermission("spaziostaff.staffchat") || online.hasPermission("spaziostaff.sc") || online.hasPermission("spaziostaff.admin")) {
                online.sendMessage(coloredMessage);
                if (sound != null) {
                    try {
                        online.playSound(online.getLocation(), sound, volume, pitch);
                    } catch (Exception ignored) {
                    }
                }
            }
        }
    }

    public Set<UUID> getToggledPlayers() {
        return toggledPlayers;
    }
}
