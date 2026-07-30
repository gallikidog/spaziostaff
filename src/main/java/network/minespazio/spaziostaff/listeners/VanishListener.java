package network.minespazio.spaziostaff.listeners;

import network.minespazio.spaziostaff.SpazioStaff;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class VanishListener implements Listener {

    private final SpazioStaff plugin;

    public VanishListener(SpazioStaff plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.getVanishManager().onPlayerJoin(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (plugin.getVanishManager().isVanished(event.getPlayer())) {
            plugin.getVanishManager().getVanishedPlayers().remove(event.getPlayer().getUniqueId());
        }
    }
}
