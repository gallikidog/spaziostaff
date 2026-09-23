package network.minespazio.spaziostaff.listeners;

import network.minespazio.spaziostaff.SpazioStaff;
import network.minespazio.spaziostaff.staffchat.StaffChatManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Locale;

public class StaffChatListener implements Listener {

    private final SpazioStaff plugin;

    public StaffChatListener(SpazioStaff plugin) {
        this.plugin = plugin;
    }

    /**
     * Intercepts /sc, /staffchat, and /schat commands with HIGHEST priority
     * to override and interpose over any conflicting plugin (e.g. Essentials, DeluxeChat, etc.).
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String fullCmd = event.getMessage();
        if (fullCmd == null || fullCmd.isEmpty()) return;

        String lowerCmd = fullCmd.toLowerCase(Locale.ROOT).trim();

        boolean isSc = lowerCmd.equals("/sc") || lowerCmd.startsWith("/sc ");
        boolean isStaffChat = lowerCmd.equals("/staffchat") || lowerCmd.startsWith("/staffchat ");
        boolean isSChat = lowerCmd.equals("/schat") || lowerCmd.startsWith("/schat ");

        if (isSc || isStaffChat || isSChat) {
            Player player = event.getPlayer();

            if (!player.hasPermission("spaziostaff.staffchat") && !player.hasPermission("spaziostaff.sc") && !player.hasPermission("spaziostaff.admin")) {
                // Cancel conflicting plugin command for unpermitted player if desired, or let command fail gracefully
                event.setCancelled(true);
                String noPermMsg = plugin.getConfig().getString("staffchat.no-permission", "&cNo tienes permisos para usar el StaffChat.");
                player.sendMessage(plugin.color(noPermMsg));
                return;
            }

            // High Priority Override: Cancel event so no other plugin processes this command!
            event.setCancelled(true);

            StaffChatManager manager = plugin.getStaffChatManager();

            // Extract message argument
            String argsStr = "";
            if (isSc && fullCmd.length() > 3) {
                argsStr = fullCmd.substring(4).trim();
            } else if (isStaffChat && fullCmd.length() > 10) {
                argsStr = fullCmd.substring(11).trim();
            } else if (isSChat && fullCmd.length() > 6) {
                argsStr = fullCmd.substring(7).trim();
            }

            if (argsStr.isEmpty()) {
                // Toggle mode
                boolean enabled = manager.toggleStaffChat(player);
                if (enabled) {
                    String msg = plugin.getConfig().getString("staffchat.toggle-enabled", "&aModo StaffChat &eACTIVADO&a. Todos tus mensajes irán al StaffChat.");
                    player.sendMessage(plugin.color(msg));
                } else {
                    String msg = plugin.getConfig().getString("staffchat.toggle-disabled", "&cModo StaffChat &eDESACTIVADO&c. Tus mensajes volverán al chat público.");
                    player.sendMessage(plugin.color(msg));
                }
            } else {
                // Direct message
                manager.sendStaffChatMessage(player, argsStr);
            }
        }
    }

    /**
     * Redirects regular chat to StaffChat when StaffChat toggle mode is active.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        StaffChatManager manager = plugin.getStaffChatManager();

        if (manager.isInStaffChat(player)) {
            if (!player.hasPermission("spaziostaff.staffchat") && !player.hasPermission("spaziostaff.sc") && !player.hasPermission("spaziostaff.admin")) {
                manager.removePlayer(player);
                return;
            }

            event.setCancelled(true);
            String message = event.getMessage();
            manager.sendStaffChatMessage(player, message);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getStaffChatManager().removePlayer(event.getPlayer());
    }
}
