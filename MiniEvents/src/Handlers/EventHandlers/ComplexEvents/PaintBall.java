package Handlers.EventHandlers.ComplexEvents;

import Mains.MiniEvents;
import Util.Effects.ParticleEffect;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PaintBall implements Listener {
    public MiniEvents plugin;

    public PaintBall(MiniEvents plugin) {
        this.plugin = plugin;
    }

    public void runThis(final Item item, final Player player) {
        item.setVelocity(player.getLocation().getDirection().multiply(3.0D));
        item.setPickupDelay(Integer.MAX_VALUE);
        item.getWorld().playSound(item.getLocation(), Sound.ARROW_HIT, 5, 10);
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Entity entity : item.getNearbyEntities(0.8, 0.8, 0.8)) {
                    if (entity instanceof Player) {
                        if (!((Player) entity).getName().equalsIgnoreCase(player.getName())) {
                            if (plugin.getPlayerEvent(player).equals("paint")) {
                                if (plugin.getInfo().eventstarted) {
                                    ((Player) entity).setHealth(((Player) entity).getHealth() - plugin.getConfig().getDouble("events.paintball-damage"));
                                    ((Player) entity).damage(0, player);
                                    ParticleEffect.LARGE_SMOKE.display(entity.getLocation(), 1F, 1F, 1F, 1F, 30);
                                }
                            }
                        }
                        cancel();
                        return;
                    }
                }
            }
        }.runTaskTimer(plugin, 2, 1);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                item.remove();
            }
        }, 3 * 20L);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (plugin.getPlayerEvent(player).equals("paint")) {
            if (plugin.getInfo().eventstarted) {
                ItemStack c = plugin.getMethods().translateItemStack("events.paintball-item");
                if (player.getItemInHand().getType().equals(c.getType()) && player.getItemInHand().getData().getData() == c.getData().getData()) {
                    event.setCancelled(true);
                    if (!plugin.getInfo().sbefore.contains(player.getName())) {
                        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                            Random dice = new Random();
                            int number;
                            number = dice.nextInt(6);
                            switch (number) {
                                case 1:
                                    final Item item = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.INK_SACK, 1, (short) 8));
                                    runThis(item, player);
                                    break;
                                case 2:
                                    final Item item2 = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.INK_SACK, 1, (short) 9));
                                    runThis(item2, player);
                                    break;
                                case 3:
                                    final Item item3 = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.INK_SACK, 1, (short) 10));
                                    runThis(item3, player);
                                    break;
                                case 4:
                                    final Item item4 = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.INK_SACK, 1, (short) 5));
                                    runThis(item4, player);
                                    break;
                                case 5:
                                    final Item item5 = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.INK_SACK, 1, (short) 13));
                                    runThis(item5, player);
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (plugin.getPlayerEvent(player).equals("paint")) {
            if (plugin.getInfo().eventstarted) {
                plugin.getMethods().basicLose(player);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (event.getDamager() instanceof Player) {
                if (plugin.getPlayerEvent(player).equals("paint")) {
                    if (plugin.getInfo().eventstarted) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPickUp(PlayerPickupItemEvent event) {
        if (plugin.getPlayerEvent(event.getPlayer()).equals("paint")) {
            if (plugin.getInfo().eventstarted) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (plugin.getPlayerEvent(event.getPlayer()).equals("paint")) {
            if (plugin.getInfo().eventstarted) {
                event.setCancelled(true);
            }
        }
    }
}

