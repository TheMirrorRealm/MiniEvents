package API.CustomEvents;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MiniEventsEndEvent extends Event{
    private static final HandlerList handlers = new HandlerList();

    String string;
    boolean removeEveryone;

    public MiniEventsEndEvent(String string, boolean removeEveryone) {
        this.string = string;
        this.removeEveryone = removeEveryone;
    }

    /**
     * @return the name of the event.
     */
    public String getEventName() {
        return this.string;
    }

    /**
     *
     * @return remove everyone from the event?
     */
    public boolean removeEveryone() {
        return this.removeEveryone;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
