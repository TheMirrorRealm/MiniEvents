package Handlers.CheckCustomEvents;

import API.CustomEvents.MiniEventsStartEvent;
import Mains.MiniEvents;
import Mains.SettingsManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.io.IOException;

public class StartEvent implements Listener {
    public MiniEvents plugin;

    public StartEvent(MiniEvents plugin) {
        this.plugin = plugin;
    }

    SettingsManager settings = SettingsManager.getInstance();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onStart(MiniEventsStartEvent event) {
        if (!event.isCancelled()) {
            if (plugin.getEventName().equalsIgnoreCase("koth")) {
                World w = Bukkit.getServer().getWorld(settings.getData().getString("koth.top.world"));
                double x = settings.getData().getDouble("koth.top.x");
                double y = settings.getData().getDouble("koth.top.y");
                double z = settings.getData().getDouble("koth.top.z");
                float yaw = Float.intBitsToFloat(settings.getData().getInt("koth.top.yaw"));
                float pitch = Float.intBitsToFloat(settings.getData().getInt("koth.top.pitch"));
                Location lmsi = new Location(w, x, y, z, yaw, pitch);
                plugin.getKOTH().startKOTH(lmsi);
            }
            for (Player o : Bukkit.getServer().getOnlinePlayers()) {
                if (plugin.getInfo().inevent.containsKey(o.getName())) {
                    if (plugin.getInfo().sblue.contains(o.getName())) {
                        plugin.send(o, "you-are-on-blue");
                    } else if (plugin.getInfo().sred.contains(o.getName())) {
                        plugin.send(o, "you-are-on-red");
                    }
                    String s = o.getName().toLowerCase();
                    FileConfiguration fc = plugin.playerFile(s);
                    if (fc.getString(s + ".total") != null) {
                        fc.set(s + ".total", fc.getInt(s + ".total") + 1);
                    } else {
                        fc.set(s + ".total", 1);
                    }
                    try {
                        fc.save(plugin.playerData(s));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    o.setGameMode(GameMode.SURVIVAL);
                    o.setAllowFlight(false);
                    o.setFlying(false);
                    plugin.getJoinEventMethod().JoinEvent(o);
                }
            }
            if (plugin.getEventName().equalsIgnoreCase("tnt") || plugin.getEventName().equalsIgnoreCase("spleef")) {
                plugin.getCountDown().checkTNT();
            }
            plugin.getSignMethods().SignUpdate(0);
        }
    }
}
