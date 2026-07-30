package network.minespazio.spaziostaff.commands;

import network.minespazio.spaziostaff.SpazioStaff;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FreezeCommand implements CommandExecutor, TabCompleter {

    private final SpazioStaff plugin;

    public FreezeCommand(SpazioStaff plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("spaziostaff.freeze") && !sender.isOp()) {
            sender.sendMessage(plugin.getConfigManager().getMsg("messages.no-permission", "&cNo tienes permiso para usar este comando."));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(plugin.getConfigManager().color("&cUso correcto: /" + label + " <jugador>"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            sender.sendMessage(plugin.getConfigManager().getMsg("messages.player-not-found", "&cJugador no encontrado o desconectado."));
            return true;
        }

        Player staff = (sender instanceof Player) ? (Player) sender : null;
        boolean isUnfreeze = label.equalsIgnoreCase("unfreeze") || label.equalsIgnoreCase("unss") || label.equalsIgnoreCase("desfreezear");

        if (isUnfreeze) {
            plugin.getFreezeManager().setFrozen(target, staff, false);
        } else {
            plugin.getFreezeManager().toggleFreeze(target, staff);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            String input = args[0].toLowerCase();
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(input)) {
                    completions.add(p.getName());
                }
            }
        }
        return completions;
    }
}
