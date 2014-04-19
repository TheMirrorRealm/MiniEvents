package API.Info;

import Mains.MiniEvents;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ApiInfo {
    public static MiniEvents plugin;

    public ApiInfo(MiniEvents plugin) {
        ApiInfo.plugin = plugin;
    }

    /**
     * @return number of players in the event.
     */
    public static int eventSize() {
        return plugin.getInfo().eventSize();
    }

    /**
     * @return "code" name of the current event running; else, "none"
     * It will return one of the following:
     * horse, koth, oitc, paint, tdm, ko, lms, parkour, spleef, tnt
     */
    public String getEventName() {
        return plugin.getInfo().getEventName();
    }

    /**
     * @return TRUE if an event is starting (counting down).
     */
    public boolean eventStarting(){
        return plugin.getInfo().eventStarting();
    }

    /**
     * @return TRUE if an event has started.
     */
    public boolean eventStarted();

    /**
     * @param player - Player to check.
     * @return TRUE if a player is currently playing in an event.
     */
    public boolean inEvent(Player player);

    /**
     * @return the "formal" name of an event that is running.
     * param eventName - the event for which to return the formal name for.
     */
    public String eventFormalEventName(String eventName);

    /**
     * @return time left until the event starts
     */
    public int getTimeLeft();

    /**
     * @param player - Gets that player's file.
     * @return the FileConfiguration where individual player data is stored.
     */
    public FileConfiguration getPlayerFile(Player player);

    /**
     * The is a big "inevent" HashSet<Player> that contains a player no matter what event
     * that player is in.
     *
     * @return the "inevent" HashSet.
     */
    public HashMap<String, String> getPlayersInEvent();

    /**
     * @return true if a player is currently in spectate mode.
     */
    public boolean inSpectateMode(Player player);

}
