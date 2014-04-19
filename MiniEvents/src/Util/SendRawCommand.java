package Util;

import Mains.MiniEvents;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SendRawCommand implements CommandExecutor {
    public MiniEvents plugin;

    public SendRawCommand(MiniEvents plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equals("sendraw")) {
            if (!(sender.hasPermission("event.sendraw") || sender.hasPermission("event.*") || sender.isOp())) {
                plugin.send(sender, "no-permission");
                return true;
            } else {
                if (args.length <= 1) {
                    plugin.send(sender, "sendraw-usage");
                    return true;
                }
                if (args.length >= 2) {
                    Player target = Bukkit.getServer().getPlayer(args[0]);
                    if (target != null) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 1; i <= args.length - 1; i++) {
                            sb.append(args[i]).append(" ");
                        }
                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', sb.toString()));
                        plugin.send(sender, "sendraw-sent", args[0]);
                    } else {
                        plugin.send(sender, "null-target", args[0]);
                    }
                }
            }
        }
        return true;
    }
}
