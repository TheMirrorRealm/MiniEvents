package Handlers.EventHandlers.SimpleEvents;

import Mains.MiniEvents;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashSet;

public class KO implements Listener {
    public static final HashSet<String> lul = new HashSet<>();
    public MiniEvents plugin;

    public KO(MiniEvents plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();
            if (plugin.getPlayerEvent(player) != null) {
                if (plugin.getPlayerEvent(player).equals("ko")) {
                    event.setDamage(0);
                }
            }
        }
    }

    public static boolean ended = false;

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Material b = player.getLocation().getBlock().getType();
        if (plugin.getPlayerEvent(player).equals("ko")) {
            if (plugin.getInfo().eventstarted) {
                if (b.equals(Material.STATIONARY_WATER) || (b.equals(Material.WATER)) || (player.getLocation().getY() <= 0.0D)) {
                    if (!lul.contains(player.getName()) && !ended) {
                        ended = true;
                        lul.add(player.getName());
                        plugin.getMethods().basicLose(player);
                    }
                }
            }
        }
    }
}

