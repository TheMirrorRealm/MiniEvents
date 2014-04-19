package Handlers.CheckCustomEvents;

import API.CustomEvents.MiniEventsJoinEvent;
import Mains.MiniEvents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class JoinEvent implements Listener {
    public MiniEvents plugin;

    public JoinEvent(MiniEvents plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onJoin(MiniEventsJoinEvent event) {
        Player player = event.getPlayer();
        for (Player o : Bukkit.getServer().getOnlinePlayers()) {
            if (plugin.getInfo().inevent.containsKey(o.getName())) {
                if (!o.getName().equals(player.getName())) {
                    plugin.send(o, "player-joined-event", player.getName(), Integer.toString(plugin.getInfo().inevent.size()));
                }
            }
        }
    }
}
