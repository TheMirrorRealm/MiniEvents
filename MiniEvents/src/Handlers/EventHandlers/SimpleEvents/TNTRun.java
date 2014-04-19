package Handlers.EventHandlers.SimpleEvents;

import Mains.MiniEvents;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;

public class TNTRun implements Listener {
    public final ArrayList<Location> block = new ArrayList<>();
    public final ArrayList<Location> block2 = new ArrayList<>();
    public static final ArrayList<String> ss = new ArrayList<>();
    public MiniEvents plugin;

    public TNTRun(MiniEvents plugin) {
        this.plugin = plugin;
    }

    public static boolean ended = false;

    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        if (plugin.getPlayerEvent(event.getPlayer()).equals("tnt")) {
            if (plugin.getInfo().eventstarted) {
                final Player player = event.getPlayer();
                if (!plugin.getInfo().sbefore.contains(player.getName()) && !plugin.getSpectateMode().inspec.contains(player.getName())) {
                    if (event.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.TNT)) {
                        final Block b = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
                        block.add(b.getLocation());
                        block2.add(b.getLocation());
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            public void run() {
                                player.getWorld().getBlockAt(b.getLocation()).setType(Material.AIR);
                                player.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, 46);
                            }
                        }, 3L);
                    }
                }
                Material b = player.getLocation().getBlock().getType();
                if (b.equals(Material.STATIONARY_WATER) || b.equals(Material.WATER) || event.getPlayer().getLocation().getY() <= 0) {
                    if (!ss.contains(player.getName()) && !ended) {
                        ended = true;
                        ss.add(player.getName());
                        plugin.getMethods().basicLose(player);
                    }
                }
            }
        }
    }
}
