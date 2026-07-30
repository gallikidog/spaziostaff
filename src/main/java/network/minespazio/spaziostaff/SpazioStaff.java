package network.minespazio.spaziostaff;

import network.minespazio.spaziostaff.commands.CommandSpyCommand;
import network.minespazio.spaziostaff.commands.FreezeCommand;
import network.minespazio.spaziostaff.commands.StaffModeCommand;
import network.minespazio.spaziostaff.commands.VanishCommand;
import network.minespazio.spaziostaff.config.ConfigManager;
import network.minespazio.spaziostaff.freeze.FreezeManager;
import network.minespazio.spaziostaff.listeners.CommandListener;
import network.minespazio.spaziostaff.listeners.FreezeListener;
import network.minespazio.spaziostaff.listeners.GUIListener;
import network.minespazio.spaziostaff.listeners.StaffModeListener;
import network.minespazio.spaziostaff.listeners.VanishListener;
import network.minespazio.spaziostaff.staffmode.StaffModeManager;
import network.minespazio.spaziostaff.vanish.VanishManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SpazioStaff extends JavaPlugin {

    private static SpazioStaff instance;
    private ConfigManager configManager;
    private VanishManager vanishManager;
    private FreezeManager freezeManager;
    private StaffModeManager staffModeManager;

    private final Set<UUID> spyUsers = new HashSet<>();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.configManager = new ConfigManager(this);
        this.vanishManager = new VanishManager(this);
        this.freezeManager = new FreezeManager(this);
        this.staffModeManager = new StaffModeManager(this);

        // Register commands
        if (getCommand("cmdspy") != null) {
            CommandSpyCommand cmdSpy = new CommandSpyCommand(this);
            getCommand("cmdspy").setExecutor(cmdSpy);
            getCommand("cmdspy").setTabCompleter(cmdSpy);
        }

        if (getCommand("staffmode") != null) {
            StaffModeCommand staffCmd = new StaffModeCommand(this);
            getCommand("staffmode").setExecutor(staffCmd);
            getCommand("staffmode").setTabCompleter(staffCmd);
        }

        if (getCommand("vanish") != null) {
            VanishCommand vanishCmd = new VanishCommand(this);
            getCommand("vanish").setExecutor(vanishCmd);
            getCommand("vanish").setTabCompleter(vanishCmd);
        }

        FreezeCommand freezeCmd = new FreezeCommand(this);
        if (getCommand("freeze") != null) {
            getCommand("freeze").setExecutor(freezeCmd);
            getCommand("freeze").setTabCompleter(freezeCmd);
        }
        if (getCommand("unfreeze") != null) {
            getCommand("unfreeze").setExecutor(freezeCmd);
            getCommand("unfreeze").setTabCompleter(freezeCmd);
        }

        // Register events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new CommandListener(this), this);
        pm.registerEvents(new StaffModeListener(this), this);
        pm.registerEvents(new FreezeListener(this), this);
        pm.registerEvents(new VanishListener(this), this);
        pm.registerEvents(new GUIListener(this), this);

        getLogger().info("SpazioStaff ha sido activado correctamente.");
    }

    @Override
    public void onDisable() {
        // Restore staff members to original inventories
        for (UUID uuid : new HashSet<>(staffModeManager.getSavedData().keySet())) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null && p.isOnline()) {
                staffModeManager.setStaffMode(p, false);
            }
        }

        spyUsers.clear();
        getLogger().info("SpazioStaff ha sido desactivado.");
    }

    public static SpazioStaff getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public VanishManager getVanishManager() {
        return vanishManager;
    }

    public FreezeManager getFreezeManager() {
        return freezeManager;
    }

    public StaffModeManager getStaffModeManager() {
        return staffModeManager;
    }

    public boolean isSpyEnabled(Player player) {
        return spyUsers.contains(player.getUniqueId());
    }

    public boolean toggleSpy(Player player) {
        if (isSpyEnabled(player)) {
            setSpyEnabled(player, false);
            return false;
        } else {
            setSpyEnabled(player, true);
            return true;
        }
    }

    public void setSpyEnabled(Player player, boolean enabled) {
        if (enabled) {
            spyUsers.add(player.getUniqueId());
        } else {
            spyUsers.remove(player.getUniqueId());
        }
    }

    public Set<UUID> getSpyUsers() {
        return spyUsers;
    }

    public String color(String text) {
        return configManager.color(text);
    }
}
