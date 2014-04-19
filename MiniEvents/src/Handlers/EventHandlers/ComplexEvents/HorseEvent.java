package Handlers.EventHandlers.ComplexEvents;

import Mains.MiniEvents;
import net.minecraft.server.v1_7_R2.EntityHorse;
import net.minecraft.server.v1_7_R2.GenericAttributes;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftHorse;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.HorseJumpEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;

public class HorseEvent implements Listener {
    public MiniEvents plugin;

    public HorseEvent(MiniEvents plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSpawn(CreatureSpawnEvent event) {
        if (event.getEntity() instanceof Horse) {
            if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.CUSTOM)) {
                if (plugin.getEventName().equalsIgnoreCase("horse")) {
                    if (plugin.getInfo().eventstarted) {
                        event.setCancelled(false);
                    }
                }
            }
        }
    }

    public void startHorse(Player player) {
        Horse h = (Horse) player.getWorld().spawnEntity(player.getLocation(), EntityType.HORSE);
        h.setTamed(true);
        h.setOwner(player);
        h.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        h.setPassenger(player);
        EntityHorse horse = ((CraftHorse) h).getHandle();
        double speed = plugin.getConfig().getDouble("events.horse-speed");
        horse.getAttributeInstance(GenericAttributes.d).setValue(speed);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (plugin.getInfo().eventstarted) {
            if (event.getEntity() instanceof Player) {
                final Player player = (Player) event.getEntity();
                if (plugin.getPlayerEvent(player).equals("horse")) {
                    event.setCancelled(true);
                }
            }
            if (event.getEntity() instanceof Horse) {
                Horse horse = (Horse) event.getEntity();
                if (horse.getPassenger() != null) {
                    if (horse.getPassenger() instanceof Player) {
                        Player player = (Player) horse.getPassenger();
                        if (plugin.getPlayerEvent(player).equals("horse")) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void jump(HorseJumpEvent event) {
        if (plugin.getInfo().eventstarted) {
            if (event.getEntity().getPassenger() != null) {
                if (event.getEntity().getPassenger() instanceof Player) {
                    Player player = (Player) event.getEntity().getPassenger();
                    if (plugin.getPlayerEvent(player).equals("horse")) {
                        event.setPower(0);
                    }
                }
            }
        }
    }

    @EventHandler
    public void Remove(VehicleExitEvent event) {
        if (plugin.getInfo().eventstarted) {
            if (event.getVehicle() instanceof Horse) {
                if (event.getVehicle().getPassenger() instanceof Player) {
                    Player player = (Player) event.getVehicle().getPassenger();
                    if (plugin.getPlayerEvent(player).equals("horse")) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    public static boolean end = false;

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (plugin.getInfo().eventstarted) {
            final Player player = event.getPlayer();
            if (event.getPlayer().isInsideVehicle()) {
                if (event.getPlayer().getVehicle() instanceof Horse) {
                    if (plugin.getPlayerEvent(player).equals("horse")) {
                        ItemStack c = plugin.getMethods().translateItemStack("events.block-below-horse");
                        Block l = player.getVehicle().getLocation().getBlock().getRelative(BlockFace.DOWN);
                        if (!l.getType().equals(Material.AIR)) {
                            if (l.getType().equals(c.getType()) && l.getData() == c.getData().getData()) {
                                if (!end) {
                                    end = true;
                                    for (Player player1 : Bukkit.getServer().getOnlinePlayers()) {
                                        if (plugin.getPlayerEvent(player).equals("horse")) {
                                            if (player1.isInsideVehicle()) {
                                                player1.getVehicle().remove();
                                            }
                                        }
                                    }
                                    plugin.getMethods().endIt(player);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
