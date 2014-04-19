package Handlers.EventHandlers.ComplexEvents;

import Mains.MiniEvents;
import Mains.SettingsManager;
import Util.BossBar;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class KOTH implements Listener {
    SettingsManager settings = SettingsManager.getInstance();
    public static final HashMap<Player, Integer> koth = new HashMap<>();
    public MiniEvents plugin;

    public KOTH(MiniEvents plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        final Player player = event.getPlayer();
        if (plugin.getPlayerEvent(player).equals("koth")) {
            if (plugin.getInfo().eventstarted) {
                plugin.getSpectateMode().inspec.add(player.getName());
                if (settings.getData().getConfigurationSection(plugin.getEventName() + ".spec.") != null) {
                    World w = Bukkit.getServer().getWorld(settings.getData().getString(plugin.getEventName() + ".spec.world"));
                    double x = settings.getData().getDouble(plugin.getEventName() + ".spec.x");
                    double y = settings.getData().getDouble(plugin.getEventName() + ".spec.y");
                    double z = settings.getData().getDouble(plugin.getEventName() + ".spec.z");
                    float yaw = Float.intBitsToFloat(settings.getData().getInt(plugin.getEventName() + ".spec.yaw"));
                    float pitch = Float.intBitsToFloat(settings.getData().getInt(plugin.getEventName() + ".spec.pitch"));
                    event.setRespawnLocation(new Location(w, x, y, z, yaw, pitch));
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            player.getInventory().clear();
                            player.getInventory().setArmorContents(null);
                            plugin.send(player, "now-in-spectate");
                        }
                    }, 3L);
                } else {
                    World w = Bukkit.getServer().getWorld(settings.getData().getString("quit.world"));
                    double x = settings.getData().getDouble("quit.x");
                    double y = settings.getData().getDouble("quit.y");
                    double z = settings.getData().getDouble("quit.z");
                    float yaw = Float.intBitsToFloat(settings.getData().getInt("quit.yaw"));
                    float pitch = Float.intBitsToFloat(settings.getData().getInt("quit.pitch"));
                    event.setRespawnLocation(new Location(w, x, y, z, yaw, pitch));
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            player.getInventory().clear();
                            player.getInventory().setArmorContents(null);
                        }
                    }, 3L);
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        final Player player = event.getEntity();
        if (plugin.getPlayerEvent(player).equals("koth")) {
            if (plugin.getInfo().eventstarted) {
                World w = Bukkit.getServer().getWorld(settings.getData().getString("koth.world"));
                double x = settings.getData().getDouble("koth.x");
                double y = settings.getData().getDouble("koth.y");
                double z = settings.getData().getDouble("koth.z");
                float yaw = Float.intBitsToFloat(settings.getData().getInt("koth.yaw"));
                float pitch = Float.intBitsToFloat(settings.getData().getInt("koth.pitch"));
                final Location lmsi = new Location(w, x, y, z, yaw, pitch);
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        if (plugin.getPlayerEvent(player).equals("koth")) {
                            player.teleport(lmsi);
                            for (Player o : Bukkit.getServer().getOnlinePlayers()) {
                                o.showPlayer(player);
                            }
                            plugin.getSpectateMode().inspec.remove(player.getName());
                            plugin.getInfo().sbefore.remove(player.getName());
                            plugin.getEquipArmor().equip(player, false, null);
                            BossBar.removeStatusBar(player);
                        }
                    }
                }, 5L * 20L);
            }
        }
    }

    public void startKOTH(final Location loc) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!plugin.getEventName().equalsIgnoreCase("none")) {
                    for (final Player o : Bukkit.getServer().getOnlinePlayers()) {
                        if (plugin.getPlayerEvent(o).equals("koth")) {
                            if (plugin.getInfo().eventstarted) {
                                if (plugin.getInfo().inevent.size() >= 2) {
                                    if (o.getLocation().distance(loc) <= 3) {
                                        if (!plugin.getSpectateMode().inspec.contains(o.getName())) {
                                            if (koth.containsKey(o)) {
                                                Float l = (float) ((koth.get(o) * 100) / plugin.getConfig().getInt("events.koth-wait"));
                                                if (l >= 100) {
                                                    plugin.getMethods().endIt(o);
                                                    cancel();
                                                    break;
                                                } else {
                                                    koth.put(o, koth.get(o) + 1);
                                                    BossBar.setStatusBar(o, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("events.koth-top-message").replace("{0}", Float.toString(l))), l);
                                                    if (l >= 70 && (l % 5 == 0)) {
                                                        for (Player lol : Bukkit.getServer().getOnlinePlayers()) {
                                                            if (plugin.getPlayerEvent(o) != null) {
                                                                if (plugin.getPlayerEvent(o).equals("koth")) {
                                                                    plugin.send(lol, "koth-announce", o.getName(), Integer.toString(l.intValue()));
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                koth.put(o, 1);
                                                Float l = (float) ((koth.get(o) * 100) / plugin.getConfig().getInt("events.koth-wait"));
                                                BossBar.setStatusBar(o, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("events.koth-top-message").replace("{0}", Float.toString(l))), l);
                                            }
                                        }
                                    }
                                } else {
                                    plugin.getMethods().endIt(o);
                                    cancel();
                                }
                            }
                        }
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20L);
    }
}
