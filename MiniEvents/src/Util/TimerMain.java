package Util;

import API.CustomEvents.MiniEventsEndEvent;
import API.CustomEvents.MiniEventsJoinEvent;
import API.CustomEvents.MiniEventsTimeOutEvent;
import Mains.MiniEvents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class TimerMain implements Listener {
    public MiniEvents plugin;

    public TimerMain(MiniEvents plugin) {
        this.plugin = plugin;
    }

    public boolean stop = false;

    @EventHandler
    public void onEnd(MiniEventsEndEvent event){
        stop = true;
        now = 0;
    }
    @EventHandler
    public void onJoinJoin(MiniEventsJoinEvent event) {
        for (Player o : Bukkit.getServer().getOnlinePlayers()) {
            if (plugin.getInfo().inevent.containsKey(o.getName())) {
                if (plugin.getInfo().eventstarted) {
                    plugin.getS1().scoreboard(o, getTimeLeft(), false);
                }
            }
        }
    }
    int now;
    public void setTime(final String s) {
        new BukkitRunnable() {
            int lms = plugin.getConfig().getInt("auto-stop." + s);

            public void run() {
                if (!stop) {
                    if (lms <= 0) {
                        MiniEventsTimeOutEvent event = new MiniEventsTimeOutEvent(plugin.getEventName(),
                                plugin.getInfo().inevent.size(), plugin.getInfo().inevent);
                        Bukkit.getServer().getPluginManager().callEvent(event);
                        if (!event.isCancelled()) {
                            if (!plugin.getEventName().equals("oitc")) {
                                for (Player o : Bukkit.getServer().getOnlinePlayers()) {
                                    if (plugin.getInfo().inevent.containsKey(o.getName())) {
                                        plugin.getDo().removePlayerFromEvent(o, true, true);
                                    }
                                }
                                plugin.broadcast("automatic-ended");
                                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                    @Override
                                    public void run() {
                                        plugin.getDo().endEvent(true);
                                    }
                                }, 20L);
                                cancel();
                            } else {
                                plugin.getOitc().endOITC();
                                cancel();
                            }
                        }
                    } else {
                        if (plugin.getInfo().cancelled) {
                            cancel();
                        } else {
                            lms--;
                            now = lms;
                            if (plugin.getConfig().getInt("auto-stop" + s) - 1 == now) {
                                for (Player o : Bukkit.getServer().getOnlinePlayers()) {
                                    if (plugin.getInfo().inevent.containsKey(o.getName())) {
                                        if (plugin.getInfo().eventstarted) {
                                            plugin.getS1().scoreboard(o, getTimeLeft(), true);
                                        }
                                    }
                                }
                            } else {
                                for (Player o : Bukkit.getServer().getOnlinePlayers()) {
                                    if (plugin.getInfo().inevent.containsKey(o.getName())) {
                                        if (plugin.getInfo().eventstarted) {
                                            plugin.getS1().scoreboard(o, getTimeLeft(), false);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 21L);
    }
    public int getTimeLeft() {
        return now;
    }
}
