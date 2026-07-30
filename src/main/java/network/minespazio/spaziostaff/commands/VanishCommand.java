package network.minespazio.spaziostaff.commands;

import network.minespazio.spaziostaff.SpazioStaff;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VanishCommand implements CommandExecutor, TabCompleter {

    private final SpazioStaff plugin;

    public VanishCommand(SpazioStaff plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getConfigManager().getMsg("messages.only-players", "&cEste comando solo puede ser ejecutado por jugadores."));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("spaziostaff.vanish") && !player.isOp()) {
            player.sendMessage(plugin.getConfigManager().getMsg("messages.no-permission", "&cNo tienes permiso para usar este comando."));
            return true;
        }

        if (args.length == 0) {
            plugin.getVanishManager().toggleVanish(player);
            if (plugin.getStaffModeManager().isInStaffMode(player)) {
                plugin.getStaffModeManager().giveStaffItems(player);
            }
            return true;
        }

        String sub = args[0].toLowerCase();
        if (sub.equals("on")) {
            plugin.getVanishManager().setVanished(player, true);
        } else if (sub.equals("off")) {
            plugin.getVanishManager().setVanished(player, false);
        } else {
            player.sendMessage(plugin.getConfigManager().color("&cUso correcto: /vanish [on|off]"));
            return true;
        }

        if (plugin.getStaffModeManager().isInStaffMode(player)) {
            plugin.getStaffModeManager().giveStaffItems(player);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            String input = args[0].toLowerCase();
            if ("on".startsWith(input)) completions.add("on");
            if ("off".startsWith(input)) completions.add("off");
        }
        return completions;
    }
}
