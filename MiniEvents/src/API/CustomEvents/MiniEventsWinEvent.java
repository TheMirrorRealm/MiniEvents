package API.CustomEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MiniEventsWinEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    Player player;
    String string;

    public MiniEventsWinEvent(Player player, String string) {
        this.player = player;
        this.string = string;
    }

    /**
     * @return the name of the event.
     */
    public String getEventName() {
        return this.string;
    }

    /**
     * @return the player who won the event.
     */
    public Player getPlayer() {
        return this.player;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
