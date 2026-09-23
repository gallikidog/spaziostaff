package network.minespazio.spaziostaff.commands;

import network.minespazio.spaziostaff.SpazioStaff;
import network.minespazio.spaziostaff.staffchat.StaffChatManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class StaffChatCommand implements CommandExecutor, TabCompleter {

    private final SpazioStaff plugin;

    public StaffChatCommand(SpazioStaff plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("spaziostaff.staffchat") && !sender.hasPermission("spaziostaff.sc") && !sender.hasPermission("spaziostaff.admin")) {
            String noPermMsg = plugin.getConfig().getString("staffchat.no-permission", "&cNo tienes permisos para usar el StaffChat.");
            sender.sendMessage(plugin.color(noPermMsg));
            return true;
        }

        StaffChatManager manager = plugin.getStaffChatManager();

        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(plugin.color("&cUso: /" + label + " <mensaje>"));
                return true;
            }

            boolean enabled = manager.toggleStaffChat(player);
            if (enabled) {
                String msg = plugin.getConfig().getString("staffchat.toggle-enabled", "&aModo StaffChat &eACTIVADO&a. Todos tus mensajes irán al StaffChat.");
                player.sendMessage(plugin.color(msg));
            } else {
                String msg = plugin.getConfig().getString("staffchat.toggle-disabled", "&cModo StaffChat &eDESACTIVADO&c. Tus mensajes volverán al chat público.");
                player.sendMessage(plugin.color(msg));
            }
            return true;
        }

        String message = String.join(" ", args);
        manager.sendStaffChatMessage(sender, message);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>();
    }
}
