package network.minespazio.spaziostaff.freeze;

import network.minespazio.spaziostaff.SpazioStaff;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class FreezeManager {

    private final SpazioStaff plugin;
    private final Set<UUID> frozenPlayers = new HashSet<>();

    public FreezeManager(SpazioStaff plugin) {
        this.plugin = plugin;
    }

    public boolean isFrozen(Player player) {
        return frozenPlayers.contains(player.getUniqueId());
    }

    public void setFrozen(Player target, Player staff, boolean freeze) {
        if (freeze) {
            frozenPlayers.add(target.getUniqueId());
            sendFreezeMessage(target);
            target.sendTitle(plugin.getConfigManager().color("&c&lCONGELADO"), plugin.getConfigManager().color("&eEntra a Discord para revisión"), 10, 70, 20);

            if (staff != null) {
                String msg = plugin.getConfigManager().getMsg("freeze.frozen-staff-notify", "&aHas congelado a &e{player}&a.")
                        .replace("{player}", target.getName());
                staff.sendMessage(msg);
            }
        } else {
            frozenPlayers.remove(target.getUniqueId());
            String unfrozenMsg = plugin.getConfigManager().getMsg("freeze.unfrozen-target-msg", "&aHas sido descongelado por el staff.");
            target.sendMessage(unfrozenMsg);
            target.sendTitle(plugin.getConfigManager().color("&a&lDESCONGELADO"), "", 10, 40, 10);

            if (staff != null) {
                String msg = plugin.getConfigManager().getMsg("freeze.unfrozen-staff-notify", "&aHas descongelado a &e{player}&a.")
                        .replace("{player}", target.getName());
                staff.sendMessage(msg);
            }
        }
    }

    public boolean toggleFreeze(Player target, Player staff) {
        boolean newState = !isFrozen(target);
        setFrozen(target, staff, newState);
        return newState;
    }

    public void sendFreezeMessage(Player target) {
        List<String> messages = plugin.getConfigManager().getMsgList("freeze.frozen-target-msg");
        for (String msg : messages) {
            target.sendMessage(msg);
        }
    }

    public Set<UUID> getFrozenPlayers() {
        return frozenPlayers;
    }
}
