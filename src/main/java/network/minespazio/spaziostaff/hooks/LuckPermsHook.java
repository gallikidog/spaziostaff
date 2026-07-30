package network.minespazio.spaziostaff.hooks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

public class LuckPermsHook {

    public static String getRank(Player player) {
        try {
            if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
                Class<?> providerClass = Class.forName("net.luckperms.api.LuckPermsProvider");
                Method getMethod = providerClass.getMethod("get");
                Object luckPerms = getMethod.invoke(null);

                Method getUserManager = luckPerms.getClass().getMethod("getUserManager");
                Object userManager = getUserManager.invoke(luckPerms);

                Method getUser = userManager.getClass().getMethod("getUser", java.util.UUID.class);
                Object user = getUser.invoke(userManager, player.getUniqueId());

                if (user != null) {
                    Method getCachedData = user.getClass().getMethod("getCachedData");
                    Object cachedData = getCachedData.invoke(user);

                    Method getMetaData = cachedData.getClass().getMethod("getMetaData");
                    Object metaData = getMetaData.invoke(cachedData);

                    Method getPrefix = metaData.getClass().getMethod("getPrefix");
                    Object prefix = getPrefix.invoke(metaData);

                    if (prefix instanceof String && !((String) prefix).trim().isEmpty()) {
                        return ((String) prefix).trim();
                    }

                    Method getPrimaryGroup = user.getClass().getMethod("getPrimaryGroup");
                    Object group = getPrimaryGroup.invoke(user);
                    if (group instanceof String && !((String) group).trim().isEmpty()) {
                        String g = (String) group;
                        return g.substring(0, 1).toUpperCase() + g.substring(1);
                    }
                }
            }
        } catch (Throwable ignored) {}

        if (player.isOp()) return "Admin";
        if (player.hasPermission("spaziostaff.staffmode")) return "Staff";
        return "Usuario";
    }

    public static String setPlaceholders(Player player, String text) {
        try {
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                Class<?> papiClass = Class.forName("me.clip.placeholderapi.PlaceholderAPI");
                Method setPlaceholders = papiClass.getMethod("setPlaceholders", Player.class, String.class);
                return (String) setPlaceholders.invoke(null, player, text);
            }
        } catch (Throwable ignored) {}
        return text;
    }
}
