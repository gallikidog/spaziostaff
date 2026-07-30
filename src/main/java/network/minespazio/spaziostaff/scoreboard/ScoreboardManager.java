package network.minespazio.spaziostaff.scoreboard;

import network.minespazio.spaziostaff.SpazioStaff;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager implements Listener {

    private final SpazioStaff plugin;
    private final Map<UUID, Long> loginTimes = new HashMap<>();
    private final Map<UUID, Scoreboard> previousScoreboards = new HashMap<>();
    private final Map<UUID, Scoreboard> staffScoreboards = new HashMap<>();

    public ScoreboardManager(SpazioStaff plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        // Record login time for currently online players
        for (Player p : Bukkit.getOnlinePlayers()) {
            loginTimes.put(p.getUniqueId(), System.currentTimeMillis());
        }

        // Start periodic scoreboard updater task (every 20 ticks / 1 second)
        Bukkit.getScheduler().runTaskTimer(plugin, this::updateScoreboards, 20L, 20L);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        loginTimes.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        loginTimes.remove(uuid);
        previousScoreboards.remove(uuid);
        staffScoreboards.remove(uuid);
    }

    public void enableStaffScoreboard(Player player) {
        if (!plugin.getConfig().getBoolean("scoreboard.enabled", true)) return;

        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("staff_board", "dummy", color(plugin.getConfig().getString("scoreboard.title", "&b&lSPAZIO STAFF")));
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        previousScoreboards.put(player.getUniqueId(), player.getScoreboard());
        staffScoreboards.put(player.getUniqueId(), board);

        player.setScoreboard(board);
        updatePlayerScoreboard(player);
    }

    public void disableStaffScoreboard(Player player) {
        UUID uuid = player.getUniqueId();
        Scoreboard prev = previousScoreboards.remove(uuid);
        staffScoreboards.remove(uuid);

        if (prev != null) {
            player.setScoreboard(prev);
        } else {
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }
    }

    public void updateScoreboards() {
        if (!plugin.getConfig().getBoolean("scoreboard.enabled", true)) return;

        for (UUID uuid : plugin.getStaffModeManager().getSavedData().keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                updatePlayerScoreboard(player);
            }
        }
    }

    private void updatePlayerScoreboard(Player player) {
        Scoreboard board = staffScoreboards.get(player.getUniqueId());
        if (board == null) return;

        Objective obj = board.getObjective("staff_board");
        if (obj == null) return;

        String newTitle = color(plugin.getConfig().getString("scoreboard.title", "&b&lSPAZIO STAFF"));
        if (!obj.getDisplayName().equals(newTitle)) {
            obj.setDisplayName(newTitle);
        }

        List<String> rawLines = plugin.getConfig().getStringList("scoreboard.lines");
        int score = rawLines.size();

        for (int i = 0; i < rawLines.size(); i++) {
            String line = rawLines.get(i);
            String formattedLine = replacePlaceholders(player, line);

            String teamName = "line_" + i;
            Team team = board.getTeam(teamName);
            if (team == null) {
                team = board.registerNewTeam(teamName);
            }

            String entry = ChatColor.values()[i].toString() + ChatColor.RESET;
            if (!team.hasEntry(entry)) {
                team.addEntry(entry);
            }

            team.setPrefix(formattedLine);
            obj.getScore(entry).setScore(score);
            score--;
        }
    }

    private String replacePlaceholders(Player player, String text) {
        boolean isVanished = plugin.getVanishManager().isVanished(player);
        int onlineCount = Bukkit.getOnlinePlayers().size();
        int maxOnline = Bukkit.getMaxPlayers();

        int staffOnline = 0;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("spaziostaff.staffmode") || p.hasPermission("spaziostaff.vanish") || p.isOp()) {
                staffOnline++;
            }
        }

        long loginTime = loginTimes.getOrDefault(player.getUniqueId(), System.currentTimeMillis());
        long sessionMillis = System.currentTimeMillis() - loginTime;
        String sessionTime = formatDuration(sessionMillis);

        return color(text
                .replace("{player}", player.getName())
                .replace("{staffmode}", "&aActivado")
                .replace("{vanish}", isVanished ? "&aActivado" : "&cDesactivado")
                .replace("{online}", String.valueOf(onlineCount))
                .replace("{max_online}", String.valueOf(maxOnline))
                .replace("{staff_online}", String.valueOf(staffOnline))
                .replace("{tps}", TpsTracker.getFormattedTps())
                .replace("{session_time}", sessionTime)
                .replace("{playtime}", sessionTime));
    }

    private String formatDuration(long millis) {
        long seconds = millis / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;

        if (hours > 0) {
            return String.format("%02dh %02dm %02ds", hours, minutes, secs);
        } else {
            return String.format("%02dm %02ds", minutes, secs);
        }
    }

    private String color(String text) {
        return plugin.getConfigManager().color(text);
    }
}
