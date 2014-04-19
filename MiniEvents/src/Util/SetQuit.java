package Util;

import Mains.MiniEvents;
import Mains.SettingsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetQuit implements CommandExecutor {
    public MiniEvents plugin;

    public SetQuit(MiniEvents plugin) {
        this.plugin = plugin;
    }

    SettingsManager settings = SettingsManager.getInstance();

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setquit")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!(player.hasPermission("event.setquit") || (player.hasPermission("event.*")))) {
                    plugin.send(player, "no-permission");
                    return true;
                }
                if (args.length >= 0) {
                    settings.getData().set("quit.world", player.getLocation().getWorld().getName());
                    settings.getData().set("quit.x", Double.valueOf(player.getLocation().getX()));
                    settings.getData().set("quit.y", Double.valueOf(player.getLocation().getY()));
                    settings.getData().set("quit.z", Double.valueOf(player.getLocation().getZ()));
                    settings.getData().set("quit.yaw", Float.floatToIntBits(player.getLocation().getYaw()));
                    settings.getData().set("quit.pitch", Float.floatToIntBits(player.getLocation().getPitch()));
                    plugin.send(player, "set-quit");
                    plugin.saveConfig();
                }
            }
        }
        return true;
    }
}
