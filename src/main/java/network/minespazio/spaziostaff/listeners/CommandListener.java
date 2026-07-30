package network.minespazio.spaziostaff.listeners;

import network.minespazio.spaziostaff.SpazioStaff;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.UUID;

public class CommandListener implements Listener {

    private final SpazioStaff plugin;

    public CommandListener(SpazioStaff plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player sender = event.getPlayer();
        String fullCommand = event.getMessage(); // Includes leading '/'

        String mainCommand = fullCommand.split(" ")[0].toLowerCase();

        List<String> ignoredCommands = plugin.getConfig().getStringList("ignored-commands");
        for (String ignored : ignoredCommands) {
            if (ignored.equalsIgnoreCase(mainCommand)) {
                return;
            }
        }

        String prefix = plugin.getConfig().getString("cmdspy.prefix", plugin.getConfigManager().getPrefix());
        String format = plugin.getConfig().getString("cmdspy.format", "{prefix}&f{player}&7: &e{command}");

        String formattedMessage = format
                .replace("{prefix}", prefix)
                .replace("{player}", sender.getName())
                .replace("{command}", fullCommand);

        String coloredMessage = plugin.getConfigManager().color(formattedMessage);

        for (UUID uuid : plugin.getSpyUsers()) {
            Player spyPlayer = Bukkit.getPlayer(uuid);
            if (spyPlayer != null && spyPlayer.isOnline()) {
                if (spyPlayer.isOp() || spyPlayer.hasPermission("spaziostaff.cmdspy")) {
                    spyPlayer.sendMessage(coloredMessage);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.setSpyEnabled(event.getPlayer(), false);
    }
}
