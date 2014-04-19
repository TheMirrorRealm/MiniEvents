package API.Info;

import Mains.MiniEvents;
import org.bukkit.entity.Player;

public class ApiDo {
    public MiniEvents plugin;

    public ApiDo(MiniEvents plugin) {
        this.plugin = plugin;
    }
    /**
     * @param player - player to be put in spectator mode.
     */
    public void putPlayerInSpectate(Player player){
        plugin.getDo().putPlayerInSpectate(player);
    }

    /**
     * Removes a player from all of the HashSets that are used for events.
     * Player still keeps armor, location, but is not technically in the event anymore.
     * @param player - Player who will be removed from the event.
     */
    public void removePlayerFromEvent(Player player, boolean spawn, boolean loadInventory){
        plugin.getDo().removePlayerFromEvent(player, spawn, loadInventory);
    }

    /**
     * Teleports player to the preset quit point (/setquit).
     * @param player - Player who will be teleported.
     */
    public void teleportPlayerToQuitPoint(Player player) throws NullPointerException{
        plugin.getDo().teleportPlayerToQuitPoint(player);
    }

    /**
     * Saves a player's inventory, potion effects, and armor.
     * @param player - Player who's inventory will be saved.
     */
    public void savePlayerInventory(Player player){
        plugin.getDo().savePlayerInventory(player);
    }

    /**
     * Loads a player's armor, inventory contents, and potion effects.
     * @param player - Player to load inventory for.
     * @throws NullPointerException if player's inventory is not already saved.
     */
    public void loadPlayerInventory(Player player) throws NullPointerException{
        plugin.getDo().loadPlayerInventory(player);
    }

    /**
     * Starts timer on scoreboard.
     * @param eventName - "code" name of an event.
     */
    public void startTimer(String eventName){
        plugin.getDo().startTimer(eventName);
    }

    /**
     * Stops the timer on the scoreboard.
     */
    public void stopTimer(){
        plugin.getDo().stopTimer();
    }

    /**
     * Completely ends any event that is happening.
     * NOTE: This will not remove all player from the event.
     * You muse loop through everyone in the event before ending the event,
     * use heavyRemovePlayerFromEvent and then end the event.
     */
    public void endEvent(boolean removeAll){
        plugin.getDo().endEvent(removeAll);
    }

    /**
     * Updates a player's scoreboard.
     * @param player - The player for whom the scoreboard will be updated.
     * @param time - Time until the event end (wise to use getTimeLeft())
     * @param setOrUpdate - Name of the current, running event.
     */
    public void updateEventScoreboard(Player player, Integer time, boolean setOrUpdate){
        plugin.getDo().updateEventScoreboard(player, time, setOrUpdate);
    }

    /**
     * Starts counting down for an event
     * @param eventName - Name of the event for which the countdown will start for.
     */
    public void startCountDown(String eventName){
        plugin.getDo().startCountDown(eventName);
    }
}
