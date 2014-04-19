package Handlers.EventHandlers.SimpleEvents;

import Mains.MiniEvents;
import Mains.SettingsManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class Parkour implements Listener {
    SettingsManager settings = SettingsManager.getInstance();
    public MiniEvents plugin;

    public Parkour(MiniEvents plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (plugin.getPlayerEvent(player).equals("parkour")) {
                event.setCancelled(true);
                player.setFoodLevel(20);
                player.setHealth(20.0);
                player.setSaturation(20);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (plugin.getPlayerEvent(player).equals("parkour") || plugin.getPlayerEvent(player).equals("spleef") || plugin.getPlayerEvent(player).equals("tnt")) {
                event.setCancelled(true);
            }
        }
    }

    public static boolean end = false;

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        if (plugin.getPlayerEvent(player) != null) {
            if (plugin.getPlayerEvent(player).equals("parkour")) {
                if (plugin.getInfo().eventstarted) {
                    World w = Bukkit.getServer().getWorld(settings.getData().getString("parkour.top.world"));
                    int x = settings.getData().getInt("parkour.top.x");
                    int y = settings.getData().getInt("parkour.top.y");
                    int z = settings.getData().getInt("parkour.top.z");
                    Location loc = new Location(w, x, y, z);
                    if (player.getLocation().distance(loc) <= 0.7) {
                        if (!end) {
                            end = true;
                            plugin.getMethods().endIt(player);
                        }
                    }
                }
            }
        }
    }
}
