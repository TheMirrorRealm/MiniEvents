package Util;

import Mains.MiniEvents;
import Mains.SettingsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;

import java.util.HashSet;

public class SpectateMode implements Listener, CommandExecutor {
    public final HashSet<String> inspec = new HashSet<>();
    SettingsManager settings = SettingsManager.getInstance();
    public MiniEvents plugin;

    public SpectateMode(MiniEvents plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equals("leave")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!(player.hasPermission("event.quit") || player.hasPermission("event.*") || player.isOp())) {
                    plugin.send(player, "no-permission");
                    return true;
                } else {
                    if (plugin.getPlayerEvent(player) != null) {
                        if (plugin.getInfo().eventstarted) {
                            if (!plugin.eventName.equalsIgnoreCase("koth")) {
                                plugin.getMethods().basicLeave(player);
                            } else {
                                plugin.getDo().removePlayerFromEvent(player, true, true);
                            }
                        } else {
                            if (plugin.getInfo().sbefore.contains(player.getName())) {
                                if (!plugin.eventName.equalsIgnoreCase("koth")) {
                                    plugin.getMethods().basicLeave(player);
                                } else {
                                    plugin.getDo().removePlayerFromEvent(player, true, true);
                                }
                            } else {
                                plugin.getMethods().basicLose(player);
                                plugin.getDo().removePlayerFromEvent(player, false, false);
                            }
                        }
                        plugin.send(player, "left-event");
                        return true;
                    } else {
                        plugin.send(player, "not-in-event");
                    }
                }
            }
        }
        return true;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        String s = event.getPlayer().getName();
        Player player = event.getPlayer();
        if (inspec.contains(s)) {
            inspec.remove(player.getName());
            plugin.getInfo().sbefore.remove(player.getName());
            plugin.getDo().removePlayerFromEvent(player, false, false);
            FileConfiguration fc = plugin.playerFile(s);
            fc.set(s.toLowerCase() + ".quit", true);
        }
    }

    @EventHandler
    public void onPickUp(PlayerPickupItemEvent event) {
        if (inspec.contains(event.getPlayer().getName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (inspec.contains(event.getPlayer().getName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHit(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (plugin.getInfo().sfire.contains(player.getName())) {
                event.setCancelled(true);
            }
            if (inspec.contains(player.getName())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHit2(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getDamager() instanceof Player) {
                Player damager = (Player) event.getDamager();
                if (inspec.contains(damager.getName())) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
