package Handlers.EventHandlers.ComplexEvents;

import API.CustomEvents.MiniEventsLoseEvent;
import API.CustomEvents.MiniEventsWinEvent;
import Mains.MiniEvents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashSet;

public class TDM implements Listener {
    public MiniEvents plugin;

    public TDM(MiniEvents plugin) {
        this.plugin = plugin;
    }

    public static boolean red = false;
    public static boolean blue = false;

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Player hitter = (Player) event.getDamager();
            if (plugin.getInfo().eventstarted) {
                if (plugin.getInfo().sred.contains(player.getName()) && plugin.getInfo().sred.contains(hitter.getName())) {
                    event.setCancelled(true);
                }
                if (plugin.getInfo().sblue.contains(player.getName()) && plugin.getInfo().sblue.contains(hitter.getName())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    public void runThis(final HashSet<String> set) {
        if (set.size() <= 1) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                        if (plugin.getPlayerEvent(player) != null) {
                            if (plugin.getPlayerEvent(player).equals("tdm") && !set.contains(player.getName())) {
                                plugin.getDo().removePlayerFromEvent(player, true, true);
                            }
                        }
                    }
                    plugin.getDo().endEvent(true);
                }
            }, 4 * 20L);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    for (Player b : Bukkit.getServer().getOnlinePlayers()) {
                        if (plugin.getSpectateMode().inspec.contains(b.getName())) {
                            plugin.getDo().removePlayerFromEvent(b, true, true);
                            if (set.contains(b.getName())) {
                                Bukkit.getServer().getPluginManager().callEvent(new MiniEventsLoseEvent(b, plugin.getEventName(),
                                        plugin.getInfo().inevent.size(), plugin.getTimerMain().getTimeLeft(), plugin.getInfo().inevent));
                            } else {
                                Bukkit.getServer().getPluginManager().callEvent(new MiniEventsWinEvent(b, plugin.getEventName()));
                            }
                        }
                    }
                }
            }, 3 * 20L);
            if (set.equals(plugin.getInfo().sblue)) {
                red = true;
                plugin.broadcast("red-team-won");
            } else if (set.equals(plugin.getInfo().sred)) {
                plugin.broadcast("blue-team-won");
                blue = true;
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (plugin.getPlayerEvent(player).equalsIgnoreCase("tdm")) {
            if (plugin.getInfo().eventstarted) {
                plugin.getDo().putPlayerInSpectate(player);
                if (plugin.getInfo().sred.contains(player.getName())) {
                    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                        if (plugin.getPlayerEvent(player).equals("tdm")) {
                            plugin.send(p, "red-death", player.getName());
                        }
                    }
                    runThis(plugin.getInfo().sred);
                } else if (plugin.getInfo().sblue.contains(player.getName())) {
                    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                        if (plugin.getPlayerEvent(player).equals("tdm")) {
                            plugin.send(p, "blue-death", player.getName());
                        }
                    }
                    runThis(plugin.getInfo().sblue);
                }
            }
        }
    }
}
