package Util.Methods;

import API.Info.ApiInfo;
import Mains.MiniEvents;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.HashSet;

public class Info implements ApiInfo{
    public MiniEvents plugin;

    public Info(MiniEvents plugin) {
        this.plugin = plugin;
    }
    public boolean eventstarting = false;
    public boolean eventstarted = false;
    public boolean cancelled = false;
    public final HashSet<String> sbefore = new HashSet<>();
    public final HashSet<String> sblue = new HashSet<>();
    public final HashSet<String> sred = new HashSet<>();
    public final HashSet<String> sfire = new HashSet<>();
    public final HashMap<String, String> inevent = new HashMap<>();
    public final HashMap<String, Scoreboard> scoreboard = new HashMap<>();

    public int eventSize(){
        return inevent.size();
    }
    public String getEventName(){
        return plugin.getEventName();

    }
    public boolean eventStarting(){
        return eventstarting;
    }
    public boolean eventStarted(){
        return eventstarted;
    }
    public boolean inEvent(Player player){
        return inevent.containsKey(player.getName());
    }
    public String eventFormalEventName(String s){
        return plugin.getFormalName(s);
    }
    public int getTimeLeft(){
        return plugin.getTimerMain().getTimeLeft();
    }
    public FileConfiguration getPlayerFile(Player player){
        return plugin.playerFile(player.getName().toLowerCase());
    }
    public HashMap<String, String> getPlayersInEvent(){
        return inevent;
    }
    public boolean inSpectateMode(Player player){
        return plugin.getSpectateMode().inspec.contains(player.getName());
    }
}

