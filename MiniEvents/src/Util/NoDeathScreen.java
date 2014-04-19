package Util;

import Mains.MiniEvents;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class NoDeathScreen implements Listener {
    public MiniEvents plugin;

    public NoDeathScreen(MiniEvents plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (plugin.getInfo().inevent.containsKey(p.getName())) {
            if (plugin.getInfo().eventstarted) {
                try {
                    Object nmsPlayer = p.getClass().getMethod("getHandle").invoke(p);
                    Object packet = Class.forName(nmsPlayer.getClass().getPackage().getName() + ".PacketPlayInClientCommand").newInstance();
                    Class<?> enumClass = Class.forName(nmsPlayer.getClass().getPackage().getName() + ".EnumClientCommand");

                    for (Object ob : enumClass.getEnumConstants()) {
                        if (ob.toString().equals("PERFORM_RESPAWN")) {
                            packet = packet.getClass().getConstructor(enumClass).newInstance(ob);
                        }
                    }

                    Object con = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
                    con.getClass().getMethod("a", packet.getClass()).invoke(con, packet);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
    }
}
