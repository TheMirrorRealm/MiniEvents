package Util.Methods;

import API.CustomEvents.MiniEventsLoseEvent;
import API.CustomEvents.MiniEventsWinEvent;
import Mains.MiniEvents;
import Handlers.EventHandlers.SimpleEvents.KO;
import Handlers.EventHandlers.SimpleEvents.Parkour;
import Handlers.EventHandlers.SimpleEvents.Spleef;
import Handlers.EventHandlers.SimpleEvents.TNTRun;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MainMethods {
    private MiniEvents plugin;

    public MainMethods(MiniEvents plugin) {
        this.plugin = plugin;
    }

    public void endIt(final Player player) {
        Bukkit.getServer().getPluginManager().callEvent(new MiniEventsWinEvent(player, plugin.getEventName()));
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                plugin.getDo().endEvent(true);
            }
        }, 30L);
    }

    public void onLose(Player player, String s) {
        for (final Player o : Bukkit.getServer().getOnlinePlayers()) {
            if (plugin.getInfo().inevent.containsKey(player.getName())) {
                plugin.send(o, s + "-death", player.getName());
            }
        }
    }
    public ItemStack translateItemStack(String n){
        String s = plugin.getConfig().getString(n);
        int items;
        int db;
        if (s.contains(":")) {
            String[] parts = s.split(":");
            try {
                items = Integer.parseInt(parts[0]);
                db = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                items = 264;
                db = 0;
            }
        } else {
            try {
                items = Integer.parseInt(s);
                db = 0;
            } catch (NumberFormatException e) {
                items = 264;
                db = 0;
            }
        }
        return new ItemStack(Material.getMaterial(items), 1, (short) db);
    }
    public void onDeath(Player player, String s) {
        for (final Player o : Bukkit.getServer().getOnlinePlayers()) {
            if (plugin.getInfo().inevent.containsKey(player.getName())) {
                plugin.send(o, s + "-death", player.getName());
            }
        }
    }

    public Player getWinner(Player player) {
        for (Player o : Bukkit.getServer().getOnlinePlayers()) {
            if (plugin.getInfo().inevent.containsKey(o.getName())) {
                if (!plugin.getSpectateMode().inspec.contains(o.getName())) {
                    if (!o.getName().equals(player.getName())) {
                        return o;
                    }
                }
            }
        }
        return null;
    }
    public void basicLeave(Player player){
        plugin.getSpectateMode().inspec.add(player.getName());
        Bukkit.getServer().getPluginManager().callEvent(new MiniEventsLoseEvent(player, plugin.getEventName(),
                plugin.getInfo().inevent.size(), plugin.getTimerMain().getTimeLeft(), plugin.getInfo().inevent));
        if (plugin.getInfo().inevent.size() - plugin.getSpectateMode().inspec.size() <= 1) {
            KO.ended = true;
            Spleef.ended = true;
            Parkour.end = true;
            TNTRun.ended = true;
            endIt(getWinner(player));
        }
        plugin.getDo().removePlayerFromEvent(player, true, true);
    }
    public void basicLose(Player player) {
        plugin.getDo().putPlayerInSpectate(player);
        Bukkit.getServer().getPluginManager().callEvent(new MiniEventsLoseEvent(player, plugin.getEventName(),
                plugin.getInfo().inevent.size(), plugin.getTimerMain().getTimeLeft(), plugin.getInfo().inevent));
        if (plugin.getInfo().inevent.size() - plugin.getSpectateMode().inspec.size() <= 1) {
            KO.ended = true;
            Spleef.ended = true;
            Parkour.end = true;
            TNTRun.ended = true;
            endIt(getWinner(player));
        }
    }
}
