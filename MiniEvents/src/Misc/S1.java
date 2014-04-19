package Misc;

import Mains.MiniEvents;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.text.NumberFormat;

public class S1 {
    public MiniEvents plugin;

    public S1(MiniEvents plugin) {
        this.plugin = plugin;
    }

    public void scoreboard(Player player, int totalSeconds, boolean set) {
        if (plugin.getConfig().getBoolean("stats.enabled")) {
            Scoreboard board;
            String s = plugin.getEventName();
            Objective o;
            if (!set) {
                board = player.getScoreboard();
                o = board.getObjective(DisplaySlot.SIDEBAR);
            } else {
                board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
                o = board.registerNewObjective("test", "dummy");
                o.setDisplaySlot(DisplaySlot.SIDEBAR);
            }
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2);
            String totalMinutes = nf.format(totalSeconds / 60);
            String seconds = nf.format(totalSeconds % 60);
            String ff;
            if (seconds.length() == 1) {
                ff = totalMinutes + ":" + "0" + seconds;
            } else {
                ff = totalMinutes + ":" + seconds;
            }
            o.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("stats." + s + ".scoreboard-title").replace("{TIME}", ff).replace("{NAME}", player.getName())));
            FileConfiguration fc = plugin.playerFile(player.getName().toLowerCase());
            String ss = player.getName().toLowerCase();
            Score totalPlayers = o.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("stats." + s + ".total-players"))));
            totalPlayers.setScore(plugin.getInfo().inevent.size());
            Score wins = o.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("stats." + s + ".wins"))));
            wins.setScore(fc.getInt(ss + ".wins"));
            Score name = o.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("stats." + s + ".name").replace("{EVENT}", plugin.getFormalName()))));
            name.setScore(-1);
            Score total = o.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("stats." + s + ".total"))));
            total.setScore(fc.getInt(ss + ".total"));
            player.setScoreboard(board);
        }
    }
}
