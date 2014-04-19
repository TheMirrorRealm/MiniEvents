package Misc;

import Mains.MiniEvents;
import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

import java.text.NumberFormat;

public class SignMethods {
    public MiniEvents plugin;

    public SignMethods(MiniEvents plugin) {
        this.plugin = plugin;
    }

    public void SignUpdate(Integer i) {
        if (plugin.getConfig().getBoolean("enabled-signs")) {
            for (World w : Bukkit.getServer().getWorlds()) {
                for (Chunk c : w.getLoadedChunks()) {
                    for (BlockState b : c.getTileEntities()) {
                        if (b instanceof Sign) {
                            Sign sign = (Sign) b;
                            NumberFormat nf = NumberFormat.getInstance();
                            nf.setMaximumFractionDigits(2);
                            String totalMinutes = nf.format(i / 60);
                            String seconds = nf.format(i % 60);
                            String ff;
                            if (seconds.length() == 1) {
                                ff = totalMinutes + ":" + "0" + seconds;
                            } else {
                                ff = totalMinutes + ":" + seconds;
                            }
                            String s1 = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("signs.first-line"));
                            String s2 = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("signs.second-line-before"));
                            String s3 = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("signs.second-line-after"));
                            String s4 = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("signs.third-line-before"));
                            String s5 = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("signs.third-line-started"));
                            String s6 = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("signs.third-line-after"));
                            String s7 = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("signs.fourth-line-before"));
                            String s8 = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("signs.fourth-line-after"));
                            if (sign.getLine(0).equals(ChatColor.translateAlternateColorCodes('&', s1))) {
                                if (!plugin.getEventName().equals("none")) {
                                    sign.setLine(1, s2.replace("{EVENT}", plugin.getFormalName()));
                                } else {
                                    sign.setLine(1, s3);
                                }
                                if (plugin.getInfo().eventstarting) {
                                    sign.setLine(2, s4.replace("{TIME}", ff));
                                } else if (plugin.getInfo().eventstarted) {
                                    sign.setLine(2, s5);
                                } else {
                                    sign.setLine(2, s6);
                                }
                                if (plugin.getInfo().eventstarting) {
                                    sign.setLine(3, s7);
                                } else {
                                    sign.setLine(3, s8);
                                }
                            }
                            sign.update();
                        }
                    }
                }
            }
        }
    }
}
