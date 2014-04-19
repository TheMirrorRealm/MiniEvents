package Util.Methods;

import API.CustomEvents.MiniEventsJoinEvent;
import API.CustomEvents.MiniEventsStartEvent;
import Mains.MiniEvents;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class CountDown implements Listener {
    public MiniEvents plugin;

    public CountDown(MiniEvents plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(MiniEventsJoinEvent event) {
        plugin.getS1().scoreboard(event.getPlayer(), now, true);
    }
    int now;
    public final ArrayList<String> world = new ArrayList<>();

    public void checkTNT() {
        if (plugin.getEventName().equals("tnt")) {
            for (final Player o : Bukkit.getServer().getOnlinePlayers()) {
                if (plugin.getPlayerEvent(o).equals("tnt") || plugin.getPlayerEvent(o).equalsIgnoreCase("spleef")) {
                    world.add(o.getWorld().getName());
                    break;
                }
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!plugin.getEventName().equals("tnt")) {
                        World o = Bukkit.getServer().getWorld(world.get(0));
                        for (Location l : plugin.getTntrun().block) {
                            o.getBlockAt(l).setType(Material.TNT);
                        }
                        for (Location l : plugin.getTntrun().block2) {
                            o.getBlockAt(l).setType(Material.TNT);
                        }
                        plugin.getTntrun().block2.clear();
                        plugin.getTntrun().block.clear();
                        world.clear();
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0, 20L);
        } else if (plugin.getEventName().equals("spleef")) {
            for (final Player o : Bukkit.getServer().getOnlinePlayers()) {
                if (plugin.getPlayerEvent(o).equals("spleef")) {
                    world.add(o.getWorld().getName());
                    break;
                }
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!plugin.getEventName().equals("spleef")) {
                        World o = Bukkit.getServer().getWorld(world.get(0));
                        for (Location l : plugin.getSpleef().block) {
                            o.getBlockAt(l).setType(Material.SNOW_BLOCK);
                        }
                        for (Location l : plugin.getSpleef().block2) {
                            o.getBlockAt(l).setType(Material.SNOW_BLOCK);
                        }
                        plugin.getSpleef().block2.clear();
                        plugin.getSpleef().block.clear();
                        world.clear();
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0, 20L);
        }
    }
    public void EventWait(final String s) {
        if (plugin.getInfo().eventstarting) {
            plugin.getInfo().cancelled = false;
            new BukkitRunnable() {
                int i = plugin.getConfig().getInt("events.time-to-start");

                @Override
                public void run() {
                    now = i;
                    if (plugin.getInfo().cancelled) {
                        plugin.getInfo().cancelled = false;
                        cancel();
                        return;
                    } else {
                        for (Player o : Bukkit.getServer().getOnlinePlayers()) {
                            if (plugin.getInfo().inevent.containsKey(o.getName())) {
                                if (now == plugin.getConfig().getInt("events.time-to-start")) {
                                    plugin.getS1().scoreboard(o, i, true);
                                } else {
                                    plugin.getS1().scoreboard(o, i, false);
                                }
                            }
                        }
                        plugin.getSignMethods().SignUpdate(i);
                        switch (i) {
                            case 60:
                            case 45:
                            case 30:
                            case 20:
                            case 10:
                            case 5:
                            case 4:
                            case 3:
                            case 2:
                            case 1:
                                plugin.broadcast("event-countdown", plugin.getFormalName(), Integer.toString(i));
                                break;
                            case 0:
                                int m = plugin.getInfo().inevent.size();
                                if (m <= 1) {
                                    plugin.broadcast("event-not-enough-players", plugin.getFormalName());
                                    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                                        if (plugin.getInfo().inevent.containsKey(player.getName())) {
                                            plugin.getDo().removePlayerFromEvent(player, false, false);
                                        }
                                    }
                                    plugin.getDo().endEvent(false);
                                    cancel();
                                    break;
                                } else {
                                    plugin.broadcast("event-countdown", plugin.getFormalName(), Integer.toString(i));
                                    plugin.broadcast("event-start", plugin.getFormalName());
                                    plugin.getInfo().eventstarting = false;
                                    plugin.getInfo().eventstarted = true;
                                    MiniEventsStartEvent event = new MiniEventsStartEvent(s, plugin.getTimerMain().getTimeLeft(), plugin.getInfo().inevent.size(), plugin.getInfo().inevent);
                                    Bukkit.getServer().getPluginManager().callEvent(event);
                                    cancel();
                                }
                                cancel();
                                break;
                        }
                    }
                    i--;
                }
            }.runTaskTimer(plugin, 0, 20L);
        }
    }
}
