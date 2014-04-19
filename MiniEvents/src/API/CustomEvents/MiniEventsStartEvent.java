package API.CustomEvents;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.HashMap;

public class MiniEventsStartEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    String string;
    Integer getTimeLeft;
    Integer size;
    Boolean cancelled = false;
    HashMap<String, String> set;

    public MiniEventsStartEvent(String string, Integer getTimeLeft, Integer size, HashMap<String, String> set) {
        this.string = string;
        this.getTimeLeft = getTimeLeft;
        this.set = set;
        this.size = size;
    }
    /**
     * @return true if event is cancelled.
     */
    public boolean isCancelled(){
        return cancelled;
    }
    /**
     * @param b - cancel event? true/false
     */
    public void setCancelled(boolean b){
        cancelled = b;
    }
    /**
     * @return the HashSet that contains all the players from any event.
     */
    public HashMap<String, String> getPlayersInEvent() {
        return this.set;
    }
    /**
     * @return the amount of players in the event.
     */
    public int getAmountOfPlayersInEvent(){
        return size;
    }
    public String getEventName() {
        return this.string;
    }
    /**
     * @return the time left until the event auto stops.
     * (seconds)
     */
    public int getTimeLeft() {
        return getTimeLeft;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
    /**
     * @return the number of players in the event.
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
