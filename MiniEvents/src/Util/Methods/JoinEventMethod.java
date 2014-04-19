package Util.Methods;

import Mains.MiniEvents;
import Mains.SettingsManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class JoinEventMethod {
    SettingsManager settings = SettingsManager.getInstance();

    public MiniEvents plugin;

    public JoinEventMethod(MiniEvents plugin) {
        this.plugin = plugin;
    }

    public void startTDM(final Player player, final String s) {
        World w = Bukkit.getServer().getWorld(settings.getData().getString(s + ".blue.world"));
        double x = settings.getData().getDouble(s + ".blue.x");
        double y = settings.getData().getDouble(s + ".blue.y");
        double z = settings.getData().getDouble(s + ".blue.z");
        float yaw = Float.intBitsToFloat(settings.getData().getInt(s + ".blue.yaw"));
        float pitch = Float.intBitsToFloat(settings.getData().getInt(s + ".blue.pitch"));
        Location lmsi = new Location(w, x, y, z, yaw, pitch);

        World ws = Bukkit.getServer().getWorld(settings.getData().getString(s + ".red.world"));
        double xs = settings.getData().getDouble(s + ".red.x");
        double ys = settings.getData().getDouble(s + ".red.y");
        double zs = settings.getData().getDouble(s + ".red.z");
        float yaws = Float.intBitsToFloat(settings.getData().getInt(s + ".red.yaw"));
        float pitchs = Float.intBitsToFloat(settings.getData().getInt(s + ".red.pitch"));
        Location lmsis = new Location(ws, xs, ys, zs, yaws, pitchs);
        if (plugin.getInfo().sblue.contains(player.getName())) {
            player.teleport(lmsi);
            plugin.getEquipArmor().equip(player, true, "blue");
        } else if (plugin.getInfo().sred.contains(player.getName())) {
            player.teleport(lmsis);
            plugin.getEquipArmor().equip(player, true, "red");
        }
        plugin.getInfo().eventstarting = false;
        plugin.getInfo().eventstarted = true;
    }

    public void start(final Player player, final String s) {
        plugin.getInfo().sbefore.add(player.getName());
        World w = Bukkit.getServer().getWorld(settings.getData().getString(s + ".world"));
        double x = settings.getData().getDouble(s + ".x");
        double y = settings.getData().getDouble(s + ".y");
        double z = settings.getData().getDouble(s + ".z");
        float yaw = Float.intBitsToFloat(settings.getData().getInt(s + ".yaw"));
        float pitch = Float.intBitsToFloat(settings.getData().getInt(s + ".pitch"));
        Location lmsi = new Location(w, x, y, z, yaw, pitch);
        player.teleport(lmsi);
        if (plugin.getEventName().equals("horse")) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    plugin.getHorseEvent().startHorse(player);
                }
            }, 5L);
        }
        int l = plugin.getConfig().getInt("events.grace-period");
        if (!plugin.getEventName().equals("horse")) {
            plugin.send(player, s + "-before", Integer.toString(l));
        }
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                if (plugin.getInfo().inevent.size() != 0) {
                    plugin.getInfo().sbefore.clear();
                    if (!plugin.getEventName().equals("parkour") && !plugin.getEventName().equals("horse")) {
                        plugin.send(player, s + "-invis-over");
                    }
                    plugin.getInfo().eventstarting = false;
                    plugin.getInfo().eventstarted = true;
                }
            }
        }, l * 20L);
    }

    public void JoinEvent(final Player player) {
        for (Player o : Bukkit.getServer().getOnlinePlayers()) {
            o.showPlayer(player);
        }
        player.closeInventory();
        plugin.getDo().savePlayerInventory(player);
        if (plugin.getInfo().eventstarted) {
            if (plugin.getEventName().equals("tdm")) {
                startTDM(player, plugin.getEventName());
            } else {
                start(player, plugin.getEventName());
                plugin.getEquipArmor().equip(player, false, null);
            }
        }
    }
}
