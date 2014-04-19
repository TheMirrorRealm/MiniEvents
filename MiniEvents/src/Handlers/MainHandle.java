package Handlers;

import Mains.MiniEvents;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class MainHandle implements Listener {
    public MiniEvents plugin;

    public MainHandle(MiniEvents plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (plugin.getInfo().inevent.containsKey(event.getEntity().getName())) {
            if (plugin.getInfo().eventstarted) {
                switch (plugin.getPlayerEvent(player)) {
                    case "lms":
                    case "paint":
                    case "koth":
                        plugin.getMethods().onDeath(player, plugin.getPlayerEvent(player));
                        break;
                    case "ko":
                    case "parkour":
                    case "spleef":
                    case "tnt":
                        plugin.getMethods().onLose(player, plugin.getPlayerEvent(player));
                        break;
                }
                event.getDrops().clear();
            }
        }
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            Player player = (Player) event.getEntity().getShooter();
            if (plugin.getInfo().inevent.containsKey(player.getName())) {
                if (plugin.getInfo().eventstarted) {
                    if (event.getEntity() instanceof Arrow) {
                        Arrow arrow = (Arrow) event.getEntity();
                        arrow.remove();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onChat(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (plugin.getInfo().inevent.containsKey(player.getName())) {
            if (!player.hasPermission("event.usecommands") && !player.hasPermission("event.*")) {
                if (!event.getMessage().equalsIgnoreCase("/leave")) {
                    event.setCancelled(true);
                    plugin.send(event.getPlayer(), "event-no-commands");
                }
            }
        }
    }

    @EventHandler
    public void onHit(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (plugin.getInfo().inevent.containsKey(player.getName())) {
                if (plugin.getInfo().eventstarted) {
                    if (plugin.getPlayerEvent(player).equals("spleef")
                            || plugin.getPlayerEvent(player).equals("tnt")
                            || plugin.getPlayerEvent(player).equals("parkour")
                            || plugin.getInfo().sbefore.contains(player.getName())) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (plugin.getInfo().inevent.containsKey(player.getName())) {
            if (plugin.getInfo().eventstarted) {
                if (!plugin.getPlayerEvent(player).equals("spleef")) {
                    event.setCancelled(true);
                } else {
                    if (!event.getBlock().getType().equals(Material.SNOW_BLOCK)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (plugin.getInfo().inevent.containsKey(player.getName())) {
            if (plugin.getInfo().eventstarted) {
                event.setCancelled(true);
            }
        }
    }
}
