package API.CustomEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MiniEventsInitiateEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    Player host;
    String string;
    Integer getTimeLeft;
    Boolean cancelled = false;

    public MiniEventsInitiateEvent(Player host, String string, Integer getTimeLeft) {
        this.string = string;
        this.getTimeLeft = getTimeLeft;
        this.host = host;
    }

    /**
     * @return true if event is cancelled.
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * @param b cancel event? true/false
     */
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    /**
     * @return the name of the event.
     */
    public String getEventName() {
        return this.string;
    }

    /**
     * @return the name of who started the event.
     */
    public Player getHost() {
        return this.host;
    }

    /**
     * @return how long until the event auto stops.
     * (seconds)
     */
    public int getTimeLeft() {
        return getTimeLeft;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
