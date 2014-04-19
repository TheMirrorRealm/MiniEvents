package Misc;

import Mains.MiniEvents;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class StatCommands implements CommandExecutor {
    public MiniEvents plugin;

    public StatCommands(MiniEvents plugin) {
        this.plugin = plugin;
    }
    public void sendStats(Player player, String target){
        String s = target.toLowerCase();
        FileConfiguration fc = plugin.playerFile(s);
        String name = fc.getString(s + ".name");
        int wins;
        int total;
        if (fc.getString(s + ".wins") != null) {
            wins = fc.getInt(s + ".wins");
        } else {
            wins = 0;
        }
        if (fc.getString(s + ".total") != null) {
            total = fc.getInt(s + ".total");
        } else {
            total = 0;
        }
        List<String> help = plugin.getLangYAML().getLang().getStringList("stats.main");
        for (String ss : help) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', ss.replace("{0}",
                    Integer.toString(wins)).replace("{1}", Integer.toString(total)).replace("{NAME}", name)));
        }
    }
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            if (cmd.getName().equals("eventstats")) {
                Player player = (Player) sender;
                if (!(player.hasPermission("event.stats") || player.hasPermission("event.*") || player.isOp())) {
                    plugin.send(player, "no-permission");
                    return true;
                } else {
                    if (args.length >= 2) {
                        plugin.send(player, "stats-usage");
                        return true;
                    }
                    if (args.length == 0) {
                        sendStats(player, player.getName());
                        return true;
                    }
                    if (args.length == 1) {
                        if (player.hasPermission("event.stats.others") || player.hasPermission("event.*") || player.isOp()) {
                            String s = args[0].toLowerCase();
                            FileConfiguration fc = plugin.playerFile(s);
                            if (fc.getString(s + ".name") == null) {
                                plugin.send(player, "null-target", args[0]);
                                return true;
                            } else {
                                sendStats(player, args[0]);
                                return true;
                            }
                        } else {
                            plugin.send(player, "no-permission");
                        }
                    }
                }
            }
        }
        return true;
    }
}
