package API.CustomEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.HashMap;

public class MiniEventsLoseEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    Player player;
    String string;
    Integer newsize;
    Integer getTimeLeft;
    HashMap<String, String> players;

    public MiniEventsLoseEvent(Player player, String string, Integer newsize
            , Integer getTimeLeft, HashMap<String, String> players) {
        this.player = player;
        this.string = string;
        this.newsize = newsize;
        this.getTimeLeft = getTimeLeft;
        this.players = players;
    }

    /**
     * @return the name of the event.
     */
    public String getEventName() {
        return this.string;
    }

    /**
     * @return the player who lost in the event.
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * @return the number of players in the event.
     */
    public int getAmountOfPlayersInEvent() {
        return newsize;
    }

    /**
     * @return the time left until the event auto stops.
     * (seconds)
     */
    public int getTimeLeft() {
        return getTimeLeft;
    }

    /**
     * @return the HashSet that contains all the players from any event.
     */
    public HashMap<String, String> getPlayersInEvent() {
        return players;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
