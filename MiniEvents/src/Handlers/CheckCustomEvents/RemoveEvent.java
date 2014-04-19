package Handlers.CheckCustomEvents;

import API.CustomEvents.MiniEventsLeaveEvent;
import Mains.MiniEvents;
import Util.BossBar;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.DisplaySlot;

public class RemoveEvent implements Listener {
    public MiniEvents plugin;

    public RemoveEvent(MiniEvents plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRemove(MiniEventsLeaveEvent event) {
        final Player player = event.getPlayer();
        plugin.getSpectateMode().inspec.remove(player.getName());
        player.setHealth(player.getMaxHealth());
        BossBar.removeStatusBar(player);
        plugin.getInfo().sbefore.remove(player.getName());
        plugin.getInfo().inevent.remove(player.getName());
        if (player.isInsideVehicle()) {
            if (player.getVehicle() instanceof Horse) {
                player.getVehicle().remove();
            }
        }
        if (player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) != null) {
            player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).unregister();
        }
        if (plugin.getInfo().scoreboard.containsKey(player.getName())) {
            player.setScoreboard(plugin.getInfo().scoreboard.get(player.getName()));
            plugin.getInfo().scoreboard.remove(player.getName());
        }
    }
}
