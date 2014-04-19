package Handlers.EventHandlers.ComplexEvents;

import Mains.MiniEvents;
import Util.Effects.ParticleEffect;
import Mains.SettingsManager;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class OITC implements Listener {
    SettingsManager settings = SettingsManager.getInstance();
    public static final ArrayList<Integer> in = new ArrayList<>();
    public static final HashSet<String> inin = new HashSet<>();
    public static final HashMap<String, Integer> oi = new HashMap<>();
    public static final HashMap<String, Integer> last = new HashMap<>();
    public static MiniEvents plugin;

    public OITC(MiniEvents plugin) {
        OITC.plugin = plugin;
    }

    public void run(Player player, int i) {
        int l = in.get(i);
        String ii = Integer.toString(l);
        last.put(player.getName(), l);
        in.clear();
        World w = Bukkit.getServer().getWorld(settings.getData().getString("oitc.rspawn." + ii + ".world"));
        double x = settings.getData().getDouble("oitc.rspawn." + ii + ".x");
        double y = settings.getData().getDouble("oitc.rspawn." + ii + ".y");
        double z = settings.getData().getDouble("oitc.rspawn." + ii + ".z");
        float yaw = Float.intBitsToFloat(settings.getData().getInt("oitc.rspawn." + ii + ".yaw"));
        float pitch = Float.intBitsToFloat(settings.getData().getInt("oitc.rspawn." + ii + ".pitch"));
        player.teleport(new Location(w, x, y, z, yaw, pitch));
    }

    public void randomTP(final Player player) {
        if (settings.getData().getConfigurationSection("oitc.rspawn") != null) {
            for (String s : settings.getData().getConfigurationSection("oitc.rspawn.").getKeys(false)) {
                int num = Integer.parseInt(s);
                in.add(num);
            }
            Collections.shuffle(in);
            if (!last.containsKey(player.getName())) {
                run(player, 0);
            } else {
                if (last.get(player.getName()).equals(in.get(0))) {
                    run(player, 1);
                } else {
                    run(player, 0);
                }
            }
        } else {
            World w = Bukkit.getServer().getWorld(settings.getData().getString("oitc.world"));
            double x = settings.getData().getDouble("oitc.x");
            double y = settings.getData().getDouble("oitc.y");
            double z = settings.getData().getDouble("oitc.z");
            float yaw = Float.intBitsToFloat(settings.getData().getInt("oitc.yaw"));
            float pitch = Float.intBitsToFloat(settings.getData().getInt("oitc.pitch"));
            Location lmsi = new Location(w, x, y, z, yaw, pitch);
            player.teleport(lmsi);
            inin.add(player.getName());
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    inin.remove(player.getName());
                }
            }, 3 * 20L);
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();
            if (plugin.getPlayerEvent(player).equals("oitc")) {
                if (plugin.getInfo().eventstarted) {
                    if (!plugin.getInfo().sbefore.contains(player.getName())) {
                        if (event.getDamager() instanceof Arrow) {
                            Arrow arrow = (Arrow) event.getDamager();
                            if (arrow.getShooter() instanceof Player) {
                                Player hitter = (Player) arrow.getShooter();
                                event.setCancelled(true);
                                if (!hitter.getName().equalsIgnoreCase(player.getName())) {
                                    ParticleEffect.CLOUD.display(player.getLocation(), 1F, 1F, 1F, 1F, 40);
                                    ParticleEffect.CLOUD.display(player.getLocation(), 1F, 1F, 1F, 1F, 40);
                                    hitter.playSound(hitter.getLocation(), Sound.SUCCESSFUL_HIT, 5, 10);
                                    if (oi.containsKey(hitter.getName())) {
                                        oi.put(hitter.getName(), oi.get(hitter.getName()) + 1);
                                    } else {
                                        oi.put(hitter.getName(), 1);
                                    }
                                    if (!oi.containsKey(player.getName())) {
                                        oi.put(player.getName(), 0);
                                    }
                                    plugin.send(player, "oitc-died", Integer.toString(oi.get(player.getName())), hitter.getName());
                                    plugin.send(hitter, "oitc-hit", Integer.toString(oi.get(hitter.getName())), player.getName());
                                    if (!hitter.getInventory().contains(Material.ARROW)) {
                                        hitter.getInventory().addItem(new ItemStack(Material.ARROW));
                                    }
                                    randomTP(player);
                                    player.setHealth(player.getMaxHealth());
                                    plugin.getEquipArmor().equip(player, false, null);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void Hitter(PlayerDeathEvent event) {
        final Player player = event.getEntity();
        if (event.getEntity().getKiller() != null) {
            Player shooter = player.getKiller();
            if (plugin.getPlayerEvent(player).equals("oitc")) {
                if (plugin.getInfo().eventstarted) {
                    if (!plugin.getInfo().sbefore.contains(player.getName())) {
                        if (!inin.contains(player.getName())) {
                            ParticleEffect.CLOUD.display(player.getLocation(), 1F, 1F, 1F, 1F, 40);
                            ParticleEffect.CLOUD.display(player.getLocation(), 1F, 1F, 1F, 1F, 40);
                            plugin.getEquipArmor().equip(player, false, null);
                            shooter.playSound(shooter.getLocation(), Sound.SUCCESSFUL_HIT, 5, 10);
                            ItemStack arrows = new ItemStack(Material.ARROW, 1);
                            if (!shooter.getInventory().contains(Material.ARROW)) {
                                shooter.getInventory().addItem(arrows);
                            }
                            plugin.send(player, "oitc-died", Integer.toString(oi.get(player.getName())), shooter.getName());
                            if (oi.containsKey(shooter.getName())) {
                                oi.put(shooter.getName(), oi.get(shooter.getName()) + 1);
                            } else {
                                oi.put(shooter.getName(), 1);
                            }
                            if (!oi.containsKey(player.getName())) {
                                oi.put(player.getName(), 0);
                            }
                            plugin.send(shooter, "oitc-hit", Integer.toString(oi.get(shooter.getName())), player.getName());
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    randomTP(player);
                                    plugin.getEquipArmor().equip(player, false, null);
                                }
                            }, 7L);
                        }
                    }
                }
            } else {
                if (plugin.getPlayerEvent(player).equals("oitc")) {
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            randomTP(player);
                            plugin.getEquipArmor().equip(player, false, null);
                        }
                    }, 7L);
                }
            }
        }
    }

    public static int getMax() {
        int i = 0;
        Map.Entry<String, Integer> maxEntry = null;
        for (Map.Entry<String, Integer> entry : oi.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }
        if (maxEntry != null) {
            i = maxEntry.getValue();
        }
        oi.clear();
        return i;
    }

    public void endOITC() {
        in.clear();
        inin.clear();
        last.clear();
        Map.Entry<String, Integer> maxEntry = null;
        for (Map.Entry<String, Integer> entry : oi.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }
        if (maxEntry != null) {
            if (plugin.getConfig().getConfigurationSection("prizes.oitc") != null) {
                for (String s : plugin.getConfig().getStringList("prizes.oitc")) {
                    Bukkit.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), s);
                }
            }
            final Player o = Bukkit.getServer().getPlayer(maxEntry.getKey());
            plugin.getMethods().endIt(o);
            in.clear();
            inin.clear();
            last.clear();
        }
    }
}



