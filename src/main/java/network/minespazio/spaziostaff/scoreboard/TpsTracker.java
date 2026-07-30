package network.minespazio.spaziostaff.scoreboard;

import network.minespazio.spaziostaff.SpazioStaff;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class TpsTracker extends BukkitRunnable {

    private static double tps = 20.0;
    private long lastTick = System.currentTimeMillis();
    private final long[] ticks = new long[60];
    private int tickCount = 0;

    public TpsTracker(SpazioStaff plugin) {
        this.runTaskTimer(plugin, 100L, 1L);
    }

    @Override
    public void run() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastTick;
        lastTick = now;

        ticks[tickCount % ticks.length] = elapsed;
        tickCount++;

        if (tickCount > 60) {
            long total = 0;
            for (long t : ticks) {
                total += t;
            }
            double targetTps = 1000.0 / ((double) total / ticks.length);
            tps = Math.min(20.0, Math.max(0.0, targetTps));
        }
    }

    public static double getTps() {
        try {
            java.lang.reflect.Method method = Bukkit.class.getMethod("getTPS");
            Object result = method.invoke(null);
            if (result instanceof double[]) {
                double[] nmsTps = (double[]) result;
                if (nmsTps.length > 0) {
                    return Math.min(20.0, Math.max(0.0, nmsTps[0]));
                }
            }
        } catch (Throwable ignored) {}
        return tps;
    }

    public static String getFormattedTps() {
        double currentTps = getTps();
        String color = "&a";
        if (currentTps < 18.0 && currentTps >= 15.0) {
            color = "&e";
        } else if (currentTps < 15.0) {
            color = "&c";
        }
        return color + String.format("%.2f", currentTps);
    }
}
