package Handlers.EventHandlers.SimpleEvents;

import Mains.MiniEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class LMS implements Listener {
    public MiniEvents plugin;

    public LMS(MiniEvents plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (plugin.getPlayerEvent(player).equals("lms")) {
            if (plugin.getInfo().eventstarted) {
                plugin.getMethods().basicLose(player);
            }
        }
    }
}

