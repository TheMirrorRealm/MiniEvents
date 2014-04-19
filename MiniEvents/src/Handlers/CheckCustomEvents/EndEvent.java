package Handlers.CheckCustomEvents;

import API.CustomEvents.MiniEventsEndEvent;
import Handlers.EventHandlers.ComplexEvents.HorseEvent;
import Handlers.EventHandlers.ComplexEvents.KOTH;
import Handlers.EventHandlers.ComplexEvents.OITC;
import Handlers.EventHandlers.SimpleEvents.KO;
import Handlers.EventHandlers.SimpleEvents.Parkour;
import Handlers.EventHandlers.SimpleEvents.Spleef;
import Handlers.EventHandlers.SimpleEvents.TNTRun;
import Mains.MiniEvents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class EndEvent implements Listener{
    public MiniEvents plugin;

    public EndEvent(MiniEvents plugin) {
        this.plugin = plugin;
    }
    @EventHandler (priority = EventPriority.LOWEST)
    public void onEnd(MiniEventsEndEvent event){
        if (event.removeEveryone()){
            for (Player o : Bukkit.getServer().getOnlinePlayers()){
                if (plugin.getInfo().inevent.containsKey(o.getName())){
                    plugin.getDo().removePlayerFromEvent(o, true, true);
                }
            }
        }
        plugin.getInfo().cancelled = true;
        plugin.getInfo().sfire.clear();
        plugin.getInfo().sblue.clear();
        plugin.getInfo().sred.clear();
        plugin.getInfo().sbefore.clear();
        plugin.getInfo().eventstarted = false;
        plugin.getInfo().eventstarting = false;
        plugin.getInfo().scoreboard.clear();
        HorseEvent.end = false;
        KO.ended = false;
        KOTH.koth.clear();
        OITC.in.clear();
        OITC.oi.clear();
        OITC.inin.clear();
        OITC.last.clear();
        KO.lul.clear();
        Spleef.ended = false;
        Spleef.lul.clear();
        TNTRun.ended = false;
        TNTRun.ss.clear();
        Parkour.end = false;
        plugin.eventName = null;
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                plugin.getInfo().inevent.clear();
                plugin.getSpectateMode().inspec.clear();
                plugin.getSignMethods().SignUpdate(0);
            }
        }, 10L);
    }
}
