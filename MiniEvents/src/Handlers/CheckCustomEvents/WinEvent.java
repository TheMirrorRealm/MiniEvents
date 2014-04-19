package Handlers.CheckCustomEvents;

import API.CustomEvents.MiniEventsWinEvent;
import Handlers.EventHandlers.ComplexEvents.OITC;
import Mains.MiniEvents;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.FireworkMeta;

import java.io.IOException;
import java.util.Random;

public class WinEvent implements Listener{
    public MiniEvents plugin;

    public WinEvent(MiniEvents plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onWin(MiniEventsWinEvent event){
        final Player player = event.getPlayer();
        final String string = event.getEventName();
        plugin.getInfo().sfire.add(player.getName());
        Firework fw = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
        Random r = new Random();
        int rt = r.nextInt(4) + 1;
        FireworkEffect.Type type = FireworkEffect.Type.BALL;
        if (rt == 1) type = FireworkEffect.Type.BALL;
        if (rt == 2) type = FireworkEffect.Type.BALL_LARGE;
        if (rt == 3) type = FireworkEffect.Type.BURST;
        if (rt == 4) type = FireworkEffect.Type.CREEPER;
        if (rt == 5) type = FireworkEffect.Type.STAR;
        FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(Color.AQUA).withFade(Color.BLUE).with(type).trail(r.nextBoolean()).build();
        fwm.addEffect(effect);
        fwm.setPower(2);
        fw.setFireworkMeta(fwm);
        if (!plugin.getEventName().equalsIgnoreCase("tdm")) {
            if (!plugin.getEventName().equalsIgnoreCase("oitc")) {
                plugin.broadcast("player-won-" + string, player.getName());
            } else {
                plugin.broadcast("player-won-" + string, player.getName(), Integer.toString(OITC.getMax()));
            }
        }
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (String k : plugin.getConfig().getStringList("prizes." + string)) {
                    Bukkit.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), k.replace("{0}", player.getName()));
                }
            }
        }, 3 * 20L);

        FileConfiguration fc = plugin.playerFile(player.getName().toLowerCase());
        String s = player.getName().toLowerCase();
        if (fc.getString(s + ".wins") != null){
            int wins = fc.getInt(s + ".wins");
            fc.set(s + ".wins", wins + 1);
        }else{
            fc.set(s + ".wins", 1);
        }
        try {
            fc.save(plugin.playerData(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
