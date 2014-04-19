package Handlers.EventHandlers.SimpleEvents;

import Mains.MiniEvents;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.HashSet;

public class Spleef implements Listener {
    public final ArrayList<Location> block = new ArrayList<>();
    public final ArrayList<Location> block2 = new ArrayList<>();
    public static final HashSet<String> lul = new HashSet<>();
    public MiniEvents plugin;

    public Spleef(MiniEvents plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBreak(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        if (plugin.getPlayerEvent(player).equals("spleef")) {
            if (plugin.getInfo().sbefore.contains(event.getPlayer().getName())) {
                event.setCancelled(true);
            } else {
                final Block b = event.getBlock();
                if (player.getItemInHand() != null && !player.getItemInHand().getType().equals(Material.AIR)) {
                    if (b.getType().equals(Material.SNOW_BLOCK)) {
                        event.setCancelled(true);
                        player.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, 80);
                        b.setType(Material.AIR);
                        block.add(b.getLocation());
                        block2.add(b.getLocation());
                    } else {
                        event.setCancelled(true);
                    }
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }

    public static boolean ended = false;

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (plugin.getPlayerEvent(player).equals("spleef")) {
            if (plugin.getInfo().eventstarted) {
                Material b = player.getLocation().getBlock().getType();
                if (b.equals(Material.STATIONARY_WATER) || b.equals(Material.WATER) || event.getPlayer().getLocation().getY() <= 0) {
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

