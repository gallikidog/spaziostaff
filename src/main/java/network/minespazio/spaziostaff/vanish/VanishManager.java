package network.minespazio.spaziostaff.vanish;

import network.minespazio.spaziostaff.SpazioStaff;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VanishManager {

    private final SpazioStaff plugin;
    private final Set<UUID> vanishedPlayers = new HashSet<>();

    public VanishManager(SpazioStaff plugin) {
        this.plugin = plugin;
    }

    public boolean isVanished(Player player) {
        return vanishedPlayers.contains(player.getUniqueId());
    }

    public void setVanished(Player player, boolean vanish) {
        if (vanish) {
            vanishedPlayers.add(player.getUniqueId());
            updateVanishState(player, true);
            player.sendMessage(plugin.getConfigManager().getMsg("vanish.enabled", "&aAhora estas en Vanish."));
        } else {
            vanishedPlayers.remove(player.getUniqueId());
            updateVanishState(player, false);
            player.sendMessage(plugin.getConfigManager().getMsg("vanish.disabled", "&cYa no estas en Vanish."));
        }
    }

    public boolean toggleVanish(Player player) {
        boolean newState = !isVanished(player);
        setVanished(player, newState);
        return newState;
    }

    public void updateVanishState(Player target, boolean isVanished) {
        boolean staffSeeVanished = plugin.getConfig().getBoolean("vanish.staff-see-vanished", true);

        for (Player other : Bukkit.getOnlinePlayers()) {
            if (other.equals(target)) continue;

            if (isVanished) {
                boolean isOtherStaff = other.hasPermission("spaziostaff.vanish") || other.hasPermission("spaziostaff.staffmode") || other.isOp();
                if (staffSeeVanished && isOtherStaff) {
                    other.showPlayer(plugin, target);
                } else {
                    other.hidePlayer(plugin, target);
                }
            } else {
                other.showPlayer(plugin, target);
            }
        }
    }

    public void onPlayerJoin(Player joiner) {
        // If joiner is vanished, update for all
        if (isVanished(joiner)) {
            updateVanishState(joiner, true);
        }

        // Hide all currently vanished players from joiner if joiner is not staff
        boolean isJoinerStaff = joiner.hasPermission("spaziostaff.vanish") || joiner.hasPermission("spaziostaff.staffmode") || joiner.isOp();
        boolean staffSeeVanished = plugin.getConfig().getBoolean("vanish.staff-see-vanished", true);

        for (UUID uuid : vanishedPlayers) {
            Player vPlayer = Bukkit.getPlayer(uuid);
            if (vPlayer != null && vPlayer.isOnline()) {
                if (staffSeeVanished && isJoinerStaff) {
                    joiner.showPlayer(plugin, vPlayer);
                } else {
                    joiner.hidePlayer(plugin, vPlayer);
                }
            }
        }
    }

    public Set<UUID> getVanishedPlayers() {
        return vanishedPlayers;
    }
}
