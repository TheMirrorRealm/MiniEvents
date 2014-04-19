package Handlers;

import API.CustomEvents.MiniEventsLoseEvent;
import Handlers.EventHandlers.SimpleEvents.KO;
import Handlers.EventHandlers.SimpleEvents.Parkour;
import Handlers.EventHandlers.SimpleEvents.Spleef;
import Handlers.EventHandlers.SimpleEvents.TNTRun;
import Mains.MiniEvents;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Handle implements Listener {
    public MiniEvents plugin;

    public Handle(MiniEvents plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        String s = event.getPlayer().getName().toLowerCase();
        if (plugin.getInfo().inevent.containsKey(event.getPlayer().getName())) {
            Player player = event.getPlayer();
            if (plugin.getInfo().eventstarted) {
                plugin.getSpectateMode().inspec.add(player.getName());
                Bukkit.getServer().getPluginManager().callEvent(new MiniEventsLoseEvent(player, plugin.getEventName(),
                        plugin.getInfo().inevent.size(), plugin.getTimerMain().getTimeLeft(), plugin.getInfo().inevent));
                FileConfiguration fc = plugin.playerFile(s);
                fc.set(s + ".quit", true);
                try {
                    fc.save(plugin.playerData(s));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (plugin.getInfo().inevent.size() - plugin.getSpectateMode().inspec.size() <= 1) {
                    KO.ended = true;
                    Spleef.ended = true;
                    Parkour.end = true;
                    TNTRun.ended = true;
                    plugin.getMethods().endIt(plugin.getMethods().getWinner(player));
                }
                plugin.getDo().removePlayerFromEvent(player, false, false);
            } else {
                plugin.getInfo().inevent.remove(player.getName());
            }
        }
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final String s = player.getName().toLowerCase();
        final FileConfiguration fc = plugin.playerFile(s);
        fc.set(s + ".name", player.getName());
        if (fc.getString(s + ".quit") != null) {
            if (fc.getBoolean(s + ".quit")) {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        plugin.getDo().removePlayerFromEvent(player, true, true);
                    }
                }, 5L);
                fc.set(s + ".quit", false);
            }
        }
        try {
            fc.save(plugin.playerData(s));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
