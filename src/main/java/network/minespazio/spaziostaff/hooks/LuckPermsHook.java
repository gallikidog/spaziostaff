package network.minespazio.spaziostaff.hooks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.lang.reflect.Method;

public class LuckPermsHook {

    public static String getPrefix(Player player) {
        String prefix = null;

        // 1. Try LuckPerms API
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
                    Object res = getPrefix.invoke(metaData);

                    if (res instanceof String && !((String) res).trim().isEmpty()) {
                        prefix = (String) res;
                    }
                }
            }
        } catch (Throwable ignored) {}

        // 2. Try Vault Chat API fallback if LuckPerms prefix is null
        if (prefix == null && Bukkit.getPluginManager().getPlugin("Vault") != null) {
            try {
                RegisteredServiceProvider<?> rsp = Bukkit.getServicesManager().getRegistration(Class.forName("net.milkbowl.vault.chat.Chat"));
                if (rsp != null) {
                    Object chat = rsp.getProvider();
                    Method getPlayerPrefix = chat.getClass().getMethod("getPlayerPrefix", Player.class);
                    Object res = getPlayerPrefix.invoke(chat, player);
                    if (res instanceof String && !((String) res).trim().isEmpty()) {
                        prefix = (String) res;
                    }
                }
            } catch (Throwable ignored) {}
        }

        if (prefix == null) {
            prefix = player.isOp() ? "&c[Admin] " : "&7[Usuario] ";
        }

        return color(prefix.trim());
    }

    public static String getRank(Player player) {
        String groupName = null;

        // 1. Try LuckPerms API for Primary Group Name
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
                    Method getPrimaryGroup = user.getClass().getMethod("getPrimaryGroup");
                    Object groupObj = getPrimaryGroup.invoke(user);
                    if (groupObj instanceof String && !((String) groupObj).trim().isEmpty()) {
                        String rawGroup = (String) groupObj;

                        // Try to get group display name from LuckPerms GroupManager
                        try {
                            Method getGroupManager = luckPerms.getClass().getMethod("getGroupManager");
                            Object groupManager = getGroupManager.invoke(luckPerms);

                            Method getGroup = groupManager.getClass().getMethod("getGroup", String.class);
                            Object group = getGroup.invoke(groupManager, rawGroup);

                            if (group != null) {
                                Method getDisplayName = group.getClass().getMethod("getDisplayName");
                                Object displayName = getDisplayName.invoke(group);
                                if (displayName instanceof String && !((String) displayName).trim().isEmpty()) {
                                    groupName = (String) displayName;
                                }
                            }
                        } catch (Throwable ignored) {}

                        if (groupName == null) {
                            groupName = rawGroup.substring(0, 1).toUpperCase() + rawGroup.substring(1);
                        }
                    }
                }
            }
        } catch (Throwable ignored) {}

        // 2. Try Vault Permissions fallback
        if (groupName == null && Bukkit.getPluginManager().getPlugin("Vault") != null) {
            try {
                RegisteredServiceProvider<?> rsp = Bukkit.getServicesManager().getRegistration(Class.forName("net.milkbowl.vault.permission.Permission"));
                if (rsp != null) {
                    Object perm = rsp.getProvider();
                    Method getPrimaryGroup = perm.getClass().getMethod("getPrimaryGroup", Player.class);
                    Object res = getPrimaryGroup.invoke(perm, player);
                    if (res instanceof String && !((String) res).trim().isEmpty()) {
                        String raw = (String) res;
                        groupName = raw.substring(0, 1).toUpperCase() + raw.substring(1);
                    }
                }
            } catch (Throwable ignored) {}
        }

        if (groupName == null) {
            if (player.isOp()) groupName = "Admin";
            else if (player.hasPermission("spaziostaff.staffmode")) groupName = "Staff";
            else groupName = "Usuario";
        }

        return color(groupName);
    }

    public static String setPlaceholders(Player player, String text) {
        if (text == null) return "";
        try {
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                Class<?> papiClass = Class.forName("me.clip.placeholderapi.PlaceholderAPI");
                Method setPlaceholders = papiClass.getMethod("setPlaceholders", Player.class, String.class);
                text = (String) setPlaceholders.invoke(null, player, text);
            }
        } catch (Throwable ignored) {}
        return color(text);
    }

    public static String color(String text) {
        if (text == null) return "";
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
