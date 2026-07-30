package network.minespazio.spaziostaff.commands;

import network.minespazio.spaziostaff.SpazioStaff;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandSpyCommand implements CommandExecutor, TabCompleter {

    private final SpazioStaff plugin;

    public CommandSpyCommand(SpazioStaff plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getConfigManager().getMsg("messages.only-players", "&cEste comando solo puede ser ejecutado por jugadores."));
            return true;
        }

        Player player = (Player) sender;

        if (!player.isOp() && !player.hasPermission("spaziostaff.cmdspy")) {
            player.sendMessage(plugin.getConfigManager().getMsg("messages.no-permission", "&cNo tienes permiso para usar este comando."));
            return true;
        }

        if (args.length == 0) {
            boolean currentState = plugin.toggleSpy(player);
            sendStateMessage(player, currentState);
            return true;
        }

        String subCommand = args[0].toLowerCase();
        if (subCommand.equals("on")) {
            plugin.setSpyEnabled(player, true);
            sendStateMessage(player, true);
        } else if (subCommand.equals("off")) {
            plugin.setSpyEnabled(player, false);
            sendStateMessage(player, false);
        } else {
            player.sendMessage(plugin.getConfigManager().color("&cUso correcto: /cmdspy [on|off]"));
        }

        return true;
    }

    private void sendStateMessage(Player player, boolean enabled) {
        String messageKey = enabled ? "cmdspy.enabled" : "cmdspy.disabled";
        String defaultMsg = enabled ? "&aCommand Spy activado correctamente." : "&cCommand Spy desactivado correctamente.";
        String message = plugin.getConfigManager().getMsg(messageKey, defaultMsg);
        player.sendMessage(message);
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
