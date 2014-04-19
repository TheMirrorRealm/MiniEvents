package Misc;

import Mains.MiniEvents;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class MainSigns implements Listener{
    public MiniEvents plugin;

    public MainSigns(MiniEvents plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onClick(PlayerInteractEvent event){
        Player player= event.getPlayer();
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if (event.getClickedBlock().getType().equals(Material.WALL_SIGN)
                    || event.getClickedBlock().getType().equals(Material.SIGN_POST)){
                Sign sign = (Sign) event.getClickedBlock().getState();
                String s = plugin.getConfig().getString("signs.first-line");
                if (sign.getLine(0).equals(ChatColor.translateAlternateColorCodes('&', s))){
                    Bukkit.getServer().dispatchCommand(player, "join");
                }
            }
        }
    }
}
